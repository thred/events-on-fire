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


// @Test
public class EventHandlerInfoUnitTest
{

    //    @Test(expectedExceptions = IllegalArgumentException.class)
    //    public void testNoMethod()
    //    {
    //        new EventHandlerAnnotationInfo(null);
    //    }
    //
    //    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*Return type.*")
    //    public void testInvalidReturnType() throws NoSuchMethodException
    //    {
    //        final Method method = EventHandlerInfoTestConsumer.class.getMethod("invalidReturnType", Number.class);
    //
    //        new OldEventHandlerAnnotationInfo(method);
    //    }
    //
    //    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*two parameters.*")
    //    public void testInvalidParameters() throws NoSuchMethodException
    //    {
    //        final Method method =
    //            EventHandlerInfoTestConsumer.class.getMethod("invalidParameters", CharSequence.class, Number.class,
    //                Object.class);
    //
    //        new OldEventHandlerAnnotationInfo(method);
    //    }
    //
    //    @Test(expectedExceptions = IllegalArgumentException.class,
    //        expectedExceptionsMessageRegExp = ".*annotation.*missing.*")
    //    public void testInvalidAnnotation() throws NoSuchMethodException
    //    {
    //        final Method method = EventHandlerInfoTestConsumer.class.getMethod("invalidAnnotation", Number.class);
    //
    //        new OldEventHandlerAnnotationInfo(method);
    //    }
    //
    //    @Test
    //    public void testSingleParameterNotAnnotated() throws NoSuchMethodException
    //    {
    //        final Method method = EventHandlerInfoTestConsumer.class.getMethod("singleParameterNotAnnotated", Number.class);
    //        final OldEventHandlerAnnotationInfo eventHandlerMethod = new OldEventHandlerAnnotationInfo(method);
    //        final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();
    //
    //        eventHandlerMethod.invoke("ProducerA", consumer, new Object());
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke("ProducerB", consumer, Byte.valueOf((byte) 84));
    //        assert Byte.valueOf((byte) 84).equals(consumer.popEvent().getEvent());
    //
    //        eventHandlerMethod.invoke("ProducerC", consumer, Integer.valueOf(42));
    //        assert Integer.valueOf(42).equals(consumer.popEvent().getEvent());
    //
    //        eventHandlerMethod.invoke("ProducerD", consumer, Double.valueOf(21));
    //        assert Double.valueOf(21).equals(consumer.popEvent().getEvent());
    //    }
    //
    //    @Test
    //    public void testSingleParameterAnnotated() throws NoSuchMethodException
    //    {
    //        final Method method = EventHandlerInfoTestConsumer.class.getMethod("singleParameterAnnotated", Number.class);
    //        final OldEventHandlerAnnotationInfo eventHandlerMethod = new OldEventHandlerAnnotationInfo(method);
    //        final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();
    //
    //        eventHandlerMethod.invoke("ProducerA", consumer, new Object());
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke("ProducerB", consumer, Byte.valueOf((byte) 84));
    //        assert Byte.valueOf((byte) 84).equals(consumer.popEvent().getEvent());
    //
    //        eventHandlerMethod.invoke("ProducerC", consumer, Integer.valueOf(42));
    //        assert Integer.valueOf(42).equals(consumer.popEvent().getEvent());
    //
    //        eventHandlerMethod.invoke("ProducerD", consumer, Double.valueOf(21));
    //        assert Double.valueOf(21).equals(consumer.popEvent().getEvent());
    //    }
    //
    //    @Test(expectedExceptions = IllegalArgumentException.class,
    //        expectedExceptionsMessageRegExp = ".*String cannot be assigned.*")
    //    public void testSingleParameterInvalidAnnotated() throws NoSuchMethodException
    //    {
    //        final Method method =
    //            EventHandlerInfoTestConsumer.class.getMethod("singleParameterInvalidAnnotated", Number.class);
    //
    //        new OldEventHandlerAnnotationInfo(method);
    //    }
    //
    //    @Test
    //    public void testSingleParameterMultiAnnotated() throws NoSuchMethodException
    //    {
    //        final Method method =
    //            EventHandlerInfoTestConsumer.class.getMethod("singleParameterMultiAnnotated", Number.class);
    //        final OldEventHandlerAnnotationInfo eventHandlerMethod = new OldEventHandlerAnnotationInfo(method);
    //        final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();
    //
    //        eventHandlerMethod.invoke("ProducerA", consumer, new Object());
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke("ProducerB", consumer, Byte.valueOf((byte) 84));
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke("ProducerC", consumer, Integer.valueOf(42));
    //        assert Integer.valueOf(42).equals(consumer.popEvent().getEvent());
    //
    //        eventHandlerMethod.invoke("ProducerD", consumer, Double.valueOf(21));
    //        assert Double.valueOf(21).equals(consumer.popEvent().getEvent());
    //    }
    //
    //    @Test
    //    public void testSingleParameterProducerAnnotated() throws NoSuchMethodException
    //    {
    //        final Method method =
    //            EventHandlerInfoTestConsumer.class.getMethod("singleParameterProducerAnnotated", Number.class);
    //        final OldEventHandlerAnnotationInfo eventHandlerMethod = new OldEventHandlerAnnotationInfo(method);
    //        final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();
    //
    //        eventHandlerMethod.invoke("ProducerA", consumer, new Object());
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke(new Object(), consumer, Integer.valueOf(84));
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke("ProducerB", consumer, Integer.valueOf(42));
    //        assert Integer.valueOf(42).equals(consumer.popEvent().getEvent());
    //    }
    //
    //    @Test
    //    public void testDoubleParameterNotAnnotated() throws NoSuchMethodException
    //    {
    //        final Method method =
    //            EventHandlerInfoTestConsumer.class.getMethod("doubleParameterNotAnnotated", CharSequence.class,
    //                Number.class);
    //        final OldEventHandlerAnnotationInfo eventHandlerMethod = new OldEventHandlerAnnotationInfo(method);
    //        final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();
    //
    //        eventHandlerMethod.invoke(new Object(), consumer, new Object());
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke("Producer", consumer, new Object());
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke(new Object(), consumer, Integer.valueOf(168));
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke("ProducerA", consumer, Byte.valueOf((byte) 84));
    //        Event event = consumer.popEvent();
    //        assert "ProducerA".equals(event.getProducer());
    //        assert Byte.valueOf((byte) 84).equals(event.getEvent());
    //
    //        eventHandlerMethod.invoke("ProducerB", consumer, Integer.valueOf(42));
    //        event = consumer.popEvent();
    //        assert "ProducerB".equals(event.getProducer());
    //        assert Integer.valueOf(42).equals(event.getEvent());
    //
    //        eventHandlerMethod.invoke("ProducerC", consumer, Double.valueOf(21));
    //        event = consumer.popEvent();
    //        assert "ProducerC".equals(event.getProducer());
    //        assert Double.valueOf(21).equals(event.getEvent());
    //    }
    //
    //    @Test
    //    public void testDoubleParameterAnnotated() throws NoSuchMethodException
    //    {
    //        final Method method =
    //            EventHandlerInfoTestConsumer.class.getMethod("doubleParameterAnnotated", Object.class, Number.class);
    //        final OldEventHandlerAnnotationInfo eventHandlerMethod = new OldEventHandlerAnnotationInfo(method);
    //        final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();
    //
    //        eventHandlerMethod.invoke(new Object(), consumer, Integer.valueOf(42));
    //        assert consumer.isEmpty();
    //
    //        eventHandlerMethod.invoke("Producer", consumer, Integer.valueOf(21));
    //        assert consumer.popEvent().getEvent() == Integer.valueOf(21);
    //    }
    //
    //    @Test(expectedExceptions = IllegalArgumentException.class,
    //        expectedExceptionsMessageRegExp = ".*Number cannot be assigned.*")
    //    public void testDoubleParameterInvalidAnnotated() throws NoSuchMethodException
    //    {
    //        final Method method =
    //            EventHandlerInfoTestConsumer.class.getMethod("doubleParameterInvalidAnnotated", CharSequence.class,
    //                Number.class);
    //
    //        new OldEventHandlerAnnotationInfo(method);
    //    }

}
