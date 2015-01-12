<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>我的消息</title>
</head>
<body>
	<h3>未读消息</h3>
	<hr>
	<c:forEach var="messageItem" items="${requestScope.messageAlertList}" >
		<c:if test="${messageItem.isHandle==0}">
			<ul>
				<li>有人邀请您参与该话题：</li>
				<li>${messageItem.authorId }</li>
				<li>${messageItem._fromUserId }</li>
				<li>${messageItem._fromUsername}</li>
				<li>${messageItem.datetime }</li>
				<li>${messageItem.topicId }</li>
				<li>${messageItem.isHandle}</li>
				<li>
					<button>同意</button>
					<button>不同意</button>
				</li>
			</ul>
		</c:if>
	</c:forEach>
	<h3>已读消息</h3>
	<hr>
	<c:forEach var="messageItem" items="${requestScope.messageAlertList}" >
		<c:if test="${messageItem.isHandle==1}">
			<ul>
				<li>有人邀请您参与该话题：</li>
				<li>${messageItem.authorId }</li>
				<li>${messageItem._fromUserId }</li>
				<li>${messageItem._fromUsername}</li>
				<li>${messageItem.datetime }</li>
				<li>${messageItem.topicId }</li>
				<li>${messageItem.isHandle}</li>
				<li>
					<button>同意</button>
					<button>不同意</button>
				</li>
			</ul>
		</c:if>
	</c:forEach>
</body>
</html>