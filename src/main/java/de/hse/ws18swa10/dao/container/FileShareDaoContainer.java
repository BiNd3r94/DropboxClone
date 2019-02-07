package de.hse.ws18swa10.dao.container;

import de.hse.ws18swa10.dao.FileShareDao;
import de.hse.ws18swa10.dao.ProjectEntityManagerFactory;

public class FileShareDaoContainer
{
	private static FileShareDao instance;
	
	private FileShareDaoContainer() {}
	
	public static FileShareDao getInstance()
	{
		if (instance == null) {
			instance = new FileShareDao(ProjectEntityManagerFactory.createEntityManager());
		}
		
		return instance;
	}
}
