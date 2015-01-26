package so.xunta.topic.model.impl;

import so.xunta.topic.entity.Topic;
import so.xunta.topic.entity.TopicGroup;
import so.xunta.topic.entity.TopicHistory;
import so.xunta.topic.model.TopicManager;

public class SaveTopicThread implements Runnable {
	private Topic topic;
	private TopicManager topicManager;
	public SaveTopicThread(TopicManager topicManager ,Topic topic) {
		this.topic=topic;
		this.topicManager=topicManager;
	}

	@Override
	public void run() {
		
		TopicGroup topicGroup = new TopicGroup(topic.topicId,topic.userId,topic.userName,topic.createTime);
		//保存话题
		topicManager.saveTopic(topic);
		//将用户保存到话题组
		topicManager.saveTopicGroup(topicGroup);
		//保存话题历史
		TopicHistory topicHistory = new TopicHistory(topic.userId,topic.topicId,topic.createTime,'p');
		topicManager.addTopicHistory(topicHistory);
	}

}
