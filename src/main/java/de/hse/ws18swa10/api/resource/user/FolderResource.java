package de.hse.ws18swa10.api.resource.user;

import java.io.IOException;
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

import de.hse.ws18swa10.api.file.DownloadResponseBuilder;
import de.hse.ws18swa10.api.file.entity.FileInfo;
import de.hse.ws18swa10.api.file.entity.FileInfoList;
import de.hse.ws18swa10.api.file.entity.FileMove;
import de.hse.ws18swa10.api.file.entity.Folder;
import de.hse.ws18swa10.api.file.user.DefaultUserSpacePathEncoder;
import de.hse.ws18swa10.api.file.user.UserPathEncoderInterface;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystemFactory;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystemInterface;
import de.hse.ws18swa10.api.resource.BaseAuthenticatedUserAwareResource;
import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PathDoesntExistException;

@Path("users/{userId}/folders")
@SecuredResource
public class FolderResource extends BaseAuthenticatedUserAwareResource
{
	private final UserPathEncoderInterface encoder = new DefaultUserSpacePathEncoder();
	private final UserSpaceFileSystemInterface fileSystem = UserSpaceFileSystemFactory
		.createDefaultUserSpaceFileSystem();
	
	@Path("{pathHash}/zips")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(
		@PathParam("userId") final Integer userId, 
		@PathParam("pathHash") final String pathHash
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		String path = encoder.decode(pathHash);
		byte[] bytes = fileSystem.getFolderAsBinary(user, path);
		
		DownloadResponseBuilder builder = new DownloadResponseBuilder();
		
		return builder.buildDownloadResponse(path, bytes);
	}
	
	@Path("{pathHash}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(
		@PathParam("userId") final Integer userId, 
		@PathParam("pathHash") final String pathHash		
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		String path = encoder.decode(pathHash);
		
		List<FileInfo> fileInfos = fileSystem.listDirectory(user, path);
		FileInfoList fileInfoList = new FileInfoList();
		fileInfoList.setFileInfos(fileInfos);
		fileInfoList.setParentHash(fileSystem.getParentPathHashFor(pathHash));
		
		return Response.ok().entity(fileInfoList).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(
		@PathParam("userId") final Integer userId, 
		final Folder folder
	) throws PathDoesntExistException, IOException {
		User user = getAuthenticatedUserOrDeny(userId);
		
		String pathHash = fileSystem.createFolder(user, folder.getPath());
		FileInfo fileInfo = fileSystem.getFileInfo(user, pathHash);
		
		return Response.ok(fileInfo).build();
	}
	
	@Path("{pathHash}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response move(
		@PathParam("userId") final Integer userId,
		@PathParam("pathHash") String pathHash,
		final FileMove fileMove
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		String sourcePath = encoder.decode(pathHash);
		
		String newPathHash = fileSystem.moveFolder(user, sourcePath, fileMove.getDestinationPath());
		FileInfo fileInfo = fileSystem.getFileInfo(user, newPathHash);
		
		return Response.ok(fileInfo).build();
	}
	
	@Path("{pathHash}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("userId") final Integer userId, 
		@PathParam("pathHash") final String pathHash
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		String path = encoder.decode(pathHash);
		
		fileSystem.deleteFolder(user, path);
		
		return Response.ok().build();
	}
}
