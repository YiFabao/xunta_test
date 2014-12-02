package so.xunta.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import so.xunta.entity.User;
import so.xunta.manager.UserManager;
import so.xunta.manager.impl.UserManagerImpl;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;

public class QQLogin extends HttpServlet {
	UserManager userManager=new UserManagerImpl();
	/**
	 * Constructor of the object.
	 */
	public QQLogin() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		String accessToken=request.getParameter("access_token");
		OpenID o = new OpenID(accessToken);
		String openId="";
		UserInfoBean userInfo=null;
		try {
			openId = o.getUserOpenID();
		
			userInfo = new UserInfo(accessToken, openId).getUserInfo();
		
		} catch (QQConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//查询数据库中是否存在openid
		User user=userManager.findUserbyQQOpenId(openId);
		System.out.println(userInfo.getNickname()+"登录");
		System.out.println("user:"+user);
	
		if(user==null)
		{
			//用户没有绑定账号
			user=new User();
			user.setQq_openId(openId);
			user.setQq_accessToken(accessToken);
			user.setCreateTime(new Date());
			userManager.addUser(user);
			
		
			System.out.println(userInfo.getNickname()+"登录");
			user.setXunta_username(userInfo.getNickname());
			request.getSession().setAttribute("user", user);
			response.sendRedirect("/");
			
		}
		else
		{
			if(user.getXunta_username()==null||"".equals(user.getXunta_username()))
			{
				user.setXunta_username("QQ_"+userInfo.getNickname());
			}
			//登录成功
			System.out.println("登录成功");
			request.getSession().setAttribute("user", user);
			response.sendRedirect(request.getContextPath()+"/");
		}
		

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
