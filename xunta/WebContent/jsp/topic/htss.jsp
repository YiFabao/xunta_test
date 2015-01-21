<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>话题搜索</title>
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
		<h1>搜索结果</h1>

	</div><br>
	<hr>	
<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/navbar.js"></script>
<script>
	$.post("${pageContext.request.contextPath }/jsp/topic/include/topic_items.jsp",null,function(res){
		$("body").append(res);
	});
</script>
</body>
</html>