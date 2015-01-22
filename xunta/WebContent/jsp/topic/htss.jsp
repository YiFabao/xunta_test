<%@page import="so.xunta.topic.Topic"%>
<%@page import="java.util.List"%>
<%@page import="so.xunta.topic.TopicManagerImpl"%>
<%@page import="so.xunta.topic.TopicManager"%>
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
.topic_item{
		width:340px;
		position:relative;
		float:left;
		margin-bottom:20px;
	}
	ol{
		margin-left:40px;
	}
	.topic_item:hover{
		cursor:pointer;
	}
	
	.hover_box{
		position:absolute;
		background: orange;
	}
</style>
</head>
<body>
	<jsp:include page="include/navbar.jsp"></jsp:include>
	<div align="center">
		<h1>搜索结果</h1>
	</div><br>
	<hr>
	<div align="center">
		<input type="hidden" value="${requestScope.searchWord }" id="searchWord">
		<c:choose>
		       <c:when test="${requestScope.topicList!=null}">
					<h3 align="center">搜索结果</h3>
					<c:forEach var="matched_topic" items="${requestScope.topicList}" varStatus="status">
					<c:if test="${matched_topic.userId!=sessionScope.user.id }">
					<div class="matched_topic">
						<div class="topic_item" userId="${matched_topic.userId }">
							<table border="1" width="300px" cellspacing="0px">
								<tr>
									<td width="48px">
										<img src="${pageContext.request.contextPath }/jsp/topic/images/2.jpg" style="width:48px;height:48px;">
									</td>
									<td width="100px">${matched_topic.userName }</td>
									<td align="right">
										<input type="button" value="邀请">
									</td>
								</tr>
								<tr>
									<td colspan="3">发起相关话题：${matched_topic.relativeTopicList.size() }个</td>
								</tr>
							</table>
						</div>
					</div>
					</c:if>
					</c:forEach>
		       </c:when>
		       <c:otherwise>
		   			搜索结果为0
		       </c:otherwise>
		</c:choose>
	</div>	
<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/navbar.js"></script>
<script>
		
		var topic_items = document.getElementsByClassName("topic_item");
		for(var i=0;i<topic_items.length;i++)
		{
			var topic_item = topic_items[i];
			topic_item.addEventListener("click",function(e){
				var mouse_x = e.clientX;
				var mouse_y = e.clientY;
				//判断是否存在hover_box
				var hover_box = document.getElementsByClassName("hover_box")[0];
				if(hover_box)
				{
					console.log("移除");
					$(".hover_box").get(0).remove();
				}
				
				var myTopicContent = $("#searchWord").attr("value");
				var currentUserId = $(this).attr("userId");
				console.log(myTopicContent);
				console.log(currentUserId);
				$.post("${pageContext.request.contextPath }/jsp/topic/include/hover.jsp",{userId:currentUserId,topicContent:myTopicContent},function(res){
					$("body").append(res);//显示悬浮框
					//获取悬浮框的高度和宽度
					var hover_box = document.getElementsByClassName("hover_box")[0];
					hover_box.style.display="block";
					var hover_box_width = hover_box.clientWidth;
					var hover_box_height = hover_box.clientHeight;
					hover_box.style.left = (mouse_x - 0.5*hover_box_width)+"px";
					hover_box.style.top = (mouse_y - 0.5*hover_box_height)+"px";
					var btn_exit = document.getElementById("btn_exit");
					btn_exit.addEventListener("click",function(){
						var hover_box = document.getElementsByClassName("hover_box")[0];
						hover_box.style.display = "none";
						$(".hover_box").get(0).remove();
					});
				});
			});
		} 
		
	</script>
</body>
</html>