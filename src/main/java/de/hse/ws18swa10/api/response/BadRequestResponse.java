package de.hse.ws18swa10.api.response;

import java.util.Map;

public final class BadRequestResponse extends ErrorResponse
{
	public static final String TYPE_VALIDATION = "validation";
	public static final String TYPE_PERSISTENCE = "persistence";
	
	private final Map<String, String> errors;
	private final String type;
	
	private BadRequestResponse(final Map<String, String> errors, final String type)
	{
		this.errors = errors;
		this.type = type;
	}
	
	public static BadRequestResponse createValidationResponse(final Map<String, String> errors)
	{
		return new BadRequestResponse(errors, TYPE_VALIDATION);
	}
	
	public static BadRequestResponse createPersistenceResponse(final Map<String, String> errors)
	{
		return new BadRequestResponse(errors, TYPE_PERSISTENCE);
	}
	
	public Map<String, String> getErrors()
	{
		return errors;
	}
	
	public String getType()
	{
		return type;
	}
}
