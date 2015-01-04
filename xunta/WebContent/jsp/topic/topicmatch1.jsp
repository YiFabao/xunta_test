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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>话题匹配</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>jsp/static/css/topicmatch.css">
</head>
<body>
	<div class="top">
		 <div class="top_content">
		 	
		<!-- 	<div class="top_title"></div>
			<div class="top_global_nav">
				<ul>
			    <li><a href="#">首页</a></li>
				<li><a href="#">发起话题</a></li>
				<li><a href="#">话题推荐</a></li>
				<li><a href="#">话题搜索</a></li>
				<li><a href="#">历史话题</a></li>
				</ul>
			</div>
			<div class="profile"></div> -->
		</div>
	</div>
	<div class="body">
		<div class="topic_box"></div>
		<div class="body_content">
		</div>
	</div>
	<div class="footing">

	</div>

	<script src="<%=basePath%>jsp/static/js/topicmatch.js"></script>
</body>
</html>