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

import org.testng.annotations.Test;

@Test
public class EventsUnitTest
{

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBindWithoutProducer()
    {
        Events.bind(null, new EventsTestConsumer());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBindWithoutConsumer()
    {
        Events.bind(new Object(), new Object[]{null});
    }

    @Test
    public void testFire() throws InterruptedException
    {
        final Object producer = new Object();
        final EventsTestConsumer consumerA = new EventsTestConsumer();
        final EventsTestConsumer consumerB = new EventsTestConsumer();

        Events.bind(producer, consumerA);
        Events.fire(producer, "Event #1");
        Events.bind(producer, consumerB);

        consumerA.waitForSize(1);
        assert consumerB.size() == 0;

        assert "Event #1".equals(consumerA.popEvent().getEvent());

        Events.fire(producer, "Event #2");

        consumerA.waitForSize(1);
        consumerB.waitForSize(1);

        assert "Event #2".equals(consumerA.popEvent().getEvent());
        assert "Event #2".equals(consumerB.popEvent().getEvent());

        Events.unbind(producer, consumerB);

        Events.fire(producer, "Event #3");

        consumerA.waitForSize(1);
        assert consumerB.size() == 0;

        assert "Event #3".equals(consumerA.popEvent().getEvent());
    }

    @Test
    public void testFireToInstancesOf() throws InterruptedException
    {
        Integer producer = new Integer(0);
        EventsTestConsumer consumerOnObject = new EventsTestConsumer();
        EventsTestConsumer consumerOnNumber = new EventsTestConsumer();
        EventsTestConsumer consumerOnInteger = new EventsTestConsumer();
        EventsTestConsumer consumerOnLong = new EventsTestConsumer();

        Events.bind(Object.class, consumerOnObject);
        Events.bind(Number.class, consumerOnNumber);
        Events.bind(Integer.class, consumerOnInteger);
        Events.bind(Long.class, consumerOnLong);

        Events.fire(producer, "Event #1");

        consumerOnObject.waitForSize(1);
        consumerOnNumber.waitForSize(1);
        consumerOnInteger.waitForSize(1);
        assert consumerOnLong.size() == 0;

        assert "Event #1".equals(consumerOnObject.popEvent().getEvent());
        assert "Event #1".equals(consumerOnNumber.popEvent().getEvent());
        assert "Event #1".equals(consumerOnInteger.popEvent().getEvent());

        Events.unbind(Object.class, consumerOnNumber);
        Events.unbind(Number.class, consumerOnInteger);
        Events.unbind(Number.class, consumerOnLong);

        Events.fire(producer, "Event #2");

        consumerOnObject.waitForSize(1);
        assert consumerOnNumber.size() == 0;
        assert consumerOnInteger.size() == 0;
        assert consumerOnLong.size() == 0;

        assert "Event #2".equals(consumerOnObject.popEvent().getEvent());
    }

    @Test
    public void testFireToInstancesOfSpecialA() throws InterruptedException
    {
        Integer producer = new Integer(0);
        EventsTestConsumer consumer = new EventsTestConsumer();

        Events.bind(Object.class, consumer);
        Events.bind(Number.class, consumer);
        Events.bind(producer, consumer);
        Events.fire(producer, "Event #1");

        consumer.waitForSize(3);

        assert "Event #1".equals(consumer.popEvent().getEvent());
        assert "Event #1".equals(consumer.popEvent().getEvent());
        assert "Event #1".equals(consumer.popEvent().getEvent());

        Events.unbind(Integer.class, consumer);
        Events.fire(producer, "Event #1");

        consumer.waitForSize(2);

        assert "Event #1".equals(consumer.popEvent().getEvent());
        assert "Event #1".equals(consumer.popEvent().getEvent());

        Events.unbind(Object.class, consumer);
        assert consumer.size() == 0;
    }

    @Test
    public void testDisable() throws InterruptedException
    {
        final Object producer = new Object();
        final EventsTestConsumer consumer = new EventsTestConsumer();

        Events.bind(producer, consumer);
        Events.fire(producer, "Event #1");
        Events.disable();
        Events.fire(producer, "Event #2");
        Events.fire(producer, "Event #3");
        Events.fire(producer, "Event #4");
        Events.enable();
        Events.fire(producer, "Event #5");

        consumer.waitForSize(2);

        assert "Event #5".equals(consumer.popEvent().getEvent());
        assert "Event #1".equals(consumer.popEvent().getEvent());
    }

    public void testCleanupConsumer()
    {
        // final Object producer = new Object();
        // final TestConsumer consumer = new TestConsumer();
        //
        // Events.bind(producer, consumer);

    }

}
