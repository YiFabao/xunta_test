<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<style>
	tr:hover{
		cursor:pointer;
	}
	tr:hover{
		background-color:orange;
	}
</style>

<h1 align="center">话题推荐</h1>
<div id="httj_container">

</div>
<script>
	$.post("${pageContext.request.contextPath }/jsp/topic/include/topic_items.jsp",null,function(res){
		$("#httj_container").append(res);
	}); 
</script>
