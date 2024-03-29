/* Copyright (c) 2011-2013 events-on-fire Team
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
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */
package com.google.code.eventsonfire;

import org.testng.annotations.Test;

import com.google.code.eventsonfire.Action.Type;

/**
 * Unit test for the {@link Action} class
 * 
 * @author Manfred Hantschel
 */
@Test
public class ActionUnitTest
{

    @Test
    public void testConstructor()
    {
        final Action action = new Action(Type.FIRE, "producer", "parameter", 42);

        assert Type.FIRE.equals(action.getType());
        assert "producer".equals(action.getProducer());
        assert "parameter".equals(action.getParameter());
        assert 42 == action.getNanosToTrigger();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorFailA()
    {
        new Action(null, "producer", "parameter", 42);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorFailB()
    {
        new Action(null, "producer", "parameter", 42);
    }

    @Test
    public void testHashCode()
    {
        final Action action = new Action(Type.FIRE, "producer", "parameter", 42);

        assert action.hashCode() == new Action(Type.FIRE, "producer", "parameter", 42).hashCode();
        assert action.hashCode() != new Action(Type.BIND, "producer", "parameter", 42).hashCode();
        assert action.hashCode() != new Action(Type.FIRE, "foobar", "parameter", 42).hashCode();
        assert action.hashCode() != new Action(Type.FIRE, "producer", "foobar", 42).hashCode();
    }

    @Test
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value="EC_NULL_ARG", justification="Testcase")
    public void testEquals()
    {
        final Action action = new Action(Type.FIRE, "producer", "parameter", 42);

        assert action.equals(action);
        assert !action.equals(null);
        assert !action.equals(new Object());
        assert !action.equals(new Action(Type.FIRE, new String("producer"), new String("parameter"), 42));
    }
}
