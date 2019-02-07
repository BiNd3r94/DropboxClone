package de.hse.ws18swa10.servlet.publicly;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.hse.ws18swa10.dao.UserDao;
import de.hse.ws18swa10.dao.container.UserDaoContainer;
import de.hse.ws18swa10.entity.User;

@WebServlet("/public/login")
public class LoginServlet extends HttpServlet
{	
	private static final long serialVersionUID = 5822372892491462852L;
	
	private static final String ROOT_URL = "/ws18-swa10";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		request.getRequestDispatcher("/public/login.html").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		UserDao userDao = UserDaoContainer.getInstance();
		User user = userDao.findUserByEmailAndPassword(email, password);
		
		if (user == null) {
			request.getRequestDispatcher("/public/login.html").forward(request, response);
			return;
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		
		response.sendRedirect(ROOT_URL);
	}
}
