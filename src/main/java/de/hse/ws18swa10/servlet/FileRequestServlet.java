package de.hse.ws18swa10.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/file-requests")
public class FileRequestServlet extends HttpServlet
{
	private static final long serialVersionUID = -37267184894325414L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		request.getRequestDispatcher("fileRequest.html").forward(request, response);
	}
}
