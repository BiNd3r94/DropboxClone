package de.hse.ws18swa10.cli;

import java.io.IOException;

public interface CommandLineInterface
{
	public CliResult run(String path, String command) throws IOException;
	
	public CliResult runStrict(String path, String command) throws IOException;
}
