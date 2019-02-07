package de.hse.ws18swa10.api.file;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

public class DownloadResponseBuilder
{	
	public Response buildDownloadResponse(String path, final byte[] bytes)
	{
		String fileName = getFileNameWithExtension(path);
		
		return buildDownloadResponseByName(fileName, bytes);
	}
	
	private Response buildDownloadResponseByName(String fileName, final byte[] bytes)
	{		
		StreamingOutput streamingOutput = new StreamingOutput()
		{
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				output.write(bytes);
				output.flush();
			}
		};
		
		return Response
			.ok()
			.entity(streamingOutput)
			.header("content-disposition", "attachment; filename = " + fileName)
			.build();
	}
	
	private String getFileNameWithExtension(String path)
	{
		int offset = path.lastIndexOf(File.separator);
		
		if (offset < 0) {
			return path;
		}
		
		return path.substring(offset);
	}
}
