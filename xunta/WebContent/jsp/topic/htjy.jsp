<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<style>
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

<div align="center">
	<h1>话题记忆</h1>
	<button id="mytopic_btn">我发的发话</button>
	<button id="myjoin_btn">我参与的话题</button>
</div><br>
<hr>
<div id="hostoryTopic_container"></div>
<!--我发起的话题列表 -->
<!--我参与的话题列表-->
<script>
	//start ==========================话题记忆====================================
	//默认显示自己发的话题
		$.post("${pageContext.request.contextPath }/jsp/topic/include/topic_items.jsp",{userId:"${sessionScope.user.id}",cmd:"myTopicHistory"},function(res){
		$("#hostoryTopic_container").append(res);
		//add_searched_topic_item_clickEventListener();
		console.log("显示自己发的话题");
	});
	//点击 我发起的话题 按钮
	$("#mytopic_btn").click(function(){
		//判断如果 #topic_recommend_item 存在则删除
		$("#topic_recommend_item")&&$("#topic_recommend_item").remove();
		$.post("${pageContext.request.contextPath }/jsp/topic/include/topic_items.jsp",{userId:"${sessionScope.user.id}",cmd:"myTopicHistory"},function(res){
			$("#hostoryTopic_container").append(res);
			//add_searched_topic_item_clickEventListener();
			console.log("显示自己发的话题");
		});
	});
	
	//点击我参与的话题按钮
	$("#myjoin_btn").click(function(){
		//判断如果 #topic_recommend_item 存在则删除
		$("#topic_recommend_item")&&$("#topic_recommend_item").remove();
		$.post("${pageContext.request.contextPath }/jsp/topic/include/topic_items.jsp",{userId:"${sessionScope.user.id}",cmd:"myJoinTopic"},function(res){
			$("#hostoryTopic_container").append(res);
			//add_searched_topic_item_clickEventListener();
		});
	});
	//end 话题记忆======================================================================
</script>