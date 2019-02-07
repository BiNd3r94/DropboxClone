package de.hse.ws18swa10.cli;

import java.util.List;

public class CliResult
{
	private final int exitCode;
	private final List<String> stdOut;
	private final List<String> stdError;
	
	public CliResult(int exitCode, final List<String> stdOut, final List<String> stdError)
	{
		this.exitCode = exitCode;
		this.stdOut = stdOut;
		this.stdError = stdError;
	}
	
	public int getExitCode()
	{
		return exitCode;
	}
	
	public List<String> getStdOut()
	{
		return stdOut;
	}

	public List<String> getStdError()
	{
		return stdError;
	}
}
