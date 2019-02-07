package de.hse.ws18swa10.api.resource.fileshare;

import java.io.InputStream;

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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.hse.ws18swa10.api.file.DownloadResponseBuilder;
import de.hse.ws18swa10.api.file.FileSharePermissionReader;
import de.hse.ws18swa10.api.file.StreamableFile;
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

@Path("file-shares/{id}/file")
@SecuredResource
public class FileResource extends BaseAuthenticatedUserAwareResource
{
	private final FileShareDao dao = FileShareDaoContainer.getInstance();
	private final UserSpaceFileSystemInterface fileSystem = UserSpaceFileSystemFactory
		.createDefaultUserSpaceFileSystem();
	private final FileSharePermissionReader reader = new FileSharePermissionReader();
	private final UserPathEncoderInterface encoder = new DefaultUserSpacePathEncoder();
	
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@PathParam("id") final Integer id) 
		throws Exception 
	{
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsReadingOfFiles(fileShare)) {
			throw new ForbiddenException();
		}

		String path = fileShare.getPath();
		byte[] bytes = fileSystem.getFileAsBinary(fileShare.getOwner(), path);
		
		DownloadResponseBuilder builder = new DownloadResponseBuilder();
		
		return builder.buildDownloadResponse(path, bytes);
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(
		@PathParam("id") final Integer id,
		@FormDataParam("file") final InputStream inputStream, 
		@FormDataParam("file") final FormDataContentDisposition disposition
	) throws Exception {
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsCreationOfFiles(fileShare)) {
			throw new ForbiddenException();
		}
		
		StreamableFile streamableFile = new StreamableFile();
		streamableFile.setTargetPath(fileShare.getPath());
		streamableFile.setInputStream(inputStream);
		streamableFile.setDisposition(disposition);
		
		fileSystem.createFile(fileShare.getOwner(), streamableFile);
		
		return Response.ok().build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response move(
		@PathParam("id") final Integer id, 
		@FormParam("sourcePath") String sourcePath,
		@FormParam("destinationPath") String destinationPath
	) throws Exception {
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsUpdateOfFiles(fileShare)) {
			throw new ForbiddenException();
		}
		
		fileSystem.moveFile(fileShare.getOwner(), sourcePath, destinationPath);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") final Integer id) 
		throws Exception 
	{
		FileShare fileShare = tryToGetFileShare(id);
		
		denyIfNotMemberOfFileShare(fileShare);
		
		if (! reader.allowsDeletionOfFiles(fileShare)) {
			throw new ForbiddenException();
		}
		
		String path = encoder.decode(fileShare.getPath());
		fileSystem.deleteFile(fileShare.getOwner(), path);
		
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
		
		for (User user : fileShare.getUsers()) {
			if (user.getId().equals(authenticatedUser.getId())) {
				return;
			}
		}
			
		throw new ForbiddenException();
	}
}
