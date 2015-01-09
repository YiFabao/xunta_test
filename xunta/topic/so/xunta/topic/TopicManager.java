package so.xunta.topic;

import java.util.List;

public interface TopicManager {
	//创建话题索引 
	public void createTopicIndex(String topicId,String topicContent,String authorId,String topicAuthorName,String topicCreatetime);
	//获取匹配的话题
	public List<Topic> matchMyTopic(String mytopic);
	public List<Topic> matchMyTopicByUserId(String mytopic);
	//将话题保存到数据库中
	public void saveTopic(Topic topic);
	//保存话题成员到数据库
	public void saveTopicMember(TopicMember topicMember);
}
