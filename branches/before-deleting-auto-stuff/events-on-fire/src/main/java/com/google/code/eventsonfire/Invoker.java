package com.google.code.eventsonfire;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Runnable used for event handler invocations by pooled threads
 * 
 * @author Manfred HANTSCHEL
 */
class Invoker implements Runnable
{

	private final Object producer;
	private final Object consumer;
	private final Object event;
	private final Method method;

	public Invoker(final Object producer, final Object consumer, final Object event, final Method method)
	{
		super();

		this.producer = producer;
		this.consumer = consumer;
		this.event = event;
		this.method = method;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
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
	}
}
