package de.hse.ws18swa10.api.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hse.ws18swa10.exception.EntityValidationException;

public class EntityValidationExceptionReducer
{	
	public Map<String, String> reduceToKeyValueMap(final EntityValidationException exception)
	{
		Map<String, String> errors = new HashMap<>();
		
		List<String> missingAttributes = exception.getMissingAttributes();
		missingAttributes.forEach(key -> errors.put(key, key + " is required"));
		
		Map<String, String> invalidAttributes = exception.getInvalidAttributes();
		invalidAttributes.forEach((key, value) -> errors.put(key, value));
		
		return errors;
	}
}
