
//导航栏的事件监听
/**
 * 1.获取子菜单元素里包含的所有<a>元素　
 * ２.给子菜单添加事件
 * ３.根据子菜单class属性值，做相应的处理
 */
function addSubMenuTagChangeListener() {
    var subMenus = getSubMenus();
    for (var i = 0; i < subMenus.length; i++) {
        var li = subMenus[i];
        var a =li.getElementsByTagName("a")[0];
        a.addEventListener("click",show);
    }
    
    function getSubMenus()
    {
        var subMenus = [];
        var menu = document.getElementsByClassName("menu")[0];
        var uls = menu.getElementsByTagName("ul");//子菜单
        for(var i=0;i<uls.length;i++)
        {
            var ul=uls[i];
            var lis=ul.getElementsByTagName("li");
            for(var j=0;j<lis.length;j++)
            {
                subMenus.push(lis[j]);
            }
        }
        return subMenus;
    }
    
    
    function show(e)
    {
        var className=this.className;
        switch(className)
        {
            case "writeTopic":
                console.log("发起话题……");
                showMyTopicSubpage("writeTopic");
                break;
            case "recommendTopic":
                console.log("推荐话题……");
                showMyTopicSubpage("recommendTopic");
                break;
            case "historicalTopic":
                console.log("历史话题……");
                showMyTopicSubpage("historicalTopic");
                //显示历史话题
                var userId=document.getElementsByName("userId")[0].value;
                htjy(userId);
                break;
            case "searchTopic":
                console.log("搜索话题……");
                showMyTopicSubpage("searchTopic");
                break;
            case "homePage":
                console.log("我的主页……");
                break;
            case "message":
                console.log("消息……");
                showMyMessage();
                break;
            case "configure":
                console.log("设置……")
                break;
            case "signOut":
                console.log("退出……");
                break;
            default:
                console.log("default")
                break;
        }
    }
}

//弹出聊天框
function startChat(e){
  var webim_page=document.getElementsByClassName("webim_page")[0];
	//给聊天框设置话题id
  var topicId=this.getAttribute("topicId");//话题id
  var private_dialogue_body = document.getElementsByClassName("private_dialogue_body")[0];
  private_dialogue_body.setAttribute("topicId",topicId);

  console.log("消息数："+msg_count);
  //给聊天框设置话题昵称
  var topicContentNode=e.target.parentNode.parentNode.parentNode;//table
  var topicContent=topicContentNode.getElementsByClassName("content")[0].innerText.trim();//话题内容
  var header_name=document.querySelector("div.header span.topic_name");//话题名称节点
  header_name.innerText=topicContent;//聊天框头部显示要聊的话题

  switch(this.innerHTML.trim()){
      case "邀请":
          //跳转到聊天页面，话题为自己的话题id
          var _fromUserId=_currentUserId;
          var toUserId=this.getAttribute("authorId");
          //给所点击的用户发送邀请信息
          addMessageAlert(_fromUserId,toUserId);
          console.log("邀请聊天");
          alert("邀请信息已发送");
          break;
      case "参与":
      	//参与聊天，自己会被加入到话题会话列到中
          console.log("参与聊天");
          broadcast(_currentUserId,topicId);//广播通知用户,调用的websocket.js中的函数
      	//合成一个函数，就是用户参与,逻辑放后台 ==>判断是否是新成员，不是就添加到数据库，然后 返回该话题下的所有联系人
      	  joinTopic(topicId);
         
          //查询该话题下的历史消息，显示历史消息
      	var msg_count = private_dialogue_body.getAttribute("msg_count");
      	var historyMsgs = getHistoryMessage(topicId,msg_count);
      	var msg_num = historyMsgs.length;
      	private_dialogue_body.setAttribute("msg_count",msg_num+msg_count);
      	for(var i=0;i<msg_num;i++)
      	{
      		var history_msg = historyMsgs[i];
      		webimHandle(history_msg);
      	}
         //TODO
      	webim_page.style.display="block";//显示聊天窗口
          
          break;
      case "进入":
    	var topicId=this.getAttribute("topicId");
    	joinTopic(topicId);
    	var msg_count = private_dialogue_body.getAttribute("msg_count");
      	var historyMsgs = getHistoryMessage(topicId,msg_count);
      	var msg_num = historyMsgs.length;
      	private_dialogue_body.setAttribute("msg_count",msg_num+msg_count);
      	for(var i=0;i<msg_num;i++)
      	{
      		var history_msg = historyMsgs[i];
      		webimHandle(history_msg);
      	}
      	webim_page.style.display="block";
      	//参与聊天，自己会被加入到话题会话列到中
          console.log("进入自己的话题下聊天");
          console.log("话题id:"+topicId);
      	break;
  }
}
//点击聊天框退出
function addClickExitListener(){
	var exit_node=document.querySelector("span.exit");
	exit_node.addEventListener("click",function(){
		var webim_page=document.getElementsByClassName("webim_page")[0];
		if(webim_page.style.display=="block")
		{
			webim_page.style.display="none";//隐藏聊天窗口
		}
		//将聊天框的内容置为空
	    var msg_bubble_list_node=document.getElementsByClassName("msg_bubble_list")[0];
	    msg_bubble_list_node.innerHTML="";
	    //清除窗口private_dialogue_body 节点上的topicId
	    var private_dialogue_body = document.getElementsByClassName("private_dialogue_body")[0];
	    private_dialogue_body.setAttribute("topicId","");
	    //清除联系人列表
	    var contacts_list_ul = document.querySelector("div.contacts_list ul");
	    contacts_list_ul.innerHTML="";
	    
	});
}

