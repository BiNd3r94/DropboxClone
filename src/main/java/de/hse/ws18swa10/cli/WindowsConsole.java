package de.hse.ws18swa10.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.hse.ws18swa10.exception.CommandLineInterfaceException;

public class WindowsConsole implements CommandLineInterface
{
	@Override
	public CliResult run(String path, String command) throws IOException
	{
		Process process = getFinishedProcessOnlyThrowingIoErrors(path, command);
		
		try {
			List<String> stdOut = readInputStream(process.getInputStream());
			List<String> stdError = readInputStream(process.getErrorStream());
			
			return new CliResult(process.exitValue(), stdOut, stdError);
		} catch (IOException e) {
			throw new CommandLineInterfaceException("IO error during execution", command);
		}
	}
	
	@Override
	public CliResult runStrict(String path, String command) throws IOException
	{
		CliResult cliResult = run(path, command);
		
		if (cliResult.getExitCode() != 0) {
			String message = "Error during execution of <" + command + ">";
			throw new CommandLineInterfaceException(message, command);
		}
		
		return cliResult;
	}
	
	private Process getFinishedProcessOnlyThrowingIoErrors(String path, String command)
		throws IOException
	{
		try {
			return getFinishedProcess(path, command);
		} catch (InterruptedException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	private Process getFinishedProcess(String path, String command) 
		throws InterruptedException, IOException
	{
		ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
		processBuilder.directory(new File(path));
		processBuilder.redirectErrorStream(false);
		
		Process process = processBuilder.start();
		process.waitFor();
		
		return process;
	}
	
	private List<String> readInputStream(InputStream inputStream) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		List<String> lines = new ArrayList<>();
		String currentLine;
		
		while ((currentLine = reader.readLine()) != null) {
			lines.add(currentLine);
		}
		
		return lines;
	}
}
