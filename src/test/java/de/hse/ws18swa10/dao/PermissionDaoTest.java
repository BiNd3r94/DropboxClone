package de.hse.ws18swa10.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hse.ws18swa10.dao.helper.PermissionTestHelper;
import de.hse.ws18swa10.entity.Permission;
import de.hse.ws18swa10.exception.PersistenceException;

public class PermissionDaoTest
{
	private PermissionTestHelper helper;
	private PermissionDao permissionDao;
		
	@Before
	public void setUp()
	{			
		helper = new PermissionTestHelper(ProjectTestEntityManagerFactory.createTestEntityManager());
		permissionDao = new PermissionDao(ProjectTestEntityManagerFactory.createTestEntityManager());
	}
	
	@After
	public void cleanUp()
	{
		helper.deletePermissionsCreatedDuringTests();
	}
	
	@Test
	public void testPersist() throws PersistenceException
	{
		Permission permission = helper.buildValidPermission();
		
		assertNull(permission.getId());
		
		permissionDao.persist(permission);
		
		assertNotNull(permission.getId());
		
		Permission permissionFromDb = helper.getPermission(permission.getId());
		
		assertEquals(permission, permissionFromDb);
	}
	
	@Test
	public void testFind()
	{
		Permission permission = helper.buildValidPermission();
				
		Integer id = helper.persistPermissionAndGetId(permission);
		permission.setId(id);
		
		Permission permissionFromDb = permissionDao.find(id);
		
		assertEquals(permission, permissionFromDb);
	}
	
	@Test
	public void testRemove() throws PersistenceException
	{
		Permission permission = helper.buildValidPermission();
		
		Integer id = helper.persistPermissionAndGetId(permission);
		
		permissionDao.remove(id);
		
		boolean doesPermissionExist = helper.doesPermissionExist(id);
		
		assertFalse(doesPermissionExist);
	}
}
