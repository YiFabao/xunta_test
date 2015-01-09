package so.xunta.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import so.xunta.topic.AddTopicIndexThread;
import so.xunta.topic.SaveTopicThread;
import so.xunta.topic.SecurityUtil;
import so.xunta.topic.TopicManager;
import so.xunta.topic.TopicManagerImpl;
import so.xunta.utils.DateTimeUtils;

/**
 * Servlet implementation class Topic
 */
@WebServlet("/Topic")
public class Topic extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static TopicManager topicManager=new TopicManagerImpl(); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Topic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
		response.setContentType("text/json; charset=UTF-8");  
		String cmd=request.getParameter("cmd");
		
		if(cmd==null||"".equals(cmd))
		{
			System.out.println("cmd为空");
			return;
		}
		//发起话题　匹配
		if("fqht".equals(cmd))
		{
			//我的话题内容
			String mytopic = request.getParameter("mytopic");//会乱码要转码
			mytopic=new String(mytopic.getBytes("ISO-8859-1"),"utf-8");
			System.out.println("我的话题："+mytopic);
			//话题发起人昵称
			String topicAuthorName = request.getParameter("topicAuthorName");
			topicAuthorName=new String(topicAuthorName.getBytes("ISO-8859-1"),"utf-8");
			//话题发起人id
			String authorId = request.getParameter("authorId");
			//话题发起时间 
			String topicCreateTime = DateTimeUtils.getCurrentTimeStr();
			//话题Id 由    [话题人id+话题内容+话题创建时间]    的字符串拼接字符串生成的md5
			String topicId=SecurityUtil.strToMD5(authorId+mytopic+topicCreateTime);
			
			List<so.xunta.topic.Topic> matchedtopic=topicManager.matchMyTopic(mytopic);
			//组合成一个json格式的对象
			JSONArray jarray=new JSONArray();
			
			for(int i=0;i<matchedtopic.size();i++)
			{
				String name=matchedtopic.get(i).getAuthorName();//作者名
				String userId=matchedtopic.get(i).getAuthorId();//作者 id
				String content=matchedtopic.get(i).getTopicContent();//话题内容
				String topic_id=matchedtopic.get(i).getTopicId();//话题内容
				String datetime=matchedtopic.get(i).getTopicCreatetime();//话题发布时间
				JSONObject jsonObj=new JSONObject();
				jsonObj.put("name",name);
				jsonObj.put("userId", userId);
				jsonObj.put("topicContent",content);
				jsonObj.put("topicId",topic_id);
				jsonObj.put("datetime",datetime);
				jarray.add(jsonObj);
				if(i>100)
					break;
			}
			System.out.println("发起话题");
			//开启线程将索引保存到数据库中 和 保存到索引中
			new Thread(new AddTopicIndexThread(topicManager, topicId,mytopic, authorId, topicAuthorName, topicCreateTime)).start();
			so.xunta.topic.Topic topic=new so.xunta.topic.Topic(topicId, authorId, mytopic, topicAuthorName, topicCreateTime);
			new Thread(new SaveTopicThread(topicManager, topic)).start();
			//设置允许谁访问
			response.addHeader("Access-Control-Allow-Origin","http://localhost:63342");
			response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
		}
		//搜索话题
		else if("htss".equals(cmd))
		{
			//我的话题内容
			String mytopic = request.getParameter("mytopic");//会乱码要转码
			mytopic=new String(mytopic.getBytes("ISO-8859-1"),"utf-8");
			System.out.println("我的话题："+mytopic);
			List<so.xunta.topic.Topic> searchedTopic=topicManager.matchMyTopic(mytopic);
			//组合成一个json格式的对象
			JSONArray jarray=new JSONArray();
			
			for(int i=0;i<searchedTopic.size();i++)
			{
				String name=searchedTopic.get(i).getAuthorName();//作者名
				String userId=searchedTopic.get(i).getAuthorId();//作者 id
				String content=searchedTopic.get(i).getTopicContent();//话题内容
				String topic_id=searchedTopic.get(i).getTopicId();//话题内容
				String datetime=searchedTopic.get(i).getTopicCreatetime();//话题发布时间
				JSONObject jsonObj=new JSONObject();
				jsonObj.put("name",name);
				jsonObj.put("userId", userId);
				jsonObj.put("topicContent",content);
				jsonObj.put("topicId",topic_id);
				jsonObj.put("datetime",datetime);
				jarray.add(jsonObj);
				if(i>100)
					break;
			}
			
			response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
		}
		//话题推荐
		else if("httj".equals(cmd))
		{
			//我的话题内容
			String mytopic = request.getParameter("mytopic");//会乱码要转码
			mytopic=new String(mytopic.getBytes("ISO-8859-1"),"utf-8");
			System.out.println("我的话题："+mytopic);
			List<so.xunta.topic.Topic> recommendTopic = topicManager.matchMyTopic(mytopic);
			// 组合成一个json格式的对象
			JSONArray jarray = new JSONArray();

			for (int i = 0; i < recommendTopic.size(); i++) {
				String name = recommendTopic.get(i).getAuthorName();
				String content = recommendTopic.get(i).getTopicContent();
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("name", name);
				jsonObj.put("topicContent", content);
				jarray.add(jsonObj);
				if (i > 100)
					break;
			}
			System.out.println("话题推荐");
			response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
		}
		//话题记忆
		else if("htjy".equals(cmd))
		{
			//话题发起人id
			String authorId = request.getParameter("authorId");
			System.out.println("用户id:"+authorId);
			List<so.xunta.topic.Topic> historyTopic=topicManager.matchMyTopicByUserId(authorId);
			System.out.println("结果数："+historyTopic.size());
			//组合成一个json格式的对象
			JSONArray jarray=new JSONArray();
			
			for(int i=0;i<historyTopic.size();i++)
			{
				String name=historyTopic.get(i).getAuthorName();//作者名
				String userId=historyTopic.get(i).getAuthorId();//作者 id
				String content=historyTopic.get(i).getTopicContent();//话题内容
				String topic_id=historyTopic.get(i).getTopicId();//话题内容
				String datetime=historyTopic.get(i).getTopicCreatetime();//话题发布时间
				JSONObject jsonObj=new JSONObject();
				jsonObj.put("name",name);
				jsonObj.put("userId", userId);
				jsonObj.put("topicContent",content);
				jsonObj.put("topicId",topic_id);
				jsonObj.put("datetime",datetime);
				jarray.add(jsonObj);
				if(i>100)
					break;
			}
			System.out.println("话题记忆");
			response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
		}
		
	
		
		/*List<String> matchedtopic=new ArrayList<>();
		matchedtopic.add("#你在做什么#");
		matchedtopic.add("#黄山哪里好玩#");
		matchedtopic.add("#周末结伴#");
		matchedtopic.add("#过年回家#");*/
		
	/*	request.setAttribute("mytopic",mytopic);
		
		request.setAttribute("matchedtopic",matchedtopic);
		
		request.getRequestDispatcher("/jsp/topic/topicmatch.jsp").forward(request, response);*/
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
