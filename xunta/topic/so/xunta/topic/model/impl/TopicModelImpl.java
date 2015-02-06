package so.xunta.topic.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import so.xunta.entity.User;
import so.xunta.manager.UserManager;
import so.xunta.manager.impl.UserManagerImpl;
import so.xunta.topic.entity.MatchedPeopleDetail;
import so.xunta.topic.entity.Topic;
import so.xunta.topic.entity.TopicGroup;
import so.xunta.topic.entity.TopicHistory;
import so.xunta.topic.model.TopicManager;
import so.xunta.topic.model.TopicModel;
import so.xunta.utils.DateTimeUtils;

public class TopicModelImpl implements TopicModel{
	
	TopicManager topicManager = new TopicManagerImpl();
	UserManager userManager = new UserManagerImpl();
	
	@Override
	public void joinTopic(HttpServletRequest request, HttpServletResponse response,String userId, String topicId) {
		//根据topicId 查询出Topic
		Topic topic = topicManager.findTopicByTopicId(topicId);
		//根据用户Id查询出发起人
		User publisher = userManager.findUserById(Integer.parseInt(topic.userId));
		//查询出参与人
		User joinUser = userManager.findUserById(Integer.parseInt(userId));
		
		//保存参与话题历史,要检查在参与话题历史是否存在
		String currentTime = DateTimeUtils.getCurrentTimeStr();
		if(!topicManager.checkTopicIsExitInHistory(userId, topicId))
		{
			TopicHistory topicHistory = new TopicHistory(userId, topicId,currentTime ,'j');
			topicManager.addTopicHistory(topicHistory);
		}
		//保存话题组,检查是否已经存在到话题组
		if(!topicManager.checkIsTopicMember(userId, topicId))
		{
			TopicGroup topicMember =new TopicGroup(topicId,userId,joinUser.getXunta_username(),currentTime);
			topicManager.saveTopicGroup(topicMember);
		}
		//根据topicId 查询出该话题下的用户列表List<User>
			//1.先从topicgroup中查询出List<userId>
			//２.再从user表中查询出List<memberId>
		List<String> memberIds =topicManager.findMemberIdsByTopicId(topicId);
		
		List<Long> memberId_long_list = new ArrayList<Long>();
		for(String memberId:memberIds)
		{
			memberId_long_list.add(new Long(memberId));
		}
		List<User> memberList = userManager.findUserListByUserIdList(memberId_long_list);
		for(User memUser:memberList)
		{
			System.out.println(memUser.xunta_username);
		}
		
		//将　发起人　Topic 及　用户列表 保存到request范围
		request.setAttribute("topic",topic);
		request.setAttribute("publisher",publisher);
		request.setAttribute("memberList",memberList);
		
	}

	@Override
	public List<MatchedPeopleDetail> matchedPeopleDetaiList(List<Topic> topicList) {
		Map<String,MatchedPeopleDetail> matchedMap = new HashMap<String,MatchedPeopleDetail>();
		//遍历每个topic
		List<String> topicIdList =new ArrayList<String>();
		for(Topic topic:topicList)
		{
			topicIdList.add(topic.topicId);
		}
		//获取与该话题id对应的话题历史
		List<TopicHistory> topicHistoryList = topicManager.findTopicHistoryByTopicId(topicIdList);
		for(TopicHistory t:topicHistoryList)
		{
			//System.out.println(t.topicId+"  "+t.publish_or_join);
			//遍历每个话题下的成员
			String key = t.authorId;
			if(matchedMap.containsKey(key)){//存在直接添加
				MatchedPeopleDetail  matchedPeopleDetail = matchedMap.get(key);
				//判断该用户是发起还是参与
				if(t.publish_or_join=='p'){//发起话题
					matchedPeopleDetail.addPulishTopic(t.topicId);
				}
				else if(t.publish_or_join=='j'){
					matchedPeopleDetail.addJoinTopic(t.topicId);
				}
			}else{//不存在要创建
				MatchedPeopleDetail  matchedPeopleDetail = new MatchedPeopleDetail();
				//判断该用户是发起还是参与
				if(t.publish_or_join=='p'){//发起话题
					matchedPeopleDetail.addPulishTopic(t.topicId);
				}
				else if(t.publish_or_join=='j'){
					matchedPeopleDetail.addJoinTopic(t.topicId);
				}
				matchedMap.put(key,matchedPeopleDetail);
			}
		}
		
		return (List<MatchedPeopleDetail>) matchedMap.values();
	}
}
