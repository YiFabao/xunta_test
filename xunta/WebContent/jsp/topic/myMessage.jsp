<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>我的消息</title>
</head>
<body>
	<h3>未读消息</h3>
	<hr>
	<c:forEach var="messageItem" items="${requestScope.messageAlertList}" >
		<c:if test="${messageItem.isHandle==0}">
			<ul>
				<li>有人邀请您参与该话题：</li>
				<li>${messageItem.authorId }</li>
				<li>${messageItem._fromUserId }</li>
				<li>${messageItem._fromUsername}</li>
				<li>${messageItem.datetime }</li>
				<li>${messageItem.topicId }</li>
				<li>${messageItem.isHandle}</li>
				<li>${messageItem.topicContent}</li>
				<li>
					<button class="y">同意</button>
					<button class="n">不同意</button>
				</li>
			</ul>
		</c:if>
	</c:forEach>
	<h3>已读消息</h3>
	<hr>
	<c:forEach var="messageItem" items="${requestScope.messageAlertList}" >
		<c:if test="${messageItem.isHandle==1}">
			<ul>
				<li>有人邀请您参与该话题：</li>
				<li>${messageItem.authorId }</li>
				<li>${messageItem._fromUserId }</li>
				<li>${messageItem._fromUsername}</li>
				<li>${messageItem.datetime }</li>
				<li>${messageItem.topicId }</li>
				<li>${messageItem.isHandle}</li>
				<li>
					<button id="y" topicId="${messageItem.topicId}">同意</button>
				</li>
			</ul>
		</c:if>
	<script src="util.js"></script>
	<script type="text/javascript">
			var btn_y = document.getElementById("y");
			
			btn_y.addEventListener("click",agreeToJoinTopic);
			
			
			function joinTopic(topicId)
			{
				//请求参数
			    var parameters={
			        topicId:topicId,
			        userId:_currentUserId,
			        userName:_currentUserName,
			        cmd:'joinTopic'
			    };
			  
			    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
			  //以下是callback函数的定义
			    function mycallback(){
			        if(xmlHttp.readyState==4)
			        {
			            if(xmlHttp.status==200)
			            {
			            	//接收的是当前话题下的，联系人列表包括自己
			            	var responseData=xmlHttp.responseText;
			            	if(responseData!=""&&responseData!=null)
			            	{
			            		var contacts= JSON.parse(responseData);
			     	           	var contacts_list=document.getElementsByClassName("contacts_list")[0];
			     	        	var contacts_list_ul_node=contacts_list.getElementsByTagName("ul")[0];
			     	        	contacts_list_ul_node.innerHTML="";//先赋空
			                    for(var i=0;i<contacts.length;i++)
			                    {
			                       console.log(contacts[i]);
			                 	   createContactsList(contacts[i]);
			                    }
			            	}
			            }
			            else{
			                console.log("添加话题下的新成员，请求没有成功响应:"+xmlHttp.status);
			            }
			        }
			    }
			}
	
	</script>
</body>
</html>