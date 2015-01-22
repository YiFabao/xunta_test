<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>话题记忆</title>
<!-- 网页头样式 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/topic/css/navbar.css">
<style>
	tr:hover{
		cursor:pointer;
	}
	tr:hover{
		background-color:orange;
	}
</style>
</head>
<body>
	<jsp:include page="include/navbar.jsp"></jsp:include>
	<div align="center">
		<h1>话题记忆</h1>
		<button id="mytopic_btn">我发的发话</button>
		<button id="myjoin_btn">我参与的话题</button>
	</div><br>
	<hr>
	<!--我发起的话题列表 -->
	<!--我参与的话题列表-->
	
<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/navbar.js"></script>
<script>
	//默认显示自己发的话题
	$.post("${pageContext.request.contextPath }/jsp/topic/include/topic_items.jsp",{userId:"${sessionScope.user.id}",cmd:"myTopicHistory"},function(res){
		$("body").append(res);
	});
	//点击 我发起的话题 按钮
	$("#mytopic_btn").click(function(){
		//判断如果 #topic_recommend_item 存在则删除
		$("#topic_recommend_item")&&$("#topic_recommend_item").remove();
		$.post("${pageContext.request.contextPath }/jsp/topic/include/topic_items.jsp",{userId:"${sessionScope.user.id}",cmd:"myTopicHistory"},function(res){
			$("body").append(res);
		});
	});
	
	//点击我参与的话题按钮
	$("#myjoin_btn").click(function(){
		//判断如果 #topic_recommend_item 存在则删除
		$("#topic_recommend_item")&&$("#topic_recommend_item").remove();
		$.post("${pageContext.request.contextPath }/jsp/topic/include/topic_items.jsp",{userId:"${sessionScope.user.id}",cmd:"myJoinTopic"},function(res){
			$("body").append(res);
		});
	});
</script>
</body>
</html>