package de.hse.ws18swa10.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "auth_token")
public class AuthToken extends BaseEntity
{
	@Column(unique=true, nullable=false)
	private String token;
	
	@ManyToOne
	@JoinColumn(name="owner_id", nullable=false)
	private User owner;
	
	@Column(name="expires_at")
	@Temporal(TemporalType.DATE)
	private Date expiresAt;
	
	public String getToken()
	{
		return token;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}
	
	public User getOwner()
	{
		return owner;
	}

	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	public Date getExpiresAt()
	{
		return expiresAt;
	}
	
	public void setExpiresAt(final Date expiresAt)
	{
		this.expiresAt = expiresAt;
	}
}
