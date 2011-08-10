package com.google.code.eventsonfire;


public class EventHandlerInfoTestConsumer extends AbstractTestConsumer
{

	public EventHandlerInfoTestConsumer()
	{
		super();
	}

	@EventHandler
	public void invalidParameters(final CharSequence producer, final Number event, final Object invalidParameter)
	{
		assert false : "Not to be called";
	}

	@EventHandler
	public String invalidReturnType(final Number event)
	{
		assert false : "Not to be called";

		return null;
	}

	public void invalidAnnotation(final Number event)
	{
		assert false : "Not to be called";
	}

	@EventHandler
	public void singleParameterNotAnnotated(final Number event)
	{
		pushEvent("singleParameterNotAnnotated", null, event);
	}

	@EventHandler(event = Number.class)
	public void singleParameterAnnotated(final Number event)
	{
		pushEvent("singleParameterAnnotated", null, event);
	}

	@EventHandler(event = {
	    Number.class, String.class
	})
	public void singleParameterInvalidAnnotated(final Number event)
	{
		pushEvent("singleParameterInvalidAnnotated", null, event);
	}

	@EventHandler(event = {
	    Integer.class, Double.class
	})
	public void singleParameterMultiAnnotated(final Number event)
	{
		pushEvent("singleParameterMultiAnnotated", null, event);
	}

	@EventHandler(producer = CharSequence.class)
	public void singleParameterProducerAnnotated(final Number event)
	{
		pushEvent("singleParameterProducerAnnotated", null, event);
	}

	@EventHandler
	public void doubleParameterNotAnnotated(final CharSequence producer, final Number event)
	{
		pushEvent("doubleParameterNotAnnotated", producer, event);
	}

	@EventHandler(producer = CharSequence.class, event = Number.class)
	public void doubleParameterAnnotated(final Object producer, final Number event)
	{
		pushEvent("doubleParameterAnnotated", String.valueOf(producer), event);
	}

	@EventHandler(producer = Number.class)
	public void doubleParameterInvalidAnnotated(final CharSequence producer, final Number event)
	{
		pushEvent("doubleParameterInvalidAnnotated", producer, event);
	}

}
