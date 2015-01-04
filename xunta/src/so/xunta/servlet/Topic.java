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
import so.xunta.topic.TopicManager;
import so.xunta.topic.TopicManagerImpl;

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
		String mytopic = request.getParameter("mytopic");//会乱码要转码
		mytopic=new String(mytopic.getBytes("ISO-8859-1"),"utf-8");
		System.out.println("我的话题："+mytopic);
		
		if(cmd==null||"".equals(cmd))
		{
			System.out.println("cmd为空");
			return;
		}
		//发起话题　匹配
		if("fqht".equals(cmd))
		{
			List<so.xunta.topic.Topic> matchedtopic=topicManager.matchMyTopic(mytopic);
			//组合成一个json格式的对象
			JSONArray jarray=new JSONArray();
			
			for(int i=0;i<matchedtopic.size();i++)
			{
				String name=matchedtopic.get(i).getAuthorName();
				String content=matchedtopic.get(i).getTopicContent();
				String datetime=matchedtopic.get(i).getDatetime();
				JSONObject jsonObj=new JSONObject();
				jsonObj.put("name",name);
				jsonObj.put("topicContent",content);
				jsonObj.put("datetime",datetime);
				jarray.add(jsonObj);
				if(i>100)
					break;
			}
			System.out.println("发起话题");
			//设置允许谁访问
			response.addHeader("Access-Control-Allow-Origin","http://localhost:63342");
			response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
		}
		//搜索话题
		else if("ssht".equals(cmd))
		{
			List<so.xunta.topic.Topic> searchedTopic=topicManager.matchMyTopic(mytopic);
			//组合成一个json格式的对象
			JSONArray jarray=new JSONArray();
			
			for(int i=0;i<searchedTopic.size();i++)
			{
				String name=searchedTopic.get(i).getAuthorName();
				String content=searchedTopic.get(i).getTopicContent();
				JSONObject jsonObj=new JSONObject();
				jsonObj.put("name",name);
				jsonObj.put("topicContent",content);
				jarray.add(jsonObj);
				if(i>100)
					break;
			}
			
			response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
		}
		//话题推荐
		else if("httj".equals(cmd))
		{
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
			List<so.xunta.topic.Topic> historyTopic=topicManager.matchMyTopic(mytopic);
			//组合成一个json格式的对象
			JSONArray jarray=new JSONArray();
			
			for(int i=0;i<historyTopic.size();i++)
			{
				String name=historyTopic.get(i).getAuthorName();
				String content=historyTopic.get(i).getTopicContent();
				JSONObject jsonObj=new JSONObject();
				jsonObj.put("name",name);
				jsonObj.put("topicContent",content);
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
