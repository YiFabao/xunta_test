//联系人鼠标移上去的显示状态
var active_li_node=null;
function addContactsShowStyleListener(){
    var li_nodes=document.querySelector(".contacts_list")
        .getElementsByTagName("ul")[0]
        .getElementsByTagName("li");
    for(var i=0;i<li_nodes.length;i++)
    {
        var li_node=li_nodes[i];
        if(li_node.getAttribute("class")=="active")
        {
            active_li_node=li_node;
            var name_span_node=document.querySelector(".header span.name");
            name_span_node.innerText=li_node.getElementsByClassName("nick_name")[0].innerText.trim();
        }
        //添加事件
        //鼠标移上的显示效果 lightgray
        li_node.addEventListener("mouseover",function(){
            this.style.backgroundColor="lightgray";
        });
        li_node.addEventListener("mouseout",function(){
            this.style.backgroundColor="";
        });
        li_node.addEventListener("click",function(){

            this.setAttribute("class","active");
            active_li_node.setAttribute("class","");
            active_li_node=this;
            //将此人的名字放在右侧的header上
            var name_span_node=document.querySelector(".header span.name");
            name_span_node.innerText=this.getElementsByClassName("nick_name")[0].innerText.trim();
        });
    }
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
//{name:"张三",mytopic:"话题"}==>name=张三&mytopic=话题==>并url编码，以便给xhr传参
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


var xmlHttp=null;//声明一个XHR对象
//创建一个XHR对象
function createXMLHttpRequest() {
  if (window.ActiveXObject) {
      xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
  } else {
      if (window.XMLHttpRequest) {
          xmlHttp = new XMLHttpRequest();
      }
  }
}
//向服务端发起异步请求:GET（入口函数）,callback为回调函数名称
function doRequestUsingPOST(url, callback) {
	if(xmlHttp==null)
  {
      createXMLHttpRequest();//创建xhr
  }
  if(xmlHttp.readyState!=0) {
      xmlHttp.abort();//初始化
  }
  xmlHttp.onreadystatechange = callback;
  xmlHttp.open("POST",window.mydomain+url + "&timeStamp=" + new Date().getTime(),false);//true表示异步,false表示同步
  xmlHttp.send(null);
}

/**
 *  //发送请求例子
	//定义请求参数
    var parameters={
        authorId:authorId,
        cmd:'htjy'
    };
    
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
    
  //以下是callback函数的定义
    function mycallback(){
        console.log(xmlHttp.readyState);
        if(xmlHttp.readyState==4)
        {
            console.log("请求完成");
            if(xmlHttp.status==200)
            {
                console.log("请求成功响应");
                var topicListData=xmlHttp.responseText;
                topicListData= JSON.parse(topicListData);
                
            }
            else{
                console.log("请求没有成功响应:"+xmlHttp.status);
            }
        }
    }
 */


