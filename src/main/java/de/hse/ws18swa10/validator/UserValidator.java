package de.hse.ws18swa10.validator;

import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.EntityValidationException;
import de.hse.ws18swa10.exception.ValidationException;

public final class UserValidator extends BaseEntityValidator<User>
{
	private static final String EMAIL_KEY = "email";
	private static final String FIRST_NAME_KEY = "firstName";
	private static final String LAST_NAME_KEY = "lastName";
	private static final String PASSWORD_KEY = "password";
	
	private final PasswordValidator passwordValidator;
	
	public UserValidator(final PasswordValidator passwordValidator)
	{
		this.passwordValidator = passwordValidator;
	}
	
	public void validate(final User user) throws EntityValidationException
	{
		addRequiredAttribute(EMAIL_KEY, user.getEmail());
		addRequiredAttribute(FIRST_NAME_KEY, user.getFirstName());
		addRequiredAttribute(LAST_NAME_KEY, user.getLastName());
		addRequiredAttribute(PASSWORD_KEY, user.getPassword());
		
		validatePassword(user.getPassword());
		
		throwOnMissingOrInvalidAttributes();
	}
	
	private void validatePassword(String password)
	{
		if (! isMissingAttribute(PASSWORD_KEY)) {
			return;
		}
		
		try {
			passwordValidator.validatePassword(password);
		} catch(ValidationException e) {
			invalidAttributes.put(PASSWORD_KEY, e.getMessage());
		}
	}
}
