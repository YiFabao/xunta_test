package so.xunta.topic.test;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import so.xunta.topic.TopicManagerImpl;
import so.xunta.topic.TopicMember;
import so.xunta.utils.DateTimeUtils;

public class TopicManagerImplTest {
	@Test
	public void testSaveTopicMember(){
		TopicManagerImpl topicManager=new TopicManagerImpl();
		//TopicMember tm=new TopicMember(topic_id, topic_member_id, join_datetime, exit, exit_datetime)
		TopicMember tm=new TopicMember("1", "11", "2015-1-9 17:20",0, "2015-1-10 18:20");
		topicManager.saveTopicMember(tm);
		System.out.println("ok");
		
	}
	
	@Test
	public void testCompareTime(){
		String str1="2015-01-09 18:02:04";
		String str2="2015-01-09 19:02:03";
		try {
			Date d1=DateTimeUtils.getCurrentDateTimeObj(str1);
			Date d2=DateTimeUtils.getCurrentDateTimeObj(str2);
			System.out.println(d1);
			System.out.println(d2);
			System.out.println(d1.getTime()>d2.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
