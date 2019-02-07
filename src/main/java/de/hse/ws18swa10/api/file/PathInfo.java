package de.hse.ws18swa10.api.file;

public class PathInfo
{
	private String name;
	private boolean isDirectory;
	private long lastModifiedAt;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isDirectory()
	{
		return isDirectory;
	}
	
	public void setIsDirectory(boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}
	
	public long getLastModifiedAt()
	{
		return lastModifiedAt;
	}
	
	public void setLastModifiedAt(long lastModifiedAt)
	{
		this.lastModifiedAt = lastModifiedAt;
	}
}
