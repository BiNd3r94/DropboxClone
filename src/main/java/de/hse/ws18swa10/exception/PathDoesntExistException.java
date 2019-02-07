package de.hse.ws18swa10.exception;

public class PathDoesntExistException extends Exception
{
	private static final long serialVersionUID = 7476425044956937025L;
	
	private final String path;
	
	public PathDoesntExistException(final String path)
	{
		super("Path " + path + " does not exist.");
		this.path = path;
	}
	
	public String getPath()
	{
		return path;
	}
}
