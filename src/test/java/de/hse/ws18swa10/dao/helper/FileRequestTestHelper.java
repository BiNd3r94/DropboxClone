package de.hse.ws18swa10.dao.helper;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.hse.ws18swa10.entity.FileRequest;
import de.hse.ws18swa10.entity.User;

public final class FileRequestTestHelper
{
	private EntityManager entityManager;
	private int tokenCount = 0;
	
	public FileRequestTestHelper(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public void wipeFileRequestTable()
	{
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("DELETE FROM file_request").executeUpdate();
		entityManager.getTransaction().commit();
	}
	
	public Integer persistFileRequestAndGetId(FileRequest fileRequest)
	{
		String sql = "INSERT INTO file_request "
				+ "(requester_id, token, request, target_path, created_at, updated_at) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?)";
		
		entityManager.getTransaction().begin();
		
		Date now = new Date();
		
		Query insertQuery = entityManager.createNativeQuery(sql);
		insertQuery.setParameter(1, fileRequest.getRequester().getId());
		insertQuery.setParameter(2, fileRequest.getToken());
		insertQuery.setParameter(3, fileRequest.getRequest());
		insertQuery.setParameter(4, fileRequest.getTargetPath());
		insertQuery.setParameter(5, now);
		insertQuery.setParameter(6, now);
		insertQuery.executeUpdate();
		
		entityManager.getTransaction().commit();
		
		// FIXME: This solution does not work if tests are run in a cluster 
		// or any other form of asynchronous processing
		return ((BigInteger) entityManager
			.createNativeQuery("SELECT LAST_INSERT_ID()")
			.getSingleResult()).intValue();
	}
	
	public FileRequest getFileRequest(Integer id)
	{
		Query query = entityManager
			.createNativeQuery("SELECT * FROM file_request WHERE id = ?", FileRequest.class);
		query.setParameter(1, id);

		return (FileRequest) query.getSingleResult();
	}
	
	public FileRequest buildValidFileRequest(User requester, User receiver)
	{
		FileRequest fileRequest = new FileRequest();
		fileRequest.setRequester(requester);
		fileRequest.setToken("testToken" + tokenCount);
		fileRequest.setRequest("Please upload the assignment for SWA");
		fileRequest.setTargetPath("assignments/swa");
		
		return fileRequest;
	}

	public boolean doesFileRequestExist(Integer id)
	{
		Query selectQuery = entityManager
			.createNativeQuery("SELECT COUNT(*) FROM file_request WHERE id = ?");
		selectQuery.setParameter(1, id);
		long fileRequestFoundInDb = (long) selectQuery.getSingleResult();
		
		return fileRequestFoundInDb == 1;
	}
}
