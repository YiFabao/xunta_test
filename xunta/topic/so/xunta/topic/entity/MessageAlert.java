package so.xunta.topic.entity;

import java.text.ParseException;
import java.util.Date;

import so.xunta.utils.DateTimeUtils;

public class MessageAlert implements Comparable<MessageAlert>{
	public int id;
	public String authorId;//作者id,谁的消息提醒
	public String _fromUsername;//来自谁昵称
	public String _fromUserId;//id
	public String topicId;//邀请要进入的话题id
	public String topicContent;//topicId对应的话题内容
	public int isHandle = 0;//0 未处理  1已处理
	public int isRead = 0;//默认未读
	public String datetime;
	
	public MessageAlert() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MessageAlert(String authorId,String _fromUsername,String _fromUserId,String topicId,String topicContent,String datetime) {
		this.authorId=authorId;
		this._fromUsername=_fromUsername;
		this._fromUserId =_fromUserId;
		this.topicId=topicId;
		this.datetime=datetime;
		this.topicContent=topicContent;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String get_fromUsername() {
		return _fromUsername;
	}
	public void set_fromUsername(String _fromUsername) {
		this._fromUsername = _fromUsername;
	}
	public String get_fromUserId() {
		return _fromUserId;
	}
	public void set_fromUserId(String _fromUserId) {
		this._fromUserId = _fromUserId;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public int getIsHandle() {
		return isHandle;
	}
	public void setIsHandle(int isHandle) {
		this.isHandle = isHandle;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getTopicContent() {
		return topicContent;
	}
	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}
	@Override
	public int compareTo(MessageAlert o) {
		try {
			Date d1 = DateTimeUtils.getCurrentDateTimeObj(this.datetime);
			Date d2=DateTimeUtils.getCurrentDateTimeObj(o.datetime);
			if(d1.getTime()>d2.getTime())
			{
				return -1;
			}
			else if(d1.getTime()<d2.getTime())
			{
				return 1;
			}else
			{
				return 0;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}
