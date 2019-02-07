package de.hse.ws18swa10.api.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import de.hse.ws18swa10.api.response.ErrorResponse;

@Provider
public class UncaughtExceptionMapper extends BaseErrorResponseExceptionMapper<Throwable>
{	
	@Override
	public Response toResponse(Throwable exception)
	{
		int status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		
		return buildErrorResponse(exception, status);
	}
	
	private Response buildErrorResponse(final Throwable exception, int status)
	{
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatusCode(status);
		errorResponse.setMessage(exception.getMessage());
		
		return buildResponse(errorResponse, status);
	}
}
