package de.hse.ws18swa10.api.security;

import de.hse.ws18swa10.entity.User;

public class TokenContainer
{
	private final User user;
	private final String token;
	
	public TokenContainer(final User user, final String token)
	{
		this.user = user;
		this.token = token;
	}

	public User getUser()
	{
		return user;
	}

	public String getToken()
	{
		return token;
	}
}
