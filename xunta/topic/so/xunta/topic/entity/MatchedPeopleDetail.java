package so.xunta.topic.entity;

import java.util.ArrayList;
import java.util.List;

public class MatchedPeopleDetail {
	public int userId;
	private int joinTopicNum;//参与的话题数
	private int publishTopicNum;//发起相关话题数
	public List<String> topicJoin=new ArrayList<String>();
	public List<String> topicPublish=new ArrayList<String>();

	//添加参与话题
	public void addJoinTopic(String topicId){
		topicJoin.add(topicId);
		joinTopicNum++;
	}
	
	//添加发起话题
	public void addPulishTopic(String topicId)
	{
		topicPublish.add(topicId);
		publishTopicNum++;
	}
	
	//获取参与话题数
	public int getJoinTopicNum(){
		return joinTopicNum;
	}
	
	//获取发起相关话题数
	public int getpublishTopicNum()
	{
		return publishTopicNum;
	}
	
	//排序以后再说!
	
}
