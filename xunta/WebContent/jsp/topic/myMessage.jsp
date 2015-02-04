<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	
	<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
	<script type="text/javascript">
			//window.mydomain="http://"+document.domain+":21280/xunta/";
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
			        cmd:'joinTopic'
			    };
				
				$.post("${pageContext.request.contextPath}/servlet/topic_service",parameters,function(res,status){
					if(status=="success")
					{
						//同意成功后，删除该消息？因为没必要了
						 var to_remove_node_ul =  e.target.parentNode.parentNode;
						 var ul_node =document.body.removeChild(to_remove_node_ul);
						 //通知邀请人，
					}
					else{
						console.log("同意邀请失败");
					}
				});
			};

			function notAgreeToJoinTopic(e)
			{
				var pid = e.target.getAttribute("topic_pid");
				//请求参数
			    var parameters={
			        id:pid,
			        cmd:'notAgreeToJoinTopic'
			    };
				
				$.post("${pageContext.request.contextPath}/servlet/topic_service",parameters,function(res,status){
					if(status=="success")
					{
						//将当前的消息删除
					    var to_remove_node_ul =  e.target.parentNode.parentNode;
					    var ul_node =document.body.removeChild(to_remove_node_ul);
					}
				});
			};
			
	</script>