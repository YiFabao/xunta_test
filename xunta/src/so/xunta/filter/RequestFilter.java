package so.xunta.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import so.xunta.entity.User;

public class RequestFilter implements Filter {
	private FilterConfig config;
	@Override
	public void destroy() {
		//System.out.println("==================>destroy");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			//System.out.println("请求："+request.getRemoteAddr());
		
			HttpServletRequest httpRequest=(HttpServletRequest) request;
			HttpServletResponse httpResponse=(HttpServletResponse) response;
			
			HttpSession session=httpRequest.getSession();
			
			String url=httpRequest.getServletPath();  
			
			
			Enumeration<String> strs=config.getInitParameterNames();
			boolean islegal=false;
			while(strs.hasMoreElements()){
				String paramName=strs.nextElement();
				//System.out.println("param-name:"+s+"  param-value:"+config.getInitParameter(s));
				String paramValue=config.getInitParameter(paramName);
				//System.out.println(url+"   "+paramValue);
				
				if(url!=null&&url.indexOf(paramValue)!=-1)
				{
					islegal=true;
					break;
				}
			}
			if(islegal)
			{
				chain.doFilter(request, response);
			}
			else{//如果不是排除的目录，就要验证登录名
				//System.out.println("需要验证用户名");
				User user=(User) session.getAttribute("user");
				//System.out.println("是否存在用户："+user);
				if(user!=null)
				{
					chain.doFilter(request, response);
				}
				else{
					httpResponse.sendRedirect(httpRequest.getContextPath()+"/jsp/xunta_user/login.jsp");//跳转到登录页面
				}
			}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		//System.out.println("Filter初始化....");
		this.config=config;
	}


	

}
