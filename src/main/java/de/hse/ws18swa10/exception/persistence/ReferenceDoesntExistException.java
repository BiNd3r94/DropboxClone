package de.hse.ws18swa10.exception.persistence;

import de.hse.ws18swa10.exception.PersistenceException;

public class ReferenceDoesntExistException extends PersistenceException
{
	private static final long serialVersionUID = 8643960075920652907L;

	public ReferenceDoesntExistException(Throwable cause)
	{
		super(cause);
	}
}
