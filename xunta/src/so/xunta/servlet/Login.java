package so.xunta.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import so.xunta.entity.User;
import so.xunta.manager.UserManager;
import so.xunta.manager.impl.UserManagerImpl;

public class Login extends HttpServlet {

	UserManager userManager = new UserManagerImpl();
	/**
	 * Constructor of the object.
	 */
	public Login() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String username=request.getParameter("xunta_username");
		String password=request.getParameter("password");
		String checkbox=request.getParameter("checkbox");
		System.out.println(checkbox);
		User user=userManager.checkRegisterUserExist(username, password);
		if(user==null)
		{
			System.out.println("用户名或密码错误");
			request.setAttribute("xunta_username",username);
			request.setAttribute("errorMsg","用户名或密码错误");
			request.getRequestDispatcher("/jsp/xunta_user/login.jsp").forward(request, response);
		}
		else
		{
			System.out.println(user.getXunta_username()+"登录成功");
			//将user保存到session中
			request.getSession().setAttribute("user",user);
			if("on".equals(checkbox))
			{
				Cookie cookie=new Cookie("user",user.getXunta_username());
				cookie.setSecure(true);
				response.addCookie(cookie);
				System.out.println("添加cookie成功");
	
			}
			response.sendRedirect(request.getContextPath()+"/jsp/topic/index.jsp");
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

	public void init() throws ServletException {

	}

}
