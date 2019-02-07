package de.hse.ws18swa10.api.exception.mapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import de.hse.ws18swa10.api.response.ErrorResponse;

@Provider
public class WebApplicationExceptionMapper 
	extends BaseErrorResponseExceptionMapper<WebApplicationException>
{
	@Override
	public Response toResponse(WebApplicationException exception)
	{
		int status = exception.getResponse().getStatus();
		
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
