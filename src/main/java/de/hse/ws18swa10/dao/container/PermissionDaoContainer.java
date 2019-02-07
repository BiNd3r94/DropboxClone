package de.hse.ws18swa10.dao.container;

import de.hse.ws18swa10.dao.PermissionDao;
import de.hse.ws18swa10.dao.ProjectEntityManagerFactory;

public class PermissionDaoContainer
{
	private static PermissionDao instance;
	
	private PermissionDaoContainer() {}

	public static PermissionDao getInstance()
	{
		if (instance == null) {
			instance = new PermissionDao(ProjectEntityManagerFactory.createEntityManager());
		}
		
		return instance;
	}
}
