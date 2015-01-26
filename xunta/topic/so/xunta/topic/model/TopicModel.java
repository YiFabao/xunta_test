package so.xunta.topic.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TopicModel {
	//用户参与话题
	public void joinTopic(HttpServletRequest request, HttpServletResponse response,String userId,String topicId);
}
