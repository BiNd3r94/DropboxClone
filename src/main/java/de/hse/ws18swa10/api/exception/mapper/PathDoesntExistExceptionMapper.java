package de.hse.ws18swa10.api.exception.mapper;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import de.hse.ws18swa10.api.response.BadRequestResponse;
import de.hse.ws18swa10.exception.PathDoesntExistException;

@Provider
public class PathDoesntExistExceptionMapper 
	extends BaseErrorResponseExceptionMapper<PathDoesntExistException>
{
	private static final String FILE_UPLOAD_TARGET_PATH_KEY = "targetPath";
	
	@Override
	public Response toResponse(PathDoesntExistException exception)
	{
		int status = Status.BAD_REQUEST.getStatusCode();
		
		Map<String, String> errors = new HashMap<>();
		errors.put(FILE_UPLOAD_TARGET_PATH_KEY, exception.getMessage());
		
		BadRequestResponse badRequestResponse = BadRequestResponse.createValidationResponse(errors);
		badRequestResponse.setStatusCode(status);
		badRequestResponse.setMessage(exception.getMessage());
		
		return buildResponse(badRequestResponse, status);
	}
}
