package de.hse.ws18swa10.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.dao.FileRequestDao;
import de.hse.ws18swa10.dao.container.FileRequestDaoContainer;
import de.hse.ws18swa10.entity.FileRequest;
import de.hse.ws18swa10.exception.EntityValidationException;
import de.hse.ws18swa10.exception.PersistenceException;
import de.hse.ws18swa10.util.DefaultTokenGenerator;
import de.hse.ws18swa10.util.TokenGeneratorInterface;
import de.hse.ws18swa10.validator.FileRequestValidator;

@Path("file-requests")
@SecuredResource
public class FileRequestResource extends BaseAuthenticatedUserAwareResource
{
	private final FileRequestDao dao = FileRequestDaoContainer.getInstance();
	private final FileRequestValidator validator = new FileRequestValidator();
	private final TokenGeneratorInterface tokenGenerator = new DefaultTokenGenerator();
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") final Integer id) throws NotFoundException
	{		
		FileRequest fileRequest = tryToGetFileRequest(id);
		
		return Response.ok().entity(fileRequest).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(final FileRequest fileRequest) 
		throws EntityValidationException, PersistenceException
	{	
		validator.validate(fileRequest);
		
		fileRequest.setToken(tokenGenerator.generateToken());
		
		dao.persist(fileRequest);
		
		return Response.ok().entity(fileRequest).build();
	}
	
	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(final FileRequest fileRequest) 
		throws EntityValidationException, PersistenceException
	{		
		validator.validate(fileRequest);
		
		dao.persist(fileRequest);
		
		return Response.ok().entity(fileRequest).build();
	}
	
	@Path("{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") final Integer id)
		throws PersistenceException
	{		
		dao.remove(id);
		
		return Response.ok().build();
	}
	
	private FileRequest tryToGetFileRequest(Integer id) throws NotFoundException
	{
		FileRequest fileRequest = dao.find(id);
		
		if (fileRequest == null) {
			throw new NotFoundException();
		}
		
		return fileRequest;
	}
}
