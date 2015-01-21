package so.xunta.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import so.xunta.topic.Topic;

/**
 * Servlet implementation class TopicMatchServlet
 */
@WebServlet("/TopicMatchServlet")
public class TopicMatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TopicMatchServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Topic topic = (Topic) request.getAttribute("myTopic");
		//匹配用户话题
		request.getRequestDispatcher("/jsp/topic/fqht.jsp").forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
