package de.hse.ws18swa10.api.file;

import de.hse.ws18swa10.api.file.user.UserSpaceInstallerInterface;

public class UserSpaceInstallerFactory
{
	public static UserSpaceInstallerInterface createPlainUserSpaceInstaller()
	{		
		return new PlainUserSpaceInstaller(
			new LocalStorageFileSystem(PathProviderFactory.createDefaultPathProvider())
		);
	}
}
