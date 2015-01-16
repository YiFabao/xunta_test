<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="zh">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="description" content="">
		<meta name="author" content="">
		<link rel="icon" href="favicon.ico">
		<link href="assets/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
		<title>XunTa 用户注册首页</title>
		<script type="text/javascript" src="assets/javascripts/jquery-1.10.2.js"></script>
		<style>
			.container{
				position:relative;
				left:300px;
				top:200px;
			}
		</style>
	</head>
	
	<body>
		<div class="container">
			<h3>用户注册</h3>
			
			<c:if test="${sessionScope.user==null}" var="value">
					<font color='red'>${requestScope.errorMsg }</font>
					
			</c:if>
			<form action="servlet/register" method="post">
				用户名(不超过30位):<input type="text" name="xunta_username" value="${xunta_username}" size=30><br/>
				邮箱:<input type="text" name="email" value="${email}"><br/>
				密码(不超过18位):<input type="password" name="password" size=18 value="${password}"><br/>
				确认密码(不超过18位):<input type="password" name="confirm" size=18 value="${confirm}"><br/>
				输入验证码(区分大小写):<input type="code" name="code"><br/>
				<img src="servlet/validateCodeServlet" width="80" height="30" class="validateImage"/>点击图片换验证码<br />
				<button type="submit" >注册新用户</button>
			</form>

		</div>
		<script type="text/javascript">
			var validateImage=document.getElementsByClassName("validateImage")[0];
			validateImage.addEventListener("click",function(){
				this.setAttribute("src","servlet/validateCodeServlet?"+Math.random());
			});
		</script>
	</body>
</html>
