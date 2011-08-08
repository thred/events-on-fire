/*
 * Copyright (c) 2011 Manfred Hantschel
 * 
 * This file is part of Events-On-Fire (http://code.google.com/p/events-on-fire), licensed under the terms of the MIT
 * License (MIT).
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.google.code.eventsonfire;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.code.eventsonfire.Action.Type;

public class Events implements Runnable
{

	/**
	 * The runnable for the thread for firing the events
	 */
	private static final Events INSTANCE = new Events();

	/**
	 * The thread for firing the events
	 */
	private static final Thread EVENTS_THREAD;

	/**
	 * The thread local variable containing the count for disabled events
	 */
	private static final ThreadLocal<Integer> DISABLED = new ThreadLocal<Integer>();

	static
	{
		EVENTS_THREAD = new Thread(INSTANCE, "Events Thread");
		EVENTS_THREAD.setDaemon(true);
		EVENTS_THREAD.start();
	}

	/**
	 * Binds the specified consumer to the specified producer
	 * 
	 * @param producer the producer
	 * @param consumer the consumer / the listener
	 */
	public static void bind(final Object producer, final Object consumer)
	{
		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		if (consumer == null)
		{
			throw new IllegalArgumentException("Consumer is null");
		}

		INSTANCE.enqueueBind(producer, consumer);
	}

	/**
	 * Unbinds the specified consumer from the specified producer
	 * 
	 * @param producer the producer
	 * @param consumer the consumer / the listener
	 */
	public static void unbind(final Object producer, final Object consumer)
	{
		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		if (consumer == null)
		{
			throw new IllegalArgumentException("Consumer is null");
		}

		INSTANCE.enqueueUnbind(producer, consumer);
	}

	/**
	 * Fires the specified event from the specified producer
	 * 
	 * @param producer the producer
	 * @param event the event
	 */
	public static void fire(final Object producer, final Object event)
	{
		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		if (event == null)
		{
			throw new IllegalArgumentException("Event is null");
		}

		if (isDisabled())
		{
			return;
		}

		INSTANCE.enqueueEvent(producer, event);
	}

	/**
	 * Disables events from the current thread. Make sure to call the enable method by using a finally block! Multiple
	 * calls to disable, increase a counter and it is necessary to call enable as often as you have called disable.
	 */
	public static void disable()
	{
		final Integer count = DISABLED.get();

		if (count != null)
		{
			DISABLED.set(Integer.valueOf(count.intValue() + 1));
		}
		else
		{
			DISABLED.set(Integer.valueOf(1));
		}
	}

	/**
	 * Returns true if events from the current thread are disabled
	 * 
	 * @return true if disabled
	 */
	public static boolean isDisabled()
	{
		final Integer count = DISABLED.get();

		return ((count != null) && (count.intValue() > 0));
	}

	/**
	 * Enables events from the current thread. It is wise to place call to this method within a finally block. Multiple
	 * calls to disable, increase a counter and it is necessary to call enable as often as you have called disable.
	 * Throws an {@link IllegalStateException} if events from the current thread are not disabled.
	 */
	public static void enable()
	{
		final Integer count = DISABLED.get();

		if ((count == null) || (count.intValue() <= 0))
		{
			throw new IllegalStateException("Events not disabled");
		}

		DISABLED.set(Integer.valueOf(count - 1));
	}

	/**
	 * The queue containing actions which wait to get executed
	 */
	private final BlockingQueue<Action> pendingActions;

	/**
	 * A map containing all {@link Producer} objects containing the consumers by the producers.
	 */
	private final Map<Reference<Object>, Producer> producers;

	/**
	 * The reference queue for all weak references used to get rid of them if the object has been garbage collected
	 */
	private final ReferenceQueue<Object> referenceQueue;

	private Events()
	{
		super();

		pendingActions = new LinkedBlockingQueue<Action>();
		producers = new ConcurrentHashMap<Reference<Object>, Producer>();
		referenceQueue = new ReferenceQueue<Object>();
	}

	/**
	 * Adds a bind to the pending list of binds
	 * 
	 * @param producer the producer
	 * @param consumer the consumer
	 */
	private void enqueueBind(final Object producer, final Object consumer)
	{
		pendingActions.add(new Action(Type.BIND, producer, consumer));
	}

	/**
	 * Adds an unbind to the pending list of unbinds
	 * 
	 * @param producer the producer
	 * @param consumer the consumer
	 */
	private void enqueueUnbind(final Object producer, final Object consumer)
	{
		pendingActions.add(new Action(Type.UNBIND, producer, consumer));
	}

	/**
	 * Enqueues an event
	 * 
	 * @param producer the producer
	 * @param event the event
	 */
	private void enqueueEvent(final Object producer, final Object event)
	{
		pendingActions.add(new Action(Type.FIRE, producer, event));
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		try
		{
			while (true)
			{
				try
				{
					final Action action = pendingActions.take();

					switch (action.getType())
					{
						case FIRE:
							executeFireAction(action);
							break;

						case BIND:
							executeBindAction(action);
							break;

						case UNBIND:
							executeUnbindAction(action);
							break;
					}

					cleanupReferences();
				}
				catch (final InterruptedException e)
				{
					throw e;
				}
				catch (final Exception e)
				{
					System.err.println("UNHANDLED EXCEPTION in Event Thread: " + e.getMessage());

					e.printStackTrace(System.err);
				}
			}
		}
		catch (final InterruptedException e)
		{
			System.err.println("Events thread got interrupted: " + e.getMessage());
		}
	}

	private void executeBindAction(final Action action)
	{
		final Reference<Object> producerReference = new WeakIdentityReference<Object>(action.getProducer(), referenceQueue);
		final Reference<Object> consumerReference = new WeakIdentityReference<Object>(action.getParameter(), referenceQueue);

		Producer producer = producers.get(producerReference);

		if (producer == null)
		{
			producer = new Producer();

			producers.put(producerReference, producer);
		}

		producer.add(consumerReference);
	}

	private void executeUnbindAction(final Action action)
	{
		final Reference<Object> producerReference = new WeakIdentityReference<Object>(action.getProducer(), referenceQueue);
		final Reference<Object> consumerReference = new WeakIdentityReference<Object>(action.getParameter(), referenceQueue);
		final Producer producer = producers.get(producerReference);

		if (producer == null)
		{
			return;
		}

		producer.remove(consumerReference);
	}

	private void executeFireAction(final Action action)
	{
		final Reference<Object> producerReference = new WeakIdentityReference<Object>(action.getProducer(), referenceQueue);
		final Producer producer = producers.get(producerReference);

		if (producer != null)
		{
			producer.fire(action.getParameter());
		}
	}

	/**
	 * Uses the reference queue to clean up unused references to garbage collected objects
	 */
	private void cleanupReferences()
	{
		Reference<?> reference;

		while ((reference = referenceQueue.poll()) != null)
		{
			final Iterator<Entry<Reference<Object>, Producer>> it = producers.entrySet().iterator();

			while (it.hasNext())
			{
				final Entry<Reference<Object>, Producer> entry = it.next();
				final Reference<Object> producerReference = entry.getKey();

				if (producerReference == reference)
				{
					it.remove();
					break;
				}

				final Producer producer = entry.getValue();

				producer.remove(reference);

				if (producer.isEmpty())
				{
					it.remove();
				}
			}
		}
	}

}
