<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

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
           <ul></ul>
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

<div class="msgAlert" id="bar_message" style="display:none">
	未读消息数:<span id="number" totalNum="0">0</span>
</div>
<script src="${pageContext.request.contextPath }/jsp/topic/js/websocket.js"></script>
<script>
	//创建websocket
	createWebsocketConnect("${sessionScope.user.id}");
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

		
			/* //改变总的未读消息数
			var readNum = getTopicItemAboutUnreadMsgByTopicId(topicId);
			console.log("点击话题记忆：topicId:"+topicId);
			console.log("对应的未读消息数为："+readNum);
			console.log("减少");
			minusReadMsgNumFromTotalUnreadMsgNum(readNum);
			
			//将点击的话题所对应的未读消息提示数清空
			clearW_new_countByTopicId(topicId); */
			changeMsgNumByTopicId(topicId);//改变消息数的显示　，改变总的未读消息数量，清空对应话题的消息数
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
				console.log("broadcast:"+userId+"===>"+topicId);
				broadcast("${sessionScope.user.id}", topicId);//用户参与聊天，发送广播
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

	
	//获取聊天框，但不马上显示
	function doPostTogetDialogueBoxNotShowImmediately(topicId,userId){
		$.post("${pageContext.request.contextPath}/servlet/topic_service",{
			cmd:"joinTopic",
			topicId:topicId,
			userId:userId
			},
			function(result,state){
				$("#webim_page").append(result);
		/* 		//切换显示
				changeShowState(topicId); */
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
		//console.log(dialogueBox);
		var topicId = $(dialogueBox).find("div.private_dialogue_body").attr("topicId");
		var currentMsgCount = $(dialogueBox).find("div.private_dialogue_body").attr("msg_count");
		//console.log("获取历史消息数 注释掉了，在chat.jsp 169行");
		//请求服务器获取历史消息
		getHistoryMessage(topicId,currentMsgCount);//调用binbin的接口,无返回值，有一个回调函数，数据获取在回调函数中，具体的数据显示在那个回调函数中做
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
	
	//创建话题列表的子元素项，并添加到父节点，点击话题时产生,
	function addTopicItemOnTopicList(topicId,topicName,imgSrc){
		var li_node = document.createElement("li");
		var div_node = document.createElement("div");
		var img_node = document.createElement("img");
		var p_node = document.createElement("p");
		
		var count_node = document.createElement("div");
		var em_node = document.createElement("em");
		em_node.setAttribute("class","W_new_count");
		em_node.setAttribute("topicId",topicId);
		em_node.innerHTML="0";
		em_node.setAttribute("num",0);
		em_node.style.display="none";//因为是点击话题时产生的，所以默认是不显示的
		count_node.appendChild(em_node);
		
	    // <div class="number W_fr"><em class="W_new_count">1</em></div>
		
		li_node.appendChild(div_node);
		li_node.appendChild(p_node);
		li_node.appendChild(count_node);
		
		div_node.appendChild(img_node);
		
		//设置属性
		li_node.setAttribute("class","");
		li_node.setAttribute("topic_id",topicId);
		div_node.setAttribute("class","head");
		img_node.setAttribute("src",imgSrc);
		p_node.setAttribute("class","topic_name");
		p_node.setAttribute("title",topicName);
		p_node.innerHTML ="#"+topicName;
		
		//将li_node 添加到	div.topic_group_list ul下
		$("div.topic_group_list ul").append(li_node);
		
		//添加事件
		addEventlistenerOn_li_node(li_node);
	};
	
	//带4个参数的添加话题表表，这个方法是在用户刚登录的时候，要获取未读消息时调用
	function addTopicItemOnTopicList_program4(topicId,topicName,imgSrc,unreadMsgNum){
		var li_node = document.createElement("li");
		var div_node = document.createElement("div");
		var img_node = document.createElement("img");
		var p_node = document.createElement("p");
		
		var count_node = document.createElement("div");
		var em_node = document.createElement("em");
		em_node.setAttribute("class","W_new_count");
		em_node.setAttribute("topicId",topicId);
		em_node.innerHTML=unreadMsgNum;
		em_node.setAttribute("num",unreadMsgNum);
		em_node.style.display="block";
		count_node.appendChild(em_node);
		
	    // <div class="number W_fr"><em class="W_new_count">1</em></div>
		
		li_node.appendChild(div_node);
		li_node.appendChild(p_node);
		li_node.appendChild(count_node);
		
		div_node.appendChild(img_node);
		
		//设置属性
		li_node.setAttribute("class","");
		li_node.setAttribute("topic_id",topicId);
		div_node.setAttribute("class","head");
		img_node.setAttribute("src",imgSrc);
		p_node.setAttribute("class","topic_name");
		p_node.setAttribute("title",topicName);
		p_node.innerHTML ="#"+topicName;
		
		//将li_node 添加到	div.topic_group_list ul下
		$("div.topic_group_list ul").append(li_node);
		console.log(li_node);
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
		console.log("打印当前的列表："+topicId);
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
	
	
	//获取对应topicId 的未读消息红框框　
	function getW_new_countByTopicId(topicId){
		var W_new_count_em_node=$("em.W_new_count[topicId="+topicId+"]")[0];
		return W_new_count_em_node;
	};
	
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
  		  	 if(active_li_node!=null){
  		  		 active_li_node.setAttribute("class","");
  		     }
          	 active_li_node=this;//更改激活的选项
          	 //console.log(active_li_node);
             //获取点击dom节点的 topicId属性值,将当显示的框隐藏,显示对应的topicId的右半聊天框,
             var topicId = $(active_li_node).attr("topic_id");
             
		   	  //div.mainBox 滚动条置底  滚动,注意获取的是对应聊天框的滚动条
/*               console.log("滚动条置底");
		   	  var dialogueBox = getDialogueByBoxId(topicId);
		   	  var mainBoxNode = $(dialogueBox).find("div.mainBox:first-child")[0];
		   	 
		   	  
		   	  setBottom(mainBoxNode);   */           
             
             
         	changeMsgNumByTopicId(topicId);//改变消息数的显示
/*              //改变总的未读消息数
             var topicItemUnreadMsgNum = getTopicItemAboutUnreadMsgByTopicId(topicId);
             minusReadMsgNumFromTotalUnreadMsgNum(topicItemUnreadMsgNum);
             
             //清除对该话题下未读消息数的提示,用户点击未读消息去除显示的未读消息数
             clearW_new_countByTopicId(topicId); */
             
             //聊天窗口切换
             var dialogue_box_toshow = getDialogueByBoxId(topicId);
             $(active_dialogueBox).hide();//隐藏前面已经显示的框　
             active_dialogueBox = dialogue_box_toshow;//切换当前活跃窗口
             $(active_dialogueBox).show();
        });
	}
	
	//获取某个话题列表项的未读消息数
	function getTopicItemAboutUnreadMsgByTopicId(topicId){
		 var em_W_new_count = getW_new_countByTopicId(topicId);
		 var numStr = $(em_W_new_count).attr("num");
		 console.log("numstr:"+numStr);
		 return parseInt(numStr);
	};
	
	//将放题列表的未读消息数置0并隐藏
	function clearW_new_countByTopicId(topicId)
	{
		 var em_W_new_count = getW_new_countByTopicId(topicId);
		 $(em_W_new_count).attr("num",0);
		 $(em_W_new_count).css("display","none");
	};
	
	//未读消息数清零时，总未读消息减少相应的数
	function minusReadMsgNumFromTotalUnreadMsgNum(readNum)
	{
		//获取当前的总未读消息数
		var totalUnreadMsgNum = getTotalUnReadMsgNum();
		console.log("当前的总未坊消息数是:"+totalUnreadMsgNum);
		//－1
		console.log("totalUnreadMsgNum:"+totalUnreadMsgNum+"====>"+"readNum:"+readNum);
		totalUnreadMsgNum = totalUnreadMsgNum-readNum;
		changeMessageAlertState(totalUnreadMsgNum);
	}
	
	//===================test
/* 	console.log("总的未读消息数：");
	console.log(getTotalUnReadMsgNum());
	console.log("减少5");
	minusReadMsgNumFromTotalUnreadMsgNum(5);
	console.log("减少后的数目:"+getTotalUnReadMsgNum()); */
	
	
	//滚动条置底
	function setBottom(mainBox_node)
	{
		console.log("滚动条的高度:"+mainBox_node.scrollHeight);
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
	var firstClicktBar_message=true;
	var bar_message = document.getElementById("bar_message");
	bar_message.addEventListener("click",function(event){
		this.style.display = "none";
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "block";
		if(firstClicktBar_message) //第一次点击bar_message,则要默认显示第一个话题聊天框
		{
			console.log("第一次点击bar_message");
			console.log("topicIdArray的数组长度:"+topicIdArray.length+"  如果不大于等于1就不正常");
			if(topicIdArray.length>=1)
			{
				changeShowState(topicIdArray[0]);
				firstClicktBar_message=false;//只在当话题窗口加载至少有一个时才将其置为false,不然在没有窗口打开时，用户就点击bar_message就会出错了
			}
		}
		if(topicIdArray.length>=1)
		{
			//将处于active状态的话题项显示的未读数清空
			var active_topicItem_topicId = getTopicIdByActiveTopicListItem();
			changeMsgNumByTopicId(active_topicItem_topicId);
		}
	});
	
	//改变消息数的显示,总数减少，对应话题的清空
	function changeMsgNumByTopicId(topicId)
	{
		var readNum =getTopicItemAboutUnreadMsgByTopicId(topicId); //
		clearW_new_countByTopicId(topicId);
		//将总数减少
		minusReadMsgNumFromTotalUnreadMsgNum(readNum);
	}
	
	//获取处于active状态节点的topicId
	function getTopicIdByActiveTopicListItem()
	{
		var topicId = $("div.topic_group_list ul li[class=active]").attr("topic_id");
		return topicId;
	}
	

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
		
	 	//测试var json ={userId:4,topicId:"DEC38294FCADEDFFA835C1D04D2DA2E1"};
		//接收广播消息
		window.receiveBroadcast = function(json)
		{
			console.log("收到广播消息...");
			console.log("用户上线"+json.userId+"   "+json.topicId);
			//查询当前对应的话题窗口有没有打开
			if(!getDialogueByBoxId(json.topicId)){
				console.log("接收广播消息==>查询当查询当前对应的话题窗口是否已经加载==>未加载");
				return;
			}
			//如果窗口对应的话题聊天窗口存在,那么查询在该聊天窗口下有没有该联系人
			var flag=checkUserIdExistInTopicGroupList(json.userId,json.topicId);
			console.log("flag:"+flag);
			if(!flag){
				//var nickname = searchUser(json.userId);
				$.post("${pageContext.request.contextPath}/servlet/topic_service",{
					cmd:"searchnicknameByUserId",
					userId:json.userId
				},function(res,status){
					var nickname = res.nickname;
					console.log(json.userId+"对应的昵称："+nickname);
					var contact={
							topic_id:json.topicId,
							topic_memberId:json.userId,
							topic_member_name:nickname
					};
					console.log("添加联系人");
					addContactor(contact);
					//在消息框显示新用户进群
					var dialogueBox = getDialogueByBoxId(json.topicId);
					var dateTime = new Date().format("yyyy-MM-dd hh:mm:ss");
					addMsgContetntIntoDialogueBoxAboutNewUserComeIn(dialogueBox,nickname,dateTime);
					
				});
			}
			else{
				console.log("联系人在列表中已经存在");
			} 
		}
		
		
		
		//添加联系人
		function addContactor(contact)
		{
			var dialogueBox = getDialogueByBoxId(contact.topic_id);
			var ul_node = $(dialogueBox).find("div.contacts ul")[0];
			
			var li_node =document.createElement("li");
			var button_node = document.createElement("button");
			button_node.setAttribute("userid",contact.topic_memberId);
			button_node.setAttribute("class","userId");
			button_node.innerHTML=contact.topic_member_name;
			
			li_node.appendChild(button_node);
			ul_node.appendChild(li_node);
		}
		
		
		//查询联系人列表中是否存在某个userId
		function checkUserIdExistInTopicGroupList(userId,topicId)
		{
			//获取topicId的聊天框
			var dialogueBox = getDialogueByBoxId(topicId);
			var userid_btn = $(dialogueBox).find("div.contacts ul li button[userid="+userId+"]");
			console.log(userid_btn[0]);
			if(userid_btn[0]){
				return true;
			}
			else{
				return false;
			};
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
			console.log("发送过来的消息，没有相应的窗口");
		
			handleDialogueIsNull(topicId);	//如果topicId对应的窗口没有加载，则创建话题列表,加载对应的聊天窗口
			//总的消息数+1
			var c_total_num = getTotalUnReadMsgNum();
			changeMessageAlertState(c_total_num+1);
			
	   	  }else{
	   		  //console.log(dialogueBox);
	   		  addMsgContetntIntoDialogueBox(dialogueBox,json);//往消息框里添加一条消息内容
		   	  
		   	  if(isDialogueBoxHidden())
		   	  {
		   		  //处于隐藏状态下的逻辑
		   		  console.log("test隐藏状态");
		   		  //1.总的消息数+1
		   		  var totalUnreadMsgNum = getTotalUnReadMsgNum();
		   		  changeMessageAlertState(totalUnreadMsgNum+1);
		   		  //2.对应话题的消息数+1,并显示数字
		   		  var W_new_count_node = getW_new_countByTopicId(topicId);
		   		  var curr_num=getTopicItemAboutUnreadMsgByTopicId(topicId);
		   		  
		   		  changeW_new_count_unreadMsgNum(topicId,curr_num+1);//改变红块显示的数目　
		   	  }
		   	  else{
		   		  //不处于隐藏状态下的逻辑
		   		  //判断当前的来的消息对应的topicId聊天窗口是否处于活动状态
		   		  var isActive = isTopicItemActive(topicId);
		   		  if(isActive){
		   			  //处于活动态的逻辑
		   			  console.log("处于活动态==>Todo");
		   			  //不做任何处理
		   		  }else{
		   			  //不处于活动状态的逻辑
		   			 console.log("不处于活动态==>");
		   			 //2.对应话题的消息数+1,并显示数字
			   		 var W_new_count_node = getW_new_countByTopicId(topicId);
			   		 var curr_num=getTopicItemAboutUnreadMsgByTopicId(topicId);
			   		 changeW_new_count_unreadMsgNum(topicId,curr_num+1);//改变红块显示的数目　
			   		 
			   		 //总的消息数+1
			   		  var totalUnreadMsgNum = getTotalUnReadMsgNum();
			   		  changeMessageAlertState(totalUnreadMsgNum+1);
		   		  }
		   	  }
	   	  }
	   	};
	   	
	   	//消息过来，但对应的聊天框为空，此时创建
	   	function handleDialogueIsNull(topicId)
	   	{
	   		  //根据topicId获取对应的Topic
	   		  $.post("${pageContext.request.contextPath}/servlet/topic_service",{
					cmd:"getTopicByTopicId",
					topicId:topicId
				},function(res,status){
					var topicName = res.topicName;
					var imgSrc = res.logoUrl;
					console.log("话题名:"+topicName);
					console.log("logoUrl:"+imgSrc);
					addTopicItemOnTopicList_program4(topicId,topicName,imgSrc,1);//添加话题列表
				   //获取聊天框 通过传递参数 topicId,userId
				    var userId ="${sessionScope.user.id}";
				    doPostTogetDialogueBoxNotShowImmediately(topicId,userId);
				    //将话题Id放到全局tipicIdArray数组中
				    topicIdArray.push(topicId);
				});
	   	};
	   	
	   	//往消息框里添加一条消息内容
	   	function addMsgContetntIntoDialogueBox(dialogueBox,json)
	   	{
	   		 var  msg_box = dialogueBox.getElementsByClassName("msg_bubble_list")[0];
	   		 //console.log(json);
		   	  //解析json
		  	  var msgId=json.msgId;
		   	  var nickname=json.nickname;
		   	  //var sender=json.senderId;
		   	  //var accepter=json.accepterIds;
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
	   	
	   	
	   	//往消息框里显示一条新用户进群的通知消息
	   	function addMsgContetntIntoDialogueBoxAboutNewUserComeIn(dialogueBox,nickname,dateTime){
	   		console.log("新用户进话题组");
	   	 var  msg_box = dialogueBox.getElementsByClassName("msg_bubble_list")[0];
	   	  //构造html
	   	  msgStr ="<p>"+nickname+"加入该话题组<br/></p>"+dateTime;
	   	  msgStr+="<hr/>";
	   	  var li_node=document.createElement("li");
	   	  li_node.innerHTML=msgStr;
	   	  msg_box.appendChild(li_node);
	   	  
	   	  //div.mainBox 滚动条置底  滚动,注意获取的是对应聊天框的滚动条
	   	  var mainBoxNode = $(dialogueBox).find("div.mainBox:first-child")[0];
	   	  setBottom(mainBoxNode);	
	   	}
	   	
	   	//判断当前的来的消息对应的聊天窗口是否处于活动状态 根据topicId,true:为活动状态,false:为不活动状态
	   	function isTopicItemActive(topicId)
	   	{
	   		var topicItem = getTopic_group_li_byTopicId(topicId);
	   		if(topicItem.getAttribute("class")=="active"){
	   			return true;
	   		}
	   		else{
	   			return false;
	   		}
	   	}
	   	
	   	//判断聊天窗口是否在隐藏状态,true为隐藏，false为显示
	   	function isDialogueBoxHidden()
	   	{
	   		if($("#webim_page").css("display")=="none")
	   		{
	   			return true;	
	   		}
	   		else{
	   			return false;
	   		}
	   	}
	   	
	   	
	    //websocket状态发生变化时触发
	   	window.webimStateChange=function(state){
	   	    if(state=="ok")
	   	    {
	   	        msgManagerReady=true;
	   	        console.log("websocket创建成功");
	   	        //获取未读消息数======================================>未读消息数
	   	        console.log("开始获取未读消息");
	   	        getUnreadMessageNum("${sessionScope.user.id}");//调用方法后，需要在回调函数中接收数据
	   	    }
	   	    else if(state="no"){
	   	        msgManagerReady=false;
	   	        console.log("websocket异常");
	   	       console.log("3秒后重新创建websocket");
	   	       setTimeout(createWebsocketConnect("${sessionScope.user.id}"),3000);
	   	    }
	   	};  
	   	
	  //显示历史消息的回调函数
	  window.historyMessageHandle = function(res){
		console.log(res);
		for(var key in res)
		{
			//console.log(res[key]);
/* 			var value = res[key];
			var nickname = value.nickname;
			var dateTime = value.dateTime;
			var msg = value.msg; */
			var value = res[key];
			showOneMessageContent(value);
		}
		//showBarMessage();
	  };
	  
	  //在聊天框里显示历史消息
	  function showOneMessageContent(jsonMsg)
	  {
		  var topicId=jsonMsg.topicId;
		  var dialogueBox =getDialogueByBoxId(topicId);//获得相应的聊天框
		  addMsgContetntIntoDialogueBox(dialogueBox,jsonMsg);//往消息框里添加一条消息内容		  
	  }
	  

	  //获取未读消息数的回调函数 消息未读数//有消息就是{topicId:num,topicId2:num2...},没有消息就是{"status":5,"status":"none"},此处有问题
	  window.unreadMessagesNum=function(json){
		 	var sum_unreadNum =0;//统计统的未读消息数
	        for(var key in json)
	        {
	        	if(key!="status")
	        	{
	        		if(json[key]>0)
	        		{
		        		sum_unreadNum+=parseInt(json[key]);
	        		}
	        		else{
	        			delete json[key];//移除未读消息为0的元素
	        		}
	        	}
	        	else{
	        		delete json[key];//移除不为topicId:messageUnReadNum的元素
	        	}
	        }
		 	//打印总的未读消息数
		 	console.log("总的未读消息:"+sum_unreadNum);
		 	//打印过滤后的json,应只包含topicId:>0的未读消息数
		 	console.log("========打印过滤后的json,应只包含topicId:>0的未读消息数============");
		 	for(var topicId in json){
		 		console.log(topicId+"==>"+json[topicId]);
		 	}
		 	changeMessageAlertState(sum_unreadNum);//将消息提示框的未读消息改为sum_unreadNum
		 	//如果总的未读消息大于0要显示消息提示框
		 	//if(sum_unreadNum>0)
		 	//{
		 		//console.log("总的未读消息数是否大于0:"+sum_unreadNum);
		 		console.log("显示聊天框");
		 		showBarMessage();
		 	//}
 	       	initChatBox(json);//初始化含有未读消息的话题列表
	  };
	  
	  //初始化导航栏的未读消息数
	  $(document).ready(function(){
		  console.log("用户刷新或初次登录，查询其导航栏的消息数,省略步骤1,2");
		  getTopicInviteRequestMsgNum();//从数据库查自己有多少条未读消息
	  });
	  
	  window.receiveTopicInviteRequestMsg = function(msg)
	  {
		  //这里处理关于邀请话题的请求
		  //msg == "TOPIC_INVITE"==>邀请,做一系列的事
		  console.log("1.接收到一条邀请的推送消息:"+msg);
		  console.log(msg);
		  //将导航栏的未读消息数增加1
		  addNavBarMsgAlertNumByOne();
	 	  for(var key in msg)
		  {
			  console.log(key+"==>"+msg[key]);
		  } 
 		  if(msg.cmd =="invite")
		  {
 			  //cmd==>invite
 			  //topicId==>5854BF71A2FEED4D28DD96E5E23930F5
 			  //userId==>3
 			  //userName==>test1
 			  //to_userId==>1
 			  //topicName==>上海哪好玩啊
			  console.log("2.是话题邀请");
			  //将邀请信息添加到消息框中
			  addOneTopicInviteMsg(msg.fromUserId,msg.fromUserName,msg.topicName,msg.topicId);
		  } 
		  //msg == "TOPIC_INVITE_RESPONSE"==>同意
		  if(msg.cmd=="agree_to_join_topic"){
			  var fromUserId = msg.fromUserId;
			  var fromUserName = msg.fromUserName;
			  var topicId = msg.topicId;
			  var topicName = msg.topicName;
			  var sysmsg=fromUserName+" 接受了参与话题 #"+topicName+"# 的邀请"+"<br/>"+new Date().format("yyyy-MM-dd hh:mm:ss");
			  console.log(sysmsg);
			 //将系统消息添加到消息框里
			  addOneSystemMsg(sysmsg);
			 //将系统消息添加到服务器
			 $.post("${pageContext.request.contextPath}/servlet/topic_service",{
				cmd:"addSysMsg",
				fromUserId:fromUserId,
				fromUserName:fromUserName,
				toUserId:"${sessionScope.user.id}",//消息是发给谁的就是谁的
				toUserName:"${sessionScope.user.xunta_username}",
				sysmsg:fromUserName+" 接受了参与话题 #"+topicName+"# 的邀请"
			 },function(res,status){
				 console.log(status);
			 });
		  }else if(msg.cmd=="refuseInvite"){
			  var fromUserId = msg.fromUserId;
			  var fromUserName = msg.fromUserName;
			  var topicId = msg.topicId;
			  var topicName = msg.topicName;
			  var sysmsg=fromUserName+" 拒绝了参与话题 #"+topicName+"# 的邀请"+"<br/>"+new Date().format("yyyy-MM-dd hh:mm:ss");
			  console.log(sysmsg);
			 //将系统消息添加到消息框里
			  addOneSystemMsg(sysmsg);
			 //将系统消息添加到服务器
			 //将系统消息添加到服务器
			 $.post("${pageContext.request.contextPath}/servlet/topic_service",{
				cmd:"addSysMsg",
				fromUserId:fromUserId,
				fromUserName:fromUserName,
				toUserId:"${sessionScope.user.id}",
				toUserName:"${sessionScope.user.xunta_username}",
				sysmsg:fromUserName+" 拒绝了参与话题 #"+topicName+"# 的邀请"
			 },function(res,status){
				 console.log(status);
			 });
		  }
	  };
	  
	  //初始化聊天框
	  function initChatBox(json_topicIdKey_unreadMsgNumValue){
			console.log("初始化聊天框");
			getTopicListByTopicIdArray(json_topicIdKey_unreadMsgNumValue);
	  };
	  

	  //获取对象的键,以数组返回
	  function getKeys(json)
	  {
		  var keysArray = new Array();
		  for(var key in json)
		  {
			  keysArray.push(key);
		  }
		  return keysArray;
	  };
	/*   var json_topicIdKey_unreadMsgNumValue={topicId1:2,topicId2:3,topicId3:4};
	  var keys = getKeys(json_topicIdKey_unreadMsgNumValue);
	  console.log("获取到的键名数组：");
	  console.log(keys); */
	  
	  //通过topicId数组，请求对应的TopicList
	  function getTopicListByTopicIdArray(json_topicIdKey_unreadMsgNumValueon)
	  {
		  var topicIdArray = getKeys(json_topicIdKey_unreadMsgNumValueon);
		  //需要初始化的tpicId数组内容:
		  console.log("需要初始化的tpicId数组内容:");
		  console.log(topicIdArray);
		  //post
		  $.post("${pageContext.request.contextPath}/servlet/topic_service",{
			  cmd:"getTopicListByTopicIdArray",
			  topicIdArray:topicIdArray.toString()
		  },function(res,status){
			  console.log("获取到的topicList:");
			  console.log(res);
			  var topicArray = res;
			  //获取到的是一个数组 每个数组里有一个对象
			 initTopicGroupList(topicArray,json_topicIdKey_unreadMsgNumValueon)
		  });
	  };
	  
	  //初始化有未读消息的话题列表
	  function initTopicGroupList(topicArray,json_topicIdKey_unreadMsgNumValueon){
		  
		  for(var i=0;i<topicArray.length;i++){
			  var topicObj = topicArray[i];
			  
			  var topicId = topicObj.topicId;
		
			  var topicName = topicObj.topicName;
			  var imgSrc = topicObj.logo_url;
			  var unreadMsgNum = json_topicIdKey_unreadMsgNumValueon[topicId];
			  console.log("topicId:"+topicId);
			  console.log("topicName:"+topicName);
			  console.log("imgSrc:"+imgSrc);
			  console.log("unreadMsgNum:"+unreadMsgNum);
			  
			  addTopicItemOnTopicList_program4(topicId,topicName,imgSrc,unreadMsgNum);//添加话题列表
			  //获取聊天框 通过传递参数 topicId,userId
			  var userId ="${sessionScope.user.id}";
			  doPostTogetDialogueBox(topicId,userId);
			  //将话题Id放到全局tipicIdArray数组中
			  topicIdArray.push(topicId);
		  }
	  };
	  
	  //改变消息聊天框的消息数
	  function  changeMessageAlertState(unReadMessageNum)
	  {
		  $("#number").attr("totalNum",unReadMessageNum);
		  $("#number").empty();
		  $("#number").append(unReadMessageNum);
	  }
	  //改变红框框的显示数　
	  function changeW_new_count_unreadMsgNum(topicId,num)
	  {
		  var W_new_count_node = getW_new_countByTopicId(topicId);
		  W_new_count_node.setAttribute("num",num);
		  W_new_count_node.style.display = "block";
		  W_new_count_node.innerHTML=num;
	  };
	  // 获取当前的总未读消息数
	  function getTotalUnReadMsgNum()
	  {
		  return parseInt($("#number").attr("totalNum"));
	  };
	  
	  //获取话题邀请相关的请求提示消息
	  function getTopicInviteRequestMsgNum(){
		 var parameters={
		        authorId:"${sessionScope.user.id}",
		        cmd:'searchUnreadMsgNum'
			 };
		 $.post("${pageContext.request.contextPath}/servlet/topic_service",parameters,function(res,status){
			 console.log("3.未读消息数："+res.num);
			 changeTopicInviteRequestMsgNum(res.num);
		 });
	  };
	  
	  function changeTopicInviteRequestMsgNum(num){
		  	 console.log("4.重新设置未读消息数");
			 $("#topic_invite_msg_num").attr("num",num);
			 $("#topic_invite_msg_num").empty();
			 $("#topic_invite_msg_num").append(num);
	  };
	  
	  /**
	   * 时间格式化函数
	   * var current_datetime=new Date().format("yyyy-MM-dd hh:mm:ss");
	   * @param format
	   * @returns {*}
	   */
	  Date.prototype.format = function(format)
	  {
	      var o = {
	          "M+" : this.getMonth()+1, //month
	          "d+" : this.getDate(), //day
	          "h+" : this.getHours(), //hour
	          "m+" : this.getMinutes(), //minute
	          "s+" : this.getSeconds(), //second
	          "q+" : Math.floor((this.getMonth()+3)/3), //quarter
	          "S" : this.getMilliseconds() //millisecond
	      } //js格式化
	      if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
	          (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	      for(var k in o)if(new RegExp("("+ k +")").test(format))
	          format = format.replace(RegExp.$1,
	              RegExp.$1.length==1 ? o[k] :
	                  ("00"+ o[k]).substr((""+ o[k]).length));
	      return format;
	  }
	  
</script>