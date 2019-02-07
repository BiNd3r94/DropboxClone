package de.hse.ws18swa10.api.file.git;

public class GitCommitMessageBuilder
{
	private static final String COMMIT_CREATE = "CREATE";
	private static final String COMMIT_CHANGE = "CHANGE";
	private static final String COMMIT_DELETE = "DELETE";
	
	private static final String SEPARATOR = ":";
		
	public String buildCreateMessage(String message)
	{
		return COMMIT_CREATE + SEPARATOR + message;
	}
	
	public String buildChangeMessage(String message)
	{
		return COMMIT_CHANGE + SEPARATOR + message;
	}
	
	public String buildDeleteMessage(String message)
	{
		return COMMIT_DELETE + SEPARATOR + message;
	}
}
