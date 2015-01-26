package so.xunta.topic.model.impl;

import so.xunta.topic.entity.Topic;
import so.xunta.topic.model.TopicManager;

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
