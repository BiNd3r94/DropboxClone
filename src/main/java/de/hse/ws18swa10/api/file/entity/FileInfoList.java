package de.hse.ws18swa10.api.file.entity;

import java.util.List;

public class FileInfoList
{
	private String parentHash;
	private List<FileInfo> fileInfos;
	
	public String getParentHash()
	{
		return parentHash;
	}
	
	public void setParentHash(String parentHash)
	{
		this.parentHash = parentHash;
	}
	
	public List<FileInfo> getFileInfos()
	{
		return fileInfos;
	}
	
	public void setFileInfos(List<FileInfo> fileInfos)
	{
		this.fileInfos = fileInfos;
	}
}
