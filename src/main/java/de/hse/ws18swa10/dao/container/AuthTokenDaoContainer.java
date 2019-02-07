package de.hse.ws18swa10.dao.container;

import de.hse.ws18swa10.dao.AuthTokenDao;
import de.hse.ws18swa10.dao.ProjectEntityManagerFactory;

public class AuthTokenDaoContainer
{
private static AuthTokenDao instance;
	
	private AuthTokenDaoContainer() {}
	
	public static AuthTokenDao getInstance()
	{
		if (instance == null) {
			instance = new AuthTokenDao(ProjectEntityManagerFactory.createEntityManager());
		}
		
		return instance;
	}
}
