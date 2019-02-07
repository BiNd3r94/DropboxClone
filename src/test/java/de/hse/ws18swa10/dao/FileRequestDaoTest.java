package de.hse.ws18swa10.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.hse.ws18swa10.dao.helper.FileRequestTestHelper;
import de.hse.ws18swa10.dao.helper.UserTestHelper;
import de.hse.ws18swa10.entity.FileRequest;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PersistenceException;

public class FileRequestDaoTest
{	
	private UserTestHelper userTestHelper;
	private FileRequestTestHelper helper;
	private FileRequestDao fileRequestDao;
	
	@Before
	public void setUp()
	{
		helper = new FileRequestTestHelper(
			ProjectTestEntityManagerFactory.createTestEntityManager()
		);
		helper.wipeFileRequestTable();
		
		userTestHelper = new UserTestHelper(ProjectTestEntityManagerFactory.createTestEntityManager());
		userTestHelper.wipeUserTable();
				
		fileRequestDao = new FileRequestDao(ProjectTestEntityManagerFactory.createTestEntityManager());
	}
	
	@Test
	public void testPersist() throws PersistenceException
	{		
		FileRequest fileRequest = buildValidFileRequest();
		
		assertNull(fileRequest.getId());
		
		fileRequestDao.persist(fileRequest);
		
		assertNotNull(fileRequest.getId());
		
		FileRequest fileRequestFromDb = helper.getFileRequest(fileRequest.getId());
		
		assertEquals(fileRequest, fileRequestFromDb);
	}
	
	@Test
	public void testFind()
	{
		FileRequest fileRequest = buildValidFileRequest();
		
		Integer id = helper.persistFileRequestAndGetId(fileRequest);
		fileRequest.setId(id);
		
		FileRequest fileRequestFromDb = fileRequestDao.find(id);
		
		assertEquals(fileRequest, fileRequestFromDb);
	}
	
	@Test
	public void testRemove() throws PersistenceException
	{
		FileRequest fileRequest = buildValidFileRequest();
		
		Integer id = helper.persistFileRequestAndGetId(fileRequest);
		
		fileRequestDao.remove(id);
		
		boolean doesUserExist = helper.doesFileRequestExist(id);
		
		assertFalse(doesUserExist);
	}
	
	private FileRequest buildValidFileRequest()
	{
		User requester = userTestHelper.buildValidTestUser();
		Integer requesterId = userTestHelper.persistUserAndGetId(requester);
		requester.setId(requesterId);
		
		User receiver = userTestHelper.buildValidTestUser();
		receiver.setEmail(receiver.getEmail() + "numero2");
		Integer receiverId = userTestHelper.persistUserAndGetId(receiver);
		receiver.setId(receiverId);
		
		return helper.buildValidFileRequest(requester, receiver);
	}
}
