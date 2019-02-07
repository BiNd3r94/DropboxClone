package de.hse.ws18swa10.entity;

import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import de.hse.ws18swa10.entity.listener.AuditListener;

@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class BaseEntity implements Auditable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Embedded
	private Audit audit;
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}
	
	public Audit getAudit()
	{
		return audit;
	}
	
	public void setAudit(Audit audit)
	{
		this.audit = audit;
	}
}
