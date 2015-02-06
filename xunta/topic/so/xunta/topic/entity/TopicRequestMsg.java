package so.xunta.topic.entity;

public class TopicRequestMsg {
	public int id;
	public String fromUserId;
	public String toUserId;
	public String fromUserName;
	public String toUserName;
	
	public String topicId;//话题id
	
	public String dateTime;//时间
	
	public String isAgree;//是否同意，0为不同意,1为同意,-1为不置可否
	public String isHandle;//0为未读，1为已读
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getIsHandle() {
		return isHandle;
	}
	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}
	public String getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}
	public TopicRequestMsg(String fromUserId, String toUserId, String fromUserName, String toUserName, String topicId, String dateTime, String isAgree,String isHandle) {
		super();
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.topicId = topicId;
		this.dateTime = dateTime;
		this.isAgree = isAgree;
		this.isHandle=isHandle;
	}
	public TopicRequestMsg() {
		super();
	}
	
	
}
