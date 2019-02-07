package de.hse.ws18swa10.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthenticationFilter implements Filter
{
	private static final String API_URI = "/api";
	private static final String API_LOGIN_URI = API_URI + "/authenticate";
	private static final String PUBLIC_URI = "/public";
	private static final String LOGIN_URI = PUBLIC_URI + "/login";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException	{}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        
        String requestUri = httpRequest.getRequestURI();
        String apiLoginUri = httpRequest.getContextPath() + API_LOGIN_URI;
        String publicApiUri = httpRequest.getContextPath() + API_URI + PUBLIC_URI;
        String loginUri = httpRequest.getContextPath() + LOGIN_URI;
        String publicUri = httpRequest.getContextPath() + PUBLIC_URI;
        
        boolean apiLoginRequest = requestUri.equals(apiLoginUri);
        boolean publicRequest = requestUri.startsWith(publicUri) || requestUri.startsWith(publicApiUri);

        if (loggedIn || apiLoginRequest || publicRequest) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(loginUri);
        }
	}

	@Override
	public void destroy() {}
}
