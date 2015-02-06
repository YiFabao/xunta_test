<%@page import="so.xunta.topic.entity.Topic"%>
<%@page import="java.util.List"%>
<%@page import="so.xunta.topic.model.impl.TopicManagerImpl"%>
<%@page import="so.xunta.topic.model.TopicManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	TopicManager topicManager = new TopicManagerImpl();
	String userId = request.getParameter("userId");
	String topicName = request.getParameter("topicName");
	String topicContent = request.getParameter("topicContent");
	List<Topic> topicList = topicManager.matchUserRelativeTopic(userId,topicName,topicContent);
	request.setAttribute("topicList",topicList);
%>
<!DOCTYPE html>
<!-- 放大 悬浮div -->
<div class="hover_box">
	<table border="1" width="800px"cellspacing="0px">
			<tr>
				<td width="48px">
					<img src="${pageContext.request.contextPath }/jsp/topic/images/2.jpg" style="width:48px;height:48px;">
				</td>
				<td width="100px">${requestScope.topicList.get(0).userName }</td>
				<td align="right">
					<input type="button" value="邀请" class ="btn_invite" userId="<%=userId %>" userName="${requestScope.topicList.get(0).userName }">
					<input id="btn_exit" type="button" value=" x ">
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<div class="published_relative_topic">
						发起的相关话题
						<c:out value="${requestScope.toicList.size() }"></c:out>
						<ol>
							<c:forEach items="${requestScope.topicList }" var="topic">
								<li topicId="${topic.topicId }">话题:${topic.topicName}|话题描述:${topic.topicContent}</li>
							</c:forEach>
						</ol>
					</div>
				</td>
			</tr>
	</table>
</div>

