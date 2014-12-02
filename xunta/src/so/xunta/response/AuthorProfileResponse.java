package so.xunta.response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.aigine.common.MyTimeClass;

import so.xunta.author.all.DocData_All_GetMore;
import so.xunta.author.all.SearchAuthor_All;
import so.xunta.author.newest.DocData_Newest_GetMore;
import so.xunta.author.newest.SearchAuthor_Newest;
import so.xunta.author.relevant.DocData_Relevant_GetMore;
import so.xunta.author.relevant.SearchAuthor_Relevant;
import so.xunta.author_profile.model.Author_Profile_PageData;
import so.xunta.ipseeker.IPSeeker;
import so.xunta.localcontext.*;
import so.xunta.service.WsManager;

//响应author_profile的请求.
public class AuthorProfileResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/*
	String searchKeywords;
	String searchMode;
	String author;
	String site;
	Author_Profile_PageData pageData;
	int totalDocs;//2014.6.14
	
	String pSearchResultPage;
	String searchResultFileIncluded;
	
	DocData_Relevant_GetMore getMore_docsdata_relevant;//获取 更多文档  数据的java处理代码的对象-最相关.
	DocData_Newest_GetMore getMore_docsdata_newest;////获取 更多文档  数据的java处理代码的对象-最新.
	DocData_All_GetMore getMore_docsdata_all;////获取 更多文档  数据的java处理代码的对象-最新.
	
	String getMoreResultPage;//dispatch到一个jsp文件的该文件名.
	String queryString;//从页面传来过的用户搜索字串.在searchPeople中,这个变量用的是searchKeywords,不够恰当.
	int thisAuthorRank;//"更多"点击操作时,所在楼层的作者在当面页上的排序序号.//由于作全新搜索,这个参数不需要了,暂留.
	int docNumShown;//该作者已显示在页面上的楼层数.新索取的文档应该从这个序号加1后开始算.
	int moreDocsFetchNum=10;
	//int moreDocFetchNum=2;//每次增加的文档数量.//这个变量移动docs_data_newest里了.
	Author_Profile_PageData author_profile_PageData=null;
	*/
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		System.out.println("----------------------------------------begin of Author_Profile_Response:"); 
		AuthorProfileVars apV = new AuthorProfileVars(); 
		apV.beginTime = System.currentTimeMillis();
		apV.IP=request.getRemoteAddr();
		prt("IP:"+apV.IP); 
		/* 
		String moreDocsFetchNum=request.getParameter("moreDocsFetchNum");
		prt("moreDocsFetchNum:"+moreDocsFetchNum); 
		if(moreDocsFetchNum!=null){
			apV.moreDocsFetchNum=Integer.parseInt(moreDocsFetchNum); 
		}*/
		
		apV.moreDocsFetchNum = 100;//首次列出的文档数量. 应该与author_profile_search.jsp里newest_relevant_Post()中的同名变量用同样的值.

		apV.searchKeywords=request.getParameter("searchKeywords");//获得搜索关键词.apV.searchMode=request.getParameter("searchMode");
		if (apV.searchKeywords==null) {apV.searchKeywords="结伴";}//如果没有任何搜索词,则赋一个临时的.//搜索框中现在会有默认搜索词,所以这句一般没用了.
		else {
			apV.searchKeywords=new String(apV.searchKeywords.getBytes("ISO-8859-1"),"utf-8");//这句是为了解决网页提交中文的乱码问题.
		}
		
		apV.searchMode=request.getParameter("searchMode"); 
		if (apV.searchMode==null) {apV.searchMode="newest";}

		apV.author=request.getParameter("author"); 
		apV.author=new String(apV.author.getBytes("ISO-8859-1"),"utf-8");//这句是为了解决网页提交中文的乱码问题.
		apV.site=request.getParameter("site"); 
		apV.site=new String(apV.site.getBytes("ISO-8859-1"),"utf-8");//这句是为了解决网页提交中文的乱码问题.

		apV.searcher = SearchMethods.getOneSearcherofTwo();//生成的结果直接为SearchMethods.searcher;
		
		call_Author_Profile_PageData(request, response,apV); 

		
		int usedTime=(int)(System.currentTimeMillis()-apV.beginTime);
		prt("Author_Profile_Response()- 搜索用时:"+usedTime+" | 发生时间:"+MyTimeClass.getCurrentTime());
		
		//WEBSOCKET广播该用户的搜索请求
		//构造JSON
		try {
		
			
			String ip=new String(WsManager.ipseeker.getCountry(apV.IP).getBytes(),"utf-8");
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time=sdf.format(date);
			
			JSONObject json=new JSONObject();
			json.append("ipaddress",ip)
			.append("searchKeywords", apV.searchKeywords)
			.append("time",time);
			
			WsManager.searchList.add(json);//将用户的搜索添加到自己定义的搜索词当中，以便后期随机显示
			
			WsManager.getInstance().broadcast(json);//给所有在线的用户广播
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		Write2XuntaVisitLog.addLog(apV.IP,apV.searchKeywords,apV.searchMode,"AuthorProfileResponse()",usedTime);
	}
	
	//先准备页面的基本信息:	
	void call_Author_Profile_PageData(HttpServletRequest request, HttpServletResponse response,AuthorProfileVars apV){
		
		try {
			apV.author_profile_PageData = new Author_Profile_PageData(apV.searchMode,apV.searchKeywords,apV.author,apV.site);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//;currentPageNo取得某页的页面数据.

		if (apV.searchMode.equals("newest")){//两种搜索模式的分支点.
			authorProfile_ReqHandle_Newest(request, response,apV); 
		}else if(apV.searchMode.equals("relevant")){
			authorProfile_ReqHandle_Relevant(request, response,apV);
		}else{
			authorProfile_ReqHandle_All(request, response,apV);
		}
	}

	//本方法完成请求处理的全过程,是总调度-"全部"请求处理:(只是不需要content关键词,其它全部按最新来排序)
	void authorProfile_ReqHandle_All(HttpServletRequest request, HttpServletResponse response,AuthorProfileVars apV){
		prt("authorProfile_ReqHandle_All()-开始..."); 
		//queryString="";//由于搜索全部文档,所以不需要关键词.2014.6.16
		try {
			SearchAuthor_All.searchAuthor(apV);//执行一次搜索,以静态变量的形式存放搜索结果集score_DocID_Map.
			//totalDocs=SearchAuthor_Newest.totalDocs;//2014.6.14 //这个文档数是总数,不是某个作者的.
			//author_profile_PageData.totalDocs=totalDocs;//2014.6.14
		} catch (Exception e) {	e.printStackTrace();} 
		//apV.docNumShown=0;//这个变量来自getMore代码,暂留.
		try {
			apV.docdata_all_getmore = new DocData_All_GetMore(apV);
			//apV.totalDocs=apV.getMore_docsdata_all.getTotalDocs();//2014.6.14
			apV.author_profile_PageData.totalDocs=apV.totalDocs;//2014.6.14
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		request.setAttribute("pageData", apV.author_profile_PageData);
		request.setAttribute("getmore_DocsData", apV.docdata_all_getmore.docData_List);//这个属性值用于getMore_Newest.jsp文件.这个文件是被included在author_profile.jsp中的.
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath+"author_profile_all.jsp",request, response);
	}
	
	//本方法完成请求处理的全过程,是总调度-"最新"请求处理:
	void authorProfile_ReqHandle_Newest(HttpServletRequest request, HttpServletResponse response,AuthorProfileVars apV){
		prt("authorProfile_ReqHandle_Newest()-开始..."); 

		try {
			SearchAuthor_Newest.searchAuthorAll(apV);//执行一次搜索,以静态变量的形式存放搜索结果集score_DocID_Map.
			//totalDocs=SearchAuthor_Newest.totalDocs;//2014.6.14 //这个文档数是总数,不是某个作者的.
			//author_profile_PageData.totalDocs=totalDocs;//2014.6.14
		} catch (Exception e) {	e.printStackTrace();} 
		//apV.docNumShown=0;//这个变量来自getMore代码,暂留.
		try {
			apV.docdata_newest_getmore	= new DocData_Newest_GetMore(apV);
			//totalDocs=getMore_docsdata_newest.totalDocs;//2014.6.14
			apV.author_profile_PageData.totalDocs=apV.totalDocs;//2014.6.14
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//;currentPageNo取得某页的页面数据.
		
		request.setAttribute("pageData", apV.author_profile_PageData);
		request.setAttribute("getmore_DocsData", apV.docdata_newest_getmore.docData_List);//这个属性值用于getMore_Newest.jsp文件.这个文件是被included在author_profile.jsp中的.
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath+"author_profile_search.jsp",request, response);
	}
	
	//本方法完成请求处理的全过程,是总调度-"最相关"请求处理:
	void authorProfile_ReqHandle_Relevant(HttpServletRequest request, HttpServletResponse response,AuthorProfileVars apV){
		prt("authorProfile_ReqHandle_Relevant()-开始..."); 
		//prt("在GetMoreAjaxResponse中再打一下搜索结果集:"+"collector中的结果集长度: "+Collector_Author_Relevant.docdata_set.size());

		try {SearchAuthor_Relevant.searchAuthorAll(apV);//执行一次搜索,以静态变量的形式存放搜索结果集score_DocID_Map.
		} catch (Exception e) {	e.printStackTrace();} 
		//apV.docNumShown=0;//2014.6.14 这个变量来自getMore代码,暂留.
		try {
			apV.docdata_relevant_getmore= new DocData_Relevant_GetMore(apV);//从搜索结果中抽取该次请求真正需要的数据,存放于一个静态变量docData_List中,供jsp页面直接调用.
			//apV.totalDocs=getMore_docsdata_relevant.totalDocs;//2014.6.14
			apV.author_profile_PageData.totalDocs=apV.totalDocs;//2014.6.14
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.setAttribute("pageData", apV.author_profile_PageData);
		request.setAttribute("getmore_DocsData", apV.docdata_relevant_getmore.docData_List);//把搜索结果中所需要的更多文档数据返回到前台.
		//PageData.prt("正在执行转交动作...");
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath+"author_profile_search.jsp",request, response);
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
	
	public static void prt(Object o){System.out.println(o);}
	public AuthorProfileResponse() {
		super();
	}
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doPost(request, response);
	}


}