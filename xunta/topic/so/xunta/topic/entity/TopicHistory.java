package so.xunta.topic.entity;

import java.text.ParseException;
import java.util.Date;

import so.xunta.utils.DateTimeUtils;

public class TopicHistory implements Comparable<TopicHistory>{
	
	public int id;
	public String authorId;
	public String topicId;
	public String datetime;
	public char publish_or_join;//p 为发起话题　j为参与话题
	
	public TopicHistory() {
		super();
	}

	public TopicHistory(String authorId,String topicId,String datetime,char publish_or_join) {
		this.authorId = authorId;
		this.topicId = topicId;
		this.datetime = datetime;
		this.publish_or_join = publish_or_join;
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

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public char getPublish_or_join() {
		return publish_or_join;
	}

	public void setPublish_or_join(char publish_or_join) {
		this.publish_or_join = publish_or_join;
	}

	@Override
	public int compareTo(TopicHistory o) {
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

	