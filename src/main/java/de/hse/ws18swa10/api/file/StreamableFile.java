package de.hse.ws18swa10.api.file;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public class StreamableFile
{
	private String targetPath;
	private InputStream inputStream;
	private FormDataContentDisposition disposition;
	
	public String getTargetPath()
	{
		return targetPath;
	}

	public void setTargetPath(String targetPath)
	{
		this.targetPath = targetPath;
	}

	public InputStream getInputStream()
	{
		return inputStream;
	}
	
	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}
	
	public FormDataContentDisposition getDisposition()
	{
		return disposition;
	}
	
	public void setDisposition(FormDataContentDisposition disposition)
	{
		this.disposition = disposition;
	}
}
