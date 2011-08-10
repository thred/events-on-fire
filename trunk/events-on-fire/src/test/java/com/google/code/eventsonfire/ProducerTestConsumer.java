package com.google.code.eventsonfire;

public class ProducerTestConsumer extends AbstractTestConsumer
{

	public ProducerTestConsumer()
	{
		super();
	}

	@EventHandler
	public void stringEventHandler(final String event)
	{
		pushEvent("stringEventHandler", null, event);
	}
	
	@EventHandler
	public void integerEventHandler(final Integer event)
	{
		pushEvent("integerEventHandler", null, event);
	}

}
