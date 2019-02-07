package de.hse.ws18swa10.api.security;

import de.hse.ws18swa10.dao.container.AuthTokenDaoContainer;
import de.hse.ws18swa10.dao.container.UserDaoContainer;
import de.hse.ws18swa10.util.DefaultTokenGenerator;

public class AuthenticatorFactory
{
	public static Authenticator createTokenIssuingAuthenticator()
	{
		return new Authenticator(
			UserDaoContainer.getInstance(), 
			AuthTokenDaoContainer.getInstance(),
			new DefaultTokenGenerator()
		);
	}
}
