package de.hse.ws18swa10.api.exception.mapper;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import de.hse.ws18swa10.api.response.BadRequestResponse;
import de.hse.ws18swa10.exception.PersistenceException;
import de.hse.ws18swa10.exception.persistence.ForeignKeyConstraintViolationException;
import de.hse.ws18swa10.exception.persistence.ReferenceDoesntExistException;
import de.hse.ws18swa10.exception.persistence.UniqueConstraintViolationException;

@Provider
public class PersistenceExceptionMapper 
	extends BaseErrorResponseExceptionMapper<PersistenceException>
{
	@Override
	public Response toResponse(PersistenceException exception)
	{
		int status = Status.BAD_REQUEST.getStatusCode();
		
		Map<String, String> errors = new HashMap<>();
		String message = exception.getMessage().replaceAll(".*Exception:", "").trim();
		
		if (exception instanceof UniqueConstraintViolationException) {
			message = "A unique field has been tried to be duplicated.";
			errors.put("error", "duplicate_entry");
		} else if (exception instanceof ForeignKeyConstraintViolationException) {
			message = "This entity is referenced by other resources and cannot be removed.";
			errors.put("error", "foreign_key_failed");
		} else if (exception instanceof ReferenceDoesntExistException) {
			message = "Your request contained references to none existing resources.";
			errors.put("error", "reference_not_found");
		}
		
		BadRequestResponse badRequestResponse = BadRequestResponse.createPersistenceResponse(errors);
		badRequestResponse.setStatusCode(status);
		badRequestResponse.setMessage(message);
		
		return buildResponse(badRequestResponse, status);
	}
}
