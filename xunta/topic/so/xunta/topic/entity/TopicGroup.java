package so.xunta.topic.entity;

public class TopicGroup {
	public int id;
	public String topicId;//话题id
	public String topicMemberId;//userId
	public String topicMemberName;//userName
	public String joinDatetime;//用户参与该组的时间
	public TopicGroup() {
		super();
	}
	public TopicGroup(String topicId,String topicMemberId,String topicMemberName,String join_datetime) {
		this.topicId = topicId;
		this.topicMemberId = topicMemberId;
		this.topicMemberName = topicMemberName;
		this.joinDatetime = join_datetime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getTopicMemberId() {
		return topicMemberId;
	}
	public void setTopicMemberId(String topicMemberId) {
		this.topicMemberId = topicMemberId;
	}
	public String getTopicMemberName() {
		return topicMemberName;
	}
	public void setTopicMemberName(String topicMemberName) {
		this.topicMemberName = topicMemberName;
	}
	public String getJoinDatetime() {
		return joinDatetime;
	}
	public void setJoinDatetime(String joinDatetime) {
		this.joinDatetime = joinDatetime;
	}

}
