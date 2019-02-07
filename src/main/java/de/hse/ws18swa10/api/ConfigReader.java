package de.hse.ws18swa10.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader
{
	private static final String CONFIG_NAME = "config.properties";
	
	public Configs getConfigs() throws IOException
	{
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_NAME);
		
		Properties properties = new Properties();
		properties.load(inputStream);
		
		return new Configs(properties);
	}
}
