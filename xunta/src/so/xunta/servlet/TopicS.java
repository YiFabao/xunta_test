package so.xunta.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.helper.DataUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import so.xunta.topic.entity.MessageAlert;
import so.xunta.topic.entity.TopicGroup;
import so.xunta.topic.entity.TopicHistory;
import so.xunta.topic.model.TopicManager;
import so.xunta.topic.model.impl.AddTopicIndexThread;
import so.xunta.topic.model.impl.SaveTopicThread;
import so.xunta.topic.model.impl.TopicManagerImpl;
import so.xunta.topic.utils.SecurityUtil;
import so.xunta.utils.DateTimeUtils;

/**
 * Servlet implementation class Topic
 */
@WebServlet("/Topic")
public class TopicS extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static TopicManager topicManager=new TopicManagerImpl(); 
    public TopicS() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
		response.setContentType("text/json; charset=UTF-8");  
		String cmd=request.getParameter("cmd");
		
		if(cmd==null||"".equals(cmd))
		{
			System.out.println("cmd为空");
			return;
		}
		
		switch(cmd){
		case "fqht":
			//发起话题　匹配
			fqht(request, response);
			break;
		case "htss":
			//搜索话题
			htss(request, response);
			break;
		case "httj":
			//话题推荐
			httj(request, response);
			break;
		case "htjy":
			//话题记忆
			htjy(request, response);
			break;
		case "addTopicMember":
			//添加话题成员
			addTopicMember(request,response);
			break;
		case "viewMessage":
			//查看自己的消息，邀请提醒
			viewMessage(request, response);
			break;
		case "addTopicHistory":
			//保存话题历史记录
			addTopicHistory(request);
			break;
		case "searchTopicMemberList":
			//查询话题下的联系人列表
			searchTopicMemberList(request,response);
			break;
		case "searchTopicContent":
			//查询话题id对应的话题内容
			searchTopicContent(request,response);
			break;
		case "addMessageAlert":
			//邀请别人到我的话题下聊天
			addMessageAlert(request,response);
			break;
		case "searchUnreadMsgNum":
			searchUnreadMsgNum(request,response);
			break;
		case "searchnicknameByUserId":
			searchnicknameByUserId(request,response);
			break;
		case "joinTopic":
			joinTopic(request,response);
			break;
		case "agreeToJoinTopic":
			agreeToJoinTopic(request,response);
			break;
		case "notAgreeToJoinTopic":
			notAgreeToJoinTopic(request,response);
			break;
		default:
				break;
		}
	}
	private void notAgreeToJoinTopic(HttpServletRequest request, HttpServletResponse response) {
		//获取 消息的主键id
		String id_str = request.getParameter("id");
		if(id_str==null||"".equals(id_str))return;
		
		int id = Integer.parseInt(id_str);
		
		//将消息改为已处理
		topicManager.updateMessageAlertToAlreadyHandle(id);
		
	}
	private void agreeToJoinTopic(HttpServletRequest request, HttpServletResponse response) {
		
		joinTopic(request, response);
		String id_str = request.getParameter("id");
		if(id_str==null||"".equals(id_str))return;
		
		int id = Integer.parseInt(id_str);
		//将消息改为已处理
		topicManager.updateMessageAlertToAlreadyHandle(id);
	}
	private void joinTopic(HttpServletRequest request, HttpServletResponse response) {
		//获取话题id
		String topicId=request.getParameter("topicId");
		//用户id
		String memberId=request.getParameter("userId");
		//用户昵称
		String memberName=request.getParameter("userName");
		System.out.println("话题"+topicId+"下添加成员");
		//检查在话题topicId下 成员memberId是否存在
		boolean isMemberIdExistinTopic=topicManager.checkIsTopicMember(memberId, topicId);
		System.out.println("话题成员是否存在："+isMemberIdExistinTopic);
		if(!isMemberIdExistinTopic)
		{
			String currentTime=DateTimeUtils.getCurrentTimeStr();
			TopicGroup topicMember =new TopicGroup(topicId, memberId,memberName,currentTime,0,"");
			topicManager.saveTopicGroup(topicMember);
		}
		else{
		}
		//保存话题成员的同时，要保存话题历史
		//查询该话题是否存在于我的话题记忆当中
		TopicHistory topicHistory=new TopicHistory(memberId, topicId,DateTimeUtils.getCurrentTimeStr());
		//检查话题历史是否存在
		if(!topicManager.checkTopicIsExitInHistory(memberId, topicId))
		{
			topicManager.addTopicHistory(topicHistory);
		}
		
		
		List<TopicGroup> topicMembers=topicManager.searchTopicMemberList(topicId);
		//组合成一个json格式的对象
		JSONArray jarray=new JSONArray();
		
		for(int i=0;i<topicMembers.size();i++)
		{
			String topic_id=topicMembers.get(i).topic_id;//话题id
			String topic_memberId=topicMembers.get(i).topic_member_id;//话题成员id
			String topic_member_name=topicMembers.get(i).topic_member_name;//成员昵称
			
			JSONObject jsonObj=new JSONObject();
			jsonObj.put("topic_id",topic_id);
			jsonObj.put("topic_memberId", topic_memberId);
			jsonObj.put("topic_member_name",topic_member_name);
			System.out.println(topic_member_name);
			jarray.add(jsonObj);
		}
		try {
			response.getOutputStream().write(jarray.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//通过用户的id 查询用户的昵称
	private void searchnicknameByUserId(HttpServletRequest request, HttpServletResponse response) {
		//获取请求参数 userId
		String userId = request.getParameter("userId");
		String nickname = topicManager.searchNicknameByUserId(Integer.parseInt(userId));
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("nickname",nickname);
		System.out.println(nickname);
		try {
			response.getOutputStream().write(jsonObj.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void searchUnreadMsgNum(HttpServletRequest request, HttpServletResponse response) {
		String authorId=request.getParameter("authorId");
		long num=topicManager.searchNotReadmessageNum(authorId);
		try {
			response.getWriter().write(num+"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void addMessageAlert(HttpServletRequest request, HttpServletResponse response) {
		String _fromUserId=request.getParameter("_fromUserId");//邀请人的id
		String authorId=request.getParameter("authorId");//被邀请的人的id,消息提配是他的
		so.xunta.topic.entity.Topic _fromUserTopic=topicManager.searchLatestTopic(_fromUserId);//查询_fromUser的最近一条话题
		MessageAlert messageAlert=new MessageAlert(authorId, _fromUserTopic.authorName, _fromUserId,_fromUserTopic.topicId,_fromUserTopic.topicContent,DateTimeUtils.getCurrentTimeStr());
		//保存消息提醒
		topicManager.addMessageAlert(messageAlert);
	}
	
	private void searchTopicContent(HttpServletRequest request, HttpServletResponse response) {
		
		String topicId=request.getParameter("topicId");
		String topicContent = topicManager.searchTopicContent(topicId);
		try {
			JSONObject jsonObj=new JSONObject();
			jsonObj.put("topicContent",topicContent);
			response.getOutputStream().write(jsonObj.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void searchTopicMemberList(HttpServletRequest request, HttpServletResponse response) {
		String topicId=request.getParameter("topicId");	
		List<TopicGroup> topicMembers=topicManager.searchTopicMemberList(topicId);
		//组合成一个json格式的对象
		JSONArray jarray=new JSONArray();
		
		for(int i=0;i<topicMembers.size();i++)
		{
			String topic_id=topicMembers.get(i).topic_id;//话题id
			String topic_memberId=topicMembers.get(i).topic_member_id;//话题成员id
			String topic_member_name=topicMembers.get(i).topic_member_name;//成员昵称
			
			JSONObject jsonObj=new JSONObject();
			jsonObj.put("topic_id",topic_id);
			jsonObj.put("topic_memberId", topic_memberId);
			jsonObj.put("topic_member_name",topic_member_name);
			jarray.add(jsonObj);
		}
		try {
			response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private void addTopicHistory(HttpServletRequest request) {
		String authorId = request.getParameter("authorId");
		String topicId = request.getParameter("topicId");
		String datetime = DateTimeUtils.getCurrentTimeStr();
		TopicHistory topicHistory=new TopicHistory(authorId, topicId, datetime);
		topicManager.addTopicHistory(topicHistory);
	}

	private void viewMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String authorId = request.getParameter("authorId");
		//查看自己的消息
		//查询自己的消息
		List<MessageAlert> messageAlertList=topicManager.searchMyMessage(authorId);
		topicManager.updateMessageAlertToAlreadyRead(authorId);
		request.setAttribute("messageAlertList", messageAlertList);
		request.getRequestDispatcher("/jsp/topic/myMessage.jsp").forward(request, response);
	}

	private void addTopicMember(HttpServletRequest request,HttpServletResponse response) {
		//获取话题id
		String topicId=request.getParameter("topicId");
		//用户id
		String memberId=request.getParameter("memberId");
		//用户昵称
		String memberName=request.getParameter("memberName");
		System.out.println("话题"+topicId+"下添加成员");
		//检查在话题topicId下 成员memberId是否存在
		boolean isMemberIdExistinTopic=topicManager.checkIsTopicMember(memberId, topicId);
		System.out.println("话题成员是否存在："+isMemberIdExistinTopic);
		if(!isMemberIdExistinTopic)
		{
			String currentTime=DateTimeUtils.getCurrentTimeStr();
			TopicGroup topicMember =new TopicGroup(topicId, memberId,memberName,currentTime,0,"");
			topicManager.saveTopicGroup(topicMember);
		}
		else{
		}
		
		//保存话题成员的同时，要保存话题历史
		//查询该话题是否存在于我的话题记忆当中
		TopicHistory topicHistory=new TopicHistory(memberId, topicId,DateTimeUtils.getCurrentTimeStr());
		//检查话题历史是否存在
		if(!topicManager.checkTopicIsExitInHistory(memberId, topicId))
		{
			topicManager.addTopicHistory(topicHistory);
		}
		searchTopicMemberList(request,response);

	}

	private void htjy(HttpServletRequest request, HttpServletResponse response) throws IOException, UnsupportedEncodingException {
		//话题发起人id
		String authorId = request.getParameter("authorId");
		List<so.xunta.topic.entity.Topic> historyTopic=topicManager.searchTopicHistory(authorId);
		//组合成一个json格式的对象
		JSONArray jarray=new JSONArray();
		
		for(int i=0;i<historyTopic.size();i++)
		{
			String name=historyTopic.get(i).getAuthorName();//作者名
			String userId=historyTopic.get(i).getAuthorId();//作者 id
			String content=historyTopic.get(i).getTopicContent();//话题内容
			String topic_id=historyTopic.get(i).getTopicId();//话题内容
			String datetime=historyTopic.get(i).getTopicCreatetime();//话题发布时间
			JSONObject jsonObj=new JSONObject();
			jsonObj.put("name",name);
			jsonObj.put("userId", userId);
			jsonObj.put("topicContent",content);
			jsonObj.put("topicId",topic_id);
			jsonObj.put("datetime",datetime);
			jarray.add(jsonObj);
			if(i>100)
				break;
		}
		response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
	}

	private void httj(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		//我的话题内容
		String mytopic = request.getParameter("mytopic");//会乱码要转码
		mytopic=new String(mytopic.getBytes("ISO-8859-1"),"utf-8");
		List<so.xunta.topic.entity.Topic> recommendTopic = topicManager.matchMyTopic(mytopic);
		// 组合成一个json格式的对象
		JSONArray jarray = new JSONArray();

		for (int i = 0; i < recommendTopic.size(); i++) {
			String name = recommendTopic.get(i).getAuthorName();
			String content = recommendTopic.get(i).getTopicContent();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", name);
			jsonObj.put("topicContent", content);
			jarray.add(jsonObj);
			if (i > 100)
				break;
		}
		response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
	}

	private void htss(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		//我的话题内容
		String search_word = request.getParameter("search_word");//会乱码要转码
		search_word=new String(search_word.getBytes("ISO-8859-1"),"utf-8");
		List<so.xunta.topic.entity.Topic> searchedTopic=topicManager.matchMyTopic(search_word);
		//组合成一个json格式的对象
		JSONArray jarray=new JSONArray();
		
		for(int i=0;i<searchedTopic.size();i++)
		{
			String name=searchedTopic.get(i).getAuthorName();//作者名
			String userId=searchedTopic.get(i).getAuthorId();//作者 id
			String content=searchedTopic.get(i).getTopicContent();//话题内容
			String topic_id=searchedTopic.get(i).getTopicId();//话题内容
			String datetime=searchedTopic.get(i).getTopicCreatetime();//话题发布时间
			JSONObject jsonObj=new JSONObject();
			jsonObj.put("name",name);
			jsonObj.put("userId", userId);
			jsonObj.put("topicContent",content);
			jsonObj.put("topicId",topic_id);
			jsonObj.put("datetime",datetime);
			jarray.add(jsonObj);
			if(i>100)
				break;
		}
		
		response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
	}

	private void fqht(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		//我的话题内容
		String mytopic = request.getParameter("mytopic");//会乱码要转码
		mytopic=new String(mytopic.getBytes("ISO-8859-1"),"utf-8");
		//话题发起人昵称
		String topicAuthorName = request.getParameter("topicAuthorName");
		topicAuthorName=new String(topicAuthorName.getBytes("ISO-8859-1"),"utf-8");
		//话题发起人id
		String authorId = request.getParameter("authorId");
		//话题发起时间 
		String topicCreateTime = DateTimeUtils.getCurrentTimeStr();
		//话题Id 由    [话题人id+话题内容+话题创建时间]    的字符串拼接字符串生成的md5
		String topicId=SecurityUtil.strToMD5(authorId+mytopic+topicCreateTime);
		
		//我的话题Topic
		so.xunta.topic.entity.Topic myselfTopic=new so.xunta.topic.entity.Topic(topicId, authorId,mytopic, topicAuthorName,topicCreateTime);
		
		List<so.xunta.topic.entity.Topic> matchedtopic=topicManager.matchMyTopic(mytopic);
		matchedtopic.add(0, myselfTopic);//将自己的话题添加到数组中返回
		//组合成一个json格式的对象
		JSONArray jarray=new JSONArray();
		
		for(int i=0;i<matchedtopic.size();i++)
		{
			String name=matchedtopic.get(i).getAuthorName();//作者名
			String userId=matchedtopic.get(i).getAuthorId();//作者 id
			String content=matchedtopic.get(i).getTopicContent();//话题内容
			String topic_id=matchedtopic.get(i).getTopicId();//话题内容
			String datetime=matchedtopic.get(i).getTopicCreatetime();//话题发布时间
			JSONObject jsonObj=new JSONObject();
			jsonObj.put("name",name);
			jsonObj.put("userId", userId);
			jsonObj.put("topicContent",content);
			jsonObj.put("topicId",topic_id);
			jsonObj.put("datetime",datetime);
			jarray.add(jsonObj);
			if(i>100)
				break;
		}
		//开启线程将索引保存到数据库中 和 保存到索引中
		new Thread(new AddTopicIndexThread(topicManager, topicId,mytopic, authorId, topicAuthorName, topicCreateTime)).start();
		so.xunta.topic.entity.Topic topic=new so.xunta.topic.entity.Topic(topicId, authorId, mytopic, topicAuthorName, topicCreateTime);
		new Thread(new SaveTopicThread(topicManager, topic)).start();
		//设置允许谁访问
		response.addHeader("Access-Control-Allow-Origin","http://localhost:63342");
		response.getOutputStream().write(jarray.toString().getBytes("UTF-8"));
	}

	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
