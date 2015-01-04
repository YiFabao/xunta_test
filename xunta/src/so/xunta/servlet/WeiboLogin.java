package so.xunta.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import so.xunta.entity.User;
import so.xunta.manager.UserManager;
import so.xunta.manager.impl.UserManagerImpl;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

public class WeiboLogin extends HttpServlet {
	UserManager userManager=new UserManagerImpl();
	/**
	 * Constructor of the object.
	 */
	public WeiboLogin() {
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
		Oauth oauth = new Oauth();
		AccessToken accessToken=null;
		String code=request.getParameter("code");
		Users um = new Users();
		weibo4j.model.User u=null;
		String weibo_acceesToken="";
		String weibo_uid="";
		User user=null;
		try {
			accessToken =oauth.getAccessTokenByCode(code);
			//通过accessToken获取用户信息
			um.client.setToken(accessToken.getAccessToken());
			 weibo_acceesToken=accessToken.getAccessToken();
			 weibo_uid=accessToken.getUid();
			user=userManager.findUserByWeiboUid(weibo_uid);
		} catch (WeiboException e) {
			System.out.println("获取accessToken对象失败："+e.getMessage());
		}

		try {
			u=um.showUserById(weibo_uid);
			System.out.println("微博名称："+u.getScreenName());
	
		} catch (WeiboException e) {
			System.out.println(e.getMessage());
		}

		if(user==null)
		{
			//用户没有绑定账号
			user=new User();
			user.setWeibo_accessToken(weibo_acceesToken);
			user.setWeibo_uid(weibo_uid);
			user.setCreateTime(new Date());
			userManager.addUser(user);

			System.out.println(u.getScreenName()+"登录");
			user.setXunta_username(u.getScreenName());
			request.getSession().setAttribute("user", user);
			response.sendRedirect(request.getContextPath()+"/");
		}
		else
		{
			if(user.getXunta_username()==null||"".equals(user.getXunta_username()))
			{
				user.setXunta_username("新浪微博_"+u.getScreenName());
			}
			//登录成功
			System.out.println("登录成功");
			request.getSession().setAttribute("user", user);
			response.sendRedirect(request.getContextPath()+"/");
		}
		
		
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
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
	
	public static void main(String[] args) {
		String code="981371869f773cabfe7c57966bc18473";
		weibo4j.model.User u=null;
		Oauth oauth = new Oauth();
		AccessToken accessToken=null;
		String weibo_acceesToken="";
		String weibo_uid="";
		User user=null;
		try {
			accessToken =oauth.getAccessTokenByCode(code);
			//通过accessToken获取用户信息
			String aa=weibo_acceesToken=accessToken.getAccessToken();
			System.out.println(aa);

		} catch (WeiboException e) {
			System.out.println("获取accessToken对象失败："+e.getMessage());
		}
	}

}
