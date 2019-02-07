package de.hse.ws18swa10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Permission extends BaseEntity
{
	@Column(nullable=false)
	private String name;
	
	@Column(name="is_directory", nullable=false, columnDefinition="tinyint(1) default 0")
	private boolean isDirectory = false;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean getIsDirectory()
	{
		return isDirectory;
	}

	public void setIsDirectory(boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null) return false;
		if (other == this) return true;
		if (! (other instanceof Permission)) return false;
		
		Permission otherPermission = (Permission) other;
		
		return otherPermission.getId().equals(getId())
			&& otherPermission.getName().equals(getName())
			&& otherPermission.getIsDirectory() == getIsDirectory();
	}
}
