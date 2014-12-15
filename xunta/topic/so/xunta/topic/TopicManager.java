package so.xunta.topic;

import java.util.List;

public interface TopicManager {
	//获取匹配的话题
	public List<Topic> matchMyTopic(String mytopic);
}
