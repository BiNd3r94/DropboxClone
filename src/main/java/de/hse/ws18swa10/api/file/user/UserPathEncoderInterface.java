package de.hse.ws18swa10.api.file.user;

public interface UserPathEncoderInterface
{
	public String encode(String path);
	
	public String decode(String pathHash);
}
