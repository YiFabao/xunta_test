<%@page import="so.xunta.topic.entity.Topic"%>
<%@page import="java.util.List"%>
<%@page import="so.xunta.topic.model.impl.TopicManagerImpl"%>
<%@page import="so.xunta.topic.model.TopicManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
.matched_topic{
	width:1024px;
	margin:auto;
}
tr:hover{
	cursor:pointer;
}
tr:hover{
	background-color:orange;
}
.msgAlert{
		width:200px;
		height:48px;
		border-radius:4px;
		border:1px solid #ccc;
		box-shadow: 1px 1px 1px rgba(0,0,0,0.15);
		position:fixed;
		bottom:5px;
		right:5px;
	}
</style>

<div align="center">
	<h1>搜索结果</h1>
</div><br>
<hr>
<div id="topic_recommend_item" align="center">
<table border="1" cellspacing="0" color="#ccc">
	<tr>
		<th>话题发起人</th>
		<th>发起人图像</th>
		<th>话题</th>
		<th>话题描述</th>
		<th>参与人数</th>
		<th>话题发起时间</th>
	</tr>
	<c:forEach items="${requestScope.topicList }" var="topic">
	<c:if test="${topic.userId!=sessionScope.user.id }">
	<tr class="searched_topic_item chat" topicId="${topic.topicId }" onclick = "chat(this)">
		<td class="topic_publisher" value="${topic.userName }">${topic.userName }</td>
		<td>
			<img src="${pageContext.request.contextPath }/jsp/topic/images/1.jpg" style="width:48px;height:48px;">
		</td>
		<td class="topic_name" value="${topic.topicName }">${topic.topicName }</td>
		<td width="400px" class="topic_content" value="${topic.topicContent }">
			${topic.topicContent }
		</td>
		<td class="topic_joinPeople_num">${topic.join_people_num }</td>
		<td class="topic_createTime">${topic.createTime }</td>
	</tr>
	</c:if>
	</c:forEach>
	</table>
</div>
