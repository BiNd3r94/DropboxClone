package de.hse.ws18swa10.dao;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import de.hse.ws18swa10.exception.PersistenceException;
import de.hse.ws18swa10.exception.persistence.ForeignKeyConstraintViolationException;

public abstract class BaseCrudDao<T>
{
	protected final EntityManager entityManager;
	
	public BaseCrudDao(final EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	abstract protected Class<T> getEntityClass();
	
	public void persist(final T entity) throws PersistenceException
	{
		try {
			tryToPersist(entity);
		} catch (RollbackException e) {
			throwDependingOnCauseForPersist(e.getCause());
		}
	}
	
	protected void tryToPersist(final T entity) throws RollbackException
	{
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
	}
	
	protected void throwDependingOnCauseForPersist(final Throwable cause) 
		throws PersistenceException
	{
		throw new PersistenceException(cause);
	}
	
	public T find(Integer id)
	{
		return (T) entityManager.find(getEntityClass(), id);
	}
	
	public void remove(Integer id) throws PersistenceException
	{
		try {
			tryToRemove(id);
		} catch (RollbackException e) {
			throwDependingOnCauseForRemove(e.getCause());
		}
	}
	
	private void tryToRemove(Integer id) throws RollbackException
	{
		T entity = find(id);
		
		if (entity != null) {
			entityManager.getTransaction().begin();
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
		}
	}
	
	protected void throwDependingOnCauseForRemove(final Throwable cause) 
		throws PersistenceException
	{
		if (cause instanceof MySQLIntegrityConstraintViolationException) {
			throw new ForeignKeyConstraintViolationException(cause);
		}
	}
}
