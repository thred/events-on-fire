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

import java.util.ArrayList;
import java.util.Collection;

import com.google.code.eventsonfire.swing.ActionEventAutoInstrumentStrategy;
import com.google.code.eventsonfire.swing.WindowEventAutoInstrumentStrategy;

public class ListenerAutoInstrumentService
{

	private static final ListenerAutoInstrumentService INSTANCE = new ListenerAutoInstrumentService();

	static
	{
		registerStrategy(new ActionEventAutoInstrumentStrategy());
		registerStrategy(new WindowEventAutoInstrumentStrategy());
	}

	public static void registerStrategy(ListenerAutoInstrumentStrategy strategy)
	{
		INSTANCE.add(strategy);
	}

	public static void instrument(Object producer, Object consumer) throws IllegalArgumentException
	{
		boolean result = INSTANCE.process(producer, ConsumerClassInfo.getInstance(consumer.getClass()));

		if (!result)
		{
			throw new IllegalArgumentException("No suitable ListenerAutoInstrumentation found for producer of "
			    + producer.getClass() + " and consumer of " + consumer.getClass());
		}
	}

	private final Collection<ListenerAutoInstrumentStrategy> strategies;

	private ListenerAutoInstrumentService()
	{
		super();

		strategies = new ArrayList<ListenerAutoInstrumentStrategy>();
	}

	private void add(ListenerAutoInstrumentStrategy strategy)
	{
		synchronized (strategies)
		{
			strategies.add(strategy);
		}
	}

	private boolean process(Object producer, ConsumerClassInfo consumerClassInfo)
	{
		boolean found = false;

		synchronized (strategies)
		{
			for (ListenerAutoInstrumentStrategy strategy : strategies)
			{
				if (strategy.isProducerSupported(producer))
				{
					for (EventHandlerInfo eventHandlerInfo : consumerClassInfo)
					{
						if (eventHandlerInfo.isAssignable(producer.getClass(), strategy.getEventType()))
						{
							strategy.instrument(producer);

							found = true;
						}
					}
				}
			}
		}

		return found;
	}
}
