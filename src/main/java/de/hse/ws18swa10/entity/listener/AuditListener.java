package de.hse.ws18swa10.entity.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import de.hse.ws18swa10.entity.Audit;
import de.hse.ws18swa10.entity.Auditable;

public class AuditListener
{
	@PrePersist
	public void setCreatedAt(Auditable auditable)
	{
		Audit audit = auditable.getAudit();
		
		if (audit == null) {
			audit = new Audit();
			auditable.setAudit(audit);
		}
		
		Date now = new Date();
		
		audit.setCreatedAt(now);
		audit.setUpdatedAt(now);
	}
	
	@PreUpdate
	public void setUpdatedAt(Auditable auditable)
	{
		Audit audit = auditable.getAudit();
		audit.setUpdatedAt(new Date());
	}
}
