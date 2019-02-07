package de.hse.ws18swa10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="file_request")
public class FileRequest extends BaseEntity
{	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="requester_id", nullable=false)
	private User requester;
	
	@Column(name="token", nullable=false)
	private String token;
	
	@Column(nullable=false)
	private String request;
	
	@Column(name="target_path", nullable=false)
	private String targetPath;
	
	@Column(nullable=false, columnDefinition="tinyint(1) default 0")
	private boolean fulfilled = false;
	
	@Column(nullable=false, columnDefinition="tinyint(1) default 1")
	private boolean open = true;

	public User getRequester()
	{
		return requester;
	}

	public void setRequester(User requester)
	{
		this.requester = requester;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getRequest()
	{
		return request;
	}

	public void setRequest(String request)
	{
		this.request = request;
	}

	public String getTargetPath()
	{
		return targetPath;
	}

	public void setTargetPath(String targetPath)
	{
		this.targetPath = targetPath;
	}

	public boolean hasBeenFulfilled()
	{
		return fulfilled;
	}

	public void setFulfilled(boolean fulfilled)
	{
		this.fulfilled = fulfilled;
	}
	
	public boolean isOpen()
	{
		return open;
	}

	public void setOpen(boolean open)
	{
		this.open = open;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null) return false;
		if (other == this) return true;
		if (! (other instanceof FileRequest)) return false;
		
		FileRequest otherFileRequest = (FileRequest) other;
		
		return otherFileRequest.getId().equals(getId())
			&& otherFileRequest.getToken().equals(getToken())
			&& otherFileRequest.getRequester().equals(getRequester())
			&& otherFileRequest.getRequest().equals(getRequest())
			&& otherFileRequest.getTargetPath().equals(getTargetPath());
	}
}
