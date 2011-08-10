package com.google.code.eventsonfire;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class AbstractTestConsumer
{

	public class Event
	{
		private final String method;
		private final CharSequence producer;
		private final Object event;

		public Event(final String method, final CharSequence producer, final Object event)
		{
			super();

			this.method = method;
			this.producer = producer;
			this.event = event;
		}

		public String getMethod()
		{
			return method;
		}

		public CharSequence getProducer()
		{
			return producer;
		}

		public Object getEvent()
		{
			return event;
		}

	}

	private static final long TIMEOUT = 5000; // 5 seconds

	private final Object semaphore = new Object();

	private final Deque<Event> deque;

	public AbstractTestConsumer()
	{
		super();

		deque = new ArrayDeque<Event>();
	}

	public void pushEvent(final String method, final CharSequence producer, final Object event)
	{
		deque.push(new Event(method, producer, event));

		synchronized (semaphore)
		{
			semaphore.notifyAll();
		}
	}

	public Event popEvent()
	{
		return deque.pop();
	}

	public boolean isEmpty()
	{
		return deque.isEmpty();
	}

	public int size()
	{
		return deque.size();
	}

	public void waitForSize(final int size) throws InterruptedException
	{
		final long millis = System.currentTimeMillis() + TIMEOUT;

		synchronized (semaphore)
		{
			while (size() != size)
			{
				final long timeToWait = millis - System.currentTimeMillis();

				assert timeToWait > 0 : "Consumer did not reach " + size + " events in time";

				semaphore.wait(timeToWait);
			}

		}
	}

}
