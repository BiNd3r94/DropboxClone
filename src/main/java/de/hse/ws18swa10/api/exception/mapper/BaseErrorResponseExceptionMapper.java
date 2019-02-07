package de.hse.ws18swa10.api.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import de.hse.ws18swa10.api.response.ErrorResponse;

public abstract class BaseErrorResponseExceptionMapper<E extends Throwable> 
	implements ExceptionMapper<E>
{	
	protected Response buildResponse(final ErrorResponse errorResponse, int status)
	{
		return Response
			.status(status)
			.entity(errorResponse)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}
}
