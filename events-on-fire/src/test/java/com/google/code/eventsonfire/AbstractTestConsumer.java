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

import java.util.Stack;


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

    //private final Deque<Event> deque;
    private final Stack<Event> deque;

	public AbstractTestConsumer()
	{
		super();

        //deque = new ArrayDeque<Event>();
        deque = new Stack<Event>();
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
