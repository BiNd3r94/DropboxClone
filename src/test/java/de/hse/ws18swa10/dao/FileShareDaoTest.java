package de.hse.ws18swa10.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.hse.ws18swa10.dao.helper.FileShareDaoTestHelper;
import de.hse.ws18swa10.dao.helper.UserTestHelper;
import de.hse.ws18swa10.entity.FileShare;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PersistenceException;

public class FileShareDaoTest
{
	private FileShareDaoTestHelper helper;
	private UserTestHelper userHelper;
	private FileShareDao fileShareDao;
	
	@Before
	public void setUp()
	{
		helper = new FileShareDaoTestHelper(
			ProjectTestEntityManagerFactory.createTestEntityManager()
		);
		helper.wipeFileShareTables();
		
		userHelper = new UserTestHelper(ProjectTestEntityManagerFactory.createTestEntityManager());
		userHelper.wipeUserTable();
		
		fileShareDao = new FileShareDao(ProjectTestEntityManagerFactory.createTestEntityManager());
	}
	
	@Test
	public void testPersist() throws PersistenceException
	{
		FileShare fileShare = buildFileShare();
		
		assertNull(fileShare.getId());
		
		fileShareDao.persist(fileShare);
		
		assertNotNull(fileShare.getId());
		
		FileShare fileShareFromDb = helper.getFileShare(fileShare.getId());
		
		assertEquals(fileShare, fileShareFromDb);
		assertTrue(helper.doFileSharePermissionMappingsExist(fileShare.getId()));
	}
	
	@Test
	public void testFind()
	{
		FileShare fileShare = buildFileShare();
		
		Integer id = helper.persistFileShareAndGetId(fileShare);
		fileShare.setId(id);
		
		FileShare fileShareFromDb = fileShareDao.find(id);
		
		assertEquals(fileShare, fileShareFromDb);
	}
	
	@Test
	public void testRemove() throws PersistenceException
	{
		FileShare fileShare = buildFileShare();
		
		Integer id = helper.persistFileShareAndGetId(fileShare);
		
		fileShareDao.remove(id);
		
		assertFalse(helper.doesFileShareExist(id));
		assertFalse(helper.doFileSharePermissionMappingsExist(id));
	}
	
	private FileShare buildFileShare()
	{
		User owner = userHelper.buildValidTestUser();
		Integer ownerId = userHelper.persistUserAndGetId(owner);
		User ownerFromDb = userHelper.getUser(ownerId);
		
		return helper.buildValidFileShare(ownerFromDb);
	}
}
