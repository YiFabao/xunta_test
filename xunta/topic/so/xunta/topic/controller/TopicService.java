package so.xunta.topic.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import so.xunta.entity.User;
import so.xunta.topic.entity.MatchedTopic;
import so.xunta.topic.entity.MessageAlert;
import so.xunta.topic.entity.Topic;
import so.xunta.topic.model.TopicManager;
import so.xunta.topic.model.TopicModel;
import so.xunta.topic.model.impl.AddTopicIndexThread;
import so.xunta.topic.model.impl.SaveTopicThread;
import so.xunta.topic.model.impl.TopicManagerImpl;
import so.xunta.topic.model.impl.TopicModelImpl;
import so.xunta.topic.utils.SecurityUtil;
import so.xunta.utils.DateTimeUtils;

/**
 * Servlet implementation class TopicService
 */
@WebServlet("/TopicService")
public class TopicService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TopicManager topicManager = new TopicManagerImpl();
	private TopicModel topicModel = new TopicModelImpl();
    public TopicService() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String cmd = request.getParameter("cmd");
		if(cmd==null)return;
		switch(cmd){
		case "fqht":
			//发起话题
			fqht(request,response);
			break;
		case "htss":
			htss(request,response);
			break;
		case "joinTopic":
			joinTopic(request,response);
			break;
		case "invite":
			invite(request,response);
			break;
		case "msgalert" :
			//显示我的消息
			showMyMessage(request,response);
			break;
		case "notAgreeToJoinTopic":
			notAgreeToJoinTopic(request,response);
			break;
		case "getTopicByTopicId":
			getTopicByTopicId(request,response);
			break;
		case "getTopicListByTopicIdArray":
			getTopicListByTopicIdArray(request,response);
			break;
		case "searchnicknameByUserId":
			searchnicknameByUserId(request,response);
			break;
		case "searchUnreadMsgNum":
			searchUnreadMsgNum(request,response);
			break;
		case "exit":
			exit(request,response);
			break;
		}
	}

	private void searchUnreadMsgNum(HttpServletRequest request, HttpServletResponse response) {
		String authorId=request.getParameter("authorId");
		long num=topicManager.searchNotReadmessageNum(authorId);
		try {
			response.setContentType("text/json");
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("num",num);
			response.getWriter().write(jsonObj.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void searchnicknameByUserId(HttpServletRequest request, HttpServletResponse response) {
		//获取请求参数 userId
		String userId = request.getParameter("userId");
		String nickname = topicManager.searchNicknameByUserId(Integer.parseInt(userId));
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("nickname",nickname);
		System.out.println(nickname);
		try {
			response.setContentType("text/json");
			response.getOutputStream().write(jsonObj.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unused")
	private void getTopicListByTopicIdArray(HttpServletRequest request, HttpServletResponse response) {
		String topicIdArray = request.getParameter("topicIdArray");
		if(topicIdArray==null||"".equals(topicIdArray.trim())){return;}
		System.out.println(topicIdArray);
		List<String> topicIdList = new ArrayList<String>();
		String[] topicIds =topicIdArray.split(",");
		for(String topicId:topicIds)
		{
			System.out.println(topicId);
			topicIdList.add(topicId);
		}
		List<Topic> topicList = topicManager.getTopicListByTopicIdList(topicIdList);
		System.out.println("数："+topicList.size());
		JSONArray topicArray = new JSONArray();
		for(Topic t:topicList)
		{
			JSONObject topicJSONObj = new JSONObject();
			topicJSONObj.put("topicId",t.topicId);
			topicJSONObj.put("userId",t.userId);
			topicJSONObj.put("userName",t.userName);
			topicJSONObj.put("topicName",t.topicName);
			topicJSONObj.put("topicContent",t.topicContent);
			topicJSONObj.put("logo_url",t.logo_url);
			topicArray.add(topicJSONObj);
			System.out.println(t.topicContent);
		}
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(topicArray.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getTopicByTopicId(HttpServletRequest request, HttpServletResponse response) {
		String topicId = request.getParameter("topicId");
		if(topicId==null||"".equals(topicId))
		{
			return;
		}
		response.setContentType("text/json; charset=UTF-8");
		Topic topic = topicManager.findTopicByTopicId(topicId);
		if(topic ==null)
		{
			try {
				response.getWriter().write("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else
		{
			String topicName = topic.getTopicName();
			String logoUrl = topic.getLogo_url();
			JSONObject datajson=new JSONObject();
			datajson.put("topicName",topicName);
			datajson.put("logoUrl",logoUrl);
			datajson.put("topicId",topicId);
			try {
				response.getWriter().write(datajson.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//用户不同意参与话题
	private void notAgreeToJoinTopic(HttpServletRequest request, HttpServletResponse response) {
		//获取 消息的主键id
		String id_str = request.getParameter("id");
		if(id_str==null||"".equals(id_str))return;
		
		int id = Integer.parseInt(id_str);
		
		//将消息改为已处理
		topicManager.updateMessageAlertToAlreadyHandle(id);
	}

	//别人邀请我时，会显示我的消息
	private void showMyMessage(HttpServletRequest request, HttpServletResponse response) {
		User  user = (User)request.getSession().getAttribute("user");
		if(user==null){
			try {
				request.getRequestDispatcher("/jsp/xunta_user/login.jsp").forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
		long userId = user.id;//获取用户id
		
		List<MessageAlert> messageAlertList=topicManager.searchMyMessage(userId+"");
		topicManager.updateMessageAlertToAlreadyRead(userId+"");
		request.setAttribute("messageAlertList", messageAlertList);
		try {
			request.getRequestDispatcher("/jsp/topic/myMessage.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void invite(HttpServletRequest request, HttpServletResponse response) {
		//获取请求参数
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String to_userId = request.getParameter("to_userId");
		String topicId = request.getParameter("topicId");
		String topicContent = request.getParameter("topicContent");
		
		System.out.println("userId:"+userId);
		System.out.println("userName:"+userName);
		System.out.println("to_userId:"+to_userId);
		System.out.println("topicId:"+topicId);
		System.out.println("topicContent:"+topicContent);
		
		MessageAlert messageAlert = new MessageAlert(to_userId,userName,userId, topicId, topicContent,DateTimeUtils.getCurrentTimeStr());
		
		try {
			topicManager.addMessageAlert(messageAlert);
			response.getWriter().write("ok");
		} catch (IOException e) {
			try {
				response.getWriter().write("failure");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	//退出登录
	private void exit(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("user");
		try {
			request.getRequestDispatcher("/jsp/xunta_user/login.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void joinTopic(HttpServletRequest request, HttpServletResponse response) {
		//参与话题
		String userId = request.getParameter("userId");
		String topicId = request.getParameter("topicId");
		//调用用户参与话题的业务处理逻辑模型
		topicModel.joinTopic(request, response, userId, topicId);
		try {
			request.getRequestDispatcher("/jsp/topic/include/dialogue_box.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void htss(HttpServletRequest request, HttpServletResponse response) {
			String searchWord = request.getParameter("search_word");
			searchWord =URLDecoder.decode(searchWord);
			System.out.println("话题搜索");
			System.out.println(searchWord);
			//搜索话题
			List<so.xunta.topic.entity.Topic> searchedtopicList=topicManager.matchMyTopic(searchWord);
			request.setAttribute("searchWord",searchWord);
			request.setAttribute("topicList",searchedtopicList);
			try {
				request.getRequestDispatcher("/jsp/topic/htss.jsp").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void fqht(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");//用户id
		String userName = request.getParameter("userName");//用户名
		String userLogoUrl = request.getParameter("userLogoUrl");//用户logo
		String topicName = request.getParameter("topicName");//话题标题
		String topicContent = request.getParameter("topicContent");//话题内容
		System.out.println("用户id:"+userId);
		System.out.println("用户名:"+userName);
		System.out.println("用户LogoUrl:"+userLogoUrl);
		System.out.println("话题名称:"+topicName);
		System.out.println("话题内容:"+topicContent);
		
		//话题发起时间 
		String topicCreateTime = DateTimeUtils.getCurrentTimeStr();
		//话题Id 由    [用户id+话题名称+话题内容+话题创建时间]    的字符串拼接字符串生成的md5
		String topicId=SecurityUtil.strToMD5(userId+topicName+topicContent+topicCreateTime);
		//保存用户话题
		Topic topic =new Topic(topicId,userId,userName,topicName,topicContent,userLogoUrl,topicCreateTime,topicCreateTime);
		AddTopicIndexThread addTopicIndexThread=new AddTopicIndexThread(topicManager, topic);
		new Thread(addTopicIndexThread).start();
		SaveTopicThread saveTopicThread = new SaveTopicThread(topicManager, topic);
		new Thread(saveTopicThread).start();//保存话题，话题组，话题历史
		request.setAttribute("myTopic",topic);
		//匹配话题
		List<so.xunta.topic.entity.Topic> matchedtopicList=topicManager.matchMyTopic(topic.topicName,topic.topicContent);
		//按userId分组
		Map<String,List<Topic>> topicMap = new HashMap<String,List<Topic>>();
		for(Topic t:matchedtopicList)
		{
			if(topicMap.containsKey(t.userId))
			{
				topicMap.get(t.userId).add(t);
				topicMap.put(t.userId,topicMap.get(t.userId));
			}
			else
			{
				List<Topic> list=new ArrayList<Topic>();
				list.add(t);
				topicMap.put(t.userId,list);
			}
		}
		System.out.println("＝＝＝＝>"+matchedtopicList.size());
		System.out.println("topicMap.size:"+topicMap.size());
		List<MatchedTopic> mtlist = new ArrayList<MatchedTopic>();
		Iterator<Entry<String, List<Topic>>> iterator =topicMap.entrySet().iterator();
		while(iterator.hasNext())
		{
			Entry<String,List<Topic>> e = iterator.next();
			MatchedTopic mt = new MatchedTopic();
			mt.setUserId(e.getKey());
			mt.setRelativeTopicList(e.getValue());
			mt.setUserName(e.getValue().get(0).getUserName());
			mtlist.add(mt);
		}
		System.out.println("mtlist====>"+mtlist.size());
		request.setAttribute("matchedTopicList",mtlist);
		try {
			request.getRequestDispatcher("/jsp/topic/fqht.jsp").forward(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
