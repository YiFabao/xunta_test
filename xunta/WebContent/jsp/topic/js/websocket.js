/*
	 	定义成员变量
	 * */
var ws; //WebSocket-Obj
var wssae = 'null'; //WebSocket state mark;
var msgArray = new Array();

document.addEventListener("plusready", PlusReady, false);

function PlusReady() {
	/*
	 	文档加载完成后绑定事件
	 **/
	document.addEventListener("pause", onAppPause, false);
	document.addEventListener("resume", onAppResume, false);
}

function createWebsocketConnect(userId) { //提供获取websocket连接的方法
	websocketEvent(userId);
}

function onAppResume() { //运行环境从后台切换到前台事件
	websocketEvent();
}

function onAppPause() { //运行环境从前台切换到后台事件
	wssae = 'no';
	ws.close();
}

function websocketEvent(userId) {
	//可以做一个连接中的效果,如果连接成功,触发onpen方法在取消连接中效果,如果10秒中没连接上会触发checkWebSocketState方法的状态,显示当前网络状态
	ws = new WebSocket('ws://aigine.eicp.net:21280/WebSocket/WebSocketServlet?userId='+userId);
	checkWebSocketState();
	//连接服务器成功触发该事件
	ws.onopen = function(event) {
			if(window.webimStateChange)
			{
				webimStateChange("ok");//readyState
			}
			wssae = 'yes';
			setInterval(function() {
				heartbeat(); //发送心跳包
			}, 60000);
		}
		//连接关闭触发该事件
	ws.onclose = function(event) {
			//将服务器中的用户管理器里删除用户
			ws.close();
			wssae = 'no';
			//fabao.yi
			if(window.webimStateChange)
			{
				webimStateChange("no");//readyState
			}
		}
		//客户端发生错误触发该事件
	ws.onerror = function(event) {}
		//客户端接受到消息触发该事件
	ws.onmessage = function(event) {
		console.log(event.data);
		var json = JSON.parse(event.data);
		var status = json.status;
		console.log('status  :   '+status);
		if (status == '2') {
			var topic_id = json.topicId;
			var msg_id = json.messageId;
			for (var s = 0; s < msgArray.length; s++) {
				if (msgArray[s].indexOf(topic_id) != -1 && msgArray[s].indexOf(msg_id) != -1) {
					msgArray.splice(s,1);//移除该条消息
				}
			}
		}else if(status == "1"){
			ws.send(event.data.replace("\"status\":\"1\"","\"status\":\"2\""));
			//聊天信息
	/*		var msg = json.msg;
			alert("收到服务器发来的消息 : "+msg);*/
			if(window.webimHandle){
				webimHandle(json);//消息处理 fabao.yi
			}
			//广播消息在此接收
		}else if(status == "3"){
			if(window.receiveBroadcast){
				receiveBroadcast(json);//接收广播消息  fabao.yi
			}
		}else if(status == "4")	{
			if(json.unreadMessage == "null"){
				//没有未读消息
			}else{
				//有未读消息
				console.log(json.unreadMessage);
			}
		}else if(status == "5")	{
			//触发此事件说明修改消息未读状态成功
			json.userId;
			json.topicId;
		}
	}
}
/**
 *sendMsg('topicId','msgId','user1',['user1'],'测试消息','time')
 *sendMsg('topicId','msgId','user1',['user1','2343'],'测试消息','time')
 * @param topicId 话题id
 * @param msgId 消息id
 * @param sender 发消息者　id
 * @param accepter 字符串数组,接收者id
 * @param msg 消息内容
 * @param time 2014-12-1 12:00:01
 */
function sendMsg(topic_id,message_id,sender_id,nickname,message,accepter_id){
	//发送消息后可以做一个发送中的动画效果,发送成功后,会触发 onmessage中的status==2,表明发送消息成功,取消动画效果,如果在10秒钟还没有发送成功,会触发 if条件表达式,显示消息发送失败
	var msg = jsonStr(1,topic_id,message_id,sender_id,nickname,encodeURIComponent(message),accepter_id);
	msgArray.unshift(msg); //将消息添加到数组,监听状态
	ws.send(msg);
	setTimeout(function() {
		for (var i = 0; i < msgArray.length; i++) {
			if (msgArray[i] == msg) {
				//如果条件成立,则说明在7秒后未收到服务器的消息确认反馈,提示用户消息发送失败
				alert("消息发送失败");
			}
		}
	}, 5000);
}

