package de.hse.ws18swa10.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.hse.ws18swa10.entity.FileShare;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.exception.PersistenceException;
import de.hse.ws18swa10.exception.persistence.ReferenceDoesntExistException;

public class FileShareDao extends BaseCrudDao<FileShare>
{
	public FileShareDao(EntityManager entityManager)
	{
		super(entityManager);
	}

	@Override
	protected Class<FileShare> getEntityClass()
	{
		return FileShare.class;
	}
	
	@Override
	protected void throwDependingOnCauseForPersist(final Throwable cause)
		throws PersistenceException
	{
		if (cause instanceof IllegalStateException) {
			throw new ReferenceDoesntExistException(cause);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FileShare> findAllOwnedBy(final User owner)
	{
		Query query = entityManager.createQuery(
			"SELECT f FROM FileShare f WHERE f.owner.id = :owner_id"
		);
		query.setParameter("owner_id", owner.getId());
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FileShare> findAllForMember(final User member)
	{
		entityManager.clear();
		
		Query query = entityManager.createQuery(
			"SELECT f FROM FileShare f INNER JOIN f.users members WHERE members.id IN :member_ids"
		);
		query.setParameter("member_ids", Arrays.asList(member.getId()));
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Integer> findShareCountForPathsOwnedBy(final List<String> paths, final User owner)
	{
		String pathList = "'" + paths.stream().collect(Collectors.joining("','")) + "'";
		
		Query query = entityManager.createNativeQuery(
			"SELECT path, CAST(COUNT(m.user_id) AS SIGNED) AS shareCount " + 
			"FROM file_share f " + 
			"INNER JOIN `user` u ON f.owner_id = u.id " + 
			"INNER JOIN file_share_user_map m ON f.id = m.file_share_id " + 
			"WHERE f.owner_id = ?1 " + 
			"GROUP BY f.path " +
			"HAVING f.path IN (" + pathList + ")"
		);
		query.setParameter(1, owner.getId());
		
		Map<String, Integer> map = new HashMap<>();
		List<Object[]> list = query.getResultList();
		
		list.forEach((r) -> map.put((String) r[0], ((Long) r[1]).intValue()));
		
		return map;
	}
	
	public void removeByPath(String path)
	{
		entityManager.getTransaction().begin();
		
		Query query = entityManager.createQuery(
			"DELETE FROM FileShare f WHERE f.path = :path"
		);
		query.setParameter("path", path);
		
		query.executeUpdate();
		
		entityManager.getTransaction().commit();
	}
}
