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



public class Register extends HttpServlet {
	UserManager userManager=new UserManagerImpl();
	private String errorMsg="";
	public Register() {
		super();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("utf-8");
		//获取表单数据
		String username=request.getParameter("xunta_username");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		String confirm=request.getParameter("confirm");
		String code=request.getParameter("code").toLowerCase();
		

		//服务器端验证
		
		//验证通过
		//保存到session中
		//跳转到注册成功页面
		boolean flag=serverValidate(request,response,username,email,password,confirm,code);
		if(flag)
		{
			//添加用户
			User user=new User();
			user.setXunta_username(username);
			user.setEmail(email);
			user.setPassword(password);
			Date createDatetime=new Date();
			user.setCreateTime(createDatetime);
			userManager.addUser(user);
			System.out.println("添加用户成功");
			//将用户保存到session中
			request.getSession().setAttribute("user",user);
			response.sendRedirect(request.getContextPath()+"/jsp/topic/index.jsp");
		}
		else
		{
			request.setAttribute("xunta_username",username);
			request.setAttribute("email",email);
			request.setAttribute("errorMsg",errorMsg);
			request.getRequestDispatcher("/jsp/xunta_user/register.jsp").forward(request, response);
		}
		//验证不通过
		//在request范围设置错误消息提示
		//跳转到注册页面
	}


	private boolean serverValidate(HttpServletRequest request,HttpServletResponse response,String username, String email, String password, String confirm, String code) {
		//密码不能为空
		if(password==null||"".equals(password.trim())||confirm==null||"".equals(confirm.trim()))
		{
			System.out.println("密码不能为空");
			errorMsg="密码不能为空";
			return false;
		}
		
		//密码与确认密码是否相同
			if(!password.equals(confirm))
			{
				System.out.println("密码与确认密码不相同");
				errorMsg="密码与确认密码不相同";
				return false;
			}
			//验证码是否相同
			String _code=(String) request.getSession().getAttribute("code");
			if(_code!=null)
			{
				_code=_code.toLowerCase();
			}
			System.out.println(_code);
			if(!code.equals(_code))
			{
				System.out.println("验证码错误");
				errorMsg="验证码错误";
				return false;
			}
			//验证邮件
			Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
			Matcher matcher = emailPattern.matcher(email);
			if(!matcher.find()){
				System.out.println("邮箱不合法");
				errorMsg="邮箱不合法";
				return false;
			}
			
			User user=userManager.findUser(username);
			if(user!=null)
			{
				errorMsg="用户名已存在";
				System.out.println("用户名已存在");
				return false;
			}
			user =userManager.findUserByEmail(email);
			if(user!=null)
			{
				errorMsg="邮箱已存在";
				System.out.println("邮箱已存在");
				return false;
			}
			//将验证码删除
			request.getSession().removeAttribute("code");
			request.removeAttribute("errorMsg");
			return true;
			
			
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);

	}
}
