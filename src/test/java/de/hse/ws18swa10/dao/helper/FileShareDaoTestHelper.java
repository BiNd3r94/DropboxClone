package de.hse.ws18swa10.dao.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.hse.ws18swa10.entity.FileShare;
import de.hse.ws18swa10.entity.Permission;
import de.hse.ws18swa10.entity.User;

public class FileShareDaoTestHelper
{
	private static final int FIRST_PERMISSION_ID = 1;
	private static final int SECOND_PERMISSION_ID = 2;
	
	private EntityManager entityManager;
	
	public FileShareDaoTestHelper(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public void wipeFileShareTables()
	{
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("DELETE FROM file_share_permission_map").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM file_share").executeUpdate();
		entityManager.getTransaction().commit();
	}
	
	public Integer persistFileShareAndGetId(FileShare fileShare)
	{
		String sql = "INSERT INTO file_share "
				+ "(owner_id, path, created_at, updated_at) "
				+ "VALUES "
				+ "(?, ?, ?, ?)";
		
		entityManager.getTransaction().begin();
		
		Date now = new Date();
		
		Query insertFileShareQuery = entityManager.createNativeQuery(sql);
		insertFileShareQuery.setParameter(1, fileShare.getOwner().getId());
		insertFileShareQuery.setParameter(2, fileShare.getPath());
		insertFileShareQuery.setParameter(3, now);
		insertFileShareQuery.setParameter(4, now);
		insertFileShareQuery.executeUpdate();
		
		entityManager.getTransaction().commit();
		
		// FIXME: This solution does not work if tests are run in a cluster 
		// or any other form of asynchronous processing
		Integer id = ((BigInteger) entityManager
				.createNativeQuery("SELECT LAST_INSERT_ID()")
				.getSingleResult()).intValue();
		
		String mappingSql = "INSERT INTO file_share_permission_map "
				+ "(file_share_id, permission_id) "
				+ "VALUES "
				+ "(?, ?), (?, ?)";
		
		entityManager.getTransaction().begin();
		
		List<Permission> permissions = fileShare.getPermissions();
				
		Query insertMappingQuery = entityManager.createNativeQuery(mappingSql);
		insertMappingQuery.setParameter(1, id);
		insertMappingQuery.setParameter(2, permissions.get(0).getId());
		
		insertMappingQuery.setParameter(3, id);
		insertMappingQuery.setParameter(4, permissions.get(1).getId());
		insertMappingQuery.executeUpdate();
		
		entityManager.getTransaction().commit();
		
		return id;
	}
	
	public FileShare buildValidFileShare(User owner)
	{		
		List<Permission> permissions = new ArrayList<>();
		permissions.add(entityManager.find(Permission.class, FIRST_PERMISSION_ID));
		permissions.add(entityManager.find(Permission.class, SECOND_PERMISSION_ID));
		
		FileShare fileShare = new FileShare();
		fileShare.setOwner(owner);
		fileShare.setPath("vacations/japan2018");
		fileShare.setPermissions(permissions);
		
		return fileShare;
	}

	public FileShare getFileShare(Integer id)
	{
		Query query = entityManager
			.createNativeQuery("SELECT * FROM file_share WHERE id = ?", FileShare.class);
		query.setParameter(1, id);

		return (FileShare) query.getSingleResult();
	}
	
	public boolean doesFileShareExist(Integer id)
	{
		Query countFileShareQuery = entityManager
			.createNativeQuery("SELECT COUNT(*) FROM file_share WHERE id = ?");
		countFileShareQuery.setParameter(1, id);
		long fileShareFoundInDb = (long) countFileShareQuery.getSingleResult();
		
		return fileShareFoundInDb == 1;
	}
	
	public boolean doFileSharePermissionMappingsExist(Integer id)
	{
		Query countMappingsQuery = entityManager
			.createNativeQuery("SELECT COUNT(*) FROM file_share_permission_map WHERE file_share_id = ?");
		countMappingsQuery.setParameter(1, id);
		long mappingsFoundInDb = (long) countMappingsQuery.getSingleResult();
		
		return mappingsFoundInDb > 0;
	}
}
