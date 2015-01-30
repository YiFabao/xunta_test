<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

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
               <div class="mainBox">
                    <div class="content">
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

<div class="msgAlert" id="bar_message" style="display:block">你有4条私信</div>
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

	function chat(obj){
		var topicId = $(obj).attr("topicId");
		//通过topicId 获取 {topicId:"ad023424s",topicName:"张三",logoUrl:"/user/logo/1.jpg"}
		$.post("${pageContext.request.contextPath}/servlet/topic_service",{
			cmd:"getTopicByTopicId",
			topicId:topicId
		},function(res,status){
			var topicName = res.topicName;
			var imgSrc = res.logoUrl;
			console.log("话题名:"+topicName);
			console.log("logoUrl:"+imgSrc);
			//触发聊天事件
			startChat(topicId,topicName,imgSrc);
		});
	}
	/* //元素定义 .chat 并设置一个属性 topicId==>用户点击该元素触发聊天
	$(".chat").click(function(event){
		//拿到topicId==>查询topic,话题下的用户列表
		var topicId = $(this).attr("topicId");
		//通过topicId 获取 {topicId:"ad023424s",topicName:"张三",logoUrl:"/user/logo/1.jpg"}
		$.post("${pageContext.request.contextPath}/servlet/topic_service",{
			cmd:"getTopicByTopicId",
			topicId:topicId
		},function(res,status){
			var topicName = res.topicName;
			var imgSrc = res.logoUrl;
			console.log("话题名:"+topicName);
			console.log("logoUrl:"+imgSrc);
			//触发聊天事件
			startChat(topicId,topicName,imgSrc);
		});
	}); */
	
	//开始聊天
	function startChat(topicId,topicName,imgSrc){
		//拿到自己的id==>查询自己
		var userId ="${sessionScope.user.id}";
		//查看当前话题下的聊天窗口是否存在,根据topicId
		if(check_topic_is_exist_in_topicList(topicId))//话题存在
		{
			console.log("聊天窗口已经存在，只做一下切换显示,显示聊天框，隐藏消息提示框");
			changeShowState(topicId);
		}
		else{
			//添加话题列表项
			addTopicItemOnTopicList(topicId,topicName,imgSrc);
			console.log("聊天窗口不存在,需发发送请求显示聊天窗口");
			//获取聊天框 通过传递参数 topicId,userId
			doPostTogetDialogueBox(topicId,userId);
			//请求到窗口后，将对应的topicId 添加到topicIdArray中
			topicIdArray.push(topicId);
			//console.log(topicIdArray);
		}
		showWebimPage();//显示聊天框 
	}
	
	//获取聊天框  通过传递参数 topicId,userId,获取的聊天框要添加事件
	function doPostTogetDialogueBox(topicId,userId){
		$.post("${pageContext.request.contextPath}/servlet/topic_service",{
			cmd:"joinTopic",
			topicId:topicId,
			userId:userId
			},
			function(result,state){
				$("#webim_page").append(result);
				//切换显示
				changeShowState(topicId);
				//添加退出按钮点击事件
				var dialogueBox = getDialogueByBoxId(topicId);
				//给指定的dialogueBox 添加退出按钮点击处理事件
				addEventlisteneron_btn_exit(dialogueBox);
				//添加发送消息事件，监听webim消息输入框
				addEventListenerOnMsgInputBox(dialogueBox);
				//显示历史消息
				showHistoryMessages(dialogueBox);
			});
	};
	//在聊天框显示历史消息
	function showHistoryMessages(dialogueBox){
		//获取对应topicId 的聊天框的历史消息数
		console.log(dialogueBox);
		var topicId = $(dialogueBox).find("div.private_dialogue_body").attr("topicId");
		var currentMsgCount = $(dialogueBox).find("div.private_dialogue_body").attr("msg_count");
		console.log("获取历史消息数 注释掉了，在chat.jsp 169行");
		//请求服务器获取历史消息
		//getHistoryMessage(topicId,currentMsgCount);//调用binbin的接口,无返回值，有一个回调函数，数据获取在回调函数中，具体的数据显示在那个回调函数中做
		//console.log("当前的历史消息数："+currentMsgCount+"  话题id:"+topicId);
		//console.log("显示历史消息 showHistoryMessages");
	};
	
	
	//给指定的dialogueBox 添加退出按钮点击处理事件
	function addEventlisteneron_btn_exit(dialogueBox){
		$(dialogueBox).find("input.btn_exit").click(function(e){
			var webim_page = document.getElementById("webim_page");
			var bar_message = document.getElementById("bar_message");
			webim_page.style.display = "none";
			bar_message.style.display = "block";
		});
	}
	
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
		p_node.innerHTML ="话题:"+topicName;
		
		//将li_node 添加到	div.topic_group_list ul下
		$("div.topic_group_list ul").append(li_node);
		
		//添加事件
		addEventlistenerOn_li_node(li_node);
	};
	
	
	//消息输入框监听事件函数，调用则添加事件
	function  addEventListenerOnMsgInputBox(dialogueBox){
		console.log($(dialogueBox).find("textarea.msg_input"));
		$(dialogueBox).find("textarea.msg_input").keydown(function(event){
	        if(event.keyCode==13)
	        {
		        
	        	//发送消息需要传的参数，话题ID,消息id,发送人id,联系人id数组,消息,时间,发送人昵称
	        	//1.话题id 上面已经获取
	        	//2获取联系人　id[]
	        	//获取连系人的id 数组
	        	var contactsIdArray = getContactsArray(dialogueBox);
	        	//3消息 
	        	var msg=this.value;
	        	//4发送人id
	        	var fromUserId ="${sessionScope.user.id}";
	        	//5发送人昵称
	        	var fromUserName = "${sessionScope.user.xunta_username}";
	        	//6 topicId
	        	var topicId = $(dialogueBox).attr("boxId");
	        	//7 消息id
	        	var msgId = fromUserId+""+new Date().getTime();
	        	
	        	console.log("话题id:"+topicId);
	        	console.log("发送人id:"+fromUserId);
	        	console.log("发送人昵称:"+fromUserName);
	        	console.log("消息:"+msg);
	        	console.log("消息id:"+msgId);
	        	console.log("联系人:"+contactsIdArray);
	        	sendMsg(topicId,msgId,fromUserId,fromUserName,msg.toString().trim(),contactsIdArray); 
	        	//清空聊天框 
	        	this.value ="";
	        	//广播告诉其他联系人,该用户上线了
	        }
		});
	};
	
	//获取联系人id数组
	function getContactsArray(dialogueBox){
		var userIds = $(dialogueBox).find("div.contacts li button");
    	var contacts=new Array();
    	for(var i=0;i<userIds.length;i++)
    	{
    		var userId_node=userIds[i];
    		contacts.push(userId_node.getAttribute("userId"));
    	}
    	return contacts;
	}
	
	//显示消息聊天框，隐藏消息提示框
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
		if(active_li_node!=null&&active_li_node!=topic_group_li_toshow)
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
	   	  
	   	  //div.mainBox 滚动条置底  滚动,注意获取的是对应聊天框的滚动条
	   	  var mainBoxNode = $(dialogueBox).find("div.mainBox:first-child")[0];
	   	  setBottom(mainBoxNode);	 
	   	};
	   	
	   	//历史消息回调函数
	   	window.chatBoxHistoryMsg=function(json)
	   	{
	   		//在消息框显示历史消息
	   		webimHandle(json);
	   	}
	   	
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
	  
	  //获取未读消息数
	  
</script>