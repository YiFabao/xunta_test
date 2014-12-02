<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String qq_openId=(String)request.getAttribute("qq_openId");
String qq_accessToken=(String)request.getAttribute("qq_accessToken");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'bindLocalAccount.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <h1>与寻Ta帐号绑定,下次用QQ帐号登录，可以同时看到两个账号的信息</h1>
    <form action="servlet/bindLocalAccount" method="post">
				用户名:<input type="text" name="xunta_username" value="${xunta_username}"/><br/>
				密码:<input type="password" name="password" ><br/>
				<input type="hidden" name="qq_openId" value="${qq_openId}"/>
				<input type="hidden" name="qq_accessToken" value="${qq_accessToken}"/>
				<button type="submit" >绑定账号</button>
	</form>
    
    
  </body>
  <script type="text/javascript">
  	var openid="<%=qq_openId%>";
  	var qq_accessToken="<%=qq_accessToken%>";
  	console.log("qq_openid:"+openid);
  	console.log("qq_accessToken:"+qq_accessToken);
  </script>
</html>
