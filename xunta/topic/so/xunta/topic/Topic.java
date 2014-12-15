package so.xunta.topic;
/**
 * 话题
 * @author YiFabao
 */
public class Topic {
	/**作者唯一id*/
	public String authorId;
	/**作者名*/
	public String authorName;
	/**匹配的话题*/
	public String topicContent;
	/**时间*/
	public String datetime;
	

	public Topic(String hightLightTopic, String authorname2, String datetime2) {
		this.authorName=authorname2;
		this.topicContent=hightLightTopic;
		this.datetime=datetime2;
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
	public String getTopicContent() {
		return topicContent;
	}
	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
}
