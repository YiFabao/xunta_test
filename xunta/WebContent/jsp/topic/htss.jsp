<%@page import="so.xunta.topic.entity.Topic"%>
<%@page import="java.util.List"%>
<%@page import="so.xunta.topic.model.impl.TopicManagerImpl"%>
<%@page import="so.xunta.topic.model.TopicManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>话题搜索</title>
<!-- 网页头样式 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/topic/css/navbar.css">
<style>
.matched_topic{
	width:1024px;
	margin:auto;
}
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
	<div id="topic_recommend_item" align="center">
	<table border="1" cellspacing="0" color="#ccc">
		<tr>
			<th>话题发起人</th>
			<th>发起人图像</th>
			<th>话题</th>
			<th>话题描述</th>
			<th>参与人数</th>
			<th>话题发起时间</th>
		</tr>
		<c:forEach items="${requestScope.topicList }" var="topic">
		<c:if test="${topic.userId!=sessionScope.user.id }">
		<tr class="searched_topic_item" topicId="${topic.topicId }" >
			<td class="topic_publisher" value="${topic.userName }">${topic.userName }</td>
			<td>
				<img src="${pageContext.request.contextPath }/jsp/topic/images/1.jpg" style="width:48px;height:48px;">
			</td>
			<td class="topic_name" value="${topic.topicName }">${topic.topicName }</td>
			<td width="400px" class="topic_content" value="${topic.topicContent }">
				${topic.topicContent }
			</td>
			<td class="topic_joinPeople_num">${topic.join_people_num }</td>
			<td class="topic_createTime">${topic.createTime }</td>
		</tr>
		</c:if>
		</c:forEach>
	</table>
</div>

<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/navbar.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/websocket.js"></script>
<script>

	
	var flag = false;//标识聊天窗口是否加载
	var topicIdArray = new Array();//话题列表的话题容器
	//定义查询topicIdArray是否存在某个话题Id的方法
	function check_topic_is_exist_in_topicList(topicId)
	{
		return topicIdArray.indexOf(topicId);//1:存在 ,-1:不存在
	}
	$(".searched_topic_item").click(function(event){
		//拿到topicId==>查询topic,话题下的用户列表
		var topicId = $(this).attr("topicId");
		//拿到自己的id==>查询自己
		var userId ="${sessionScope.user.id}";
		//查看当前的聊天窗口是否存在
		if(!flag)
		{
			//post
			$.post("${pageContext.request.contextPath}/servlet/topic_service",{cmd:"joinTopic",topicId:topicId,userId:userId},function(result,state){
				$("body").append(result);
			});
			topicIdArray.push(topicId);
			flag =true;
		}
		else//窗口已经加载
		{
			if(check_topic_is_exist_in_topicList(topicId)==1)//话题存在
			{
				return;
			}
			else{
				//话题不存在,将话题添加到话题列表
/* 				 <li topicId="${requestScope.topic.topicId}">
	                 <div class="head">
	                     <img src="http://tp3.sinaimg.cn/1298064414/50/5617529344/1">
	                 </div>
	                 <p class=topic_name>话题：${requestScope.topic.topicName }</p>
           		  </li> */
           		var li_node = document.createElement(li);
           		li_node.setAttribute("topicId");
			}
			console.log("聊天窗口已加载");
			//添加点击的话题到话题列表
			//获取点击的话题id:
			showWebimPage();
		}
	});
	function showWebimPage()
	{
		var bar_message = document.getElementById("bar_message");
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "block";
		bar_message.style.display = "none";
	}
	
	
</script>
</body>
</html>