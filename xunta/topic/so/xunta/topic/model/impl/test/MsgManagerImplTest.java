package so.xunta.topic.model.impl.test;

import java.util.List;

import org.junit.Test;

import so.xunta.topic.entity.SysMessage;
import so.xunta.topic.entity.TopicRequestMsg;
import so.xunta.topic.entity.TopicRequestMsgPlusTopicDetail;
import so.xunta.topic.model.impl.MsgManagerImpl;
import so.xunta.utils.DateTimeUtils;

public class MsgManagerImplTest {
	
	MsgManagerImpl msgManager = new MsgManagerImpl();
	
	@Test
	public void testAddMsg(){
		
		SysMessage sysmsg=new SysMessage("11114","易发宝","444","张三", "谁同意了你的请求",DateTimeUtils.getCurrentTimeStr(),0);
		
		msgManager.addMsg(sysmsg);
		
		System.out.println("添加成功");
		
	}
	
	@Test
	public void testFinRequestMsgNum()
	{
		long num =msgManager.findUnreadTopicRequestMsgNum("1");
		long num2 = msgManager.findUnreadSysMsgNum("3");
		System.out.println(num+"  "+num2 );
	}
	
	@Test
	public void testfindSysMsgByUserId()
	{
		List<SysMessage> msgList = msgManager.findSysMsgByUserId("444");
		
		for(SysMessage msg:msgList)
		{
			System.out.println(msg.fromUserId+"  ==> "+msg.toUserId);
		}
	}
	
	@Test
	public void updateSysMsgToHandledByUserId()
	{
		msgManager.updateSysMsgToHandledByUserId("444");
	}
	
	@Test
	public void addTopicRequestMsg(){
		
		TopicRequestMsg topicRm=new TopicRequestMsg("1133","2222","yi","zhang","A00B376E9ACD91A4C1D61322B56CC333",DateTimeUtils.getCurrentTimeStr(),"-1","0");
		msgManager.addTopicRequestMsg(topicRm);
	}
	
	@Test 
	public void findTopicRequestList(){
		List<TopicRequestMsgPlusTopicDetail> l=msgManager.findTopicRequestMsgByUserId("2222");
		for(TopicRequestMsgPlusTopicDetail t:l)
		{
			System.out.println(t.fromUserName+" 邀请您参与话题 #"+t.topicName+"#"+"   "+t.dateTime+" 是否处理:"+t.isAgree);
		}
	}
	
	@Test
	public void updateTopicRequestStateByUserIdAndTopicId(){
		msgManager.updateTopicRequestMsgHandledState("2222","C079941A59B3D649A6BB8D0CAE38ADAD","0");
	}
}
