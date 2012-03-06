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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Abstract implementation of an {@link EventHandlerStrategy}, that uses simple annotations like the
 * {@link EventHandler}, {@link PooledEventHandler} or {@link SwingEventHandler} annotations.
 * 
 * @author Manfred HANTSCHEL
 * @param <ANNOTATION_TYPE> the type of the annotation
 */
public abstract class AbstractAnnotatedEventHandlerStrategy<ANNOTATION_TYPE extends Annotation> extends
    AbstractEventHandlerStrategy
{

    public AbstractAnnotatedEventHandlerStrategy()
    {
        super();
    }

    /**
     * Returns the type of the annotation
     * 
     * @return the type of the annotation
     */
    protected abstract Class<ANNOTATION_TYPE> getAnnotationType();

    /**
     * Extracts all producer classes either from the annotation or the method itself. Checks for failures. Returns an
     * empty array if all producers are permitted.
     * 
     * @param annotation the annotation
     * @param method the method
     * @return all producer classes, empty if all permitted
     * @throws IllegalArgumentException on any failure
     */
    protected Class<?>[] getProducerTypes(ANNOTATION_TYPE annotation, Method method) throws IllegalArgumentException
    {
        Set<Class<?>> result = new LinkedHashSet<Class<?>>();
        Class<?> methodProducerType = getPossibleProducerType(method);
        Class<?>[] annotatedProducerTypes = getAllowedProducerTypes(annotation, method);

        if ((annotatedProducerTypes == null) || (annotatedProducerTypes.length == 0))
        {
            if (methodProducerType != null)
            {
                result.add(methodProducerType);
            }
        }
        else
        {
            for (final Class<?> annotatedProducer : annotatedProducerTypes)
            {
                if ((methodProducerType != null) && (!methodProducerType.isAssignableFrom(annotatedProducer)))
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
     * @return all event classes, never null, never empty
     * @throws IllegalArgumentException on any failure
     */
    protected Class<?>[] getEventTypes(ANNOTATION_TYPE annotation, final Method method) throws IllegalArgumentException
    {
        final Set<Class<?>> result = new LinkedHashSet<Class<?>>();
        Class<?> methodEventType = getPossibleEventType(method);
        Class<?>[] annotatedEventTypes = getAllowedEventTypes(annotation, method);

        if ((annotatedEventTypes == null) || (annotatedEventTypes.length == 0))
        {
            if (methodEventType != null)
            {
                result.add(methodEventType);
            }
        }
        else
        {
            for (final Class<?> annotatedEventType : annotatedEventTypes)
            {
                if (!methodEventType.isAssignableFrom(annotatedEventType))
                {
                    throw new IllegalArgumentException("Invalid event handler method signature. Event of "
                        + annotatedEventType + " cannot be assigned: " + method);
                }

                result.add(annotatedEventType);
            }
        }

        return result.toArray(new Class<?>[result.size()]);
    }

    /**
     * Returns the possible producer type as determined from the parameters of the method. By default this is the first
     * parameter, if the method has two parameters or null, if the method has just one parameter (the event)
     * 
     * @param method the method
     * @return the type of the producer, or null if not specified
     * @throws IllegalArgumentException if the parameter types of the method are invalid
     */
    protected Class<?> getPossibleProducerType(Method method) throws IllegalArgumentException
    {
        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length == 2)
        {
            return parameterTypes[0];
        }
        else if (parameterTypes.length == 1)
        {
            return null;
        }

        throw new IllegalArgumentException("Invalid number of parameters; either (producer, event) or just (event): "
            + method);
    }

    /**
     * Returns the possible event type as determined from the parameters of the method. By default this is the second
     * parameter, if the method has two parameters or the first parameter if the method has just one parameter (the
     * event)
     * 
     * @param method the method
     * @return the type of the event, never null
     * @throws IllegalArgumentException if the parameter types of the method are invalid
     */
    protected Class<?> getPossibleEventType(Method method) throws IllegalArgumentException
    {
        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length == 2)
        {
            return parameterTypes[1];
        }
        else if (parameterTypes.length == 1)
        {
            return parameterTypes[0];
        }

        throw new IllegalArgumentException("Invalid number of parameters; either (producer, event) or just (event): "
            + method);
    }

    /**
     * Returns the allowed producer types as specified in the annotation. If the annotation does not specify any special
     * producer types, the method returns an empty array.
     * 
     * @param annotation the annotation
     * @param method the method
     * @return an array of producer types, never null
     */
    protected abstract Class<?>[] getAllowedProducerTypes(ANNOTATION_TYPE annotation, Method method);

    /**
     * Returns the allowed event types as specified in the annotation. If the annotation does not specify any special
     * event types, the method returns an empty array.
     * 
     * @param annotation the annotation
     * @param method the method
     * @return an array of event types, never null
     */
    protected abstract Class<?>[] getAllowedEventTypes(ANNOTATION_TYPE annotation, Method method);

    /**
     * {@inheritDoc}
     */
    @Override
    protected EventHandlerInfo createEventHandlerInfo(Method method)
    {
        if (method == null)
        {
            throw new IllegalArgumentException("Method is null");
        }

        ANNOTATION_TYPE annotation = method.getAnnotation(getAnnotationType());

        if (annotation == null)
        {
            return null;
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

        Class<?>[] producerTypes = getProducerTypes(annotation, method);
        Class<?>[] eventTypes = getEventTypes(annotation, method);

        return createEventHandlerInfo(annotation, method, producerTypes, eventTypes);
    }

    /**
     * Called to create the {@link EventHandlerInfo}.
     * 
     * @param annotation the annotation
     * @param method the method
     * @param producerTypes the producer types
     * @param eventTypes the event types
     * @return the event handler information object
     */
    protected abstract EventHandlerInfo createEventHandlerInfo(ANNOTATION_TYPE annotation, Method method,
        Class<?>[] producerTypes, Class<?>[] eventTypes);

}
