<%@page import="so.xunta.topic.Topic"%>
<%@page import="java.util.List"%>
<%@page import="so.xunta.topic.TopicManagerImpl"%>
<%@page import="so.xunta.topic.TopicManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	TopicManager topicManager = new TopicManagerImpl();
	String userId = request.getParameter("userId");
	String topicContent = request.getParameter("topicContent");
	List<Topic> topicList = topicManager.matchUserRelativeTopic(userId, topicContent);
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
					<input type="button" value="邀请">
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
								<li topicId="${topic.topicId }">${topic.topicContent}</li>
							</c:forEach>
						</ol>
					</div>
				</td>
			</tr>
	</table>
</div>
