package so.xunta.topic;

public class AddTopicIndexThread implements Runnable{

	private TopicManager topicmanager;
	private Topic topic;
	
	public AddTopicIndexThread(TopicManager topicmanager,Topic topic) {
		
		this.topicmanager=topicmanager;
		this.topic=topic;
		
	}
	
	@Override
	public void run() {
		topicmanager.createTopicIndex(topic);
	}
	

}
