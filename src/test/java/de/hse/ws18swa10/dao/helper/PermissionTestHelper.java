package de.hse.ws18swa10.dao.helper;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.hse.ws18swa10.entity.Permission;

public final class PermissionTestHelper
{
	private static final String PERMISSION_TEST_NAME = "CREATE_FOR_TEST_PURPOSES";
	
	private EntityManager entityManager;
	
	public PermissionTestHelper(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public void deletePermissionsCreatedDuringTests()
	{
		String sql = "DELETE FROM permission WHERE name LIKE '" + PERMISSION_TEST_NAME + "%'";
		
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery(sql).executeUpdate();
		entityManager.getTransaction().commit();
	}
	
	public Integer persistPermissionAndGetId(Permission permission)
	{
		String sql = "INSERT INTO permission "
				+ "(name, is_directory, created_at, updated_at) "
				+ "VALUES "
				+ "(?, ?, ?, ?)";
		
		entityManager.getTransaction().begin();
		
		Date now = new Date();
		
		Query insertQuery = entityManager.createNativeQuery(sql);
		insertQuery.setParameter(1, permission.getName());
		insertQuery.setParameter(2, permission.getIsDirectory() ? 1 : 0);
		insertQuery.setParameter(3, now);
		insertQuery.setParameter(4, now);
		insertQuery.executeUpdate();
		
		entityManager.getTransaction().commit();
		
		// FIXME: This solution does not work if tests are run in a cluster 
		// or any other form of asynchronous processing
		return ((BigInteger) entityManager
			.createNativeQuery("SELECT LAST_INSERT_ID()")
			.getSingleResult()).intValue();
	}

	public Permission getPermission(Integer id)
	{
		Query query = entityManager
			.createNativeQuery("SELECT * FROM permission WHERE id = ?", Permission.class);
		query.setParameter(1, id);

		return (Permission) query.getSingleResult();
	}
	
	public boolean doesPermissionExist(Integer id)
	{
		Query selectQuery = entityManager
			.createNativeQuery("SELECT COUNT(*) FROM permission WHERE id = ?");
		selectQuery.setParameter(1, id);
		long permissionFoundInDb = (long) selectQuery.getSingleResult();
		
		return permissionFoundInDb == 1;
	}
	
	public Permission buildValidPermission()
	{
		Permission permission = new Permission();
		permission.setName(PERMISSION_TEST_NAME);
		permission.setIsDirectory(false);
				
		return permission;
	}
}
