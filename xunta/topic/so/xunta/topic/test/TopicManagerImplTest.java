package so.xunta.topic.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import so.xunta.topic.MessageAlert;
import so.xunta.topic.Topic;
import so.xunta.topic.TopicManagerImpl;
import so.xunta.topic.TopicGroup;
import so.xunta.utils.DateTimeUtils;

public class TopicManagerImplTest {
	@Test
	public void searhMyJoinTopic()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		List<Topic> topicList =topicManager.searhMyJoinTopic("3");
		for(Topic t:topicList)
		{
			System.out.println(t.userName);
		}
		
	}
	@Test
	public void testsearchMyTopicHistory()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		List<Topic> topicList = topicManager.searchMyTopicHistory("3");
		for(Topic t:topicList)
		{
			System.out.println(t.topicContent);
		}
		
	}
	@Test
	public void testmatchUserRelativeTopic()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();
		List<Topic> l=topicManager.matchUserRelativeTopic("2","上海");
		for(Topic t:l)
		{
			System.out.println(t.topicContent);
		}
	}
	
	@Test
	public void searchNicknameByUserId()
	{
		TopicManagerImpl topicManager=new TopicManagerImpl();

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
