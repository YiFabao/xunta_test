package so.xunta.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		request.setCharacterEncoding("utf-8");
		String mytopic = request.getParameter("mytopic");
		
		System.out.println("我的话题："+mytopic);
		
		List<so.xunta.topic.Topic> matchedtopic=topicManager.matchMyTopic(mytopic);
		
		/*List<String> matchedtopic=new ArrayList<>();
		matchedtopic.add("#你在做什么#");
		matchedtopic.add("#黄山哪里好玩#");
		matchedtopic.add("#周末结伴#");
		matchedtopic.add("#过年回家#");*/
		
		request.setAttribute("mytopic",mytopic);
		
		request.setAttribute("matchedtopic",matchedtopic);
		
		request.getRequestDispatcher("/jsp/topic/topicmatch.jsp").forward(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
