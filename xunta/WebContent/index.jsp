<%@ page language="java" import="java.util.*,so.xunta.entity.*" pageEncoding="utf-8"%>

<%@page import="so.xunta.localcontext.LocalContext"%>

<%@page import="so.xunta.ipseeker.IPSeeker"%>

<%@ include file="/inc/_00meta.inc"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

	User user=(User)session.getAttribute("user");
	String cmd=request.getParameter("cmd");
	if("exit".equals(cmd))
	{
		session.invalidate();
		cmd=null;
	}
%>

<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="xunta.so" content="寻TA网">
		<meta name="description" content="寻TA网_跨社区旅游搜人引擎">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>寻TA网_跨社区旅游搜人引擎|XunTa.so|输入"知识点"关键词,寻找可信赖的旅伴,团长,导游,店家,餐馆,车主,博主...</title>
		<link rel="stylesheet" href="<%=basePath%>assets/bootstrap/3.0.0/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="<%=basePath%>assets/bootstrap/3.0.0/css/bootstrap-theme.min.css"/>
		<link rel="stylesheet" href="<%=basePath%>assets/stylesheets/index.css" />

	</head>
	
	<body>
	<%@ include file="jsp/search_resultpage/top_nav_bar.jsp"%>
		<div id="container">
			<div id="header"></div>
			<div id="body">
				
				<form class="form-inline search-form" role="form"
					action="<%=basePath%>psearch" method="get">
					<input type="hidden" name="searchTime" value="30days" />
					<input type="hidden" name="searchMode" value="newest">

					<table width="450" border="0" align="center" cellpadding="2"
						cellspacing="2">
						<tr>

							<td width="700" align="center">
								<img src="<%=basePath%>/assets/images/xunta_logo_small.jpg"
									width="250" height="" align="center"><span id="test">(测试版)</span>
							</td>
						</tr>
						<tr>
							<td>
								<div class="row">
									<div class="col-lg-4">
										<div class="input-group">
											<label class="sr-only" for="keyword">
												搜索关键词
											</label>
											<input style="width: 450px;"
												class="form-control search-keywords" type="text"
												name="searchKeywords"
												placeholder="输入(多个)知识点关键词，寻找旅游达人、组织者、导游及结伴消息。"
												value="${pageData.searchKeywords}" />
											<span class="input-group-btn">
												<button id="search" class="btn btn-primary" type="submit">
													.SO
												</button>
											</span>
										</div>
										<!-- /input-group -->
									</div>
									<!-- /.col-lg-4 -->
								</div>
							</td>
						</tr>
					</table>
				</form>
				<br />
		
					<div id="text" align="center" color="#FF5809">大家在搜什么</div>
					
					<div id="history_searchWord">
					</div>
			</div>
			<div id="footer">
				<div id="logo" align="center">
					<img src="<%=basePath%>assets/images/LvYoulogo/lvyoulogo.jpg" width="1024" height="60" border="0" usemap="#Map">
					<map name="Map">
					  <area shape="rect" coords="9,16,62,45" href="http://www.ctrip.com/?utm_source=baidu&utm_medium=cpc&utm_campaign=baidu81&campaign=CHNbaidu81&adid=index&gclid=&isctrip=T " target="_blank">
					<area shape="rect" coords="65,15,117,45" href="http://www.qunar.com/" target="_blank">
					<area shape="rect" coords="126,18,181,44" href="http://www.elvxing.net/" target="_blank">
					  <area shape="rect" coords="191,17,253,44" href="http://www.lvmama.com/" target="_blank">
					  <area shape="rect" coords="260,16,293,45" href="http://www.lvye.cn/" target="_blank">
					<area shape="rect" coords="301,18,353,45" href="http://www.qyer.com/" target="_blank">
					  <area shape="rect" coords="363,15,431,45" href="http://www.sanfo.com/" target="_blank">
					    <area shape="rect" coords="439,18,495,44" href="http://travel.sina.com.cn/" target="_blank">
					    <area shape="rect" coords="502,18,550,44" href="http://www.weibo.com" target="_blank">
					    <area shape="rect" coords="561,17,618,45" href="http://www.tuniu.com/" target="_blank">
					      <area shape="rect" coords="623,22,688,47" href="http://www.douban.com/" target="_blank"><area shape="rect" coords="692,20,747,46" href="http://www.traveler365.com/" target="_blank">
					<area shape="rect" coords="752,19,798,43" href="http://bendi.niwota.com/" target="_blank">
					  <area shape="rect" coords="804,17,847,45" href="http://www.tianya.cn/" target="_blank"><area shape="rect" coords="852,21,917,45" href="http://bbs.wanjingchina.com/forum.php" target="_blank">
					<area shape="rect" coords="921,22,982,45" href="http://bbs.163.com/" target="_blank">
					<area shape="rect" coords="987,18,1014,45" href="http://www.doyouhike.net/" target="_blank">
					</map>
				</div>
				<div id="info">
					<div align="center">
						<font size="2">Powered by <a href="http://www.aigine.com"
							target="_blank">Aigine InfoTech Co.</a> </font>
					</div>
					<div align="center">
						<a href="http://www.miitbeian.gov.cn/"><font size="1">沪ICP备13012815-1号</font>
						</a>
					</div>
					<div align="center">
						<font size="2">© XunTa.so 2014</font>
					</div>
				</div>
			</div>
		</11111>
				<script
			src="<%=basePath%>assets/javascripts/jquery-1.10.2.min.js">
