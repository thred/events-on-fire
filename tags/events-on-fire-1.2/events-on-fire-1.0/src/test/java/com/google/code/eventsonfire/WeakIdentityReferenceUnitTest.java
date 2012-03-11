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

import org.testng.annotations.Test;

/**
 * Unit test for the {@link WeakIdentityReference} class
 * 
 * @author Manfred Hantschel
 */
@Test
public class WeakIdentityReferenceUnitTest
{

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructor()
	{
		new WeakIdentityReference<Object>(null);
	}

	@Test
	public void testHashCode()
	{
		String referent = new String("Referent A");

		final WeakIdentityReference<String> reference = new WeakIdentityReference<String>(referent);

		assert reference.get() == referent;

		assert reference.hashCode() == "Referent A".hashCode();

		referent = null;

		System.gc();

		assert reference.get() == null : "Garbage collection does not perform well on your system...";

		assert reference.hashCode() == "Referent A".hashCode();
	}

	@Test
	public void testEquals()
	{
		String referentA = new String("Referent A");
		String referentA3 = new String("Referent A");
		String referentB = new String("Referent B");

		final WeakIdentityReference<String> referenceA1 = new WeakIdentityReference<String>(referentA);
		final WeakIdentityReference<String> referenceA2 = new WeakIdentityReference<String>(referentA);
		final WeakIdentityReference<String> referenceA3 = new WeakIdentityReference<String>(referentA3);
		final WeakIdentityReference<String> referenceB = new WeakIdentityReference<String>(referentB);

		assert referenceA1.get() == referentA;

		assert referenceA1.equals(referenceA1);
		assert !referenceA1.equals(null);
		assert !referenceA1.equals(new Object());
		assert referenceA1.equals(referenceA2);
		assert !referenceA1.equals(referenceA3);
		assert !referenceA1.equals(referenceB);

		referentA = null;
		referentB = null;

		System.gc();

		assert referenceA1.get() == null : "Garbage collection does not perform well on your system...";

		assert referenceA1.equals(referenceA1);
		assert !referenceA1.equals(referenceA2);
		assert !referenceA1.equals(referenceA3);
	}

}
