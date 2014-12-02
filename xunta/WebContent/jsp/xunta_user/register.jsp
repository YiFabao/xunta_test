<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html lang="en">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="description" content="">
		<meta name="author" content="">
		<link rel="icon" href="favicon.ico">
		<link href="assets/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
		<title>XunTa 用户注册首页</title>
		<script type="text/javascript" src="assets/javascripts/jquery-1.10.2.js"></script>
	</head>

	<body>
		<div class="container">
			用户注册：
			<form action="servlet/Register" method="post">
				用户名:<input type="text" name="xunta_username" value="${xunta_username}"/><br/>
				邮箱:<input type="text" name="email" value="${email}"/><br/>
				密码:<input type="password" name="password" ><br/>
				确认密码:<input type="password" name="confirm"><br/>
				输入验证码:<input type="code" name="code"><br/>
				<img src="servlet/ValidateCodeServlet" width="80" height="30" />点击图片换验证码<br />
				<button type="submit" >注册新用户</button>
			</form>

		</div>
		<script type="text/javascript">
			var codeImg=$("[src='servlet/ValidateCodeServlet']");
			codeImg.click(function(){
				codeImg.attr("src","servlet/ValidateCodeServlet?"+Math.random());				
			})
		</script>
	</body>
</html>
