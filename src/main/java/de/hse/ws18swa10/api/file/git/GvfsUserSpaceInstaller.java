package de.hse.ws18swa10.api.file.git;

import java.io.IOException;
import java.nio.file.Path;

import de.hse.ws18swa10.api.file.FileSystemInterface;
import de.hse.ws18swa10.api.file.user.UserSpaceInstallerInterface;
import de.hse.ws18swa10.cli.CommandLineInterface;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.CommandLineInterfaceException;

public class GvfsUserSpaceInstaller implements UserSpaceInstallerInterface
{		
	private final FileSystemInterface fileSystem;
	private final CommandLineInterface cli;
	private final GitClientInterface gitClient;
	
	public GvfsUserSpaceInstaller(
		final FileSystemInterface fileSystem, 
		final CommandLineInterface cli,
		final GitClientInterface gitClient
	) {
		this.fileSystem = fileSystem;
		this.cli = cli;
		this.gitClient = gitClient;
	}
	
	@Override
	public void installUserSpace(final User user) throws Exception
	{		
		Path fullPath = fileSystem.getFullPath(user.getFileSpaceName());
		
		fileSystem.createFolder(user.getFileSpaceName());
		gitClient.init(fullPath.toAbsolutePath().toString());
		
		initGvfsAndRollBackOnError(fullPath.toAbsolutePath().toString());
	}
	
	private void initGvfsAndRollBackOnError(String path) 
		throws CommandLineInterfaceException, IOException
	{
		try {
			cli.runStrict(path, "gvfs init");
		} catch (CommandLineInterfaceException e) {
			fileSystem.deleteFolder(path);
			throw e;
		}
	}
}
