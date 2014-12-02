<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
  <head>
    <script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" charset="utf-8" data-callback="true"></script>
    <title>XunTa.so</title>
    	<script type="text/javascript">
		var s=window.location.hash;
		if(s!=null&&s!="")
		{
			var token=s.substring(s.indexOf("=")+1,s.indexOf("&"));
			var expires_in=s.substring(s.lastIndexOf("=")+1);
			window.location.href="${pageContext.request.contextPath}/servlet/qq_login?access_token="+token+"&expires_in="+expires_in+"_"+new Date(); 
		}
	</script>
  </head>
  
  <body>	
  </body>
</html>
