package de.hse.ws18swa10.api.file.entity;

import de.hse.ws18swa10.api.file.PathInfo;

public class FileInfo extends PathInfo
{
	private String hash;
	private String parentHash;
	private int shareCount = 0;
	
	public String getHash()
	{
		return hash;
	}
	
	public void setHash(String hash)
	{
		this.hash = hash;
	}
	
	public String getParentHash()
	{
		return parentHash;
	}

	public void setParentHash(String parentHash)
	{
		this.parentHash = parentHash;
	}

	public int getShareCount()
	{
		return shareCount;
	}

	public void setShareCount(int shareCount)
	{
		this.shareCount = shareCount;
	}
}
