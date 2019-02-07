package de.hse.ws18swa10.api.resource.user;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.entity.StatusChange;
import de.hse.ws18swa10.api.resource.BaseAuthenticatedUserAwareResource;
import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.dao.FileRequestDao;
import de.hse.ws18swa10.dao.container.FileRequestDaoContainer;
import de.hse.ws18swa10.entity.FileRequest;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.validator.FileRequestValidator;

@Path("users/{requesterId}/file-requests")
@SecuredResource
public class FileRequestResource extends BaseAuthenticatedUserAwareResource
{
	private final FileRequestValidator validator = new FileRequestValidator();
	private final FileRequestDao dao = FileRequestDaoContainer.getInstance();
	
	@Path("status/open")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllOpen(@PathParam("requesterId") final Integer requesterId) 
		throws Exception
	{		
		User requester = getAuthenticatedUserOrDeny(requesterId);
		List<FileRequest> fileRequests = dao.findAllOpenRequestedBy(requester);
		
		return Response.ok().entity(fileRequests).build();
	}
	
	@Path("status/closed")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllClosed(@PathParam("requesterId") final Integer requesterId) 
		throws Exception
	{		
		User requester = getAuthenticatedUserOrDeny(requesterId);
		List<FileRequest> fileRequests = dao.findAllClosedRequestedBy(requester);
		
		return Response.ok().entity(fileRequests).build();
	}
	
	@Path("{id}/status")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStatus(
		@PathParam("requesterId") final Integer requesterId,
		@PathParam("id") final Integer id,
		final StatusChange statusChange
	) throws Exception {
		denyIfUserDoesntOwn(id, requesterId);
		
		FileRequest fileRequest = dao.find(id);
		fileRequest.setOpen(statusChange.isOpen());
		dao.persist(fileRequest);
		
		return Response.ok(statusChange).build();
	}
	
	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(
		@PathParam("requesterId") final Integer requesterId,
		@PathParam("id") final Integer id, 
		final FileRequest fileRequest
	) throws Exception {		
		denyIfUserDoesntOwn(id, requesterId);
		fileRequest.setId(id);
		
		validator.validate(fileRequest);
		
		dao.persist(fileRequest);
		
		return Response.ok().entity(fileRequest).build();
	}
	
	private void denyIfUserDoesntOwn(final Integer id, final Integer requesterId)
	{
		User requester = getAuthenticatedUserOrDeny(requesterId);
		
		if (! dao.wasRequestedBy(id, requester)) {
			throw new NotFoundException(
				"No file request found for user " + requesterId + " with id " + id
			);
		}
	}
}
