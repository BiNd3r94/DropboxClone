package de.hse.ws18swa10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class User extends BaseEntity
{
	@Column(unique=true, nullable=false)
	private String email;
	
	@Column(unique=true, nullable=false)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@Column(name="first_name", nullable=false)
	private String firstName;
	
	@Column(name="last_name", nullable=false)
	private String lastName;
	
	@Column(name="file_space_name")
	private String fileSpaceName;
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getFileSpaceName()
	{
		return fileSpaceName;
	}
	
	public void setFileSpaceName(String fileSpaceName)
	{
		this.fileSpaceName = fileSpaceName;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null) return false;
		if (other == this) return true;
		if (! (other instanceof User)) return false;
		
		User otherUser = (User) other;
		
		return otherUser.getId().equals(getId()) 
			&& otherUser.getEmail().equals(getEmail())
			&& otherUser.getPassword().equals(getPassword())
			&& otherUser.getFirstName().equals(getFirstName())
			&& otherUser.getLastName().equals(getLastName());
	}
}
