package de.hse.ws18swa10.validator;

import de.hse.ws18swa10.exception.EntityValidationException;

public interface EntityValidatorInterface<T>
{
	public void validate(T entity) throws EntityValidationException;
}
