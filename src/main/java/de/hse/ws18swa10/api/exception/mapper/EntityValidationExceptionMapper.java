package de.hse.ws18swa10.api.exception.mapper;

import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import de.hse.ws18swa10.api.exception.EntityValidationExceptionReducer;
import de.hse.ws18swa10.api.response.BadRequestResponse;
import de.hse.ws18swa10.exception.EntityValidationException;

@Provider
public class EntityValidationExceptionMapper 
	extends BaseErrorResponseExceptionMapper<EntityValidationException>
{
	private final EntityValidationExceptionReducer reducer = new EntityValidationExceptionReducer();
	
	@Override
	public Response toResponse(EntityValidationException exception)
	{
		int status = Status.BAD_REQUEST.getStatusCode();
		
		Map<String, String> errors = reducer.reduceToKeyValueMap(exception);
		
		BadRequestResponse badRequestResponse = BadRequestResponse.createValidationResponse(errors);
		badRequestResponse.setStatusCode(status);
		badRequestResponse.setMessage(exception.getMessage());
		
		return buildResponse(badRequestResponse, status);
	}
}
