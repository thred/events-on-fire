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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores information about a method which is used as event handler. Collects information from the method signature and
 * the {@link EventHandler} annotation. Checks the declaration for failures.
 * 
 * @author Manfred HANTSCHEL
 */
class EventHandlerInfo
{

	private final Method method;
	private final Class<?>[] producerTypes;
	private final Class<?>[] eventTypes;
	private final boolean pooled;

	/**
	 * Creates a new instance by gathering the information from the specified method and its annotation.
	 * 
	 * @param method the method, mandatory
	 * @throws IllegalArgumentException if the method is null, the {@link EventHandler} annotation is missing or the
	 *             specification is invalid
	 */
	public EventHandlerInfo(final Method method) throws IllegalArgumentException
	{
		super();

		if (method == null)
		{
			throw new IllegalArgumentException("Method is null");
		}

		if (method.getReturnType() != void.class)
		{
			throw new IllegalArgumentException("Invalid event handler method signature. Return type not supported: "
			    + method);
		}

		final Class<?>[] parameterTypes = method.getParameterTypes();

		if ((parameterTypes.length < 1) || (parameterTypes.length > 2))
		{
			throw new IllegalArgumentException("Invalid event handler method signature. One or two parameters needed: "
			    + method);
		}

		final EventHandler annotation = method.getAnnotation(EventHandler.class);

		if (annotation == null)
		{
			throw new IllegalArgumentException("EventHandler annotation is missing: " + method);
		}

		this.method = method;

		producerTypes = determineProducers(annotation, method, (parameterTypes.length == 2) ? parameterTypes[0] : null);
		eventTypes =
		    determineEvents(annotation, method, (parameterTypes.length == 2) ? parameterTypes[1] : parameterTypes[0]);
		pooled = annotation.pooled();
	}

	/**
	 * Invokes the method references by this information object if applicable for the type of producer, consumer and
	 * event. If an error occurs when invoking the method, the invocationFailed method of the {@link ErrorHandler} is
	 * called. If the invocation type is set to parallel, it uses the thread pool of the {@link Events} class to
	 * delegate the invocation of the method.
	 * 
	 * @param producer the producer, mandatory
	 * @param consumer the consumer, mandatory
	 * @param event the event, mandatory
	 * @return true if invoked (or will be invoked in near future), false otherwise
	 */
	public boolean invoke(final Object producer, final Object consumer, final Object event)
	{
		if (!isAssignable(producer.getClass(), event.getClass()))
		{
			return false;
		}

		if (pooled)
		{
			Events.invokeLater(producer, consumer, event, method);

			return true;
		}

		final Class<?>[] parameterTypes = method.getParameterTypes();

		try
		{
			if (parameterTypes.length == 1)
			{
				method.invoke(consumer, event);
			}
			else
			{
				method.invoke(consumer, producer, event);
			}

			return true;
		}
		catch (final IllegalArgumentException e)
		{
			Events.getErrorHandler().invocationFailed(producer, consumer, event, method, "Invalid argument", e);
		}
		catch (final IllegalAccessException e)
		{
			Events.getErrorHandler().invocationFailed(producer, consumer, event, method, "Illegal access", e);
		}
		catch (final InvocationTargetException e)
		{
			Events.getErrorHandler().invocationFailed(producer, consumer, event, method, "Invocation failed", e);
		}
		catch (final Throwable e)
		{
			Events.getErrorHandler().invocationFailed(producer, consumer, event, method, "Unhandled exception", e);
		}

		return false;
	}

	public boolean isAssignable(final Class<?> producerType, final Class<?> eventType)
	{
		return isProducerAssignable(producerType) && isEventAssignable(eventType);
	}

	/**
	 * Returns true if the producer is permitted by the annotation and the parameter
	 * 
	 * @param type the type of the producer
	 * @return true if permitted
	 */
	private boolean isProducerAssignable(final Class<?> type)
	{
		if (producerTypes.length == 0)
		{
			return true;
		}

		for (final Class<?> producerType : producerTypes)
		{
			if (producerType.isAssignableFrom(type))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the event is permitted by the annotation and the parameter
	 * 
	 * @param type the type of the event
	 * @return true if permitted
	 */
	private boolean isEventAssignable(final Class<?> type)
	{
		for (final Class<?> eventType : eventTypes)
		{
			if (eventType.isAssignableFrom(type))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Extracts all producer classes either from the annotation or the method itself. Checks for failures. Returns an
	 * empty list if all producers are permitted.
	 * 
	 * @param annotation the annotation
	 * @param method the method
	 * @param parameterType the parameter type for the producer, null if not specified
	 * @return all producer classes, empty if all permitted
	 * @throws IllegalArgumentException on any failure
	 */
	private static Class<?>[] determineProducers(final EventHandler annotation, final Method method, final Class<?> parameterType) throws IllegalArgumentException
	{
		final Set<Class<?>> result = new HashSet<Class<?>>();
		final Class<?>[] annotatedProducers = annotation.producer();

		if ((annotatedProducers == null) || (annotatedProducers.length == 0))
		{
			if (parameterType != null)
			{
				result.add(parameterType);
			}
		}
		else
		{
			for (final Class<?> annotatedProducer : annotatedProducers)
			{
				if ((parameterType != null) && (!parameterType.isAssignableFrom(annotatedProducer)))
				{
					throw new IllegalArgumentException("Invalid event handler method signature. Producer of "
					    + annotatedProducer + " cannot be assigned: " + method);
				}

				result.add(annotatedProducer);
			}
		}

		return result.toArray(new Class<?>[result.size()]);
	}

	/**
	 * Extracts all event classes either from the annotation or the method itself. Checks for failures.
	 * 
	 * @param annotation the annotation
	 * @param method the method
	 * @param parameterType the parameter type for the event
	 * @return all event classes, never null, never empty
	 * @throws IllegalArgumentException on any failure
	 */
	private static Class<?>[] determineEvents(final EventHandler annotation, final Method method, final Class<?> parameterType) throws IllegalArgumentException
	{
		final Set<Class<?>> result = new HashSet<Class<?>>();
		final Class<?>[] annotatedEvents = annotation.event();

		if ((annotatedEvents == null) || (annotatedEvents.length == 0))
		{
			result.add(parameterType);
		}
		else
		{
			for (final Class<?> annotatedEvent : annotatedEvents)
			{
				if (!parameterType.isAssignableFrom(annotatedEvent))
				{
					throw new IllegalArgumentException("Invalid event handler method signature. Event of "
					    + annotatedEvent + " cannot be assigned: " + method);
				}

				result.add(annotatedEvent);
			}
		}

		return result.toArray(new Class<?>[result.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("EventHandler ");

		builder.append(method);
		builder.append(" with producers " + Arrays.toString(producerTypes) + " and events "
		    + Arrays.toString(eventTypes));

		return builder.toString();
	}

}
