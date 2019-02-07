package de.hse.ws18swa10.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ProjectEntityManagerFactory
{
	private final static String LIVE_PU_NAME = "ws18-swa10";
	
	public static EntityManager createEntityManager()
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(LIVE_PU_NAME);
		
		return factory.createEntityManager();
	}
}
