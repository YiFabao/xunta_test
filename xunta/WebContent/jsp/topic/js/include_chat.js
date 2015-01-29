
$.post("${pageContext.request.contextPath}/jsp/topic/include/",null,function(res,status){
	document.body.appendChild(res);
});