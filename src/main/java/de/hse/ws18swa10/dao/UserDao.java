package de.hse.ws18swa10.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PersistenceException;
import de.hse.ws18swa10.exception.persistence.UniqueConstraintViolationException;

public final class UserDao extends BaseCrudDao<User>
{	
	public UserDao(EntityManager entityManager)
	{
		super(entityManager);
	}
	
	@Override
	protected Class<User> getEntityClass()
	{
		return User.class;
	}
	
	@Override
	protected void throwDependingOnCauseForPersist(Throwable cause) 
		throws PersistenceException
	{
		if (cause instanceof MySQLIntegrityConstraintViolationException) {
			throw new UniqueConstraintViolationException(cause, "email");
		}
		
		super.throwDependingOnCauseForPersist(cause);
	}
	
	public User findUserByEmailAndPassword(final String email, final String password)
	{		
		try {
			Query query = entityManager.createQuery(
				"SELECT u FROM User u WHERE u.email = :email AND u.password = :password"
			);
			query.setParameter("email", email);
			query.setParameter("password", password);
			
			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	};
	
	public User findByEmail(final String email)
	{
		try {
			Query query = entityManager.createQuery(
				"SELECT u FROM User u WHERE u.email = :email"
			);
			query.setParameter("email", email);
			
			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public void register(final User user) throws PersistenceException 
	{
		user.setFileSpaceName(user.getUsername());
		persist(user);
	}
}
