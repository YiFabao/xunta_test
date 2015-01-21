package so.xunta.topic;

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
		TopicHistory topicHistory = new TopicHistory(topic.userId,topic.topicId,topic.createTime,"publish");
		topicManager.addTopicHistory(topicHistory);
	}

}
