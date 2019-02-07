package de.hse.ws18swa10.api.file.user;

import java.util.Base64;

public class DefaultUserSpacePathEncoder implements UserPathEncoderInterface
{
	public String encode(String path)
	{
		return Base64.getEncoder().encodeToString(path.getBytes());
	}
	
	public String decode(String pathHash)
	{
		return new String(Base64.getDecoder().decode(pathHash));
	}
}
