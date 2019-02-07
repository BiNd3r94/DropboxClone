package de.hse.ws18swa10.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hse.ws18swa10.exception.EntityValidationException;

public abstract class BaseEntityValidator<T> implements EntityValidatorInterface<T>
{
	protected final List<String> missingAttributes = new ArrayList<>();
	protected final Map<String, String> invalidAttributes = new HashMap<>();
	
	protected void addRequiredAttribute(String name, Object attribute)
	{
		if (attribute == null || (attribute instanceof String && attribute == "")) {
			missingAttributes.add(name);
		}
	}
	
	protected void addRequiredAttributeIgnoreString(String name, Object attribute)
	{
		if (attribute == null) {
			missingAttributes.add(name);
	}
	}
	protected boolean isMissingAttribute(String name)
	{
		return missingAttributes.contains(name);
	}
	
	protected void throwOnMissingOrInvalidAttributes() throws EntityValidationException
	{
		if (! missingAttributes.isEmpty() || ! invalidAttributes.isEmpty()) {
			throw new EntityValidationException(missingAttributes, invalidAttributes);
		}
	}
}
