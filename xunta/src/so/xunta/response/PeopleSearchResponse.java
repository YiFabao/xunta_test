package so.xunta.response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import so.xunta.localcontext.LocalContext;
import so.xunta.people.newest.PageData_Newest;
import so.xunta.people.newest.SearchPeople_Newest;
import so.xunta.people.relevant.PageData_Relevant;
import so.xunta.people.relevant.SearchPeople_Relevant;
import so.xunta.service.WsManager;

import com.aigine.common.p;

;

//消除servlet的实例变量,解决多用户同时访问时参数混乱冲突的问题.//2014.7.17
//创建一个专门用于存放请求参数的类:PeopleSearchVars.每个请求都会new一个出来. //2014.7.17
public class PeopleSearchResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("----------------------------------------begin of AcceptPeopleSearchRequest");
		PeopleSearchVars sV = new PeopleSearchVars();// 每个请求都创建一个存放请求参数的实例,供该请求独享.2014.7.17
		
		
		sV.beginTime = System.currentTimeMillis();
		// 获得搜索关键词:
		
		sV.searchKeywords = request.getParameter("searchKeywords");// 获得搜索关键词.
		sV.IP = request.getRemoteAddr();//获取IP地址
		
		//prt("IP:" + sV.IP);
		// sV.IP=request.getHeader("x-forwarded-for");
		// prt("x-forwarded-for:"+sV.IP);
		// sV.IP=request.getHeader("user-agent");
		// prt("user-agent:"+sV.IP);
		// prt("searchKeywords1:"+sV.searchKeywords);
		// prt("searchKeywords1-urldecoded:"+java.net.URLDecoder.decode(sV.searchKeywords));
		// prt("searchKeywords1-binarytohexstring:"+BinaryToHexString(searchKeywords.getBytes()));
		// prt("searchkeywords1-urlencoded:"+java.net.URLEncoder.encode(searchKeywords));

		
		if (sV.searchKeywords == null) {
			sV.searchKeywords = "结伴";
		}// 如果没有任何搜索词,则赋一个临时的.//搜索框中现在会有默认搜索词,所以这句一般没用了.
		else {
			sV.searchKeywords = new String(sV.searchKeywords.getBytes("ISO-8859-1"), "utf-8");// 这句是为了解决网页提交中文的乱码问题.
		}
		
		prt("searchKeywords：" + sV.searchKeywords);

		
		
		// 获得搜索结果的页号:
		sV.pageNo = request.getParameter("page");// 获得搜索结果的页号.
		
		if (sV.pageNo == null) {
			sV.pageNo = "1";
		} // 默认的页号为1.


		// String sessionId=request.getSession().getId();//独得会话期的ID.暂时没用上.
		// System.out.println("accept Request() - sessionId:   "+sessionId);
		
		//p.prt("PeopleSearchResponse()-getOneSearcherofTwo()之前: sV.searcher=" + sV.searcher);
		// 获得indexsearcher:
		//long t1=System.currentTimeMillis();
		sV.searcher = SearchMethods.getOneSearcherofTwo();// 生成的结果直接为SearchMethods.searcher;
		//long t2=System.currentTimeMillis();
		//System.out.println("获取searcher用时:"+(t2-t1));
		//p.prt("PeopleSearchResponse()-getOneSearcherofTwo()之后: sV.searcher=" + sV.searcher);

		// 获得搜索模式并分配执行方法:
		sV.searchMode = request.getParameter("searchMode");
		if (sV.searchMode == null) {
			sV.searchMode = "newest";
		}
		
		if (sV.searchMode.equals("newest")) {// 两种搜索模式的分支点.
			long t3=System.currentTimeMillis();
			call_SearchplusPageData_Newest(request, response, sV);
			long t4=System.currentTimeMillis();
			System.out.println("call_SearchplusPageData_newest 用时:"+(t4-t3));
		} else {
			long t3=System.currentTimeMillis();
			call_SearchplusPageData_Relevant(request, response, sV);
			long t4=System.currentTimeMillis();
			System.out.println("call_SearchplusPageData_Relevant 用时:"+(t4-t3));
			
		}
		long usedTime=(System.currentTimeMillis()-sV.beginTime);
		System.out.println("PeopleSearchResponse()- 搜索用时："+usedTime+ "|发生时间:" +System.currentTimeMillis());
		
		//WEBSOCKET广播该用户的搜索请求
		//构造JSON
		try {
		
			
			String ip=new String(WsManager.ipseeker.getCountry(sV.IP).getBytes(),"utf-8");
			
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time=sdf.format(date);
			
			JSONObject json=new JSONObject();
			
			json.append("ipaddress",ip).append("searchKeywords", sV.searchKeywords).append("time",time);
			
			WsManager.searchList.add(json);//将用户的搜索添加到自己定义的搜索词当中，以便后期随机显示
			System.out.println(json);
			WsManager.getInstance().broadcast(json);//给所有在线的用户广播
			System.out.println("//给所有在线的用户广播");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		Write2XuntaVisitLog.addLog(sV.IP, sV.searchKeywords, sV.searchMode, "PeopelSearchResponse()",(int)usedTime);
	}


	//搜索最新
	void call_SearchplusPageData_Newest(HttpServletRequest request, HttpServletResponse response, PeopleSearchVars sV) {
		System.out.println("搜索模式：最新");
		try {
			SearchPeople_Newest.searchPeople(sV);// 执行一次搜索,返回的AuthorRanking数据放在sV中.
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("AcceptPeopleSearchRequest-执行searchPeople之后:" +
		// (System.currentTimeMillis() - sV.begin) + "ms");

		try {
			sV.pageData_newest = new PageData_Newest(sV);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// ;currentPageNo取得某页的页面数据.

		sV.pageData_newest.setSearchKeywords(sV.searchKeywords);// 保留当前的搜索关键词,在结果中仍然出现.

		request.setAttribute("pageData", sV.pageData_newest);
		request.setAttribute("timepoint",sV.timepoint);

		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath + "pSearchResult_Newest.jsp", request, response);
	}

	
	//搜索达人
	void call_SearchplusPageData_Relevant(HttpServletRequest request, HttpServletResponse response, PeopleSearchVars sV) {
		prt("搜索模式：达人");
		try {
			long t1=System.currentTimeMillis();
			SearchPeople_Relevant.searchPeople(sV);// 获取请求参数中的搜索关键词字串,并执行一次搜索,返回AI数据.
			long t2=System.currentTimeMillis();
			System.out.println("earchPeople_Relevant.searchPeople(sV) 用时："+(t2-t1));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			long t3=System.currentTimeMillis();
			sV.pageData_relavent = new PageData_Relevant(sV);
			long t4=System.currentTimeMillis();
			System.out.println("sV.pageData_relavent = new PageData_Relevant(sV) 用时："+(t4-t3));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}// ;currentPageNo取得某页的页面数据.


		sV.pageData_relavent.setSearchKeywords(sV.searchKeywords);// 保留当前的搜索关键词,在结果中仍然出现.
		request.setAttribute("pageData", sV.pageData_relavent);
		request.setAttribute("timepoint",sV.timepoint);
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath + "pSearchResult_Relevant.jsp", request, response);
	}

	// 一段标准转发至jsp页面的代码,含异常处理:
	void dispatch(String jspPage, HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher requestDispatcher = this.getServletContext().getRequestDispatcher(jspPage);
		try {
			requestDispatcher.include(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// requestDispatcher.forward(request, response);
	}

	public void init() throws ServletException {
		(new Thread(new threadInit_IndexCheckTimer())).start();
		p.prt("SwapIndexSearcherIfChanged()线程已启动.");
	}

	public static void prt(Object o) {
		System.out.println(o);
	}

	public PeopleSearchResponse() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}