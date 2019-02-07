package de.hse.ws18swa10.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public final class DefaultTokenGenerator implements TokenGeneratorInterface
{
	public String generateToken()
	{
		Random random = new SecureRandom();
    	String token = new BigInteger(130, random).toString(32);
    	
    	return token;
	}
}
