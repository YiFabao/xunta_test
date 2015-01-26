package so.xunta.topic.entity;

import java.text.ParseException;
import java.util.Date;

import so.xunta.utils.DateTimeUtils;

/**
 * 话题
 * @author YiFabao
 */
public class Topic implements Comparable<Topic>{
	public int id=0;
	//话题唯一id
	public String topicId;
	/**作者唯一id*/
	public String userId;
	/**作者名*/
	public String userName;
	/**话题标题*/
	public String topicName;
	/**话题内容*/
	public String topicContent;
	/**参与人数*/
	public int join_people_num =1;
	/**发起人头像 url*/
	public String logo_url;
	/**话题发起时间*/
	public String createTime;
	/**话题最后活跃时间*/
	public String lastUpdateTime;

	public Topic() {
		super();
	}
	public Topic(String topicId,String userId,String userName,String topicName,String topicContent,String logo_url,String topicCreateTime ,String lastUpdateTime) {
		this.topicId = topicId;
		this.userId=userId;
		this.userName = userName;
		this.topicName = topicName;
		this.topicContent = topicContent;
		this.logo_url = logo_url;
		this.createTime = topicCreateTime;
		this.lastUpdateTime = lastUpdateTime;
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
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getTopicContent() {
		return topicContent;
	}
	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}
	public int getJoin_people_num() {
		return join_people_num;
	}
	public void setJoin_people_num(int join_people_num) {
		this.join_people_num = join_people_num;
	}
	public String getLogo_url() {
		return logo_url;
	}
	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@Override
	public int compareTo(Topic o) {
		try {
			Date d1 = DateTimeUtils.getCurrentDateTimeObj(this.createTime);
			Date d2=DateTimeUtils.getCurrentDateTimeObj(o.createTime);
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
		return -1;
	}
}
