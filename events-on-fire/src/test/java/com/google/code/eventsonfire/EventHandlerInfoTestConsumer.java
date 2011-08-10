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
