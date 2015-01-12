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
		
		TopicMember tm=new TopicMember(topic.topicId,topic.authorId,topic.authorName,topic.topicCreatetime,0,"");
		
		//保存话题
		topicManager.saveTopic(topic);
		//保存话题成员
		topicManager.saveTopicMember(tm);
		//保存话题历史
		TopicHistory topicHistory = new TopicHistory(topic.authorId,topic.topicId,topic.topicCreatetime);
		topicManager.addTopicHistory(topicHistory);
		
	}

}
