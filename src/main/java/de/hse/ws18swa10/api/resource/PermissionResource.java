package de.hse.ws18swa10.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.dao.PermissionDao;
import de.hse.ws18swa10.dao.container.PermissionDaoContainer;
import de.hse.ws18swa10.entity.Permission;

@Path("permissions")
@SecuredResource
public class PermissionResource
{
	private final PermissionDao permissionDao = PermissionDaoContainer.getInstance();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll()
	{		
		return Response.ok().entity(permissionDao.findAll()).build();
	}
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") Integer id) throws NotFoundException
	{		
		Permission permission = permissionDao.find(id);
		
		if (permission == null) {
			throw new NotFoundException();
		}
		
		return Response.ok().entity(permission).build();
	}
}
