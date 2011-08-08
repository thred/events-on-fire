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

import java.lang.ref.Reference;

import org.testng.annotations.Test;

/**
 * Tests the {@link Producer} class
 * 
 * @author Manfred HANTSCHEL
 */
@Test
public class ProducerUnitTest
{

	@Test
	public void testConsumerReferences()
	{
		final Reference<TestConsumer> consumerA = new WeakIdentityReference<TestConsumer>(new TestConsumer());
		final Reference<TestConsumer> consumerB = new WeakIdentityReference<TestConsumer>(new TestConsumer());
		final Producer producer = new Producer();

		assert !producer.contains(consumerA);
		assert !producer.contains(consumerB);
		assert producer.isEmpty();

		producer.add(consumerA);

		assert producer.contains(consumerA);
		assert !producer.contains(consumerB);
		assert !producer.isEmpty();

		producer.add(consumerB);

		assert producer.contains(consumerA);
		assert producer.contains(consumerB);
		assert !producer.isEmpty();

		producer.remove(consumerA);

		assert !producer.contains(consumerA);
		assert producer.contains(consumerB);
		assert !producer.isEmpty();

		producer.remove(consumerB);

		assert !producer.contains(consumerA);
		assert !producer.contains(consumerB);
		assert producer.isEmpty();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConsumerReferencesFailA()
	{
		final Reference<String> reference = new WeakIdentityReference<String>("not a consumer");
		final Producer producer = new Producer();

		producer.add(reference);
	}

	@Test
	public void testFire()
	{
		final TestConsumer consumer = new TestConsumer();
		final Reference<TestConsumer> reference = new WeakIdentityReference<TestConsumer>(consumer);
		final Producer producer = new Producer();

		producer.add(reference);

		producer.fire("Event #1");

		assert "Event #1".equals(consumer.popEvent());

		producer.fire(Integer.valueOf(42));

		assert Integer.valueOf(42).equals(consumer.popEvent());

		producer.fire(new Object());

		assert consumer.size() == 0;
	}

}
