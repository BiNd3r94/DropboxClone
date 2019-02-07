package de.hse.ws18swa10.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file_share")
public class FileShare extends BaseEntity
{
	@ManyToOne
	@JoinColumn(name="owner_id", nullable=false)
	private User owner;
	
	@Column(nullable=false)
	private String path;
	
	@Column(name="is_directory", nullable=false, columnDefinition="tinyint(1) default 0")
	private boolean isDirectory = false;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
		name = "file_share_user_map",
        joinColumns = @JoinColumn(name = "file_share_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
	private List<User> users = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
		name = "file_share_permission_map",
        joinColumns = @JoinColumn(name = "file_share_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
	private List<Permission> permissions = new ArrayList<>();

	public User getOwner()
	{
		return owner;
	}

	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public boolean getIsDirectory()
	{
		return isDirectory;
	}

	public void setIsDirectory(boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	public List<Permission> getPermissions()
	{
		return permissions;
	}

	public void setPermissions(List<Permission> permissions)
	{
		this.permissions = permissions;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null) return false;
		if (other == this) return true;
		if (! (other instanceof FileShare)) return false;
		
		FileShare otherFileShare = (FileShare) other;
				
		return otherFileShare.getId().equals(getId())
			&& otherFileShare.getPath().equals(getPath())
			&& otherFileShare.getOwner().equals(getOwner())
			&& (getPermissions() != null && getPermissions().equals(otherFileShare.getPermissions()))
			&& (getUsers() != null && getUsers().equals(otherFileShare.getUsers()));
	}
}
