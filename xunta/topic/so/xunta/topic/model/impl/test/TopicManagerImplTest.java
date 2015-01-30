package so.xunta.topic.model.impl.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import so.xunta.entity.User;
import so.xunta.manager.UserManager;
import so.xunta.manager.impl.UserManagerImpl;
import so.xunta.topic.entity.Topic;
import so.xunta.topic.model.TopicManager;
import so.xunta.topic.model.impl.TopicManagerImpl;

public class TopicManagerImplTest {
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
