package com.google.code.eventsonfire;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * A weak reference with a hashCode and an equals method that checks for identity of the referent. Warning: the equals
 * methods only works, if the referent has not been garbage collected. After the removal of the referent the equals
 * method only checks for identity of the reference, not the referent.
 * 
 * @author Manfred HANTSCHEL
 * @param <TYPE> the type of the reference
 */
class WeakIdentityReference<TYPE> extends WeakReference<TYPE>
{

	private final int hashCode;

	/**
	 * Creates the weak identity reference that refers to the specified referent. The reference is not registered to any
	 * queue.
	 * 
	 * @param referent the object referenced by the weak reference, must not be null
	 * @throws IllegalArgumentException if the referent is null
	 */
	public WeakIdentityReference(final TYPE referent) throws IllegalArgumentException
	{
		this(referent, null);
	}

	/**
	 * Creates the weak identity reference with the specified referent. The reference is registered with the specified
	 * queue.
	 * 
	 * @param referent the object referenced by the weak reference, must not be null
	 * @param queue the queue with which the reference is to be registered, or null if registration is not required
	 * @throws IllegalArgumentException if the referent is null
	 */
	public WeakIdentityReference(final TYPE referent, final ReferenceQueue<? super TYPE> queue)
	    throws IllegalArgumentException
	{
		super(referent, queue);

		if (referent == null)
		{
			throw new IllegalArgumentException("Referent is null");
		}

		hashCode = referent.hashCode();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return hashCode;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
		{
			return true;
		}

		if (obj == null)
		{
			return false;
		}

		if (!(obj instanceof WeakIdentityReference))
		{
			return false;
		}

		final TYPE referent = get();

		if (referent == null)
		{
			return false;
		}

		return referent == ((WeakIdentityReference<?>) obj).get();
	}
}
