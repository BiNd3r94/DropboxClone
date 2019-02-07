package de.hse.ws18swa10.api.file;

import java.io.IOException;
import java.util.Properties;

import de.hse.ws18swa10.api.ConfigReader;
import de.hse.ws18swa10.api.Configs;

public class PathProviderFactory
{
	public static PathProviderInterface createDefaultPathProvider()
	{	
		Configs configs;
		ConfigReader configReader = new ConfigReader();
		
		try {
			configs = configReader.getConfigs();
		} catch (IOException e) {
			configs = new Configs(new Properties());
		}
		
		return new DefaultPathProvider(configs);
	}	
}
