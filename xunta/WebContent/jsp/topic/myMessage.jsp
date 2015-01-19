<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>我的消息</title>
</head>
<body>
	<h3>未读消息</h3>
	<hr>
	<c:forEach var="messageItem" items="${requestScope.messageAlertList}" >
		<c:if test="${messageItem.isRead==0}">
			<ul>
				<li>
				有人邀请您参与该话题：<br>
				<table>
						<tr>
							<td>邀请人id:</td>
							<td>${messageItem.authorId }</td>
						</tr>
						<tr>
							<td>邀请人昵称:</td>
							<td>${messageItem._fromUsername}</td>
						</tr>
						<tr>
							<td>邀请时间:</td>
							<td>${messageItem.datetime }</td>
						</tr>
						<tr>
							<td>邀请你参与的话题:</td>
							<td>${messageItem.topicContent}</td>
						</tr>
					</table>
				</li>
				<li>
					<button id="y" topicId="${messageItem.topicId}" topic_pid="${messageItem.id}">同意</button>
					<button id="n" topicId="${messageItem.topicId}" topic_pid="${messageItem.id}">不同意</button>
				</li>
			</ul>
		</c:if>
	</c:forEach>
	<h3>已读消息</h3>
	<hr>
	<c:forEach var="messageItem" items="${requestScope.messageAlertList}" >
		<c:if test="${messageItem.isRead==1}">
			<ul>
				<li>
					有人邀请您参与该话题：<br>
				<table>
						<tr>
							<td>邀请人id:</td>
							<td>${messageItem.authorId }</td>
						</tr>
						<tr>
							<td>邀请人昵称:</td>
							<td>${messageItem._fromUsername}</td>
						</tr>
						<tr>
							<td>邀请时间:</td>
							<td>${messageItem.datetime }</td>
						</tr>
						<tr>
							<td>邀请你参与的话题:</td>
							<td>${messageItem.topicContent}</td>
						</tr>
					</table>
				<li>
					<button id="y" topicId="${messageItem.topicId}" topic_pid="${messageItem.id}">同意</button>
					<button id="n" topicId="${messageItem.topicId}" topic_pid="${messageItem.id}">不同意</button>
				</li>
			</ul>
		</c:if>
	</c:forEach>
	<script type="text/javascript">
			window.mydomain="http://"+document.domain+":21280/xunta/";
			var btn_y = document.getElementById("y");
			var btn_n = document.getElementById("n");
			if(btn_y!=null){
				btn_y.addEventListener("click",agreeToJoinTopic);
			}
			if(btn_n!=null){
				btn_n.addEventListener("click",notAgreeToJoinTopic);
			}
			
			
			
			function agreeToJoinTopic(e)
			{
				var topicId = e.target.getAttribute("topicId");
				var pid = e.target.getAttribute("topic_pid");
				//请求参数
			    var parameters={
					id:pid,
			        topicId:topicId,
			        userId:'${sessionScope.user.id}',
			        userName:'${sessionScope.user.xunta_username}',
			        cmd:'agreeToJoinTopic'
			    };
			    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
			    var to_remove_node_ul =  e.target.parentNode.parentNode;
			    var ul_node =document.body.removeChild(to_remove_node_ul);
			  //以下是callback函数的定义
			    function mycallback(){
			        if(xmlHttp.readyState==4)
			        {
			            if(xmlHttp.status==200)
			            {
			            	
			            }
			            else{
			                console.log("添加话题下的新成员，请求没有成功响应:"+xmlHttp.status);
			            }
			        }
			    }
			}
			
			function notAgreeToJoinTopic(e)
			{
				var pid = e.target.getAttribute("topic_pid");
				//请求参数
			    var parameters={
			        id:pid,
			        cmd:'notAgreeToJoinTopic'
			    };
			    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
			    //将当前的消息删除
			    var to_remove_node_ul =  e.target.parentNode.parentNode;
			    var ul_node =document.body.removeChild(to_remove_node_ul);
			  //以下是callback函数的定义
			    function mycallback(){
			        if(xmlHttp.readyState==4)
			        {
			            if(xmlHttp.status==200)
			            {
			            	
			            }
			            else{
			                console.log("添加话题下的新成员，请求没有成功响应:"+xmlHttp.status);
			            }
			        }
			    }
			}
			
			
			
			//{name:"张三",mytopic:"话题"}==>name=张三&mytopic=话题==>并url编码，以便给xhr传参
			function toDomString(json){
			    var domString="";
			    for(var p in json)//p为json对象里的属性名
			    {
			        if(domString=="")
			        {
			            domString+=(p+"="+json[p]);
			        }
			        else{
			            domString+="&"+p+"="+json[p];
			        }
			    }
			    return encodeURI(domString);
			}


			var xmlHttp=null;//声明一个XHR对象
			//创建一个XHR对象
			function createXMLHttpRequest() {
			  if (window.ActiveXObject) {
			      xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			  } else {
			      if (window.XMLHttpRequest) {
			          xmlHttp = new XMLHttpRequest();
			      }
			  }
			}
			//向服务端发起异步请求:GET（入口函数）,callback为回调函数名称
			function doRequestUsingPOST(url, callback) {
				if(xmlHttp==null)
			  {
			      createXMLHttpRequest();//创建xhr
			  }
			  if(xmlHttp.readyState!=0) {
			      xmlHttp.abort();//初始化
			  }
			  xmlHttp.onreadystatechange = callback;
			  xmlHttp.open("POST",window.mydomain+url + "&timeStamp=" + new Date().getTime(),false);//true表示异步,false表示同步
			  xmlHttp.send(null);
			}

	
	</script>
</body>
</html>