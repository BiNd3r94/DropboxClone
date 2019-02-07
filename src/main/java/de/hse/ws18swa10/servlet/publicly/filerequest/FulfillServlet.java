package de.hse.ws18swa10.servlet.publicly.filerequest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/public/file-requests/fulfill/*")
public class FulfillServlet extends HttpServlet
{
	private static final long serialVersionUID = 7527421048423452428L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		request.getRequestDispatcher("/public/fileRequest/fulfill.html").forward(request, response);
	}
}
