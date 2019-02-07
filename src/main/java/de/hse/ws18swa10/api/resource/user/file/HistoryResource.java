package de.hse.ws18swa10.api.resource.user.file;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.file.HistoryRepositoryFactory;
import de.hse.ws18swa10.api.file.user.DefaultUserSpacePathEncoder;
import de.hse.ws18swa10.api.file.user.UserPathEncoderInterface;
import de.hse.ws18swa10.api.file.FileHistoryRepositoryInterface;
import de.hse.ws18swa10.api.resource.BaseAuthenticatedUserAwareResource;
import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.entity.User;

@Path("users/{userId}/files/{pathHash}/history")
@SecuredResource
public class HistoryResource extends BaseAuthenticatedUserAwareResource
{
	private final UserPathEncoderInterface encoder = new DefaultUserSpacePathEncoder();
	private final FileHistoryRepositoryInterface repository = HistoryRepositoryFactory
		.createDefaultHistoryRepository();
	
	@Path("changes")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChanges(
		@PathParam("userId") Integer userId, 
		@PathParam("pathHash") String pathHash
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		
		String path = encoder.decode(pathHash);
		List<String> changes = repository.getChanges(user, path);
		
		return Response.ok().entity(changes).build();
	}
}
