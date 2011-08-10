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

/**
 * <p>
 * The base class for events-on-fire.
 * </p>
 * <p>
 * For a detailed usage description see <a href="http://code.google.com/p/events-on-fire/wiki/Usage">Usage on Google
 * Project Hosting</a>.
 * </p>
 * <p>
 * All references in this class are weak, unless otherwise noted. Any producer or consumer will get garbage collected,
 * if there is no reference to it outside this class. All checks will be made on identity instead of equality.
 * </p>
 * 
 * @see <a href="http://code.google.com/p/events-on-fire/wiki/Usage" target="_blank">Usage on Google Project Hosting</a>
 * @author Manfred Hantschel
 */
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
	 * Binds the specified consumer / listener to the specified producer. The consumer must contain at least one
	 * <code>public void handleEvent(* event)</code> method, otherwise an exception is thrown. Both, the producer and
	 * the consumer must have references outside of the Events class. All references within the Events class are weak,
	 * so the objects and the binding gets garbage collected if not referenced. Does nothing if the objects are already
	 * bonded.
	 * 
	 * @param producer the producer, mandatory
	 * @param consumer the consumer / the listener, mandatory
	 * @throws IllegalArgumentException if the producer or the consumer is null or the consumer cannot handle events
	 */
	public static void bind(final Object producer, final Object consumer) throws IllegalArgumentException
	{
		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		if (consumer == null)
		{
			throw new IllegalArgumentException("Consumer is null");
		}

		INSTANCE.enqueue(new Action(Type.BIND, producer, consumer));
	}

	/**
	 * Unbinds the specified consumer from the specified producer. Does nothing if the objects are not bonded.
	 * 
	 * @param producer the producer, mandatory
	 * @param consumer the consumer / the listener, mandatory
	 * @throws IllegalArgumentException if the producer or the consumer is null
	 */
	public static void unbind(final Object producer, final Object consumer) throws IllegalArgumentException
	{
		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		if (consumer == null)
		{
			throw new IllegalArgumentException("Consumer is null");
		}

		INSTANCE.enqueue(new Action(Type.UNBIND, producer, consumer));
	}

	/**
	 * Fires the specified event from the specified producer. Calls the appropriate
	 * <code>public void handleEvent(* event)</code> method of all consumers. Does nothing, if events are disabled for
	 * the current thread. Does nothing, if there are no consumers bonded to the producer.
	 * 
	 * @param producer the producer, mandatory
	 * @param event the event, mandatory
	 * @throws IllegalArgumentException if the producer or the event is null
	 */
	public static void fire(final Object producer, final Object event) throws IllegalArgumentException
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

		INSTANCE.enqueue(new Action(Type.FIRE, producer, event));
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
	 * Returns true if events from the current thread are disabled.
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
	 * 
	 * @throws IllegalStateException if events from the current thread are not disabled
	 */
	public static void enable() throws IllegalStateException
	{
		final Integer count = DISABLED.get();

		if ((count == null) || (count.intValue() <= 0))
		{
			throw new IllegalStateException("Events not disabled");
		}

		DISABLED.set(Integer.valueOf(count - 1));
	}

	/**
	 * Returns the error handler, the {@link DefaultErrorHandler} if not specified.
	 * 
	 * @return the error handler, never null
	 */
	public static ErrorHandler getErrorHandler()
	{
		return INSTANCE.errorHandler;
	}

	/**
	 * Sets the error handler.
	 * 
	 * @param errorHandler the error handler, mandatory
	 * @throws IllegalArgumentException if the error handler is null
	 */
	public static void setErrorHandler(final ErrorHandler errorHandler) throws IllegalArgumentException
	{
		if (errorHandler == null)
		{
			throw new IllegalArgumentException("Error handler is null");
		}

		INSTANCE.errorHandler = errorHandler;
	}

	/**
	 * The queue containing actions which wait to get executed.
	 */
	private final BlockingQueue<Action> actions;

	/**
	 * A map containing all {@link Producer} objects containing the consumers by the producers.
	 */
	private final Map<Reference<Object>, Producer> producers;

	/**
	 * The reference queue for all weak references used to get rid of them if the object has been garbage collected.
	 */
	private final ReferenceQueue<Object> referenceQueue;

	/**
	 * Handler used for logging.
	 */
	private ErrorHandler errorHandler;

	private Events()
	{
		super();

		actions = new LinkedBlockingQueue<Action>();
		producers = new ConcurrentHashMap<Reference<Object>, Producer>();
		referenceQueue = new ReferenceQueue<Object>();
		errorHandler = new DefaultErrorHandler();
	}

	/**
	 * Adds an action to the pending actions
	 * 
	 * @param action the action
	 */
	private void enqueue(final Action action)
	{
		actions.add(action);
	}

	/**
	 * Worker for pending actions. There is no need to call this method.
	 * 
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
					final Action action = actions.take();

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
					errorHandler.unhandledException("Exception in event thread", e);
				}
			}
		}
		catch (final InterruptedException e)
		{
			errorHandler.interrupted(e);
		}
	}

	/**
	 * Binds a consumer to a producer
	 * 
	 * @param action the action
	 */
	private void executeBindAction(final Action action)
	{
		final Reference<Object> producerReference =
		    new WeakIdentityReference<Object>(action.getProducer(), referenceQueue);
		final Reference<Object> consumerReference =
		    new WeakIdentityReference<Object>(action.getParameter(), referenceQueue);

		Producer producer = producers.get(producerReference);

		if (producer == null)
		{
			producer = new Producer();

			producers.put(producerReference, producer);
		}

		producer.add(consumerReference);
	}

	/**
	 * Unbinds a consumer form a producer
	 * 
	 * @param action the action
	 */
	private void executeUnbindAction(final Action action)
	{
		final Reference<Object> producerReference =
		    new WeakIdentityReference<Object>(action.getProducer(), referenceQueue);
		final Reference<Object> consumerReference =
		    new WeakIdentityReference<Object>(action.getParameter(), referenceQueue);
		final Producer producer = producers.get(producerReference);

		if (producer == null)
		{
			return;
		}

		producer.remove(consumerReference);
	}

	/**
	 * Fires an event from the producer
	 * 
	 * @param action the action
	 */
	private void executeFireAction(final Action action)
	{
		final Reference<Object> producerReference =
		    new WeakIdentityReference<Object>(action.getProducer(), referenceQueue);
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
