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
		TopicMember tm=new TopicMember(topic.topicId,topic.authorId,topic.topicCreatetime,0,"");
		topicManager.saveTopic(topic);
		topicManager.saveTopicMember(tm);
	}

}
