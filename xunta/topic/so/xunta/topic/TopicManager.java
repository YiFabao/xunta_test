package so.xunta.topic;

import java.util.List;

public interface TopicManager {
	//创建话题索引 
	public void createTopicIndex(String topicId,String topicContent,String authorId,String topicAuthorName,String topicCreatetime);
	//获取匹配的话题,从索引里查询
	public List<Topic> matchMyTopic(String mytopic);
	public List<Topic> matchMyTopicByUserId(String mytopic);
	//将话题保存到数据库中
	public void saveTopic(Topic topic);
	//保存话题成员
	public void saveTopicMember(TopicMember topicMember);
	//添加消息
	public void addMessageAlert(MessageAlert messageAlert);
	//查询自己的消息
	public List<MessageAlert> searchMyMessage(String authorId);
	//查询未读消息数
	public long searchNotReadmessageNum(String authorId);
	//将未读消息更改为已读
	public void updateMessageAlertToAlreadyRead(String authorId);
	//将未读消息更改为已处理,pid为主键id
	public void updateMessageAlertToAlreadyHandle(int pid);
	//根据消息的自增id删除
	public void deleteOneMessage(int id);
	//添加话题历史，当用户发起话题或参与话题时记录
	public void addTopicHistory(TopicHistory topicHistory);
	//查询某用户是否是某话题id下的成员
	public boolean checkIsTopicMember(String memberId,String topicId);
	//查询话题topicId 下的联系人列表 [昵称 id]
	public List<TopicMember> searchTopicMemberList(String topicId);
	//查询topicId对应的话题内容
	public String searchTopicContent(String topicId);
	//查询某人的最近的一条话题的话题Topic
	public Topic searchLatestTopic(String authorId);
	//搜索话题历史，从数据库中查询
	public List<Topic> searchTopicHistory(String authorId);
	//查询某条话题是否存在于作者的话题历史当中
	public boolean checkTopicIsExitInHistory(String authorId,String topicId);
	//通过userId 查询用户的昵称
	public String searchNicknameByUserId(int userId);

	
}
