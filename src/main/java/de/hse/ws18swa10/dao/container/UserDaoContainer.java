package de.hse.ws18swa10.dao.container;

import de.hse.ws18swa10.dao.ProjectEntityManagerFactory;
import de.hse.ws18swa10.dao.UserDao;

public class UserDaoContainer
{
	private static UserDao instance;
	
	private UserDaoContainer() {}
	
	public static UserDao getInstance()
	{
		if (instance == null) {
			instance = new UserDao(ProjectEntityManagerFactory.createEntityManager());
		}
		
		return instance;
	}
}
