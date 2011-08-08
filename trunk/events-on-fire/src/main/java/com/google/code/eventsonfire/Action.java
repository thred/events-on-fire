package com.google.code.eventsonfire;

/**
 * An action within the {@link Events} object. All references are hard ones, because this object should not live long.
 * 
 * @author Manfred HANTSCHEL
 */
class Action
{

	enum Type
	{
		BIND, UNBIND, FIRE
	}

	private final Type type;
	private final Object producer;
	private final Object parameter;

	/**
	 * Creates a new action.
	 * 
	 * @param type the type of the action, must not be null
	 * @param producer the producer, must not be null
	 * @param parameter the parameter
	 * @throws IllegalArgumentException if the action or the producer is null
	 */
	public Action(final Type type, final Object producer, final Object parameter) throws IllegalArgumentException
	{
		super();

		if (type == null)
		{
			throw new IllegalArgumentException("Type is null");
		}

		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		this.type = type;
		this.producer = producer;
		this.parameter = parameter;
	}

	/**
	 * Returns the type of the action.
	 * 
	 * @return the type, never null
	 */
	public Type getType()
	{
		return type;
	}

	/**
	 * Returns the producer
	 * 
	 * @return the producer, never null
	 */
	public Object getProducer()
	{
		return producer;
	}

	/**
	 * Returns the parameter
	 * 
	 * @return the parameter
	 */
	public Object getParameter()
	{
		return parameter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return type.hashCode() ^ producer.hashCode() ^ ((parameter != null) ? parameter.hashCode() : -1);
	}

	/**
	 * {@inheritDoc}
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

		if (!(obj instanceof Action))
		{
			return false;
		}

		final Action link = (Action) obj;

		return (type == link.type) && (producer == link.producer) && (parameter == link.parameter);
	}

}