</script>
		<script src="<%=basePath%>assets/bootstrap/3.0.0/js/bootstrap.js">
</script>

		<script type="text/javascript"
			src="<%=basePath%>assets/javascripts/application.js">
</script>

<script type="text/javascript">
  		//创建websocket
  		var count=0;

	    var webSocket = new WebSocket('ws://192.168.234.129:8080/xunta/ws/websocket');
	    
	    webSocket.onerror = function(event) {
	  		console.log("connet websocket failure");
	    };
	 
	    webSocket.onopen = function(event) {
	    	console.log("connet websocket succeed");
	    };
	    
		 webSocket.onclose = function(event)
		 {
			 console.log(event);
		 }
	 	
	    function stringToJson(stringValue) 
		{ 
			eval("var theJsonValue = "+stringValue); 
			return theJsonValue; 
		}
	    function json2str(o) { 
			var arr = []; 
			var fmt = function(s) { 
			if (typeof s == 'object' && s != null) return json2str(s); 
			return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s; 
			} 
			for (var i in o) arr.push("'" + i + "':" + fmt(o[i])); 
			return '{' + arr.join(',') + '}'; 
		} 
	   
	    webSocket.onmessage = function(event) {
	     	console.log(event.data);
	   	 	var json_msg=stringToJson(event.data);
	   	 	//console.log(json_msg.searchKeywords);
	   	 	var skw=encodeURI(json_msg.searchKeywords);
	   	 	//console.log(skw);
	   	 	var str="<div class='ip_search' id="+(count++)+" style=\"display:none\">"+json_msg.ipaddress+"&nbsp;&nbsp;<a href=\"psearch?searchTime=30days&searchMode=newest&searchKeywords="+skw+"\">"+json_msg.searchKeywords+"</a>&nbsp;&nbsp;&nbsp;"+json_msg.time+"</div>";
	     	//console.log(str);
	   	 	$("#history_searchWord").prepend(str);
	   	 	
	   	 	//15
	   	 	var divs= $("#history_searchWord").children();
	   	 	if(divs.size()>15)
	   	 	{
	   	 		$("#history_searchWord div:last").remove();
	   	 	}

			//console.log(str);	
	     	var str_id="#"+(count-1);
				$(str_id).fadeIn(2000);
	     
	    };

	      <%-- var userName="${user.xunta_username}";
	      var cmd="<%=cmd%>";
	      console.log("用户名："+userName+"在线");
	      console.log("cmd:"+cmd); --%>
	      console.log("欢迎试用寻Ta搜人引擎……");

  </script>

	</body>

</html>