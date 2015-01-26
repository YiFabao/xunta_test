package so.xunta.topic.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import so.xunta.entity.User;
import so.xunta.manager.UserManager;
import so.xunta.manager.impl.UserManagerImpl;
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
}
