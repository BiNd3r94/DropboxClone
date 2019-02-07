package de.hse.ws18swa10.api.resource.publicly;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.hse.ws18swa10.api.file.StreamableFile;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystemFactory;
import de.hse.ws18swa10.api.file.user.UserSpaceFileSystemInterface;
import de.hse.ws18swa10.dao.FileRequestDao;
import de.hse.ws18swa10.dao.container.FileRequestDaoContainer;
import de.hse.ws18swa10.entity.FileRequest;

@Path("public/file-requests")
public class FileRequestResource
{
	private final FileRequestDao dao = FileRequestDaoContainer.getInstance();
	private final UserSpaceFileSystemInterface fileSystem = UserSpaceFileSystemFactory
		.createDefaultUserSpaceFileSystem();
	
	@Path("token/{token}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByToken(@PathParam("token") final String token) throws NotFoundException
	{		
		FileRequest fileRequest = dao.findByToken(token);
		
		if (fileRequest == null) {
			throw new NotFoundException();
		}
		
		return Response.ok().entity(fileRequest).build();
	}
	
	@Path("{token}")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fulfill(
		@PathParam("token") final String token,
		@FormDataParam("file") final InputStream inputStream, 
		@FormDataParam("file") final FormDataContentDisposition disposition
	) throws Exception {
		FileRequest fileRequest = tryToGetFileRequest(token);
		
		if (! fileRequest.isOpen()) {
			throw new NotFoundException();
		}
				
		StreamableFile streamableFile = new StreamableFile();
		streamableFile.setTargetPath(fileRequest.getTargetPath());
		streamableFile.setInputStream(inputStream);
		streamableFile.setDisposition(disposition);
		
		fileSystem.createFile(fileRequest.getRequester(), streamableFile);
		dao.fulfill(fileRequest);
		
		return Response.ok(fileRequest).build();
	}
	
	private FileRequest tryToGetFileRequest(String token) throws NotFoundException
	{
		FileRequest fileRequest = dao.findByToken(token);
		
		if (fileRequest == null) {
			throw new NotFoundException();
		}
		
		return fileRequest;
	}
}
