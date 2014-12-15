<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<% 
    String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="<%=basePath %>assets/javascripts/jquery-1.10.2.js"></script>
<title>话题匹配(测试)</title>
</head>
<body>
	<h1>话题匹配(测试)</h1>
	<hr>
		<form action="<%=basePath %>servlet/topic" method="post">
			发起话题：<br/>
			<textarea rows="5" cols="100" name="mytopic"></textarea><br/>
			<input type="submit"/>
		</form>
		
		<div>
			我的话题：<font color="red">${mytopic}</font><br>
			匹配的话题：
			<table>
				<c:forEach items="${matchedtopic}" var="topic">
					<tr>
						<td>#${topic.topicContent}#</td>
						<td>${topic.authorName}</td>
						<td>${topic.datetime}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
</body>
<script>
	
</script>
</html>