package de.hse.ws18swa10.dao.container;

import de.hse.ws18swa10.dao.FileRequestDao;
import de.hse.ws18swa10.dao.ProjectEntityManagerFactory;

public class FileRequestDaoContainer
{
	private static FileRequestDao instance;
	
	private FileRequestDaoContainer() {}

	public static FileRequestDao getInstance()
	{
		if (instance == null) {
			instance = new FileRequestDao(ProjectEntityManagerFactory.createEntityManager());
		}
		
		return instance;
	}
}
