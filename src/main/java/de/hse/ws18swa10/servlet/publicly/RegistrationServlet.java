package de.hse.ws18swa10.servlet.publicly;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/public/register")
public class RegistrationServlet extends HttpServlet
{	private static final long serialVersionUID = -984065828304143962L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		request.getRequestDispatcher("/public/register.html").forward(request, response);
	}
}