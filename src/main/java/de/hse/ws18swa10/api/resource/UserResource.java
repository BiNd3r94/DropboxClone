package de.hse.ws18swa10.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.dao.UserDao;
import de.hse.ws18swa10.dao.container.UserDaoContainer;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.EntityValidationException;
import de.hse.ws18swa10.exception.PersistenceException;
import de.hse.ws18swa10.validator.PasswordValidator;
import de.hse.ws18swa10.validator.UserValidator;

@Path("users")
@SecuredResource
public class UserResource
{
	private final UserDao dao = UserDaoContainer.getInstance();
	private final UserValidator validator = new UserValidator(new PasswordValidator());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByEmail(@QueryParam("email") String email) throws NotFoundException
	{		
		User user = dao.findByEmail(email);
		
		if (user == null) {
			throw new NotFoundException();
		}
		
		return Response.ok().entity(user).build();
	}
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") Integer id) throws NotFoundException
	{		
		User user = dao.find(id);
		
		if (user == null) {
			throw new NotFoundException();
		}
		
		return Response.ok().entity(user).build();
	}
	
	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(final User user) 
		throws EntityValidationException, PersistenceException
	{					
		validator.validate(user);
		
		dao.persist(user);
		
		return Response.ok().entity(user).build();
	}
	
	@Path("{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") Integer id)
		throws PersistenceException
	{				
		dao.remove(id);
		
		return Response.ok().build();
	}
}
