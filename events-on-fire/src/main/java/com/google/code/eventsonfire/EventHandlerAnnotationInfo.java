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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An {@link EventHandlerInfo} for methods tagged with the {@link EventHandler} annotation
 * 
 * @author Manfred HANTSCHEL
 */
class EventHandlerAnnotationInfo extends AbstractEventHandlerInfo
{

    private final boolean pooled;

    public EventHandlerAnnotationInfo(Method method, Class<?>[] producerTypes, Class<?>[] eventTypes, boolean pooled)
    {
        super(method, producerTypes, eventTypes);

        this.pooled = pooled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void call(Object producer, Object consumer, Object event)
    {
        if (pooled)
        {
            Events.invokeLater(new EventHandlerInvoker(producer, consumer, event, method));

            return;
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
        catch (final Exception e)
        {
            Events.getErrorHandler().invocationFailed(producer, consumer, event, method, "Unhandled exception", e);
        }
    }

}
