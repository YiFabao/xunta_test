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
.msgAlert{
		width:200px;
		height:48px;
		border-radius:4px;
		border:1px solid #ccc;
		box-shadow: 1px 1px 1px rgba(0,0,0,0.15);
		position:fixed;
		bottom:5px;
		right:5px;
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

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jsp/topic/css/chat_box.css">

<div class="webim_page" style="display:none" id="webim_page">
    <div class="people_list">
        <div class="search webim_search">
            <span class="search_s">
                <input type="text" maxlength="10" value="查找话题或联系人">
                <span class="pos">
                    <a href="javascript:;" title="搜索">f</a>
                </span>

            </span>
        </div>
        <div class="topic_group_list">
           <ul>
          </ul>
      	</div>
    </div>
    <div class="dialogue_box" boxId = "1" style ="display:none">
        <div class="private_dialogue_body" topicId="${requestScope.topic.topicId}" msg_count="0">
            <div class="header">
				<ul>
					<li class="people_logo">
						<img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg" title="张三">
					</li>
					<li class="xunta_username">
						${requestScope.publisher.xunta_username }
					</li>
					<li class="topicName">
						话题:${requestScope.topic.topicName }
					</li>
					<li>
						<input type="button" value=" X " class="btn_exit">
					</li>
				</ul>        		
            </div>
            <!-- 联系人图像 -->
            <div class="contacts"> 
            	参与人：
	            <ul>
	            	<c:forEach items="${requestScope.memberList }" var="member">
	            	<li>
						<button userId = ${member.id } class="userId"> ${member.xunta_username}</button>
						<%-- <img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg" title="张三"> --%>
					</li>
	            	</c:forEach>
	
				</ul>
            </div>
            <div class="msg_bubble">
               <div id="mainBox" style="overflow:auto">
                    <div id="content">
                        <div class="msg_bubble_list">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="private_send_box">
            <div class="sendbox_area">
                <textarea class="msg_input" placeholder="按回车键发送信息"></textarea>
            </div>
        </div>
    </div>
</div>

<div class="msgAlert" id="bar_message" style="display:none">你有4条私信</div>

<script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/navbar.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/websocket.js"></script>
<script>

	//看一下聊天框切换的效果
	var topicIdArray = new Array();//话题列表的话题容器
	//定义查询topicIdArray是否存在某个话题Id的方法
	function check_topic_is_exist_in_topicList(topicId)
	{
		if(topicIdArray.indexOf(topicId)!=-1){return true;}//1:存在 ,-1:不存在
		else{
			return false
		}
	}

	$(".searched_topic_item").click(function(event){
		//拿到topicId==>查询topic,话题下的用户列表
		var topicId = $(this).attr("topicId");
		var topicName = $(this).find("td.topic_name").attr("value");
		var imgSrc = $(this).find("img").attr("src");
		//拿到自己的id==>查询自己
		var userId ="${sessionScope.user.id}";
		//查看当前话题下的聊天窗口是否存在
		if(check_topic_is_exist_in_topicList(topicId))//话题存在
		{
			console.log("聊天窗口已经存在，只做一下切换显示");
			changeShowState(topicId);
		}
		else{
			//添加话题列表项
			addTopicItemOnTopicList(topicId,topicName,imgSrc);
			console.log("聊天窗口不存在,需发发送请求显示聊天窗口");
			$.post("${pageContext.request.contextPath}/servlet/topic_service",{cmd:"joinTopic",topicId:topicId,userId:userId},function(result,state){
				$("#webim_page").append(result);
				//切换显示
				changeShowState(topicId);
				//添加退出按钮点击事件
				var dialogueBox = getDialogueByBoxId(topicId);
				$(dialogueBox).find("input.btn_exit").click(function(e){
					var webim_page = document.getElementById("webim_page");
					var bar_message = document.getElementById("bar_message");
					webim_page.style.display = "none";
					bar_message.style.display = "block";
				});
				//添加发送消息事件
				//监听webim消息输入框
				console.log($(dialogueBox).find("textarea.msg_input"));
   				$(dialogueBox).find("textarea.msg_input").keydown(function(event){
   			        if(event.keyCode==13)
			        {
   			        	
			        	//发送消息需要传的参数，话题ID,消息id,发送人id,联系人id数组,消息,时间,发送人昵称
			        	//1.话题id 上面已经获取
			        	//2获取联系人　id[]
			        	var userIds = $(dialogueBox).find("div.contacts li button");
			        	var contacts=new Array();
			        	for(var i=0;i<userIds.length;i++)
			        	{
			        		var userId_node=userIds[i];
			        		contacts.push(userId_node.getAttribute("userId"));
			        	}
			        	//3消息 
			        	var msg=this.value;
			        	//4发送人id
			        	var fromUserId ="${sessionScope.user.id}";
			        	//5发送人昵称
			        	var fromUserName = "${sessionScope.user.xunta_username}";
			        	//7 消息id
			        	var msgId = fromUserId+""+new Date().getTime();
			        	
			        	console.log("话题id:"+topicId);
			        	console.log("发送人id:"+fromUserId);
			        	console.log("发送人昵称:"+fromUserName);
			        	console.log("消息:"+msg);
			        	console.log("消息id:"+msgId);
			        	console.log("联系人:"+contacts);
			        	sendMsg(topicId,msgId,fromUserId,fromUserName,msg.toString().trim(),contacts); 
			        	//清空聊天框 
			        	this.value ="";
			        	
			        	//广播告诉其他联系人,该用户上线了
			        }
   				});
			});
			//请求到窗口后，将对应的topicId 添加到topicIdArray中
			topicIdArray.push(topicId);
			//console.log(topicIdArray);
			
		}
		showWebimPage();//显示聊天框
	});

	//创建话题列表的子元素项，并添加到父节点，点击话题时产生
	function addTopicItemOnTopicList(topicId,topicName,imgSrc){
		var li_node = document.createElement("li");
		var div_node = document.createElement("div");
		var img_node = document.createElement("img");
		var p_node = document.createElement("p");
		
		li_node.appendChild(div_node);
		li_node.appendChild(p_node);
		
		div_node.appendChild(img_node);
		
		//设置属性
		li_node.setAttribute("class","");
		li_node.setAttribute("topic_id",topicId);
		div_node.setAttribute("class","head");
		img_node.setAttribute("src",imgSrc);
		p_node.setAttribute("class","topic_name");
		p_node.innerHTML =topicName;
		
		//将li_node 添加到	div.topic_group_list ul下
		$("div.topic_group_list ul").append(li_node);
		
		//添加事件
		addEventlistenerOn_li_node(li_node);
	}
	
	
	function showWebimPage()
	{
		var bar_message = document.getElementById("bar_message");
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "block";
		bar_message.style.display = "none";
	}
	
	//获取　boxId ＝topicId 的div.dialogue_box节点　
	function getDialogueByBoxId(topicId){
		//console.log($("div.dialogue_box[boxId="+topicId+"]"));
		return $("div.dialogue_box[boxId="+topicId+"]")[0];//转原生javascript对象
	}
	//获取话题列表节点  topic_id =topicId
		function getTopic_group_li_byTopicId(topicId){
		return $("div.topic_group_list ul li[topic_id="+topicId+"]")[0];//转原生javascript对象
	}
	//话题列表，鼠标上移和点击选中的切换效果
	var active_li_node=null;
	var active_dialogueBox=null;
	//切换显示状态
	function changeShowState(topicId){
		//列改话题列表的显示状态
		var topic_group_li_toshow = getTopic_group_li_byTopicId(topicId);
		topic_group_li_toshow.setAttribute("class","active");
		if(active_li_node!=null)
		{
			active_li_node.setAttribute("class","");
		}
      	  active_li_node=topic_group_li_toshow;//更改激活的选项
		//更改聊天框的显示状态
		 var dialogue_box_toshow = getDialogueByBoxId(topicId);
  
	     $(active_dialogueBox).hide();//隐藏前面已经显示的框　
	     active_dialogueBox = dialogue_box_toshow;//切换当前活跃窗口
	     $(active_dialogueBox).show();
		
        
	}
	
	contactsShowStyle();//初始化就要调用，给列表添加初始化状态
	function contactsShowStyle(){
	    var li_nodes=document.querySelector(".topic_group_list")
	        .getElementsByTagName("ul")[0]
	        .getElementsByTagName("li");
	    //遍历每个topic_li 添加相应的事件，改变相应的样式
	 	for(var i=0;i<li_nodes.length;i++)
	    {
	        var li_node=li_nodes[i];
	        if(li_node.getAttribute("class")=="active")
	        {
	            active_li_node=li_node;//记录当前激活的按钮
	            //获取topicId属性值
	            var topicId = li_node.getAttribute("topic_id");
	           	var dialogute_box =getDialogueByBoxId(topicId);
	           	active_dialogueBox=dialogute_box;
	           	$(active_dialogueBox).show();
	           	topicIdArray.push(topicId);
	        }
	       addEventlistenerOn_li_node(li_node);
	    } 
	}
	//给创建的话题列表项添加事件
	function addEventlistenerOn_li_node (li_node){
		 //添加事件
        //鼠标移上的显示效果 lightgray
        li_node.addEventListener("mouseover",function(){
            this.style.backgroundColor="lightgray";
        });
        
        li_node.addEventListener("mouseout",function(){
            this.style.backgroundColor="";
        });
        //点击事件
        li_node.addEventListener("click",function(){
  		  	 this.setAttribute("class","active");
  		  	 if(active_li_node!=null)
  		  	{
             active_li_node.setAttribute("class","");
  		  	}
          	 active_li_node=this;//更改激活的选项
          	 //console.log(active_li_node);
             //获取点击dom节点的 topicId属性值,将当显示的框隐藏,显示对应的topicId的右半聊天框,
             var topicId = $(active_li_node).attr("topic_id");
             var dialogue_box_toshow = getDialogueByBoxId(topicId);
             $(active_dialogueBox).hide();//隐藏前面已经显示的框　
             active_dialogueBox = dialogue_box_toshow;//切换当前活跃窗口
             $(active_dialogueBox).show();
        });
	}
	
	//滚动条置底
	function setBottom(mainBox_node)
	{
		mainBox_node.scrollTop = mainBox_node.scrollHeight;
	}
	
	//显示消息提示框
	function showBarMessage()
	{
		var bar_message = document.getElementById("bar_message");
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "none";
		bar_message.style.display = "block";
	}
	//消息提示框的点击事件监听
	var bar_message = document.getElementById("bar_message");
	bar_message.addEventListener("click",function(event){
		this.style.display = "none";
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "block";
	});
	

	//话题列表的点击事件
	 document.addEventListener("click",function(e){
		 if(e.target.tagName=="TD")return;
		//聊天框是否隐藏起来
		var webim_page = document.getElementById("webim_page");
		if(webim_page.style.display=="none"){
			return;
		}
		else{
			//计算鼠标点击的坐票
			var mouse_x = e.clientX;
			var mouse_y = e.clientY;
			//console.log(mouse_x+"  "+mouse_y);
			//计算聊天框的左顶点坐标
			var webim_page_position = getPositionOnScreen(webim_page);
			//console.log(webim_page_position.x +"  "+webim_page_position.y);
			//如果鼠标点击的地方在聊天窗口之外就隐藏聊天框，显示消息框
			if(!(mouse_x>webim_page_position.x&&mouse_y>webim_page_position.y))
			{
				showBarMessage();//显示消息提醒框
			}
		}
	}); 
	
	 //获取元素在屏幕中的位置
	 function getPositionOnScreen(obj){
	        /**
	         * 获取元素4个角的坐标{x1:,x2,y1,y2}
	         * @param obj 元素
	         */
	        var _this=obj;
	        var x= 0,y=0;
	        do{
	            x+=obj.offsetLeft;
	            y+=obj.offsetTop;
	        }while(obj=obj.offsetParent);

	        return {
	            x:x,
	            y:y
	        }
		}
	 
	//创建消息处理函数
	   	window.webimHandle=function(json){
	   		/**
		   		key:status=====>value:1
				key:topicId=====>value:DEC38294FCADEDFFA835C1D04D2DA2E1
				key:messageId=====>value:21422350893108
				key:senderId=====>value:2
				key:nickname=====>value: oliver
				key:message=====>value:%E4%B8%8A%E6%B5%B7
				key:accepterIds=====>value:1,2
				key:dateTime=====>value:2015-01-27 17:27:38
				key:date=====>value:20150127
				key:time=====>value:172738
	   		*/
	   	  //获取对应topicId 的窗口
	   	  var msgStr="";
	   	  var topicId=json.topicId;
	   	  
	   	  var dialogueBox =getDialogueByBoxId(topicId);
	   	  if(dialogueBox==null)
	   	  {
	   		  return ;
	   	  }
	   	  console.log(dialogueBox);
	   	  var  msg_box = dialogueBox.getElementsByClassName("msg_bubble_list")[0];
	   	  console.log(json);
	   	  //解析json
	  	  var msgId=json.msgId;
	   	  var nickname=json.nickname;
	   	  var sender=json.senderId;
	   	  var accepter=json.accepterIds;
	   	  var msg = decodeURIComponent(json.message);
	   	  var dateTime = json.dateTime;
	   	  //构造html
	   	  msgStr="<p>发送人:"+nickname+"<br/>"+msg+"<br/>"+dateTime+"</p>";
	   	  msgStr+="<hr/>";
	   	  var li_node=document.createElement("li");
	   	  li_node.innerHTML=msgStr;
	   	  msg_box.appendChild(li_node);
	   	  //滚动条置底 */
	   	};
	   	
	    //websocket状态发生变化时触发
	   	window.webimStateChange=function(state){
	   	    if(state=="ok")
	   	    {
	   	        msgManagerReady=true;
	   	        console.log("websocket创建成功");
	   	    }
	   	    else if(state="no"){
	   	        msgManagerReady=false;
	   	        console.log("websocket异常");
	   	        //console.log("重新创建websocket");
	   	       // setTimeout(createWebsocketConnect("${sessionScope.user.id}"),1000);
	   	    }
	   	};  
	   	
	  //创建websocket
		createWebsocketConnect("${sessionScope.user.id}");
</script>
</body>
</html>