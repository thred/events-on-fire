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
