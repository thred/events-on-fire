/*
 * Copyright (c) 2011-2013 events-on-fire Team
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
 * An {@link EventHandlerStrategy} for classes with methods that were tagged with the {@link EventHandler} annotation.
 * 
 * @author Manfred HANTSCHEL
 */
class EventHandlerAnnotationStrategy extends AbstractAnnotatedEventHandlerStrategy<EventHandler>
{

    public EventHandlerAnnotationStrategy()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<EventHandler> getAnnotationType()
    {
        return EventHandler.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?>[] getAllowedProducerTypes(EventHandler annotation, Method method)
    {
        return annotation.producer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?>[] getAllowedEventTypes(EventHandler annotation, Method method)
    {
        return annotation.event();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getAnyTags(EventHandler annotation, Method method)
    {
        return annotation.anyTag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getEachTags(EventHandler annotation, Method method)
    {
        return annotation.eachTag();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    protected EventHandlerInfo createEventHandlerInfo(EventHandler annotation, Method method,
        Class<?>[] producerTypesByAnnotation, Class<?>[] eventTypesByAnnotation, String[] anyTagsByAnnotation,
        String[] eachTagsByAnnotation)
    {
        return new EventHandlerAnnotationInfo(method, producerTypesByAnnotation, eventTypesByAnnotation,
            anyTagsByAnnotation, eachTagsByAnnotation, annotation.pooled());
    }

}
