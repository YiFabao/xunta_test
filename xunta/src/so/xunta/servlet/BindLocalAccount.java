package so.xunta.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import so.xunta.entity.User;
import so.xunta.manager.UserManager;
import so.xunta.manager.impl.UserManagerImpl;

public class BindLocalAccount extends HttpServlet {
	UserManager userManager=new UserManagerImpl();
	/**
	 * Constructor of the object.
	 */
	public BindLocalAccount() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		String qq_openId=request.getParameter("qq_openId");
		String qq_accessToken=request.getParameter("qq_accessToken");
		String xunta_username=request.getParameter("xunta_username");
		String password=request.getParameter("password");
		String email=request.getParameter("email");
		
		boolean flag=serverValidate(request, response, xunta_username, email, password);
		
		if(flag)
		{
			System.out.println("验证通过,调转到成功页面");
			User user=new User();
			user.setXunta_username(xunta_username);
			user.setEmail(email);
			user.setPassword(password);
			user.setQq_openId(qq_openId);
			user.setQq_accessToken(qq_accessToken);
			Date createTime=new Date();
			user.setCreateTime(createTime);
			userManager.addUser(user);
			System.out.println("保存数据成功");
			request.getSession().setAttribute("user", user);
			
			
		}
		
		System.out.println(qq_openId);
		System.out.println(qq_accessToken);
		System.out.println(xunta_username);
		System.out.println(password);
		System.out.println(email);
		
		
	}

	private boolean serverValidate(HttpServletRequest request,HttpServletResponse response,String username, String email, String password) {
		//密码不能为空
		if(password==null||"".equals(password.trim()))
		{
			System.out.println("密码不能为空");
			return false;
		}
		
		//验证邮件
		Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = emailPattern.matcher(email);
		if(!matcher.find()){
			System.out.println("邮箱不合法");
			return false;
		}
		
		User user=userManager.findUser(username);
		if(user!=null)
		{
			System.out.println("用户名不存在");
			return false;
		}
		user =userManager.findUserByEmail(email);
		if(user!=null)
		{
			System.out.println("邮箱已存在");
			return false;
		}
		return true;
			
			
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
