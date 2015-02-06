<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<!-- 这就是一个顶级容器，唯一不变的就是聊天框，其他任何元素都通过js获取更换 -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/topic/css/navbar.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/topic/css/navbar_msg_tab.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jsp/topic/css/chat_box.css">
<style>

</style>

</head>
<body>
<!-- 导航栏 -->
<jsp:include page="include/navbar.jsp"></jsp:include>
<div id="container_all">
	
</div>
<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/nav_bar_msg_tab.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/navbar.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/include_chat.js"></script>
<script>
 	//默认显示 话题记忆
	console.log("户点击话题记忆");
	$.post(contextPath+"/jsp/topic/htjy.jsp",null,function(res,status){
		$("#container_all").empty();
		$("#container_all").append(res);
	});

</script>
</body>
</html>