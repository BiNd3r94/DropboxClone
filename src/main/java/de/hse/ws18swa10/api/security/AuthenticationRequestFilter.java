package de.hse.ws18swa10.api.security;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import de.hse.ws18swa10.dao.AuthTokenDao;
import de.hse.ws18swa10.dao.container.AuthTokenDaoContainer;
import de.hse.ws18swa10.entity.AuthToken;

@SecuredResource
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationRequestFilter implements ContainerRequestFilter
{
	private static final String REALM = "ws18-swa10";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    
    private AuthTokenDao dao = AuthTokenDaoContainer.getInstance();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {
        String authorizationHeader = requestContext
    		.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (! isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader
    		.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            AuthToken authToken = dao.findByToken(token);
            validateToken(authToken);
            requestContext.setProperty("authenticatedUser", authToken.getOwner());
        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader)
    {
        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
            .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext)
    {
        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
    	String wwwAuthenticateHeader = AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"";
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
            .header(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticateHeader)
            .build());
    }

    private void validateToken(AuthToken authToken) throws Exception
    {
        if (authToken == null || authToken.getExpiresAt().before(new Date())) {
        	throw new Exception("No valid authentication token found");
        }
    }
}
