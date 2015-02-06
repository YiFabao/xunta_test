<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="msg" items="${requestScope.topicRequestMsgPlusTopicDetailList}">
	<c:if test="${msg.isAgree=='-1'}">
		<div>
			<p>${msg.fromUserName } 邀请您参与话题　#${msg.topicName }#</p>
			${msg.dateTime }<br/>
			<button topicId="${msg.topicId}" topicName="${msg.topicName}" fromUserId="${msg.fromUserId }" class="receiveInvite">同意</button>
			<button topicId="${msg.topicId}" topicName="${msg.topicName}" fromUserId="${msg.fromUserId }" class="refuseInvite">不同意</button>
		</div>
	</c:if>
	<c:if test="${msg.isAgree=='0'}">
		<div>
			<p>
				${msg.fromUserName } 邀请您参与话题　#${msg.topicName }#<br/>${msg.dateTime }
			</p>
			<span>===>已拒绝</span>
		</div>
	</c:if>
		<c:if test="${msg.isAgree=='1'}">
		<div>
			<p>${msg.fromUserName } 邀请您参与话题　#${msg.topicName }#<br/>${msg.dateTime }</p>
			<span>===>已同意</span>
		</div>
	</c:if>
</c:forEach>
<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script>
	$(document).ready(function(){
		$("button.receiveInvite").click(function(event){
			console.log("点击同意事件");
			//通知邀请人，已经同意其邀请
			var current_datetime2=new Date().format("yyyy-MM-dd hh:mm:ss");
			var topicId=$(this).attr("topicId");
			var topicName=$(this).attr("topicname");
			var fromUserId=$(this).attr("fromUserId");
			var parameters={
					cmd:"agree_to_join_topic",
					fromUserId:"${sessionScope.user.id}",
					fromUserName:"${sessionScope.user.xunta_username}",//该fromUserName是返回消息的
					topicId:topicId,
					topicName:topicName,
					dateTime:current_datetime2
			};
			var parametersStr=JSON.stringify(parameters);
			var regx = new RegExp("\"","g");
			var jsonStr = parametersStr.replace(regx,"\'");
			inviteFriend("["+fromUserId+"]",jsonStr);//此处的fromUserId是对方的
			chat(this);
			//将该条消息替换成 已处理的样式
			var div_node=$(this).parent("div")[0];
			console.log(div_node);
			$(div_node).append("<span>===>已同意</span>");
			$(this).parent("div").find("button").remove();
			//弹出聊天框
			console.log("同意后要弹出聊天框");
			//将对应的toUserId,topicId更改话题邀请信息的状态　,并执行参与话题流程
			$.post(contextPath+"/servlet/topic_service",{
				cmd:"receiveInvite",
				toUserId:"${sessionScope.user.id}",
				topicId:topicId
			},function(res,status){
				console.log(status);
				if(status=="success")
				{
					console.log("话题邀请信息改为已同意成功");
				}else{
					console.log("话题邀请信息改为已同意失败");
				}
			});
		});	
		
		$("button.refuseInvite").click(function(event){
			console.log("点击不同意事件");
			var current_datetime2=new Date().format("yyyy-MM-dd hh:mm:ss");
			var div_node=$(event.target).parent("div")[0];
			console.log(div_node);
			$(div_node).append("<span>===>已拒绝</span>");var topicId=$(this).attr("topicId");
			$(this).parent("div").find("button").remove();
			var topicName=$(this).attr("topicname");
			var fromUserId=$(this).attr("fromUserId");
			
			var parameters={
					cmd:"refuseInvite",
					fromUserId:"${sessionScope.user.id}",
					fromUserName:"${sessionScope.user.xunta_username}",//该fromUserName是返回消息的
					topicId:topicId,
					topicName:topicName,
					dateTime:current_datetime2
			};
			var parametersStr=JSON.stringify(parameters);
			var regx = new RegExp("\"","g");
			var jsonStr = parametersStr.replace(regx,"\'");
			inviteFriend("["+fromUserId+"]",jsonStr);
			
			//将对应的toUserId,topicId更改话题邀请信息的状态　
			$.post(contextPath+"/servlet/topic_service",{
				cmd:"refuseInvite",
				toUserId:currentUserId,
				topicId:topicId
			},function(res,status){
				console.log(status);
				if(status=="success")
				{
					console.log("话题邀请信息改为已拒绝成功");
				}else{
					console.log("话题邀请信息改为已拒绝失败");
				}
			});
		});	
	});
</script>

    

