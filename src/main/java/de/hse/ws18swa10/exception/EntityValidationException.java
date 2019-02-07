package de.hse.ws18swa10.exception;

import java.util.List;
import java.util.Map;

public class EntityValidationException extends Exception
{
	private static final long serialVersionUID = 4425756629659579557L;
	
	private final List<String> missingAttributes;
	private final Map<String, String> invalidAttributes;
	
	public EntityValidationException(
		final List<String> missingAttributes, 
		final Map<String, String> invalidAttributes
	) {
		super("Validation failed");
		this.missingAttributes = missingAttributes;
		this.invalidAttributes = invalidAttributes;
	}
	
	public List<String> getMissingAttributes()
	{
		return missingAttributes;
	}

	public Map<String, String> getInvalidAttributes()
	{
		return invalidAttributes;
	}
}
