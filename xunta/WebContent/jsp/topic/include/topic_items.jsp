<%@page import="so.xunta.topic.Topic"%>
<%@page import="java.util.List"%>
<%@page import="so.xunta.topic.TopicManagerImpl"%>
<%@page import="so.xunta.topic.TopicManager"%>
<%@page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	
	String userId = request.getParameter("userId");
	TopicManager topicManager =new TopicManagerImpl();
	List<Topic> topicList =topicManager.searchMyTopicHistory(userId);
	pageContext.setAttribute("myTopicList", topicList);
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
		<c:forEach items="${pageScope.myTopicList }" var="topic">
		<tr>
			<td>${topic.userName }</td>
			<td>
				<img src="${pageContext.request.contextPath }/jsp/topic/images/1.jpg" style="width:48px;height:48px;">
			</td>
			<td>${topic.topicName }</td>
			<td width="400px">
				${topic.topicContent }
			</td>
			<td>${topic.join_people_num }</td>
			<td>${topic.createTime }</td>
			<td>${topic.lastUpdateTime }</td>
		</tr>
		</c:forEach>
	</table>
</div>