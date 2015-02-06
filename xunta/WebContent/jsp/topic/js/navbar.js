$("#search_word").keydown(function(e){
	if(e.keyCode==13){
		//获取search_word
		//发出搜索请求
		var search_word = $("#search_word").val();
		var request_parameters = {
			cmd:'htss',
			search_word:encodeURI(search_word)
		};
		console.log("按键搜索……");
		//window.location.href = contextPath+"/servlet/topic_service?"+toDomString(request_parameters);
		$.post(contextPath+"/servlet/topic_service",request_parameters,function(res,status){
			//console.log(res);
			$("#container_all").empty();
			$("#container_all").append(res);
			console.log($("#container_all")[0]);
		});
	}
});

$("#btn_search").click(function(e){
	//获取search_word
	//发出搜索请求
	var search_word = $("#search_word").val();
	var request_parameters = {
		cmd:'htss',
		search_word:encodeURI(search_word)
	};
	console.log("鼠标点击搜索……");
	//window.location.href = contextPath+"/servlet/topic_service?"+toDomString(request_parameters);
	$.post(contextPath+"/servlet/topic_service",request_parameters,function(res,status){
		//console.log(res);
		$("#container_all").empty();
		$("#container_all").append(res);
		console.log($("#container_all")[0]);
	});
});

//用户点击话题推荐
$(".recommendTopic").click(function(event){
	console.log("用户点击话题推荐");
	//请求话题推荐页面
	$.post(contextPath+"/jsp/topic/httj.jsp",null,function(res,status){
		//console.log(res);
		$("#container_all").empty();
		$("#container_all").append(res);
		console.log($("#container_all")[0]);
	});
});

//用户点击发起话题
$(".writeTopic").click(function(event){
	console.log("用户点击发起话题");
	//请求发起话题页面
	$.post(contextPath+"/jsp/topic/fqht.jsp",null,function(res,status){
		//console.log(res);
		$("#container_all").empty();
		$("#container_all").append(res);
	});
});

//用户点击话题记忆
$(".historicalTopic").click(function(event){
	console.log("户点击话题记忆");
	$.post(contextPath+"/jsp/topic/htjy.jsp",null,function(res,status){
		$("#container_all").empty();
		$("#container_all").append(res);
	});
});

//用户点击消息提醒
$(".message").click(function(event){
	console.log("用户点击消息提醒");
	/*$.post(contextPath+"/servlet/topic_service?cmd=msgalert",null,function(res,status){
		//console.log(res);
		$("#container_all").empty();
		$("#container_all").append(res);
		console.log($("#container_all")[0]);
	});*/
});

//点击未读消息
/*$("#topic_request_msg_num").click(function(event){
	console.log("用户点击未读消息");
	$.post(contextPath+"/servlet/topic_service?cmd=msgalert",null,function(res,status){
		//console.log(res);
		$("#container_all").empty();
		$("#container_all").append(res);
		console.log($("#container_all")[0]);
	});
});*/
//点击未读消息，弹出下拉框，下拉框里有两个tab,一个是话题邀请信息，一个是验证消息

$(document).ready(function(){
	  $("#topic_request_msg_num").click(function(){
		  console.log("将消息总数清零");
		  //查询数目是否为0
		  $(".alert_msg_pull_down").slideToggle("slow");
		  var numStr =$("#topic_invite_msg_num").attr("num");
		  console.log("导航栏消息是否为0:"+(parseInt(numStr)==0));
		  //如果不为0就将该用户在数据库中所有的消息都置为已读
		  if(parseInt(numStr)!=0)
		  {
			  $.post(contextPath+"/servlet/topic_service",{
				  cmd:"clearNavBarMsgNum",
				  userId:currentUserId
			  },function(res,status){
				  console.log(status);
			  });
		  }
		  clearNavBarMsgAlertNum();
		  
	  });
	  
	  //初始化话题请求页页
	  console.log("初始化话题请求消息提示...");
	  $.post(contextPath+"/servlet/topic_service",{
		  cmd:"initTopicRequestMsg",
		  userId:currentUserId
	  },function(res,status){
		  	console.log(res);
		  	$(".msg_tab .content ul li:first").append(res);
	  });
	  //初始化系统消息
	  console.log("初始化系统请求消息提示...");
	  $.post(contextPath+"/servlet/topic_service",{
		  cmd:"initSysMsg",
		  userId:currentUserId
	  },function(res,status){
		 $(".msg_tab .content ul li:eq(1)").append(res);
	  });
});

