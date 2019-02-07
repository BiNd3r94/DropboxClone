package de.hse.ws18swa10.exception.persistence;

import de.hse.ws18swa10.exception.PersistenceException;

public class ForeignKeyConstraintViolationException extends PersistenceException
{	
	private static final long serialVersionUID = 4996785329639493122L;

	public ForeignKeyConstraintViolationException(final Throwable cause)
	{
		super(cause);
	}
}
