//包含该脚本之前要包含jquery
$.post("jsp/topic/include/chat.jsp",null,function(res,status){
	//console.log(document.body);
	//console.log(res);
	$(document.body).append(res);
});