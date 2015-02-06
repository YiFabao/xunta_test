<%@page import="so.xunta.entity.User"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 网页头样式 -->
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
	<h1 align="center">发起话题</h1>
	
		<input type="hidden" name="userId" value="${sessionScope.user.id }">
		<input type="hidden" name="userName" value="${sessionScope.user.xunta_username }">
		<input type="hidden" name="userLogoUrl" value="/usrlogo/1.jpg">
		<div class="topic_input_box" align="center">
			<table>
				<tr>
					<td>话题:</td>
					<td><input type="text" size="78px" id="topic_name" name="topicName"></td>
				</tr>
				<tr>
					<td>话题描述:</td>
					<td>
						<textarea rows="5" cols="80" id ="topic_content" name="topicContent"></textarea>
					</td>
				</tr>
			</table>
			<input id="btn_publish" type="button" value="发　布">
		</div>
	<hr/>
	
	<c:if test="${requestScope.myTopic!=null}">
	<div align="center" topicId="${requestScope.myTopic.topicId}" topicName="${requestScope.myTopic.topicName }" topicContent="${requestScope.myTopic.topicContent}" id="myTopic">
		<table border="1" cellspacing="0px" color="#ccc" cellpadding="0px" width="1024px" >
			<tr>
				<td width="48px">
					<img src="${pageContext.request.contextPath }/jsp/topic/images/1.jpg" style="width:48px;height:48px;">
				</td>
				<td width="100px">${requestScope.myTopic.userName }</td>
				<td width="">话题:${requestScope.myTopic.topicName }</td>
				<td width="100px">参与人数:${requestScope.myTopic.join_people_num}</td>
			</tr>
			<tr>
				<td colspan="4">
					${requestScope.myTopic.topicContent}
				</td>
			</tr>
			<tr>
				<td colspan="4" align="right">
					${requestScope.myTopic.createTime}
				</td>
			</tr>
		</table>
	</div>
	<hr>
	</c:if>	
	<c:if test="${requestScope.matchedTopicList!=null}">
		<h3 align="center">匹配的人</h3>
		<c:forEach var="matched_topic" items="${requestScope.matchedTopicList}" varStatus="status">
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
							<input type="button" value="邀请" class="btn_invite" userId="${matched_topic.userId }" userName="${matched_topic.userName }">
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
	</c:if>
