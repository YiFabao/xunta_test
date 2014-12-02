package so.xunta.response;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aigine.common.MyTimeClass;

import so.xunta.people.newest.PageData_Newest;
import so.xunta.people.newest.SearchPeople_Newest;
import so.xunta.people.relevant.PageData_Relevant;
import so.xunta.people.relevant.SearchPeople_Relevant;
import so.xunta.localcontext.*;;


public class WeixinSearchResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("----------------------------------------begin of AcceptPeopleSearchRequest"); 
		PeopleSearchVars sV=new PeopleSearchVars();//每个请求都创建一个存放请求参数的实例,供该请求独享.2014.7.17
		sV.beginTime = System.currentTimeMillis();
		
		sV.searchKeywords=request.getParameter("searchKeywords");//获得搜索关键词.
		sV.IP=request.getRemoteAddr();
		prt("IP:"+sV.IP); 
		//prt("searchKeywords1:"+searchKeywords);
		//prt("searchKeywords1-urldecoded:"+java.net.URLDecoder.decode(searchKeywords));
		//prt("searchKeywords1-binarytohexstring:"+BinaryToHexString(searchKeywords.getBytes()));
		//prt("searchkeywords1-urlencoded:"+java.net.URLEncoder.encode(searchKeywords));
		
		if (sV.searchKeywords==null) {sV.searchKeywords="结伴";}//如果没有任何搜索词,则赋一个临时的.//搜索框中现在会有默认搜索词,所以这句一般没用了.
		else {
			sV.searchKeywords=new String(sV.searchKeywords.getBytes("ISO-8859-1"),"utf-8");//这句是为了解决网页提交中文的乱码问题.
		}
		prt("searchKeywords2:"+sV.searchKeywords);
		//prt("searchkeywords2-urlencoded:"+java.net.URLEncoder.encode(searchKeywords));
		//prt("searchkeywords2-urldecoded:"+java.net.URLDecoder.decode(searchKeywords));
		sV.searchMode=request.getParameter("searchMode");
		prt("WeixinSearchResponse - searchMode:"+sV.searchMode);
		if (sV.searchMode==null) {sV.searchMode="newest";}
		
		sV.pageNo=request.getParameter("page");//获得搜索结果的页号.
		if (sV.pageNo==null){sV.pageNo="1";} //默认的页号为1.
		
		//printRequestInfo(request);//---------------------临时测试:获取所有请求参数.		
		//prt("searchKeywords:"+searchKeywords);
		//String sessionId=request.getSession().getId();//独得会话期的ID.暂时没用上.
		//System.out.println("accept Request() - sessionId:   "+sessionId);
		
		if (sV.searchMode.equals("newest")){//两种搜索模式的分支点.
			call_SearchplusPageData_Newest(request, response,sV); 
		}else{
			call_SearchplusPageData_Relevant(request, response,sV);
		}
		
		int usedTime=(int)(System.currentTimeMillis()-sV.beginTime);
		prt("WeixinSearchResponse()- 搜索用时:"+usedTime+" | 发生时间:"+MyTimeClass.getCurrentTime());
		Write2XuntaVisitLog.addLog(sV.IP,sV.searchKeywords,sV.searchMode,"WeixinSearchResponse()",usedTime);
	}
	
	void call_SearchplusPageData_Newest(HttpServletRequest request, HttpServletResponse response, PeopleSearchVars sV){
		prt("搜索最新......"); 
		try {SearchPeople_Newest.searchPeople(sV);//获取请求参数中的搜索关键词字串,并执行一次搜索,返回AI数据.
		} catch (Exception e) {	e.printStackTrace();} 

		//int currentPageNo=1;
		//PageData.prt("调用pagedata初始化之前");
		try {
			sV.pageData_newest	= new PageData_Newest(sV);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//;currentPageNo取得某页的页面数据.
		
		sV.pageData_newest.setSearchKeywords(sV.searchKeywords);//保留当前的搜索关键词,在结果中仍然出现.
		//sV.pageData_newest.setSearchKeywords_urlencoded(java.net.URLEncoder.encode(sV.searchKeywords));//保留当前的搜索关键词,在结果中仍然出现.
		
		//GroupingTest_Collector_Xu.end = System.currentTimeMillis();//TODO 要删掉.这个时间很短.
		//System.out.println("AcceptPeopleSearchRequest-PageData初始化之后:" + (GroupingTest_Collector_Xu.end - GroupingTest_Collector_Xu.begin) + "ms");
		//PageData.prt("pagedata初始化完成,转回accept...request.");
		//happyBirthday2Xiangyi(response);

		request.setAttribute("pageData", sV.pageData_newest);
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath+"WeixinSearchResult_newest.jsp",request, response);

	}
	
	void call_SearchplusPageData_Relevant(HttpServletRequest request, HttpServletResponse response, PeopleSearchVars sV){
		prt("搜索最相关......"); 
		try {SearchPeople_Relevant.searchPeople(sV);//获取请求参数中的搜索关键词字串,并执行一次搜索,返回AI数据.
		} catch (Exception e) {	e.printStackTrace();} 
		
		//int currentPageNo=1;
		//PageData.prt("调用pagedata初始化之前");
		try {
			sV.pageData_relavent= new PageData_Relevant(sV);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//;currentPageNo取得某页的页面数据.
		
		sV.pageData_relavent.setSearchKeywords(sV.searchKeywords);//保留当前的搜索关键词,在结果中仍然出现.
		//pageData_relavent(java.net.URLEncoder.encode(searchKeywords));//保留当前的搜索关键词,在结果中仍然出现.
			
		//GroupingTest_Collector_Xu.end = System.currentTimeMillis();//TODO 要删掉.这个时间很短.
		//System.out.println("AcceptPeopleSearchRequest-PageData初始化之后:" + (GroupingTest_Collector_Xu.end - GroupingTest_Collector_Xu.begin) + "ms");
		
		//PageData.prt("pagedata初始化完成,转回accept...request.");

		request.setAttribute("pageData", sV.pageData_relavent);
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath + "WeixinSearchResult_relevant.jsp",request, response);
	}
	//一段标准转发至jsp页面的代码,含异常处理:
	void dispatch(String jspPage,HttpServletRequest request, HttpServletResponse response){
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
		//requestDispatcher.forward(request, response);
	}
	
	public void init() throws ServletException {
		// Put your code here
	}
	public static void prt(Object o){System.out.println(o);}
	public WeixinSearchResponse() {
		super();
	}
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		doPost(request, response);
	}
}