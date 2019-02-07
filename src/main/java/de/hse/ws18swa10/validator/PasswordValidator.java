package de.hse.ws18swa10.validator;

import de.hse.ws18swa10.exception.ValidationException;

public class PasswordValidator
{
	private static final int MIN_LENGTH = 8;
	private static final String ALLOWED_CHAR_REGEX = "[a-zA-Z0-9!?$\\*]";
	
	public void validatePassword(String password) throws ValidationException
	{
		if (password.length() < MIN_LENGTH) {
			throw new ValidationException(
				"Password needs to be at least " + MIN_LENGTH + " characters long."
			);
		}
		
		if (! password.matches(ALLOWED_CHAR_REGEX)) {
			throw new ValidationException("Password contains invalid characters.");
		}
	}
}
