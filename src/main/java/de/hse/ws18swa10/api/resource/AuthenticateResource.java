package de.hse.ws18swa10.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hse.ws18swa10.api.security.Authenticator;
import de.hse.ws18swa10.api.security.AuthenticatorFactory;
import de.hse.ws18swa10.api.security.Credentials;
import de.hse.ws18swa10.api.security.TokenContainer;

@Path("authenticate")
public class AuthenticateResource
{
	private final Authenticator authenticator = AuthenticatorFactory.createTokenIssuingAuthenticator();
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(final Credentials credentials)
	{
        try {
            TokenContainer tokenContainer = authenticator.authenticate(credentials);
            
            return Response.ok(tokenContainer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }      
    }
}
