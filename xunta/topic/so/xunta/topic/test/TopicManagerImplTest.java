package so.xunta.topic.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import so.xunta.topic.MessageAlert;
import so.xunta.topic.Topic;
import so.xunta.topic.TopicManagerImpl;
import so.xunta.topic.TopicMember;
import so.xunta.utils.DateTimeUtils;

public class TopicManagerImplTest {
	
	@Test
	public void topicIsExitInHistoryTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		System.out.println(topicManager.checkTopicIsExitInHistory("8","DD4D08F997C854C81FDC2CE090BCC25A"));
	}
	
	@Test
	public void updateMessageAlertToAlreadyReadTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		topicManager.updateMessageAlertToAlreadyRead("10");
		System.out.println("ok");
	}
	
	@Test 
	public void searchMyMessageTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		List<MessageAlert> ml=topicManager.searchMyMessage("10");
		for(MessageAlert m:ml)
		{
			System.out.println(m.topicContent);
		}
	}
	@Test
	public void searchNotReadmessageNumTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		long num=topicManager.searchNotReadmessageNum("d");
		System.out.println(num);
	}
	
	@Test
	public void testSaveTopicMember(){
		TopicManagerImpl topicManager=new TopicManagerImpl();
		//TopicMember tm=new TopicMember(topic_id, topic_member_id, join_datetime, exit, exit_datetime)
		TopicMember tm=new TopicMember("1", "11","fabaoyi", "2015-1-9 17:20",0, "2015-1-10 18:20");
		topicManager.saveTopicMember(tm);
		System.out.println("ok");
		
	}
	@Test
	public void searchTopicMemberListTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		List<TopicMember> topicMembers=topicManager.searchTopicMemberList("1");
		for(TopicMember t:topicMembers)
		{
			System.out.println(t.topic_member_name);
			System.out.println(t.id);
			System.out.println(t.topic_id);
			System.out.println(t.topic_member_id);
			System.out.println(t.join_datetime);
		}
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
	
	@Test
	public void checkIsTopicMemberTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		boolean t=topicManager.checkIsTopicMember("10","6D009264C81F1E04B2564A42B7960DAC");
		System.out.println(t);
	}
	
	@Test
	public void searchTopicContentTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		String aa=topicManager.searchTopicContent("9F98463FED345CE93DD93613CBE9E7EC");
		System.out.println(aa);
		
	}
	
	@Test
	public void searchLatestTopicTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		Topic topic=topicManager.searchLatestTopic("8");
		System.out.println(topic.topicContent);
	}
	
	@Test 
	public void listTest()
	{
		List<String> l=new ArrayList<String>();
		l.add("aaa");
		l.add("bbb");
		l.add(0,"ccc");
		for(String s:l)
		{
			System.out.println(s);
		}
		
	}
	
	@Test
	public void searchTopicHistoryTest()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		List<Topic> topicList = topicManager.searchTopicHistory("8");
		for(Topic t:topicList)
		{
			System.out.println(t.topicContent);
		}
	}
	

	
}
