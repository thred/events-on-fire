/*
 * Copyright (c) 2011, 2012 events-on-fire Team
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

/**
 * Abstract implementation of an {@link EventHandlerInfo} based on a method with possible and allowed producer and event
 * types.
 * 
 * @author Manfred HANTSCHEL
 */
public abstract class AbstractEventHandlerInfo implements EventHandlerInfo
{

    protected final Method method;
    protected final Class<?>[] producerTypes;
    protected final Class<?>[] eventTypes;

    public AbstractEventHandlerInfo(Method method, Class<?>[] producerTypes, Class<?>[] eventTypes)
    {
        super();

        this.method = method;
        this.producerTypes = producerTypes;
        this.eventTypes = eventTypes;
    }

    /**
     * Returns the method
     * 
     * @return the method
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * Returns all allowed producer types
     * 
     * @return an array of producer types
     */
    public Class<?>[] getProducerTypes()
    {
        return producerTypes;
    }

    /**
     * Returns all allowed event types
     * 
     * @return an array of event types
     */
    public Class<?>[] getEventTypes()
    {
        return eventTypes;
    }

    /**
     * {@inheritDoc}
     */
    public boolean invoke(Object producer, Object consumer, Object event)
    {
        if (!isCallable(producer.getClass(), event.getClass()))
        {
            return false;
        }

        call(producer, consumer, event);

        return true;
    }

    /**
     * If the event handler is invokable, this method calls it
     * 
     * @param producer the producer
     * @param consumer the consumer
     * @param event the event
     */
    protected abstract void call(Object producer, Object consumer, Object event);

    /**
     * Returns true if the event handler is callable
     * 
     * @param producerType the type of the producer
     * @param eventType the type of the event
     * @return true if invokable
     */
    protected boolean isCallable(final Class<?> producerType, final Class<?> eventType)
    {
        return isProducerAssignable(producerType) && isEventAssignable(eventType);
    }

    /**
     * Returns true if the producer is permitted by the annotation and the parameter
     * 
     * @param type the type of the producer
     * @return true if permitted
     */
    protected boolean isProducerAssignable(final Class<?> type)
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
    protected boolean isEventAssignable(final Class<?> type)
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

}
