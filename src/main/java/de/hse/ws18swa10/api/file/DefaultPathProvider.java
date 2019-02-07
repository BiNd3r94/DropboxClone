package de.hse.ws18swa10.api.file;

import de.hse.ws18swa10.api.Configs;

public class DefaultPathProvider implements PathProviderInterface
{
	private final Configs configs;
	
	public DefaultPathProvider(final Configs configs)
	{
		this.configs = configs;
	}

	@Override
	public String getRootPath()
	{
		return configs.getLocalFsRootPath();
	}

	@Override
	public String getTempPath()
	{
		return configs.getLocalFsTempPath();
	}
}
