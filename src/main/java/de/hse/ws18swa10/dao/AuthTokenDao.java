package de.hse.ws18swa10.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import de.hse.ws18swa10.entity.AuthToken;
import de.hse.ws18swa10.entity.User;

public class AuthTokenDao extends BaseCrudDao<AuthToken>
{
	public AuthTokenDao(EntityManager entityManager)
	{
		super(entityManager);
	}

	@Override
	protected Class<AuthToken> getEntityClass()
	{
		return AuthToken.class;
	}
	
	public AuthToken findByToken(String token)
	{
		try {
			Query query = entityManager.createQuery(
				"SELECT at FROM AuthToken at WHERE at.token = :token"
			);
			query.setParameter("token", token);
			
			return (AuthToken) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public void removeAllOwnedBy(User owner)
	{
		entityManager.getTransaction().begin();
		
		Query query = entityManager.createQuery(
			"DELETE FROM AuthToken at WHERE at.owner.id = :ownerId"
		);
		query.setParameter("ownerId", owner.getId());
		query.executeUpdate();
		
		entityManager.getTransaction().commit();
	}
}
