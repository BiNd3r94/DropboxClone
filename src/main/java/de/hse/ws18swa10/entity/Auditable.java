package de.hse.ws18swa10.entity;

public interface Auditable
{
	public Audit getAudit();
	
    public void setAudit(Audit audit);
}
