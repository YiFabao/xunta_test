package so.xunta.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Authorization extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Authorization() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		String contextPath=request.getContextPath();
		String redirect_url = URLEncoder.encode("http://xunta.so"+contextPath+"/jsp/xunta_user/jsp_token.jsp","utf-8");
		String url = "https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=101100198&redirect_uri="
		+ redirect_url + "&scope=do_like,get_user_info,get_info,add_t";
		response.sendRedirect(url);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		doGet(request, response);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
