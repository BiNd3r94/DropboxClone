package de.hse.ws18swa10.api;

import java.util.Properties;

public class Configs
{
	private static final String FS = "fs";
	private static final String FS_LOCAL = FS + ".local";
	private static final String FS_LOCAL_ROOTPATH = FS_LOCAL + ".rootPath";
	private static final String FS_LOCAL_TEMPPATH = FS_LOCAL + ".tempPath";
	
	private final Properties configs;
	
	public Configs(final Properties configs)
	{
		this.configs = configs;
	}
	
	public String get(String name)
	{
		return configs.getProperty(name);
	}
	
	public String getLocalFsRootPath()
	{
		return configs.getProperty(FS_LOCAL_ROOTPATH); 
	}
	
	public String getLocalFsTempPath()
	{
		return configs.getProperty(FS_LOCAL_TEMPPATH); 
	}
}
