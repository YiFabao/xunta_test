package so.xunta.topic;

public class AddTopicIndexThread implements Runnable{

	private TopicManager topicmanager;
	private String topicId,topicContent,authorId,authorName,datetime;
	
	public AddTopicIndexThread(TopicManager topicmanager,String topicId,String topicContent,String authorId,String authorName,String datetime) {
		this.topicmanager=topicmanager;
		this.topicId=topicId;
		this.authorId=authorId;
		this.topicContent=topicContent;
		this.authorName=authorName;
		this.datetime=datetime;
	}

	public TopicManager getTopicmanager() {
		return topicmanager;
	}

	public void setTopicmanager(TopicManager topicmanager) {
		this.topicmanager = topicmanager;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getTopicContent() {
		return topicContent;
	}

	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	@Override
	public void run() {
		topicmanager.createTopicIndex(topicId, topicContent, authorId, authorName, datetime);
	}
	

}
