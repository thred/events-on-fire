/*
 * This file is part of Peas'e'Plate.
 * 
 * Peas'e'Plate is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Peas'e'Plate is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Peas'e'Plate. If not, see
 * <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2008-2011, the Peas'e'Plate Team
 */
package com.google.code.eventsonfire;

import java.util.ArrayDeque;
import java.util.Deque;

public class TestConsumer
{

	private static final long TIMEOUT = 5000; // 5 seconds

	private final Object semaphore = new Object();

	private final Deque<Object> deque;

	public TestConsumer()
	{
		super();

		deque = new ArrayDeque<Object>();
	}

	public Object popEvent()
	{
		return deque.pop();
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

	public void handleEvent(final Number event)
	{
		synchronized (semaphore)
		{
			deque.push(event);
			semaphore.notifyAll();
		}
	}

	public void handleEvent(final String event)
	{
		synchronized (semaphore)
		{
			deque.push(event);
			semaphore.notifyAll();
		}
	}

}
