package de.hse.ws18swa10.dao;

import java.util.List;

import javax.persistence.EntityManager;

import de.hse.ws18swa10.entity.Permission;

public class PermissionDao extends BaseCrudDao<Permission>
{
	public PermissionDao(EntityManager entityManager)
	{
		super(entityManager);
	}

	@Override
	protected Class<Permission> getEntityClass()
	{
		return Permission.class;
	}

	@SuppressWarnings("unchecked")
	public List<Permission> findAll()
	{
		entityManager.getTransaction().begin();
		
		List<Permission> permissions = entityManager
			.createQuery("SELECT p FROM Permission p")
			.getResultList();
		
		entityManager.getTransaction().commit();
		
		return permissions;
	}
}
