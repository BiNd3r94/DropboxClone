package de.hse.ws18swa10.exception.persistence;

import de.hse.ws18swa10.exception.PersistenceException;

public class UniqueConstraintViolationException extends PersistenceException
{
	private static final long serialVersionUID = 8952281782404262686L;
	private final String key;
	
	public UniqueConstraintViolationException(final Throwable cause, final String key)
	{
		super(cause);
		this.key = key;
	}
	
	public String getKey()
	{
		return key;
	}
}