function heartbeat() {
	ws.send('{"status" : "-1","msg" : "ping"}');
}
//广播该用户进入聊天窗口
function broadcast(user_id,topic_id) {
	console.log("调用广播方法........");
	ws.send('{"status" : "3","userId" : "'+user_id+'","topicId":"'+topic_id+'"}');//用户打开聊天框"",""
}
//获取未读消息
function getUnreadMessage(userId){
	ws.send('{"status" : "4","userId" : "'+userId+'"}');
}
//设置消息读取状态
function setMsgReadStatus(userId,topicId){
	ws.send('{"status" : "5","userId" : "'+userId+'","topicId" : "'+topicId+'"}');
}
//邀请好友
function inviteFriend(inviteIds, inviteMsg){
	//inviteIds is jsonArray
	ws.send('{"status" : "6","inviteIds" : "'+inviteIds+'","inviteMsg" : "'+inviteMsg+'"}');
}
function jsonStr(status, topic_id, message_id, sender_id, nickname, message,accepter_id) {
	var jsonString = '{"status":"' + status + '","topicId":"' + topic_id + '","messageId":"' + message_id + '","senderId":"' + sender_id + '","nickname":" ' + nickname + '","message":"' + message + '","accepterIds": [';
	for (var a = 0; a < accepter_id.length; a++) {
		if (a == accepter_id.length - 1) {
			jsonString += '"' + accepter_id[a] + '"]}';
		} else {
			jsonString += '"' + accepter_id[a] + '",';
		}
	}
	return jsonString;
}


function checkWebSocketState() {
	/*setTimeout(function() {
		if(wssae != "yes"){
			// WebSocket创建失败
			var netState = plus.networkinfo.getCurrentType();
			switch (netState) {
				case plus.networkinfo.CONNECTION_WIFI:
					alert('当前网络:wifi,请联系开发人员.');
					break;
				case plus.networkinfo.CONNECTION_CELL2G:
					alert('当前网络:2G,请联系开发人员.');
					break;
				case plus.networkinfo.CONNECTION_CELL3G:
					alert('当前网络:3G,请联系开发人员.');
					break;
				case plus.networkinfo.CONNECTION_CELL4G:
					alert('当前网络:4G,请联系开发人员.');
					break;
				default:
					alert('请检查网络是否连接');
					break;
			}
		}
	}, 10000);*/
}

function getHistoryMessage(topicId,count){
	var ret_msgs = null;
	 var parameters={
       topicId:topicId,
       biginIndex:count,
       endIndex:count+20
   };
   doRequestUsingPOST_fang("http://localhost:3306/WebSocket/TopicHistoryMessage?"+toDomString_fang(parameters),function(){

       if(xmlHttp.readyState==4)
       {
           console.log("请求完成");
           if(xmlHttp.status==200)
           {
               console.log("请求成功响应");
               var historyMessage = xmlHttp.responseText;
               ret_msgs = JSON.parse(historyMessage);
               //console.log("赋值在前:"+ret_msgs);
           }
           else{
               console.log("请求没有成功响应:"+xmlHttp.status);
           }
       }
   });
   //console.log("返回在后");
   return ret_msgs;
}

function toDomString_fang(json){
   var domString="";
   for(var p in json)//p为json对象里的属性名
   {
       if(domString=="")
       {
           domString+=(p+"="+json[p]);
       }
       else{
           domString+="&"+p+"="+json[p];
       }
   }
   return encodeURI(domString);
}


var xmlHttp=null;//声明一个XHR对象
//创建一个XHR对象
function createXMLHttpRequest_fang() {
 if (window.ActiveXObject) {
     xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
 } else {
     if (window.XMLHttpRequest) {
         xmlHttp = new XMLHttpRequest();
     }
 }
}
 
//向服务端发起异步请求:GET（入口函数）,callback为回调函数名称
function doRequestUsingPOST_fang(url, callback) {
if(xmlHttp==null)
 {
     createXMLHttpRequest_fang();//创建xhr
 }
 if(xmlHttp.readyState!=0) {
     xmlHttp.abort();//初始化
 }
 xmlHttp.onreadystatechange = callback;
 xmlHttp.open("POST",url + "&timeStamp=" + new Date().getTime(),false);//true表示异步,false表示同步
 xmlHttp.send(null);
}
