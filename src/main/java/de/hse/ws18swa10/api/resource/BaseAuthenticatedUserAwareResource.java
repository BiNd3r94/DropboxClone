package de.hse.ws18swa10.api.resource;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import de.hse.ws18swa10.entity.User;

abstract public class BaseAuthenticatedUserAwareResource
{
	@Inject @Context
	private ContainerRequestContext context;
	
	public User getAuthenticatedUser()
	{
		return (User) context.getProperty("authenticatedUser");
	}
	
	public User getAuthenticatedUserOrDeny(final Integer id) throws ForbiddenException
	{
		User user = getAuthenticatedUser();
		
		if (user.getId().intValue() != id.intValue()) {
			throw new ForbiddenException();
		}
		
		return user;
	}
	
	public void denyIfNotAuthenticatedUser(final Integer id) throws ForbiddenException
	{
		User user = getAuthenticatedUser();
		
		if (user.getId().intValue() != id.intValue()) {
			throw new ForbiddenException();
		}
	}
}
