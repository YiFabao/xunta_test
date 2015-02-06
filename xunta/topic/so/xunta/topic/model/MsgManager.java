package so.xunta.topic.model;

import java.util.List;

import so.xunta.topic.entity.SysMessage;
import so.xunta.topic.entity.TopicRequestMsg;
import so.xunta.topic.entity.TopicRequestMsgPlusTopicDetail;

public interface MsgManager {
	//添加系统消息
	public void addMsg(SysMessage sysMsg);
	//查询系统消息
	public List<SysMessage> findSysMsgByUserId(String userId);
	//将系统消息改为已处理,输入为一个用户id
	public void updateSysMsgToHandledByUserId(String userId);
	//查询未读消息数
	public long findUnreadSysMsgNum(String toUserId);
	
	//添加话题请求消息
	public void addTopicRequestMsg(TopicRequestMsg topicRequestMsg);
	//查询话题请求消息
	public List<TopicRequestMsgPlusTopicDetail> findTopicRequestMsgByUserId(String toUserId);
	//更改消息的处理状态,传入一个数值1代表同意,0代表不同意
	public void updateTopicRequestMsgHandledState(String toUserId,String topicId,String state);
	//将消息改为已读
	public void updateTopicRequestMsgReaded(String toUserId);
	//查询邀请未读消息数
	public long findUnreadTopicRequestMsgNum(String toUserId);
	
}
