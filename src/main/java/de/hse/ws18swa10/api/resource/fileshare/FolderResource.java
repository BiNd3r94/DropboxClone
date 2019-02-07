package de.hse.ws18swa10.api.resource.fileshare;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.file.DownloadResponseBuilder;
import de.hse.ws18swa10.api.file.FileSharePermissionReader;
import de.hse.ws18swa10.api.file.entity.FileInfo;
import de.hse.ws18swa10.api.file.user.DefaultUserSpacePathEncoder;
import de.hse.ws18swa10.api.file.user.UserPathEncoderInterface;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystemFactory;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystemInterface;
import de.hse.ws18swa10.api.resource.BaseAuthenticatedUserAwareResource;
import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.dao.FileShareDao;
import de.hse.ws18swa10.dao.container.FileShareDaoContainer;
import de.hse.ws18swa10.entity.FileShare;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PathDoesntExistException;

@Path("file-shares/{id}/folders")
@SecuredResource
public class FolderResource extends BaseAuthenticatedUserAwareResource
{
	private final FileShareDao dao = FileShareDaoContainer.getInstance();
	private final UserSpaceFileSystemInterface fileSystem = UserSpaceFileSystemFactory
		.createDefaultUserSpaceFileSystem();
	private final FileSharePermissionReader reader = new FileSharePermissionReader();
	private final UserPathEncoderInterface encoder = new DefaultUserSpacePathEncoder();
	
	@Path("{pathHash}/zips")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(
		@PathParam("id") final Integer id, 
		@PathParam("pathHash") final String pathHash
	) throws Exception {
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsReadingOfFolders(fileShare)) {
			throw new ForbiddenException();
		}

		String path = encoder.decode(pathHash);
		byte[] bytes = fileSystem.getFolderAsBinary(fileShare.getOwner(), path);
		
		DownloadResponseBuilder builder = new DownloadResponseBuilder();
		
		return builder.buildDownloadResponse(path, bytes);
	}
	
	@Path("{pathHash}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(
		@PathParam("id") final Integer id, 
		@PathParam("pathHash") final String pathHash		
	) throws Exception {
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsReadingOfFolders(fileShare)) {
			throw new ForbiddenException();
		}
		
		String path = encoder.decode(pathHash);
		
		List<FileInfo> filesInfos = fileSystem.listDirectory(fileShare.getOwner(), path);
		
		return Response.ok().entity(filesInfos).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(
		@PathParam("id") final Integer id, 
		@FormParam("path") final String path
	) throws PathDoesntExistException, IOException {
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsCreationOfFolders(fileShare)) {
			throw new ForbiddenException();
		}
		
		fileSystem.createFolder(fileShare.getOwner(), path);
		
		return Response.ok().build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response move(
		@PathParam("id") final Integer id,
		@FormParam("sourcePath") final String sourcePath,
		@FormParam("destinationPath") final String destinationPath
	) throws Exception {
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsUpdateOfFolders(fileShare)) {
			throw new ForbiddenException();
		}
		
		fileSystem.moveFolder(fileShare.getOwner(), sourcePath, destinationPath);
		
		return Response.ok().build();
	}
	
	@Path("{pathHash}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("id") final Integer id, 
		@PathParam("pathHash") final String pathHash
	) throws Exception {
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsDeletionOfFolders(fileShare)) {
			throw new ForbiddenException();
		}

		String path = encoder.decode(pathHash);
		
		fileSystem.deleteFolder(fileShare.getOwner(), path);
		
		return Response.ok().build();
	}
	
	private FileShare tryToGetFileShare(final Integer id)
	{
		FileShare fileShare = dao.find(id);
		
		if (fileShare == null) {
			throw new NotFoundException();
		}
		
		return fileShare;
	}
	
	private void denyIfNotMemberOfFileShare(final FileShare fileShare)
	{
		User authenticatedUser = getAuthenticatedUser();
		
		if (! fileShare.getUsers().stream()
			.anyMatch((u) -> u.getId() == authenticatedUser.getId())) {
			throw new ForbiddenException();
		}
	}
}
