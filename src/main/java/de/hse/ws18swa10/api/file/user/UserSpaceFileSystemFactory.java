package de.hse.ws18swa10.api.file.user;

import de.hse.ws18swa10.api.file.LocalStorageFileSystem;
import de.hse.ws18swa10.api.file.PathProviderFactory;
import de.hse.ws18swa10.api.file.git.DefaultGitClient;
import de.hse.ws18swa10.api.file.git.GitCommitMessageBuilder;
import de.hse.ws18swa10.api.file.git.GitHistoryUserSpaceFileSystem;
import de.hse.ws18swa10.cli.WindowsConsole;

public class UserSpaceFileSystemFactory
{
	public static UserSpaceFileSystemInterface createDefaultUserSpaceFileSystem()
	{
		return new UserSpaceFileSystem(
			new LocalStorageFileSystem(PathProviderFactory.createDefaultPathProvider()),
			new DefaultUserSpacePathEncoder()
		);
	}
	
	public static UserSpaceFileSystemInterface createHistoryAwareUserSpaceFileSystem()
	{
		return new GitHistoryUserSpaceFileSystem(
			new GitCommitMessageBuilder(),
			new DefaultGitClient(new WindowsConsole()),
			new LocalStorageFileSystem(PathProviderFactory.createDefaultPathProvider()),
			new DefaultUserSpacePathEncoder()
		);
	}
}
