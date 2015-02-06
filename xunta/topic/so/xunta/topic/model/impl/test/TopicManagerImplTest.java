package so.xunta.topic.model.impl.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import so.xunta.entity.User;
import so.xunta.manager.UserManager;
import so.xunta.manager.impl.UserManagerImpl;
import so.xunta.topic.entity.MatchedPeopleDetail;
import so.xunta.topic.entity.Topic;
import so.xunta.topic.entity.TopicHistory;
import so.xunta.topic.model.TopicManager;
import so.xunta.topic.model.impl.TopicManagerImpl;

public class TopicManagerImplTest {
	@Test
	public void testmatchedPeopleDetaiList(){
		TopicManager topicManager = new TopicManagerImpl();
		List<String> topicIdList =  new ArrayList<String>();
		//List<MatchedPeopleDetail> matchedPeopleDetaiList =topicManager.findTopicHistoryByTopicId(topicIdList );
	}
	
	
	@Test
	public void testfindTopicHistoryByTopicId(){
		TopicManager topicManager = new TopicManagerImpl();
		List<String> topicIdList=new ArrayList<String>();
		topicIdList.add("69078E1A128D0E3A9327037A3DB4BD9E");
		topicIdList.add("E0C303E8B63FE2D169C564EFD1587E1D");
		topicIdList.add("86A8CB81E691582620E9F52105B0AAFF");
		List<TopicHistory> topicHistoryList = topicManager.findTopicHistoryByTopicId(topicIdList);
		for(TopicHistory t:topicHistoryList)
		{
			System.out.println(t.topicId+"  "+t.publish_or_join+"  是否发起话题:"+(t.publish_or_join=='p'));
		}
	}
	
	@Test
	public void testMap(){
		
		Map<String,User> map = new HashMap<String,User>();
		User user1 = new User();
		user1.setEmail("1019357922@qq.com");
		map.put("user1",user1);
		User user2 = map.get("user1");
		user2.setEmail("fabaoyi@126.com");
		
		System.out.println(map.get("user1").email);
	}
	
	@Test
	public void testgetTopicListByTopicIdList(){
		List<String> topicIdList = new ArrayList<>();
		topicIdList.add("DEC38294FCADEDFFA835C1D04D2DA2E1");
		topicIdList.add("A39517B49B8ADED2B8D3634834D301EB");
		topicIdList.add("A0971F53688530FB460D2430FDE2D854");
		TopicManager topicManager = new TopicManagerImpl();
		List<Topic> topics =topicManager.getTopicListByTopicIdList(topicIdList);
		for(Topic t:topics)
		{
			System.out.println(t.topicContent);
		}
		
	}
	
	@Test
	public void searhMyJoinTopic()
	{
		TopicManager topicManager = new TopicManagerImpl();
		List<Topic> topics = topicManager.searhMyJoinTopic("1"	);
		for(Topic t:topics)
		{
			System.out.println(t.getTopicContent());
		}
	}
	
	@Test
	public void testUser()
	{
		UserManager userManager = new UserManagerImpl();
		User user = userManager.findUserById(3);
	
	}
	
	@Test
	public void testfindUserListByUserIdList()
	{
		TopicManager topicManager = new TopicManagerImpl();
		List<String> list = topicManager.findMemberIdsByTopicId("1D521E9B58F938D2515D132709037733");
		List<Long> userIdList = new ArrayList<Long>();
		for(String l:list)
		{
			userIdList.add(new Long(l));
		}

	}
	
	@Test
	public void testfindMemberIdsByTopicId()
	{
		TopicManager topicManager = new TopicManagerImpl();
		List<String> memberIds = topicManager.findMemberIdsByTopicId("1D521E9B58F938D2515D132709037733");
		System.out.println(memberIds.get(0));

	}
	
	@Test
	public void testFindTopicByTopicId()
	{
		TopicManager topicManager = new TopicManagerImpl();
		Topic topic = topicManager.findTopicByTopicId("1D521E9B58F938D2515D132709037733");
		System.out.println(topic.topicContent);
	}

}
