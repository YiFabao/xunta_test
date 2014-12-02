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

import so.xunta.author.all.DocData_All_GetMore;
import so.xunta.author.all.SearchAuthor_All;
import so.xunta.author.newest.DocData_Newest_GetMore;
import so.xunta.author.newest.SearchAuthor_Newest;
import so.xunta.author.relevant.DocData_Relevant_GetMore;
import so.xunta.author.relevant.SearchAuthor_Relevant;
import so.xunta.ipseeker.IPSeeker;
import so.xunta.localcontext.LocalContext;
import so.xunta.people.relevant.DocData_Relevant;
import so.xunta.service.WsManager;

import com.aigine.common.MyTimeClass;

//new出getMore_Newest.java或getMore_Relevant.java, 生成getMore所需要文档数据.

public class GetMoreAjaxResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	//本方法处理最初的页面请求,并获得请求参数:
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("----------------------------------------begin of ajax-getmoredocs"); 
		AuthorProfileVars apV=new AuthorProfileVars(); 
		apV.beginTime = System.currentTimeMillis();
		
		apV.moreDocsFetchNum=Integer.parseInt(request.getParameter("moreDocsFetchNum")); 
		//thisAuthorRank=Integer.parseInt(request.getParameter("thisAuthorRank"));//获知是哪位作者? 页面上的第几位.
		apV.docNumShown=Integer.parseInt(request.getParameter("docNumShown"));//已经显示的文档数.
		apV.author=request.getParameter("author");
		apV.site=request.getParameter("site");
		
		apV.timepoint=request.getParameter("timepoint");
		System.out.println("timepoint:"+apV.timepoint);
		
		System.out.println("网页传过来的 timepoint:"+apV.timepoint);

		apV.IP=request.getRemoteAddr();
		prt("IP:"+apV.IP); 
		
		
		apV.searchMode=request.getParameter("searchMode");
		apV.searchKeywords=request.getParameter("searchKeywords");
		apV.searcher = SearchMethods.getOneSearcherofTwo();//生成的结果直接为SearchMethods.searcher;
		//prt("GetMoreAjaxResponse()-接收到的一些参数:"+searchMode+"|"+queryString+"|已显:"+docNumShown+"|"+author+"|"+site);//TODO 要删除。
		if (apV.searchMode.equals("newest")){//两种搜索模式的分支点.
			getMore_ReqHandle_Newest(request, response,apV); 
		}else if(apV.searchMode.equals("relevant")){
			getMore_ReqHandle_Relevant(request, response,apV);
		}else{
			getMore_ReqHandle_All(request, response,apV);
		}
		
		int usedTime=(int)(System.currentTimeMillis()-apV.beginTime);
		prt("GetMoreAjaxResponse()- 搜索用时:"+usedTime+" | 发生时间:"+MyTimeClass.getCurrentTime());
		
	
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
			.append("time", time);
			
			WsManager.searchList.add(json);//将用户的搜索添加到自己定义的搜索词当中，以便后期随机显示
			
			WsManager.getInstance().broadcast(json);//给所有在线的用户广播
			
		} catch (Exception e) {
			System.out.println(e);
		}
		Write2XuntaVisitLog.addLog(apV.IP,apV.searchKeywords,apV.searchMode,"GetMoreAjaxResponse()",usedTime);
	}

	//本方法完成请求处理的全过程,是总调度-"全部"请求处理:
	void getMore_ReqHandle_All(HttpServletRequest request, HttpServletResponse response,AuthorProfileVars apV){
		prt("搜索最新 条件下的 [更多]处理-getMore_ReqHandle_All"); 

		try {SearchAuthor_All.searchAuthor(apV);//执行一次搜索,以静态变量的形式存放搜索结果集score_DocID_Map.
		} catch (Exception e) {	e.printStackTrace();} 

		try {
			apV.docdata_all_getmore = new DocData_All_GetMore(apV);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//;currentPageNo取得某页的页面数据.

		request.setAttribute("getmore_DocsData", apV.docdata_all_getmore.docData_List);
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath+"getMore_Newest.jsp",request, response);
	}

	
	
	//本方法完成请求处理的全过程,是总调度-"最新"请求处理:
	void getMore_ReqHandle_Newest(HttpServletRequest request, HttpServletResponse response,AuthorProfileVars apV){
		prt("搜索最新 条件下的 [更多]处理-getMore_ReqHandle_Newest"); 
		try {SearchAuthor_Newest.searchAuthor(apV);//执行一次搜索,以静态变量的形式存放搜索结果集score_DocID_Map.
		} catch (Exception e) {	e.printStackTrace();} 

		try {
			apV.docdata_newest_getmore	= new DocData_Newest_GetMore(apV);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//;currentPageNo取得某页的页面数据.

		request.setAttribute("getmore_DocsData", apV.docdata_newest_getmore.docData_List);
		//转交给pSearchResultPage
		//PageData.prt("正在执行转交动作...");
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath+"getMore_Newest.jsp",request, response);

	}
	//本方法完成请求处理的全过程,是总调度-"最相关"请求处理:
	void getMore_ReqHandle_Relevant(HttpServletRequest request, HttpServletResponse response,AuthorProfileVars apV){
		//prt("最相关 [更多]处理  GetMoreAjaxResponse.java中的getMore_ReqHandle_Relevant()"); 
		//prt("在GetMoreAjaxResponse中再打一下搜索结果集:"+"collector中的结果集长度: "+Collector_Author_Relevant.docdata_set.size());

		try {SearchAuthor_Relevant.searchAuthor(apV);//执行一次搜索,以静态变量的形式存放搜索结果集score_DocID_Map.
		} catch (Exception e) {	e.printStackTrace();} 
		
		try {
			apV.docdata_relevant_getmore= new DocData_Relevant_GetMore(apV);//从搜索结果中抽取该次请求真正需要的数据,存放于一个静态变量docData_List中,供jsp页面直接调用.
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		prt("在GetMoreAjaxResponse中再打一下搜索结果集:生成docdata之后: "+"collector中的结果集长度: "+Collector_Author_Relevant.docdata_set.size());
/*		prt("将打印更多文档的内容:getMore_docsdata_relevant.docData_List");
		for (DocData_Relevant doc:apV.docdata_relevant_getmore.docData_List){
			prt(doc.getTitle()+"|"+doc.getContent()); 
	}*/
		request.setAttribute("getmore_DocsData", apV.docdata_relevant_getmore.docData_List);//把搜索结果中所需要的更多文档数据返回到前台.
		response.setContentType("text/html;charset=UTF-8");
		dispatch(LocalContext.jspFilePath+"getMore_Relevant.jsp",request, response);
			
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
	
	public GetMoreAjaxResponse() {
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