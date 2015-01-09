package so.xunta.topic;

import java.text.ParseException;
import java.util.Date;

import so.xunta.utils.DateTimeUtils;

/**
 * 话题
 * @author YiFabao
 */
public class Topic implements Comparable<Topic>{
	public int id=0;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	//话题唯一id
	public String topicId;
	/**作者唯一id*/
	public String authorId;
	/**作者名*/
	public String authorName;
	/**匹配的话题*/
	public String topicContent;
	/**话题发起时间*/
	public String topicCreatetime;
	

	public Topic(String topicId,String authorId,String hightLightTopic, String authorName,String topicCreatetime) {
		this.topicId = topicId;
		this.authorId=authorId;
		this.authorName = authorName;
		this.topicContent = hightLightTopic;
		this.topicCreatetime = topicCreatetime;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getTopicCreatetime() {
		return topicCreatetime;
	}
	public void setTopicCreatetime(String topicCreatetime) {
		this.topicCreatetime = topicCreatetime;
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
	@Override
	public int compareTo(Topic o) {
		try {
			Date d1 = DateTimeUtils.getCurrentDateTimeObj(this.topicCreatetime);
			Date d2=DateTimeUtils.getCurrentDateTimeObj(o.topicCreatetime);
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
