package de.hse.ws18swa10.api.file;

import de.hse.ws18swa10.api.file.user.UserSpaceInstallerInterface;
import de.hse.ws18swa10.entity.User;

public class PlainUserSpaceInstaller implements UserSpaceInstallerInterface
{
	private final FileSystemInterface fileSystem;
	
	public PlainUserSpaceInstaller(final FileSystemInterface fileSystem)
	{
		this.fileSystem = fileSystem;
	}

	@Override
	public void installUserSpace(User user) throws Exception
	{		
		fileSystem.createFolder(user.getFileSpaceName());
	}
}
