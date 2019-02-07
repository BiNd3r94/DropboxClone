package de.hse.ws18swa10.api.resource.publicly;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.file.UserSpaceInstallerFactory;
import de.hse.ws18swa10.api.file.user.UserSpaceInstallerInterface;
import de.hse.ws18swa10.dao.UserDao;
import de.hse.ws18swa10.dao.container.UserDaoContainer;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.validator.PasswordValidator;
import de.hse.ws18swa10.validator.UserValidator;

@Path("/public/users")
public class UserResource
{
	private final UserDao dao = UserDaoContainer.getInstance();
	private final UserValidator validator = new UserValidator(new PasswordValidator());
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(final User user) throws Exception
	{			
		validator.validate(user);
		
		dao.register(user);
		
		UserSpaceInstallerInterface installer = UserSpaceInstallerFactory
			.createPlainUserSpaceInstaller();
		installer.installUserSpace(user);
		
		return Response.ok().entity(user).build();
	}
}