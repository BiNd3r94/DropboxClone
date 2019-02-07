package de.hse.ws18swa10.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ProjectTestEntityManagerFactory
{
	private final static String TEST_PU_NAME = "ws18-swa10-test";
	
	public static EntityManager createTestEntityManager()
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(TEST_PU_NAME);
		
		return factory.createEntityManager();
	}
}