//在话题邀请信息框里添加一条话题邀请信息
function addOneTopicInviteMsg(fromUserId,_fromNickName,topicName,topicId){
	console.log(_fromNickName+" 邀请参与 话题#"+topicName+"#topicId:"+topicId);
/*	<div>
		<span>张三 邀请您参与话题　#黄山哪好玩#</span>
		<span>已同意</span>
	</div> 
	<div>
		<p>张三 邀请您参与话题　#黄山哪好玩#</p>
		<button>同意</button>
		<button>不同意</button>
	</div>
	*/
	var div_node = document.createElement("div");
	var p_node = document.createElement("p");
	var button_agree = document.createElement("button");
	var button_refuse =document.createElement("button");
	button_agree.innerHTML="同意";
	button_refuse.innerHTML="不同意";
	div_node.appendChild(p_node);
	div_node.appendChild(button_agree);
	div_node.appendChild(button_refuse);
	
	p_node.innerHTML=_fromNickName+" 邀请您参与话题  #"+topicName+"#"+"<br/>"+new Date().format("yyyy-MM-dd hh:mm:ss");
	button_agree.setAttribute("topicId",topicId);
	$(".msg_tab .content ul li:first").prepend(div_node);
	//=================================================同意和不同意后
	$(button_agree).click(function(event){
		console.log("点击同意事件");
		//通知邀请人，已经同意其邀请
		var current_datetime2=new Date().format("yyyy-MM-dd hh:mm:ss");
		var parameters={
				cmd:"agree_to_join_topic",
				fromUserId:currentUserId,
				fromUserName:currentUserName,
				topicId:topicId,
				topicName:topicName,
				dateTime:current_datetime2
		};
		var parametersStr=JSON.stringify(parameters);
		var regx = new RegExp("\"","g");
		var jsonStr = parametersStr.replace(regx,"\'");
		inviteFriend("["+fromUserId+"]",jsonStr);
		chat(this);
		//将该条消息替换成 已处理的样式
		$(this).parent("div").find("button").remove();
		$(div_node).append("<span>===>已同意</span>");
		//弹出聊天框
		console.log("同意后要弹出聊天框");
		//将对应的toUserId,topicId更改话题邀请信息的状态　,并执行参与话题流程
		$.post(contextPath+"/servlet/topic_service",{
			cmd:"receiveInvite",
			toUserId:currentUserId,
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
	
	
	$(button_refuse).click(function(event){
		console.log("点击不同意事件");
		var current_datetime2=new Date().format("yyyy-MM-dd hh:mm:ss");
		$(this).parent("div").find("button").remove();
		$(div_node).append("<span>===>已拒绝</span>");
		var parameters={
				cmd:"refuseInvite",
				fromUserId:currentUserId,
				fromUserName:currentUserName,
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

	
};

//在邀请框里添加一条系统消息
function addOneSystemMsg(sysmsg)
{
/*	<div>
		<span>张三 接受了参与话题　#黄山哪好玩#　的邀请</span>
	</div>*/
	var div_node = document.createElement("div");
	var span_node = document.createElement("span");
	span_node.innerHTML=sysmsg;
	div_node.appendChild(span_node);
	
	$(".msg_tab .content ul li:eq(1)").prepend(div_node);
	
}

//将消息总数清零
function clearNavBarMsgAlertNum()
{
	$("#topic_invite_msg_num").attr("num",0);
	$("#topic_invite_msg_num").empty();
	$("#topic_invite_msg_num").append(0);
	
}

//将导航栏消息数增加1
function addNavBarMsgAlertNumByOne(){
	var numStr =$("#topic_invite_msg_num").attr("num");
	var numInt = parseInt(numStr)+1;
	$("#topic_invite_msg_num").empty();
	$("#topic_invite_msg_num").attr("num",numInt);
	$("#topic_invite_msg_num").append(numInt);
	console.log("将导航栏消息数增加1");
}

function toDomString(json){
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
