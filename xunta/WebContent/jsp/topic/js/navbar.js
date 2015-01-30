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
	$.post(contextPath+"/servlet/topic_service?cmd=msgalert",null,function(res,status){
		//console.log(res);
		$("#container_all").empty();
		$("#container_all").append(res);
		console.log($("#container_all")[0]);
	});
});

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
