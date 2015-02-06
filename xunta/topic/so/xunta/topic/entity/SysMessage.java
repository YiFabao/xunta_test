package so.xunta.topic.entity;

public class SysMessage {
	public int id;
	public String fromUserId;
	public String fromUserName;
	public String toUserId;
	public String toUserName;
	public String sysmsg;
	public String dateTime;
	public int isHandle;//是否处理过或查看过,0未，1处理过
	
	
	
	public SysMessage() {
		super();
	}
	public SysMessage(String fromUserId, String fromUserName, String toUserId, String toUserName, String sysmsg, String dateTime, int isHandle) {
		super();
		this.fromUserId = fromUserId;
		this.fromUserName = fromUserName;
		this.toUserId = toUserId;
		this.toUserName = toUserName;
		this.sysmsg = sysmsg;
		this.dateTime = dateTime;
		this.isHandle = isHandle;
	}
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
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getSysmsg() {
		return sysmsg;
	}
	public void setSysmsg(String sysmsg) {
		this.sysmsg = sysmsg;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public int getIsHandle() {
		return isHandle;
	}
	public void setIsHandle(int isHandle) {
		this.isHandle = isHandle;
	}
	
}
