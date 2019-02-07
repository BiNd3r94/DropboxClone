package de.hse.ws18swa10.api.resource.user;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.hse.ws18swa10.api.file.DownloadResponseBuilder;
import de.hse.ws18swa10.api.file.StreamableFile;
import de.hse.ws18swa10.api.file.entity.FileInfo;
import de.hse.ws18swa10.api.file.entity.FileMove;
import de.hse.ws18swa10.api.file.user.DefaultUserSpacePathEncoder;
import de.hse.ws18swa10.api.file.user.UserPathEncoderInterface;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystemFactory;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystemInterface;
import de.hse.ws18swa10.api.resource.BaseAuthenticatedUserAwareResource;
import de.hse.ws18swa10.api.security.SecuredResource;
import de.hse.ws18swa10.dao.FileShareDao;
import de.hse.ws18swa10.dao.container.FileShareDaoContainer;
import de.hse.ws18swa10.entity.User;

@Path("users/{userId}/files")
@SecuredResource
public class FileResource extends BaseAuthenticatedUserAwareResource
{
	private final UserPathEncoderInterface encoder = new DefaultUserSpacePathEncoder();
	private final UserSpaceFileSystemInterface fileSystem = UserSpaceFileSystemFactory
		.createDefaultUserSpaceFileSystem();
	private final FileShareDao fileShareDao = FileShareDaoContainer.getInstance();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAll(@PathParam("userId") final Integer userId)
		throws Exception
	{
		User user = getAuthenticatedUserOrDeny(userId);
		
		List<FileInfo> files = fileSystem.listDirectory(user, "");
		List<String> paths = files.stream().map(FileInfo::getHash).collect(Collectors.toList());
		Map<String, Integer> shareCounts = fileShareDao.findShareCountForPathsOwnedBy(paths, user);

		for (int i = 0; i < files.size(); i++) {
			FileInfo fileInfo = files.get(i);
			Integer shareCount = shareCounts.get(fileInfo.getHash());
			fileInfo.setShareCount(shareCount == null ? 0 : shareCount);
		}
		
		return Response.ok().entity(files).build();
	}
	
	@Path("{pathHash}")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(
		@PathParam("userId") final Integer userId, 
		@PathParam("pathHash") String pathHash
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		String path = encoder.decode(pathHash);
		byte[] bytes = fileSystem.getFileAsBinary(user, path);
		
		DownloadResponseBuilder builder = new DownloadResponseBuilder();
		
		return builder.buildDownloadResponse(path, bytes);
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(
		@PathParam("userId") final Integer userId,
		@FormDataParam("destinationPath") String destinationPath,
		@FormDataParam("file") final InputStream inputStream, 
		@FormDataParam("file") final FormDataContentDisposition disposition
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		
		StreamableFile streamableFile = new StreamableFile();
		streamableFile.setTargetPath(destinationPath);
		streamableFile.setInputStream(inputStream);
		streamableFile.setDisposition(disposition);
		
		String pathHash = fileSystem.createFile(user, streamableFile);
		FileInfo fileInfo = fileSystem.getFileInfo(user, pathHash);
				
		return Response.ok(fileInfo).build();
	}
	
	@Path("unzips")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFromZip(
		@PathParam("userId") final Integer userId,
		@FormDataParam("destinationPath") String destinationPath,
		@FormDataParam("file") final InputStream inputStream, 
		@FormDataParam("file") final FormDataContentDisposition disposition
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		
		StreamableFile streamableFile = new StreamableFile();
		streamableFile.setTargetPath(destinationPath);
		streamableFile.setInputStream(inputStream);
		streamableFile.setDisposition(disposition);
		
		List<FileInfo> rootFileInfos = fileSystem.createFromStreamableZip(user, streamableFile);
		
		return Response.ok(rootFileInfos).build();
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
		
		String newPathHash = fileSystem.moveFile(user, sourcePath, fileMove.getDestinationPath());
		FileInfo fileInfo = fileSystem.getFileInfo(user, newPathHash);
		
		return Response.ok(fileInfo).build();
	}
	
	@Path("{pathHash}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("userId") final Integer userId, 
		@PathParam("pathHash") String pathHash
	) throws Exception {
		User user = getAuthenticatedUserOrDeny(userId);
		String path = encoder.decode(pathHash);
		
		fileSystem.deleteFile(user, path);
		fileShareDao.removeByPath(pathHash);
		
		return Response.ok().build();
	}
}
