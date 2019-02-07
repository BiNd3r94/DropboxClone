package de.hse.ws18swa10.api.file.git;

import java.io.IOException;
import java.util.List;

import de.hse.ws18swa10.cli.CliResult;
import de.hse.ws18swa10.cli.CommandLineInterface;

public class DefaultGitClient implements GitClientInterface
{
	private final CommandLineInterface cli;
	private String directory = "";
	
	public DefaultGitClient(final CommandLineInterface cli)
	{
		this.cli = cli;
	}

	@Override
	public void setProjectDir(String directory)
	{
		this.directory = directory;
	}

	@Override
	public void init(String path) throws IOException
	{
		cli.runStrict(path, "git init");
	}
	
	@Override
	public void addAll(String path) throws IOException
	{
		cli.runStrict(path, "git add .");
	}

	@Override
	public void commit(String path, String message) throws IOException
	{
		cli.runStrict(path, "git commit -m \"" + message + "\"");
	}

	@Override
	public List<String> getCommits(String path) throws IOException
	{		
		CliResult cliResult = cli
			.run(directory, "git rev-list --all --remotes --pretty=oneline -- " + path);
		
		return cliResult.getStdOut();
	}
}
