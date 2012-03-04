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

import java.lang.reflect.Method;

/**
 * Default implementation of the {@link ErrorHandler}. Writes messages to System.err.
 * 
 * @author Manfred HANTSCHEL
 */
public class DefaultErrorHandler implements ErrorHandler
{

	/**
	 * Default constructor for the error handler
	 */
	public DefaultErrorHandler()
	{
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public void invocationFailed(final Object producer, final Object consumer, final Object event, final Method method, final String message, final Throwable cause)
	{
		System.err.println("Invocation of event handler failed: " + message);
		System.err.println("\tMethod:   " + method);
		System.err.println("\tProducer: " + producer);
		System.err.println("\tConsumer: " + consumer);
		System.err.println("\tEvent:    " + event);

		if (cause != null)
		{
			System.err.print("\tCause:    ");
			cause.printStackTrace(System.err);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void unhandledException(final String message, final Throwable cause)
	{
		System.err.println("UNHANDLED EXCEPTION: " + message);
		cause.printStackTrace(System.err);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interrupted(InterruptedException e)
	{
		System.err.println("Events thread got interrupted.");
		e.printStackTrace(System.err);
	}

}
