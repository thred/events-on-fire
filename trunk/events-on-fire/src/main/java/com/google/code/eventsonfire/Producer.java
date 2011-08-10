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
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A producer with its consumers
 * 
 * @author Manfred Hantschel
 */
class Producer
{

	/**
	 * The set of references to consumers. The set it not synchronized. All calls are made by the {@link Events} thread.
	 */
	private final Set<Reference<?>> consumerReferences;

	public Producer()
	{
		super();

		consumerReferences = new LinkedHashSet<Reference<?>>();
	}

	/**
	 * Adds the reference to the consumer to this producer.
	 * 
	 * @param consumerReference the reference to the consumer
	 * @throws IllegalArgumentException if the consumer does not contain at least one method annotated with the
	 *             {@link EventHandler} annotation
	 */
	public void add(final Reference<?> consumerReference) throws IllegalArgumentException
	{
		final Object consumer = consumerReference.get();

		if (consumer == null)
		{
			return;
		}

		// ensure validity
		ConsumerClassInfo.getInstance(consumer.getClass());

		consumerReferences.add(consumerReference);
	}

	/**
	 * Returns true, if the reference to the consumer was already added to the producer
	 * 
	 * @param consumerReference the reference to the consume
	 * @return true if the producer contains the reference to the consumer
	 */
	public boolean contains(final Reference<?> consumerReference)
	{
		return consumerReferences.contains(consumerReference);
	}

	/**
	 * Removes the reference to the consumer from this producer
	 * 
	 * @param consumerReference the reference to the consumer
	 */
	public void remove(final Reference<?> consumerReference)
	{
		consumerReferences.remove(consumerReference);
	}

	/**
	 * Returns true if the set of consumers is empty
	 * 
	 * @return true if empty
	 */
	public boolean isEmpty()
	{
		return consumerReferences.isEmpty();
	}

	/**
	 * Fires an event to all consumers
	 * 
	 * @param event the event
	 */
	public void fire(final Object event)
	{
		for (final Reference<?> consumerReference : consumerReferences)
		{
			final Object consumer = consumerReference.get();

			if (consumer != null)
			{
				final ConsumerClassInfo consumerClassInfo = ConsumerClassInfo.getInstance(consumer.getClass());

				consumerClassInfo.invoke(this, consumer, event);
			}
		}
	}

	//	/**
	//	 * Fires an event to all consumers
	//	 * 
	//	 * @param event the event
	//	 */
	//	public void fire(final Object event)
	//	{
	//		for (final Reference<?> consumerReference : consumerReferences)
	//		{
	//			final Object consumer = consumerReference.get();
	//
	//			if (consumer != null)
	//			{
	//				final ConsumerClassInfo consumerClassInfo = ConsumerClassInfo.getInstance(consumer.getClass());
	//				Class<? extends Object> eventType = event.getClass();
	//
	//				eventTypeLoop: while (eventType != null)
	//				{
	//					if (fire(consumer, consumerClassInfo, eventType, event))
	//					{
	//						break;
	//					}
	//
	//					final Class<?>[] eventInterfaces = eventType.getInterfaces();
	//
	//					for (final Class<?> eventInterface : eventInterfaces)
	//					{
	//
	//						if (fire(consumer, consumerClassInfo, eventInterface, event))
	//						{
	//							break eventTypeLoop;
	//						}
	//
	//					}
	//
	//					eventType = eventType.getSuperclass();
	//				}
	//			}
	//		}
	//	}

	//	/**
	//	 * Calls the handleEvent method of the consumer if appropriate
	//	 * 
	//	 * @param consumer the consumer
	//	 * @param consumerClassInfo TODO
	//	 * @param eventType the type of the event
	//	 * @param event the event
	//	 * @return
	//	 */
	//	private boolean fire(final Object consumer, final ConsumerClassInfo consumerClassInfo, final Class<? extends Object> eventType, final Object event)
	//	{
	//		
	//		try
	//		{
	//			final Method method = consumer.getClass().getMethod("handleEvent", eventType);
	//
	//			method.invoke(consumer, event);
	//
	//			return true;
	//		}
	//		catch (final SecurityException e)
	//		{
	//			throw new RuntimeException("Failed to handle event", e);
	//		}
	//		catch (final NoSuchMethodException e)
	//		{
	//			// ignore
	//		}
	//		catch (final IllegalArgumentException e)
	//		{
	//			throw new RuntimeException("Failed to handle event", e);
	//		}
	//		catch (final IllegalAccessException e)
	//		{
	//			throw new RuntimeException("Failed to handle event", e);
	//		}
	//		catch (final InvocationTargetException e)
	//		{
	//			throw new RuntimeException("Failed to handle event", e);
	//		}
	//
	//		return false;
	//	}

}