//监听用户发送消息
function addChatSendMessageListener(){
    //监听webim消息输入框
    var msg_input_node=document.getElementsByClassName("msg_input")[0];
    msg_input_node.addEventListener("keyup",function(){
        if(event.keyCode==13)
        {
        	//获取好友列表
        	var p_nodes=document.getElementsByClassName("nick_name");
        	
        	var fromUserid=document.getElementsByName("userId")[0].value;//发送消息人id
        	//话题id
        	var topicId=p_nodes[0].getAttribute("topicId");
        	var contacts=new Array();
        	for(var i=0;i<p_nodes.length;i++)
        	{
        		var p_node=p_nodes[i];
        		contacts.push(p_node.getAttribute("memberid"));
        	}
            if(msgManagerReady)
            {
                var msg=msg_input_node.value;
                var nickname = document.getElementsByName("userName")[0].value;
                sendMsg(topicId,"msg1",fromUserid,contacts,msg.toString().trim(),new Date().format("yyyy-MM-dd hh:mm:ss"),nickname);
                msg_input_node.value = "";
            }
            else{
                console.log("网络不通");
            }
            event.returnValue=false;
        }
    });
}

//发起话题监听器
function addPublishTopicListener() {
    //div.topic_input_box a
    var topic_box_node = document.getElementsByClassName("topic_input_box")[0];
    var button_node = topic_box_node.getElementsByTagName("button")[0];
    button_node.addEventListener("click",fqht);
    var textArea_node = document.querySelector("div.topic_input_box textarea");
    //textArea_node.focus();//让输入框获取焦点
    textArea_node.addEventListener("keyup",function(){
        //获取输入框里的字符数……
        if(event.keyCode==13)
        {
            fqht(event);
        }
    });
}

//话题搜索监听器 
function addSearchTopicListener(){
	//获取话题搜索输入框,并添加键盘事件回车键事件
	var search_input_box = document.querySelector("#searchTopic textarea.search_input_box");
	//键盘回车事件
	search_input_box.addEventListener("keyup",function(){
	
        if(event.keyCode==13)
        {
           htss(event);
        }
	});
	
	//获取搜索按钮，并添加鼠标点击事件
	var btn_search=document.querySelector("#searchTopic button.btn_search");
	btn_search.addEventListener("click",htss);
	
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
        var fromUserid=document.getElementsByName("userId")[0].value;//发送消息人id;
        console.log("重新创建websocket");
        createWebsocketConnect(fromUserid);
    }
};

//接收广播消息
window.receiveBroadcast = function(json)
{
	console.log("用户上线"+json.userId+"   "+json.topicId);
	//查询当前对应的话题窗口有没有打开
	var flag=checkUserIdExistInList(json.userId)
	if(!flag){
		var nickname = searchUser(json.userId);
		console.log("247行:"+nickname);
		var contact={
				topic_id:json.topicId,
				topic_memberId:json.userId,
				topic_member_name:nickname
		};
		console.log("添加联系人");
		createContactsList(contact);
	}
	else{
		console.log("联系人在列表中已经存在");
	}
	
}

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
  
  var msgId=json.msgId;
  var nickname=json.nickname;
  var sender=json.sender;
  var accepter=json.accepter;
  var msg = decodeURIComponent(json.msg);
  var time = json.time;
  msgStr=nickname+"<br/>"+sender+"<br/>"+msg+"<br/>"+time;
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
  console.log("主窗体的高度:"+mainBox.offsetHeight);
  console.log("内容体的高度:"+contentBox.offsetHeight);
  var moveD=contentBox.offsetHeight-mainBox.offsetHeight;
  var scrollMove=mainBox.offsetHeight-scrollDiv.offsetHeight;
  //如果是大于0才移动
  if(moveD>0)
  {
      contentBox.style.top=-moveD+"px";
      scrollDiv.style.top=scrollMove+"px";
  }
};
