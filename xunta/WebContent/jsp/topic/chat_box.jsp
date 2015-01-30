<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jsp/topic/css/chat_box.css">
<style>
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
<div class="webim_page" style="display:block" id="webim_page">
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
              <li class="active"　topicId="${requestScope.topic.topicId}">
                  <div class="head">
                      <img src="http://tp3.sinaimg.cn/1298064414/50/5617529344/1">
                  </div>
                  <p class=topic_name>话题：${requestScope.topic.topicName }</p>
              </li>
              <li topicId="${requestScope.topic.topicId}">
                  <div class="head">
                      <img src="http://tp3.sinaimg.cn/1298064414/50/5617529344/1">
                  </div>
                  <p class=topic_name>话题：${requestScope.topic.topicName }</p>
              </li>
          </ul>
      </div>
    </div>
    <div class="dialogue_box">
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
						<input type="button" value=" X " id="btn_exit">
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
               <div id="mainBox">
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


<script src="${pageContext.request.contextPath }/jsp/topic/js/myscroll.js"></script>
<script>
	//创建websocket
	createWebsocketConnect("${sessionScope.user.id}");
	
	var bar_message = document.getElementById("bar_message");
	bar_message.addEventListener("click",function(event){
		this.style.display = "none";
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "block";
	});
	
	//点击退出按钮
	var btn_exit = document.getElementById("btn_exit");
	btn_exit.addEventListener("click",function(event){
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "none";
		bar_message.style.display = "block";
	});
	
	function showBarMessage()
	{
		var bar_message = document.getElementById("bar_message");
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "none";
		bar_message.style.display = "block";
	}
	

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
			console.log(mouse_x+"  "+mouse_y);
			//计算聊天框的左顶点坐标
			var webim_page_position = getPositionOnScreen(webim_page);
			console.log(webim_page_position.x +"  "+webim_page_position.y);
			//如果鼠标点击的地方在聊天窗口之外就隐藏聊天框，显示消息框
			if(!(mouse_x>webim_page_position.x&&mouse_y>webim_page_position.y))
			{
				showBarMessage();
			}
		}
	}); 
	 
	 
 	//监听webim消息输入框
   	var msg_input_node=document.getElementsByClassName("msg_input")[0];
   	msg_input_node.addEventListener("keyup",function(){
   		console.log("aaaa");
        if(event.keyCode==13)
        {
        	//发送消息需要传的参数，话题ID,消息id,发送人id,联系人id数组,消息,时间,发送人昵称
        	//1.话题id
        	var  private_dialogure_body = document.getElementsByClassName("private_dialogue_body")[0]; 
        	var topicId = private_dialogure_body.getAttribute("topicId");
        	//2获取联系人　id[]
        	var div_contacts_node = document.getElementsByClassName("contacts")[0];
        	var userIds = div_contacts_node.getElementsByClassName("userId");
        	var contacts=new Array();
        	for(var i=0;i<userIds.length;i++)
        	{
        		var userId_node=userIds[i];
        		contacts.push(userId_node.getAttribute("userId"));
        	}
        	//3消息 
        	var msg=msg_input_node.value;
        	//4发送人id
        	var fromUserId ="${sessionScope.user.id}";
        	//5发送人昵称
        	var fromUserName = "${sessionScope.user.xunta_username}";
        	//6时间
        	var datetime = new Date().format("yyyy-MM-dd hh:mm:ss");
        	//7 消息id
        	var msgId = fromUserId+""+new Date().getTime();
        	
        	console.log("话题id:"+topicId);
        	console.log("发送人id:"+fromUserId);
        	console.log("发送人昵称:"+fromUserName);
        	console.log("时间:"+datetime);
        	console.log("消息:"+msg);
        	console.log("消息id:"+msgId);
        	console.log("联系人:"+contacts);
        	sendMsg(topicId,msgId,fromUserId,fromUserName,msg.toString().trim(),datetime,contacts);
        } 
    });
   	
  //创建消息处理函数
   	window.webimHandle=function(json){
   	  console.log("接收到的消息："+json.msg);
   	  var msg_bubble_list_node=document.getElementsByClassName("msg_bubble_list")[0];
   	  var li_node=document.createElement("li");
   	  var msgStr="";
   	  var topicId=json.topicId;
   	  //消息要发送到指定的topicId窗口上
   	  var private_dialogue_body = document.getElementsByClassName("private_dialogue_body")[0];
   	  var chatTopicId=private_dialogue_body.getAttribute("topicId");
   	  if(topicId!=chatTopicId)
   	  {
   		  console.log("不是当前话题的消息");
   		  return;
   	  }
   	  console.log(json);
  	  var msgId=json.msgId;
   	  var nickname=json.nickname;
   	  var sender=json.senderId;
   	  var accepter=json.accepterIds;
   	  var msg = decodeURIComponent(json.message);
   	  var time = json.time;
   	  msgStr="<p>"+nickname+"<br/>"+sender+"<br/>"+msg+"<br/>"+time+"</p>";
   	  msgStr+="<hr/>";
   	  li_node.innerHTML=msgStr;
   	  msg_bubble_list_node.appendChild(li_node);
   	  //更改滚动条的状态
   	  var mainBox = document.getElementById("mainBox");
   	  var contentBox = document.getElementById("content");
   	  var scrollDiv = document.getElementsByClassName("scrollDiv")[0];
   	  myscroll._resizeScorll(scrollDiv, mainBox, contentBox);

   	  //显示最底部的消息
   	  //1.将滚动条移动到底部
   	  //2.将内容div 移动一个负值:该值为 内容的高度-主窗体的高度
   	  //3.将滚动条的top 设置为 :窗体的高度-滚动条的高度
   	  var moveD=contentBox.offsetHeight-mainBox.offsetHeight;
   	  var scrollMove=mainBox.offsetHeight-scrollDiv.offsetHeight;
   	  //如果是大于0才移动
   	  if(moveD>0)
   	  {
   	      contentBox.style.top=-moveD+"px";
   	      scrollDiv.style.top=scrollMove+"px";
   	  }
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
    //创建聊天框的滚动条
    myscroll=new addScroll('mainBox', 'content', 'scrollDiv');
	
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
	
	
	//话题列表，鼠标上移和点击选中的切换效果
	var active_li_node=null;
	contactsShowStyle();
	function contactsShowStyle(){
	    var li_nodes=document.querySelector(".topic_group_list")
	        .getElementsByTagName("ul")[0]
	        .getElementsByTagName("li");
	 	for(var i=0;i<li_nodes.length;i++)
	    {
	        var li_node=li_nodes[i];
	        if(li_node.getAttribute("class")=="active")
	        {
	            active_li_node=li_node;//记录当前激活的按钮
	        }
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
	            active_li_node.setAttribute("class","");
	            active_li_node=this;//更改激活的选项
	        });
	    } 
	}
	
</script>
