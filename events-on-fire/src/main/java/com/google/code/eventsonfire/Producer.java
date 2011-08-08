package com.google.code.eventsonfire;

import java.lang.ref.Reference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A producer with its consumers
 * 
 * @author Manfred HANTSCHEL
 */
class Producer
{

	private static final String HANDLE_EVENT_METHOD_NAME = "handleEvent";

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
	 * @throws IllegalArgumentException if the consumer does not contain at least one handleEvent(..) method
	 */
	public void add(final Reference<?> consumerReference) throws IllegalArgumentException
	{
		final Object consumer = consumerReference.get();

		if (consumer == null)
		{
			return;
		}

		final Method[] methods = consumer.getClass().getMethods();

		for (final Method method : methods)
		{
			if ((HANDLE_EVENT_METHOD_NAME.equals(method.getName())) && (method.getParameterTypes().length == 1))
			{
				consumerReferences.add(consumerReference);

				return;
			}
		}

		throw new IllegalArgumentException("Consumer is missing method: public void handleEvent(* event)");
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
				Class<? extends Object> eventType = event.getClass();

				eventTypeLoop: while (eventType != null)
				{
					if (fire(consumer, eventType, event))
					{
						break;
					}

					final Class<?>[] eventInterfaces = eventType.getInterfaces();

					for (final Class<?> eventInterface : eventInterfaces)
					{

						if (fire(consumer, eventInterface, event))
						{
							break eventTypeLoop;
						}

					}

					eventType = eventType.getSuperclass();
				}
			}
		}
	}

	/**
	 * Calls the handleEvent method of the consumer if appropriate
	 * 
	 * @param consumer the consumer
	 * @param eventType the type of the event
	 * @param event the event
	 * @return
	 */
	private boolean fire(final Object consumer, final Class<? extends Object> eventType, final Object event)
	{
		try
		{
			final Method method = consumer.getClass().getMethod("handleEvent", eventType);

			method.invoke(consumer, event);

			return true;
		}
		catch (final SecurityException e)
		{
			throw new RuntimeException("Failed to handle event", e);
		}
		catch (final NoSuchMethodException e)
		{
			// ignore
		}
		catch (final IllegalArgumentException e)
		{
			throw new RuntimeException("Failed to handle event", e);
		}
		catch (final IllegalAccessException e)
		{
			throw new RuntimeException("Failed to handle event", e);
		}
		catch (final InvocationTargetException e)
		{
			throw new RuntimeException("Failed to handle event", e);
		}

		return false;
	}

}
