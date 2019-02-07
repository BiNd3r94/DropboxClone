package de.hse.ws18swa10.api.security;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import de.hse.ws18swa10.dao.AuthTokenDao;
import de.hse.ws18swa10.dao.UserDao;
import de.hse.ws18swa10.entity.AuthToken;
import de.hse.ws18swa10.entity.User;
import de.hse.ws18swa10.util.TokenGeneratorInterface;

public class Authenticator
{
	private final UserDao userDao;
	private final AuthTokenDao tokenDao;
	private final TokenGeneratorInterface tokenGenerator;
	
	public Authenticator(
		final UserDao userDao, 
		final AuthTokenDao tokenDao,
		final TokenGeneratorInterface tokenGenerator
	) {
		this.userDao = userDao;
		this.tokenDao = tokenDao;
		this.tokenGenerator = tokenGenerator;
	}

	public TokenContainer authenticate(final Credentials credentials)
		throws Exception
	{
		User user = userDao.findUserByEmailAndPassword(
			credentials.getUsername(), 
			credentials.getPassword()
		);
		
		if (user == null) {
			throw new Exception("No user found for provided credentials");
		}
		
		String token = issueToken(user);
        tokenDao.removeAllOwnedBy(user);
        storeToken(user, token);
        
        return new TokenContainer(user, token);
	}
	
	private String issueToken(User owner)
    {
    	return owner.getEmail() + tokenGenerator.generateToken();
    }
    
    private void storeToken(User owner, String token) throws Exception
    {
    	LocalDate dateInAMonth = LocalDate.now().plusMonths(1);
    	Date expiresAt = Date.from(dateInAMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	
    	AuthToken authToken = new AuthToken();
    	authToken.setOwner(owner);
    	authToken.setToken(token);
    	authToken.setExpiresAt(expiresAt);
    	
    	tokenDao.persist(authToken);
    }
}
