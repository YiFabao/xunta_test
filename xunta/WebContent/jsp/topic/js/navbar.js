$("#btn_search").click(function(e){
	//获取search_word
	//发出搜索请求
	var search_word = $("#search_word").val();
	var request_parameters = {
		cmd:'htss',
		search_word:search_word
	};
	window.location = contextPath+"/jsp/topic/htss.jsp";
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
