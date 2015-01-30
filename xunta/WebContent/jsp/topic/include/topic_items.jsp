<%@page import="so.xunta.topic.entity.Topic"%>
<%@page import="java.util.List"%>
<%@page import="so.xunta.topic.model.impl.TopicManagerImpl"%>
<%@page import="so.xunta.topic.model.TopicManager"%>
<%@page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	//获取cmd 参数，此模版 被 我发的话题与我参与的话题公用
	String cmd = request.getParameter("cmd");
	String userId = request.getParameter("userId");
	TopicManager topicManager =new TopicManagerImpl();
	if("myTopicHistory".equals(cmd))
	{
		List<Topic> topicList =topicManager.searchMyTopicHistory(userId);
		pageContext.setAttribute("topicList", topicList);
	}else if("myJoinTopic".equals(cmd))
	{
		List<Topic> topicList =topicManager.searhMyJoinTopic(userId);
		pageContext.setAttribute("topicList", topicList);
	}
%>
<!DOCTYPE html>
<div id="topic_recommend_item" align="center">
	<table border="1" cellspacing="0" color="#ccc">
		<tr>
			<th>话题发起人</th>
			<th>发起人图像</th>
			<th>话题</th>
			<th>话题描述</th>
			<th>参与人数</th>
			<th>话题发起时间</th>
			<th>最后活跃时间</th>
		</tr>
		<c:forEach items="${pageScope.topicList }" var="topic">
		<tr class="history_topic_item" topicId="${topic.topicId }" onclick="chat(this)">
			<td >${topic.userName }</td>
			<td>
				<img src="${pageContext.request.contextPath }/jsp/topic/images/1.jpg" style="width:48px;height:48px;">
			</td>
			<td class="topic_name" value="${topic.topicName }">${topic.topicName }</td>
			<td width="400px">
				${topic.topicContent }
			</td>
			<td class="topic_joinPeople_num">${topic.join_people_num }</td>
			<td class="topic_createTime">${topic.createTime }</td>
			<td>${topic.lastUpdateTime }</td>
		</tr>
		</c:forEach>
	</table>
</div>

