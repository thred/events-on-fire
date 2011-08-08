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

import org.testng.annotations.Test;

@Test
public class EventsUnitTest
{

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testBindWithoutProducer()
	{
		Events.bind(null, new TestConsumer());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testBindWithoutConsumer()
	{
		Events.bind(new Object(), null);
	}

	@Test
	public void testFire() throws InterruptedException
	{
		final Object producer = new Object();
		final TestConsumer consumerA = new TestConsumer();
		final TestConsumer consumerB = new TestConsumer();

		Events.bind(producer, consumerA);
		Events.fire(producer, "Event #1");
		Events.bind(producer, consumerB);

		consumerA.waitForSize(1);
		assert consumerB.size() == 0;
		
		assert "Event #1".equals(consumerA.popEvent());
		
		Events.fire(producer, "Event #2");
		
		consumerA.waitForSize(1);
		consumerB.waitForSize(1);

		assert "Event #2".equals(consumerA.popEvent());
		assert "Event #2".equals(consumerB.popEvent());
		
		Events.unbind(producer, consumerB);
		
		Events.fire(producer, "Event #3");

		consumerA.waitForSize(1);
		assert consumerB.size() == 0;

		assert "Event #3".equals(consumerA.popEvent());

	}
	
	@Test
	public void testDisable() throws InterruptedException
	{
		final Object producer = new Object();
		final TestConsumer consumer = new TestConsumer();
		
		Events.bind(producer, consumer);
		Events.fire(producer, "Event #1");
		Events.disable();
		Events.fire(producer, "Event #2");
		Events.fire(producer, "Event #3");
		Events.fire(producer, "Event #4");
		Events.enable();
		Events.fire(producer, "Event #5");
		
		consumer.waitForSize(2);
		
		assert "Event #5".equals(consumer.popEvent());
		assert "Event #1".equals(consumer.popEvent());
	}
	
	public void testCleanupConsumer() {
//		final Object producer = new Object();
//		final TestConsumer consumer = new TestConsumer();
//		
//		Events.bind(producer, consumer);
		
		
		
	}

}
