package de.hse.ws18swa10.exception;

public class PersistenceException extends Exception
{	
	private static final long serialVersionUID = 1888379179180197448L;

	public PersistenceException(final Throwable cause)
	{
		super(cause.getMessage());
	}
}