<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script>
	var topic_items = document.getElementsByClassName("topic_item");
	for(var i=0;i<topic_items.length;i++)
	{
		var topic_item = topic_items[i];
		topic_item.addEventListener("click",function(e){
			if(e.target.getAttribute("type")=="button"){return;}//如果点击的是btn_invite 则不是执行的显示放大的操作
			var mouse_x = e.clientX;
			var mouse_y = e.clientY;
			//判断是否存在hover_box
			var hover_box = document.getElementsByClassName("hover_box")[0];
			if(hover_box)
			{
				console.log("移除");
				$(hover_box).remove();
			}
			
			var topicName = $("#myTopic").attr("topicName");
			var myTopicContent = $("#myTopic").attr("topicContent");
			var currentUserId = $(this).attr("userId");
			console.log(myTopicContent);
			console.log(currentUserId);
			console.log(topicName);
			$.post("${pageContext.request.contextPath }/jsp/topic/include/hover.jsp",{userId:currentUserId,topicName:topicName,topicContent:myTopicContent},function(res){
				
				$("body").append(res);//添加悬浮框
				
				//给放大的悬浮框添加邀请按钮事件
				$("div.hover_box input.btn_invite").click(function(event){
					console.log("放大的事件的邀请信息");
					//var to_userId =$(this).attr(userId);//被邀请人的userId
					var fromUserId = "${sessionScope.user.id}";//邀请人的userId
					var fromUserName = "${sessionScope.user.xunta_username}";//邀请人的userId
					var to_userId =this.getAttribute("userId");//被邀请人的userId
					var to_userName =this.getAttribute("userName");//被邀请人的userId
					var topicId = $("#myTopic").attr("topicId");
					var topicName =$("#myTopic").attr("topicName");
					var topicContent =$("#myTopic").attr("topicContent");
					
					console.log("fromUserId:"+fromUserId);
					console.log("fromUserName:"+fromUserName);
					console.log("to_userId:"+to_userId);
					console.log("to_userName:"+to_userName);
					console.log("topicId:"+topicId);
					console.log("topicName:"+topicName);
					console.log("topicContent:"+topicContent);
					
					var parameters = {
							cmd:"invite",
							topicId:topicId,
							fromUserId:fromUserId,
							fromUserName:fromUserName,
							topicName:"${requestScope.myTopic.topicName }",
							topicContent:topicContent,
							to_userId:to_userId,
							to_userName:to_userName
							};
					
					$.post("${pageContext.request.contextPath}/servlet/topic_service",parameters,
						function(res,status){
							console.log("邀请状态:"+status+"===>"+res);
							if(status=="success"&&res=="ok")
							{
								//ws通知邀请人
								console.log(JSON.stringify(parameters).toString());
								var parametersStr=JSON.stringify(parameters);
								var regx = new RegExp("\"","g");
								var jsonStr = parametersStr.replace(regx,"\'");
								inviteFriend("["+to_userId+"]",jsonStr);
								alert("邀请成功!!!  别人同意后，在顶部导航栏会看到消息提醒");
							}
							else{
								alert("邀请失败，再试一次");
							}
					});
				});
				
				//获取悬浮框的高度和宽度
				var hover_box = document.getElementsByClassName("hover_box")[0];
				hover_box.style.display="block";//显示
				//定位
				var hover_box_width = hover_box.clientWidth;
				var hover_box_height = hover_box.clientHeight;
				if(mouse_x - 0.5*hover_box_width<0)
				{
					hover_box.style.left="0px";
				}else if(mouse_x+0.5*hover_box>document.body.clientWidth)
				{
					hover_box.style.right=document.body.clientWidth+"px";
				}
				else{
					hover_box.style.left = (mouse_x - 0.5*hover_box_width)+"px";
				}
				
				if(mouse_y - 0.5*hover_box_height<0)
				{
					hover_box.style.top="0px";
				}
				else{
					hover_box.style.top = (mouse_y - 0.5*hover_box_height)+"px";
				}
				
				//点击退出按钮事件
				var btn_exit = document.getElementById("btn_exit");
				btn_exit.addEventListener("click",function(){
					var hover_box = document.getElementsByClassName("hover_box")[0];
					hover_box.style.display = "none";
					$(hover_box).remove();
				});
			});
		});
	} 
	
	$(".btn_invite").click(function(event){
		console.log("用户点击邀请");
		//获取需要的参数
		//邀请人userId
		//邀请人的userName
		//被邀请人的userId
		//邀请人的topicId
		//邀请人的话题内容
		var fromUserId = "${sessionScope.user.id}";//邀请人的userId
		var fromUserName = "${sessionScope.user.xunta_username}";//邀请人的userId
		var to_userId =this.getAttribute("userId");//被邀请人的userId
		var to_userName =this.getAttribute("userName");//被邀请人的userId
		var topicId = $("#myTopic").attr("topicId");
		var topicContent =$("#myTopic").attr("topicContent");
		
		console.log("userId:"+fromUserId);
		console.log("userName:"+fromUserName);
		console.log("to_userId:"+to_userId);
		console.log("topicId:"+topicId);
		console.log("topicContent:"+topicContent);
		console.log("to_userName:"+to_userName);
		
		var parameters = {
				cmd:"invite",
				topicId:topicId,
				fromUserId:fromUserId,
				fromUserName:fromUserName,
				topicName:"${requestScope.myTopic.topicName }",
				topicContent:topicContent,
				to_userId:to_userId,
				to_userName:to_userName
				};
		
		$.post("${pageContext.request.contextPath}/servlet/topic_service",parameters,
			function(res,status){
				console.log("邀请状态:"+status+"===>"+res);
				if(status=="success"&&res=="ok")
				{
					//ws通知邀请人
					console.log(JSON.stringify(parameters).toString());
					var parametersStr=JSON.stringify(parameters);
					var regx = new RegExp("\"","g");
					var jsonStr = parametersStr.replace(regx,"\'");
					inviteFriend("["+to_userId+"]",jsonStr);
					alert("邀请成功!!!   别人同意后，在顶部导航栏会看到消息提醒");
				}
				else{
					alert("邀请失败，再试一次");
				}
		});
		
	});
	
	$("#btn_publish").click(function(){
		
		var topic_name = $("#topic_name").val();
		var topic_content = $("#topic_content").val();
		var userId ="${sessionScope.user.id}";
		var userName ="${sessionScope.user.xunta_username}";
		var userLogoUrl =$("input[name=userLogoUrl]").val();
		console.log("topic_name:"+topic_name);
		console.log("topic_content:"+topic_content);
		console.log("userId:"+userId);
		console.log("userName:"+userName);
		console.log("userLogoUrl:"+userLogoUrl);
		
		if(topic_name=="")
		{
			alert("话题　及　话题描述不能为空");
			return;
		}
		var parameters={
				userId:userId,
				userName:userName,
				userLogoUrl:userLogoUrl,
				topicName:topic_name,
				topicContent:topic_content
		};
		$.post("${pageContext.request.contextPath}/servlet/topic_service?cmd=fqht",parameters,function(res,status){
			$("#container_all").empty();
			$("#container_all").append(res);
		});
		//$("#form1").submit();
	});
</script>
