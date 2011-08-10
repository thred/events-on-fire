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

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import com.google.code.eventsonfire.AbstractTestConsumer.Event;

@Test
public class EventHandlerInfoUnitTest
{

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testNoMethod()
	{
		new EventHandlerInfo(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*Return type.*")
	public void testInvalidReturnType() throws NoSuchMethodException
	{
		final Method method = EventHandlerInfoTestConsumer.class.getMethod("invalidReturnType", Number.class);

		new EventHandlerInfo(method);
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*two parameters.*")
	public void testInvalidParameters() throws NoSuchMethodException
	{
		final Method method =
		    EventHandlerInfoTestConsumer.class.getMethod("invalidParameters", CharSequence.class, Number.class, Object.class);

		new EventHandlerInfo(method);
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*annotation.*missing.*")
	public void testInvalidAnnotation() throws NoSuchMethodException
	{
		final Method method = EventHandlerInfoTestConsumer.class.getMethod("invalidAnnotation", Number.class);

		new EventHandlerInfo(method);
	}

	@Test
	public void testSingleParameterNotAnnotated() throws NoSuchMethodException
	{
		final Method method = EventHandlerInfoTestConsumer.class.getMethod("singleParameterNotAnnotated", Number.class);
		final EventHandlerInfo eventHandlerMethod = new EventHandlerInfo(method);
		final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();

		eventHandlerMethod.invoke("ProducerA", consumer, new Object());
		assert consumer.isEmpty();

		eventHandlerMethod.invoke("ProducerB", consumer, Byte.valueOf((byte) 84));
		assert Byte.valueOf((byte) 84).equals(consumer.popEvent().getEvent());

		eventHandlerMethod.invoke("ProducerC", consumer, Integer.valueOf(42));
		assert Integer.valueOf(42).equals(consumer.popEvent().getEvent());

		eventHandlerMethod.invoke("ProducerD", consumer, Double.valueOf(21));
		assert Double.valueOf(21).equals(consumer.popEvent().getEvent());
	}

	@Test
	public void testSingleParameterAnnotated() throws NoSuchMethodException
	{
		final Method method = EventHandlerInfoTestConsumer.class.getMethod("singleParameterAnnotated", Number.class);
		final EventHandlerInfo eventHandlerMethod = new EventHandlerInfo(method);
		final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();

		eventHandlerMethod.invoke("ProducerA", consumer, new Object());
		assert consumer.isEmpty();

		eventHandlerMethod.invoke("ProducerB", consumer, Byte.valueOf((byte) 84));
		assert Byte.valueOf((byte) 84).equals(consumer.popEvent().getEvent());

		eventHandlerMethod.invoke("ProducerC", consumer, Integer.valueOf(42));
		assert Integer.valueOf(42).equals(consumer.popEvent().getEvent());

		eventHandlerMethod.invoke("ProducerD", consumer, Double.valueOf(21));
		assert Double.valueOf(21).equals(consumer.popEvent().getEvent());
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*String cannot be assigned.*")
	public void testSingleParameterInvalidAnnotated() throws NoSuchMethodException
	{
		final Method method = EventHandlerInfoTestConsumer.class.getMethod("singleParameterInvalidAnnotated", Number.class);

		new EventHandlerInfo(method);
	}

	@Test
	public void testSingleParameterMultiAnnotated() throws NoSuchMethodException
	{
		final Method method = EventHandlerInfoTestConsumer.class.getMethod("singleParameterMultiAnnotated", Number.class);
		final EventHandlerInfo eventHandlerMethod = new EventHandlerInfo(method);
		final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();

		eventHandlerMethod.invoke("ProducerA", consumer, new Object());
		assert consumer.isEmpty();

		eventHandlerMethod.invoke("ProducerB", consumer, Byte.valueOf((byte) 84));
		assert consumer.isEmpty();

		eventHandlerMethod.invoke("ProducerC", consumer, Integer.valueOf(42));
		assert Integer.valueOf(42).equals(consumer.popEvent().getEvent());

		eventHandlerMethod.invoke("ProducerD", consumer, Double.valueOf(21));
		assert Double.valueOf(21).equals(consumer.popEvent().getEvent());
	}

	@Test
	public void testSingleParameterProducerAnnotated() throws NoSuchMethodException
	{
		final Method method = EventHandlerInfoTestConsumer.class.getMethod("singleParameterProducerAnnotated", Number.class);
		final EventHandlerInfo eventHandlerMethod = new EventHandlerInfo(method);
		final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();

		eventHandlerMethod.invoke("ProducerA", consumer, new Object());
		assert consumer.isEmpty();

		eventHandlerMethod.invoke(new Object(), consumer, Integer.valueOf(84));
		assert consumer.isEmpty();

		eventHandlerMethod.invoke("ProducerB", consumer, Integer.valueOf(42));
		assert Integer.valueOf(42).equals(consumer.popEvent().getEvent());
	}

	@Test
	public void testDoubleParameterNotAnnotated() throws NoSuchMethodException
	{
		final Method method =
		    EventHandlerInfoTestConsumer.class.getMethod("doubleParameterNotAnnotated", CharSequence.class, Number.class);
		final EventHandlerInfo eventHandlerMethod = new EventHandlerInfo(method);
		final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();

		eventHandlerMethod.invoke(new Object(), consumer, new Object());
		assert consumer.isEmpty();

		eventHandlerMethod.invoke("Producer", consumer, new Object());
		assert consumer.isEmpty();

		eventHandlerMethod.invoke(new Object(), consumer, Integer.valueOf(168));
		assert consumer.isEmpty();

		eventHandlerMethod.invoke("ProducerA", consumer, Byte.valueOf((byte) 84));
		Event event = consumer.popEvent();
		assert "ProducerA".equals(event.getProducer());
		assert Byte.valueOf((byte) 84).equals(event.getEvent());

		eventHandlerMethod.invoke("ProducerB", consumer, Integer.valueOf(42));
		event = consumer.popEvent();
		assert "ProducerB".equals(event.getProducer());
		assert Integer.valueOf(42).equals(event.getEvent());

		eventHandlerMethod.invoke("ProducerC", consumer, Double.valueOf(21));
		event = consumer.popEvent();
		assert "ProducerC".equals(event.getProducer());
		assert Double.valueOf(21).equals(event.getEvent());
	}

	@Test
	public void testDoubleParameterAnnotated() throws NoSuchMethodException
	{
		final Method method = EventHandlerInfoTestConsumer.class.getMethod("doubleParameterAnnotated", Object.class, Number.class);
		final EventHandlerInfo eventHandlerMethod = new EventHandlerInfo(method);
		final EventHandlerInfoTestConsumer consumer = new EventHandlerInfoTestConsumer();

		eventHandlerMethod.invoke(new Object(), consumer, Integer.valueOf(42));
		assert consumer.isEmpty();

		eventHandlerMethod.invoke("Producer", consumer, Integer.valueOf(21));
		assert consumer.popEvent().getEvent() == Integer.valueOf(21);
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*Number cannot be assigned.*")
	public void testDoubleParameterInvalidAnnotated() throws NoSuchMethodException
	{
		final Method method =
		    EventHandlerInfoTestConsumer.class.getMethod("doubleParameterInvalidAnnotated", CharSequence.class, Number.class);

		new EventHandlerInfo(method);
	}

}
