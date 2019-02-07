package de.hse.ws18swa10.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import de.hse.ws18swa10.entity.FileRequest;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PersistenceException;
import de.hse.ws18swa10.exception.persistence.ReferenceDoesntExistException;

public final class FileRequestDao extends BaseCrudDao<FileRequest>
{	
	public FileRequestDao(EntityManager entityManager)
	{
		super(entityManager);
	}
	
	@Override
	protected Class<FileRequest> getEntityClass()
	{
		return FileRequest.class;
	}
	
	@Override
	protected void throwDependingOnCauseForPersist(final Throwable cause)
		throws PersistenceException
	{
		if (cause instanceof IllegalStateException) {
			throw new ReferenceDoesntExistException(cause);
		}
	}
	
	public boolean wasRequestedBy(final Integer id, final User requester)
	{
		try {
			Query query = entityManager.createQuery(
				"SELECT f FROM FileRequest f WHERE f.id = :id AND f.requester.id = :requester_id"
			);
			query.setParameter("id", id);
			query.setParameter("requester_id", requester.getId());
			
			query.getSingleResult();
		} catch (NoResultException e) {
			return false;
		}
		
		return true;
	}
	
	public FileRequest findByToken(final String token)
	{
		entityManager.clear();
		
		try {
			Query query = entityManager.createQuery(
				"SELECT f FROM FileRequest f JOIN FETCH f.requester WHERE f.token = :token"
			);
			query.setParameter("token", token);
			
			return (FileRequest) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<FileRequest> findAllOpenRequestedBy(final User requester)
	{
		return findAllByOpenStatusRequestedBy(true, requester);
	}
	
	public List<FileRequest> findAllClosedRequestedBy(final User requester)
	{
		return findAllByOpenStatusRequestedBy(false, requester);
	}
	
	@SuppressWarnings("unchecked")
	private List<FileRequest> findAllByOpenStatusRequestedBy(boolean open, final User requester)
	{		
		List<FileRequest> fileRequests = entityManager
			.createQuery("SELECT f FROM FileRequest f WHERE f.open = :open AND f.requester.id = :requester_id")
			.setParameter("open", open)
			.setParameter("requester_id", requester.getId())
			.getResultList();		
				
		return fileRequests;
	}
	
	public void fulfill(final FileRequest fileRequest) throws PersistenceException
	{
		fileRequest.setFulfilled(true);
		fileRequest.setOpen(false);
		
		persist(fileRequest);
	}
}
