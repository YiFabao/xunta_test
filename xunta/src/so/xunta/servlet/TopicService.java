package so.xunta.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import so.xunta.topic.AddTopicIndexThread;
import so.xunta.topic.MatchedTopic;
import so.xunta.topic.SaveTopicThread;
import so.xunta.topic.SecurityUtil;
import so.xunta.topic.Topic;
import so.xunta.topic.TopicManager;
import so.xunta.topic.TopicManagerImpl;
import so.xunta.utils.DateTimeUtils;

/**
 * Servlet implementation class TopicService
 */
@WebServlet("/TopicService")
public class TopicService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TopicManager topicManager = new TopicManagerImpl();
    public TopicService() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String cmd = request.getParameter("cmd");
		if(cmd==null)return;
		switch(cmd){
		case "fqht":
			//发起话题
			fqht(request,response);
			break;
		case "htss":
			htss(request,response);
			break;
		}
	}

	private void htss(HttpServletRequest request, HttpServletResponse response) {
			String searchWord = request.getParameter("search_word");
			try {
				searchWord=new String(searchWord.getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			System.out.println("话题搜索");
			System.out.println(searchWord);
			//搜索话题
			List<so.xunta.topic.Topic> matchedtopicList=topicManager.matchMyTopic(searchWord);
			//按userId分组
			Map<String,List<Topic>> topicMap = new HashMap<String,List<Topic>>();
			for(Topic t:matchedtopicList)
			{
				if(topicMap.containsKey(t.userId))
				{
					topicMap.get(t.userId).add(t);
					topicMap.put(t.userId,topicMap.get(t.userId));
				}
				else
				{
					List<Topic> list=new ArrayList<Topic>();
					list.add(t);
					topicMap.put(t.userId,list);
				}
			}
			System.out.println("＝＝＝＝>"+matchedtopicList.size());
			System.out.println("topicMap.size:"+topicMap.size());
			List<MatchedTopic> mtlist = new ArrayList<MatchedTopic>();
			Iterator<Entry<String, List<Topic>>> iterator =topicMap.entrySet().iterator();
			while(iterator.hasNext())
			{
				Entry<String,List<Topic>> e = iterator.next();
				MatchedTopic mt = new MatchedTopic();
				mt.setUserId(e.getKey());
				mt.setRelativeTopicList(e.getValue());
				mt.setUserName(e.getValue().get(0).getUserName());
				mtlist.add(mt);
			}
			System.out.println("mtlist====>"+mtlist.size());
			request.setAttribute("searchWord",searchWord);
			request.setAttribute("topicList",mtlist);
	
			try {
				request.getRequestDispatcher("/jsp/topic/htss.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	private void fqht(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");//用户id
		String userName = request.getParameter("userName");//用户名
		String userLogoUrl = request.getParameter("userLogoUrl");//用户logo
		String topicName = request.getParameter("topicName");//话题标题
		String topicContent = request.getParameter("topicContent");//话题内容
		System.out.println("用户id:"+userId);
		System.out.println("用户名:"+userName);
		System.out.println("用户LogoUrl:"+userLogoUrl);
		System.out.println("话题名称:"+topicName);
		System.out.println("话题内容:"+topicContent);
		
		//话题发起时间 
		String topicCreateTime = DateTimeUtils.getCurrentTimeStr();
		//话题Id 由    [用户id+话题名称+话题内容+话题创建时间]    的字符串拼接字符串生成的md5
		String topicId=SecurityUtil.strToMD5(userId+topicName+topicContent+topicCreateTime);
		//保存用户话题
		Topic topic =new Topic(topicId,userId,userName,topicName,topicContent,userLogoUrl,topicCreateTime,topicCreateTime);
		AddTopicIndexThread addTopicIndexThread=new AddTopicIndexThread(topicManager, topic);
		new Thread(addTopicIndexThread).start();
		SaveTopicThread saveTopicThread = new SaveTopicThread(topicManager, topic);
		new Thread(saveTopicThread).start();//保存话题，话题组，话题历史
		request.setAttribute("myTopic",topic);
		//匹配话题
		List<so.xunta.topic.Topic> matchedtopicList=topicManager.matchMyTopic(topic.topicContent);
		//按userId分组
		Map<String,List<Topic>> topicMap = new HashMap<String,List<Topic>>();
		for(Topic t:matchedtopicList)
		{
			if(topicMap.containsKey(t.userId))
			{
				topicMap.get(t.userId).add(t);
				topicMap.put(t.userId,topicMap.get(t.userId));
			}
			else
			{
				List<Topic> list=new ArrayList<Topic>();
				list.add(t);
				topicMap.put(t.userId,list);
			}
		}
		System.out.println("＝＝＝＝>"+matchedtopicList.size());
		System.out.println("topicMap.size:"+topicMap.size());
		List<MatchedTopic> mtlist = new ArrayList<MatchedTopic>();
		Iterator<Entry<String, List<Topic>>> iterator =topicMap.entrySet().iterator();
		while(iterator.hasNext())
		{
			Entry<String,List<Topic>> e = iterator.next();
			MatchedTopic mt = new MatchedTopic();
			mt.setUserId(e.getKey());
			mt.setRelativeTopicList(e.getValue());
			mt.setUserName(e.getValue().get(0).getUserName());
			mtlist.add(mt);
		}
		System.out.println("mtlist====>"+mtlist.size());
		request.setAttribute("matchedTopicList",mtlist);
		try {
			request.getRequestDispatcher("/jsp/topic/fqht.jsp").forward(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
