package de.hse.ws18swa10.exception;

import java.io.IOException;

public class CommandLineInterfaceException extends IOException
{
	private static final long serialVersionUID = -6829462530458211778L;
	
	private String command;
	
	public CommandLineInterfaceException(String message, String command)
	{
		super(message);
		this.command = command;
	}

	public String getCommand()
	{
		return command;
	}
}
