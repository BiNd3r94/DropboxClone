package de.hse.ws18swa10.exception;

public class ValidationException extends Exception
{
	private static final long serialVersionUID = -4529488866251446710L;

	public ValidationException(String message)
	{
		super(message);
	}
}
