package com.google.code.eventsonfire;

import org.testng.annotations.Test;

/**
 * Unit test for the {@link WeakIdentityReference} class
 * 
 * @author Manfred HANTSCHEL
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
