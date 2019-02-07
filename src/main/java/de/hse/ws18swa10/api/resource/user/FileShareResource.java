package de.hse.ws18swa10.api.resource.user;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.file.user.DefaultUserSpacePathEncoder;
import de.hse.ws18swa10.api.file.user.UserPathEncoderInterface;
import de.hse.ws18swa10.api.resource.BaseAuthenticatedUserAwareResource;
import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.dao.FileShareDao;
import de.hse.ws18swa10.dao.container.FileShareDaoContainer;
import de.hse.ws18swa10.entity.FileShare;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.EntityValidationException;
import de.hse.ws18swa10.exception.PersistenceException;
import de.hse.ws18swa10.validator.FileShareValidator;

@Path("users/{ownerId}/file-shares")
@SecuredResource
public class FileShareResource extends BaseAuthenticatedUserAwareResource
{
	private final FileShareDao dao = FileShareDaoContainer.getInstance();
	private final FileShareValidator validator = new FileShareValidator();
	private final UserPathEncoderInterface encoder = new DefaultUserSpacePathEncoder();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@PathParam("ownerId") final Integer ownerId)
	{
		User owner = getAuthenticatedUserOrDeny(ownerId);
		
		List<FileShare> fileShares = dao.findAllOwnedBy(owner);
		fileShares.stream().forEach((fs) -> { fs.setPath(encoder.decode(fs.getPath())); });
		
		return Response.ok(fileShares).build();
	}
	
	@Path("inverse")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAsMember(@PathParam("ownerId") final Integer ownerId)
	{
		User member = getAuthenticatedUserOrDeny(ownerId);
		
		List<FileShare> fileShares = dao.findAllForMember(member);
		fileShares.stream().forEach((fs) -> { fs.setPath(encoder.decode(fs.getPath())); });
		
		return Response.ok(fileShares).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(
		@PathParam("ownerId") final Integer ownerId, 
		final FileShare fileShare
	) throws EntityValidationException, PersistenceException {		
		User owner = getAuthenticatedUserOrDeny(ownerId);
		fileShare.setOwner(owner);
		
		validator.validate(fileShare);
		
		dao.persist(fileShare);
		
		return Response.ok().entity(fileShare).build();
	}
	
	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(
		@PathParam("ownerId") final Integer ownerId, 
		final FileShare fileShare
	) throws EntityValidationException, PersistenceException {		
		User owner = getAuthenticatedUserOrDeny(ownerId);
		fileShare.setOwner(owner);
		
		validator.validate(fileShare);
		
		dao.persist(fileShare);
		
		return Response.ok().entity(fileShare).build();
	}
	
	@Path("{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("ownerId") final Integer ownerId, 
		@PathParam("id") Integer id
	) throws PersistenceException {		
		denyIfNotAuthenticatedUser(ownerId);
		
		dao.remove(id);
		
		return Response.ok().build();
	}
}
