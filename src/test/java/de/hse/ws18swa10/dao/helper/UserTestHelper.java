package de.hse.ws18swa10.dao.helper;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.hse.ws18swa10.entity.User;

public final class UserTestHelper
{
	private EntityManager entityManager;
	private int fileSpaceNameCounter = 0;
	
	public UserTestHelper(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public void wipeUserTable()
	{
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("DELETE FROM user").executeUpdate();
		entityManager.getTransaction().commit();
	}
	
	public Integer persistUserAndGetId(User user)
	{
		String sql = "INSERT INTO user "
			+ "(email, password, first_name, last_name, file_space_name, created_at, updated_at, username) "
			+ "VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?, ?)";
		
		entityManager.getTransaction().begin();
		
		Date now = new Date();
		
		Query insertQuery = entityManager.createNativeQuery(sql);
		insertQuery.setParameter(1, user.getEmail());
		insertQuery.setParameter(2, user.getPassword());
		insertQuery.setParameter(3, user.getFirstName());
		insertQuery.setParameter(4, user.getLastName());
		insertQuery.setParameter(5, user.getFileSpaceName());
		insertQuery.setParameter(6, now);
		insertQuery.setParameter(7, now);
		insertQuery.setParameter(8, user.getUsername());
		insertQuery.executeUpdate();
		
		entityManager.getTransaction().commit();
		
		Query selectQuery = entityManager.createNativeQuery("SELECT id FROM user WHERE email = ?");
		selectQuery.setParameter(1, user.getEmail());
		
		return (Integer) selectQuery.getSingleResult();
	}
	
	public User getUser(Integer id)
	{
		Query query = entityManager.createNativeQuery("SELECT * FROM user WHERE id = ?", User.class);
		query.setParameter(1, id);

		return (User) query.getSingleResult();
	}
	
	public boolean doesUserExist(Integer id)
	{
		Query selectQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM user WHERE id = ?");
		selectQuery.setParameter(1, id);
		long userFoundInDb = (long) selectQuery.getSingleResult();
		
		return userFoundInDb == 1;
	}
	
	public User buildValidTestUser()
	{
		User user = new User();
		
		user.setEmail("max.mustermann@gmail.com");
		user.setUsername("testspace" + fileSpaceNameCounter);
		user.setPassword("123456");
		user.setFirstName("Max");
		user.setLastName("Mustermann");
		user.setFileSpaceName("testspace" + fileSpaceNameCounter);
		
		fileSpaceNameCounter++;
		
		return user;
	}
}
