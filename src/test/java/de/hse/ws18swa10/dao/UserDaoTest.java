package de.hse.ws18swa10.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.hse.ws18swa10.dao.helper.UserTestHelper;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PersistenceException;

public class UserDaoTest
{
	private UserDao userDao;
	private UserTestHelper helper;
	
	@Before
	public void setUp()
	{
		helper = new UserTestHelper(ProjectTestEntityManagerFactory.createTestEntityManager());
		helper.wipeUserTable();
				
		userDao = new UserDao(ProjectTestEntityManagerFactory.createTestEntityManager());
	}
	
	@Test()
	public void testPersist() throws PersistenceException
	{
		User user = helper.buildValidTestUser();
		
		assertNull(user.getId());
		
		userDao.persist(user);

		assertNotNull(user.getId());

		User userFromDb = helper.getUser(user.getId());
		
		assertEquals(user, userFromDb);
	}
	
	@Test(expected = PersistenceException.class)
	public void testPersistThrowsOnDuplicateUniqueEmail() throws PersistenceException
	{
		User user = helper.buildValidTestUser();
		userDao.persist(user);
		
		User duplicateUser = helper.buildValidTestUser();
		userDao.persist(duplicateUser);
	}
	
	@Test
	public void testFind()
	{
		User user = helper.buildValidTestUser();
		Integer id = helper.persistUserAndGetId(user);
		user.setId(id);
				
		User userFromDb = userDao.find(id);
		
		assertEquals(user, userFromDb);
	}
	
	@Test
	public void testRemove() throws PersistenceException
	{
		User user = helper.buildValidTestUser();	
		Integer id = helper.persistUserAndGetId(user);
		
		userDao.remove(id);
		
		boolean doesUserExist = helper.doesUserExist(id);
		
		assertFalse(doesUserExist);
	}
	
	@Test
	public void testTimestampsGetsSet() throws PersistenceException
	{
		User user = helper.buildValidTestUser();
		
		assertNull(user.getAudit());
		
		userDao.persist(user);
		
		assertNotNull(user.getAudit());
		assertNotNull(user.getAudit().getCreatedAt());
		assertNotNull(user.getAudit().getUpdatedAt());
	}
	
	@Test
	public void testUpdatedAtGetsChanged() throws PersistenceException, InterruptedException
	{
		User user = helper.buildValidTestUser();
		
		userDao.persist(user);
		
		Integer oldId = user.getId();
		Date oldUpdatedAt = user.getAudit().getUpdatedAt();
		
		user.setPassword("123456789");
		
		// Wait at least a second so we can see a change in the date times
		Thread.sleep(1000);
		
		userDao.persist(user);
		
		assertEquals(oldId, user.getId());
		assertNotEquals(oldUpdatedAt, user.getAudit().getUpdatedAt());
	}
}
