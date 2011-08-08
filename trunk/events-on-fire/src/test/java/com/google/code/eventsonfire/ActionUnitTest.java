package com.google.code.eventsonfire;

import org.testng.annotations.Test;

import com.google.code.eventsonfire.Action.Type;

/**
 * Unit test for the {@link Action} class
 * 
 * @author Manfred HANTSCHEL
 */
@Test
public class ActionUnitTest
{

	@Test
	public void testConstructor()
	{
		final Action action = new Action(Type.FIRE, "producer", "parameter");

		assert Type.FIRE.equals(action.getType());
		assert "producer".equals(action.getProducer());
		assert "parameter".equals(action.getParameter());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorFailA()
	{
		new Action(null, "producer", "parameter");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorFailB()
	{
		new Action(null, "producer", "parameter");
	}

	@Test
	public void testHashCode()
	{
		final Action action = new Action(Type.FIRE, "producer", "parameter");

		assert action.hashCode() == new Action(Type.FIRE, "producer", "parameter").hashCode();
		assert action.hashCode() != new Action(Type.BIND, "producer", "parameter").hashCode();
		assert action.hashCode() != new Action(Type.FIRE, "foobar", "parameter").hashCode();
		assert action.hashCode() != new Action(Type.FIRE, "producer", "foobar").hashCode();
	}

	@Test
	public void testEquals()
	{
		final Action action = new Action(Type.FIRE, "producer", "parameter");

		assert action.equals(action);
		assert !action.equals(null);
		assert !action.equals(new Object());
		assert !action.equals(new Action(Type.FIRE, new String("producer"), new String("parameter")));
	}
}
