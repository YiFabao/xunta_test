package so.xunta.topic.entity;

import java.util.List;

public class MatchedTopic {
	public String userId;
	public String userName;
	public List<Topic> relativeTopicList;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<Topic> getRelativeTopicList() {
		return relativeTopicList;
	}
	public void setRelativeTopicList(List<Topic> relativeTopicList) {
		this.relativeTopicList = relativeTopicList;
	}
}
