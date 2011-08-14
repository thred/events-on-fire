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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains information about a consumer class. Scans the class for methods annotated with the {@link EventHandler}
 * annotation. Holds these methods and invoke them if necessary and appropriate.
 * 
 * @author Manfred HANTSCHEL
 */
class ConsumerClassInfo
{

	/**
	 * Holds all previously generated {@link ConsumerClassInfo} objects
	 */
	private static final Map<Class<?>, ConsumerClassInfo> CACHE = new HashMap<Class<?>, ConsumerClassInfo>();

	/**
	 * Returns the class info for the specified consumer class. Uses a cache to speed up processing.
	 * 
	 * @param type the class, mandatory
	 * @return the class info for the consumer, never null
	 * @throws IllegalArgumentException if the type is null or the class does not contain any method annotated with the
	 *             {@link EventHandler} annotation
	 */
	public static ConsumerClassInfo getInstance(Class<?> type) throws IllegalArgumentException
	{
		ConsumerClassInfo result = CACHE.get(type);

		if (result != null)
		{
			return result;
		}

		result = new ConsumerClassInfo(type);

		CACHE.put(type, result);

		return result;
	}

	private final EventHandlerInfo[] infos;

	/**
	 * Creates the info for the specified consumer class.
	 * 
	 * @param type the class, mandatory
	 * @throws IllegalArgumentException if the type is null or the class does not contain any method annotated with the
	 *             {@link EventHandler} annotation
	 */
	private ConsumerClassInfo(Class<?> type) throws IllegalArgumentException
	{
		super();

		if (type == null)
		{
			throw new IllegalArgumentException("Type is null");
		}

		List<EventHandlerInfo> infos = new ArrayList<EventHandlerInfo>();

		for (Method method : type.getMethods())
		{
			EventHandler annotation = method.getAnnotation(EventHandler.class);

			if (annotation != null)
			{
				infos.add(new EventHandlerInfo(method));
			}
		}

		if (infos.size() == 0)
		{
			throw new IllegalArgumentException("No event handler found: " + type);
		}

		this.infos = infos.toArray(new EventHandlerInfo[infos.size()]);
	}

	/**
	 * Invokes all event handler methods of the class if the method is applicable for the type of producer, consumer and
	 * event. If an error occurs when invoking the method, the invocationFailed method of the {@link ErrorHandler} is
	 * called. If the invocation type is set to parallel, it uses the thread pool of the {@link Events} class to
	 * delegate the invocation of the method.
	 * 
	 * @param producer the producer, mandatory
	 * @param consumer the consumer, mandatory
	 * @param event the event, mandatory
	 */
	public void invoke(Object producer, Object consumer, Object event)
	{
		for (EventHandlerInfo info : infos)
		{
			info.invoke(producer, consumer, event);
		}
	}

}
