//所有文档完全加载完成后执行
window.onload=function(){
    //imgLocation("body_content","box");
    console.log("所有页面资源包括图片加载完成");
}

//dom结构加载后执行
window.addEventListener("DOMContentLoaded",function(){
    console.log("文档内容加载完毕");
    //创建websocket
    var userName=document.getElementsByName("userName")[0].value;
    createWebsocketConnect(userName);
    console.log("创建websocket");
    //创建聊天框的滚动条
    myscroll=new addScroll('mainBox', 'content', 'scrollDiv');
    //给聊天框添加退出事件
    addClickExitListener();
    //单击菜单按钮切换效果
    addSubMenuTagChangeListener();
    //发起话题监听器
    addPublishTopicListener();
    //话题索索监听器 
    addSearchTopicListener();
    //联系人相关监听器
    addContactsShowStyleListener();
});

var confirm_node=document.getElementById("confirm");
confirm_node.addEventListener("click",function(){
    var fromUserid=document.getElementsByName("fromUserId")[0].value;
    var toUserId=document.getElementsByName("toUserId")[0].value;
    var topicId=document.getElementsByName("topicId")[0].value;
    console.log("自己的id:"+fromUserid);
    console.log("对方的id:"+toUserId);

    //监听webim消息输入框
    var msg_input_node=document.getElementsByClassName("msg_input")[0];
    msg_input_node.addEventListener("keyup",function(){
        if(event.keyCode==13)
        {
            if(msgManagerReady)
            {
                var msg=msg_input_node.value;
                sendMsg(topicId,"msg1",fromUserid,[toUserId],msg.toString().trim(),new Date().format("yyyy-MM-dd hh:mm:ss"));
                msg_input_node.value = "";
            }
            else{
                console.log("网络不通");
            }
            event.returnValue=false;
        }
    });
});

//创建消息处理函数
window.webimHandle=function(json){
    console.log("接收到的消息："+json.msg);
    var msg=json.msg;
    var msg_bubble_list_node=document.getElementsByClassName("msg_bubble_list")[0];
    var li_node=document.createElement("li");
    var msgStr="";
    for(var p in json)
     {
        msgStr+=p+":"+json[p]+"<br>";
     }
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


//监听websocket状态
var msgManagerReady=false;
window.webimStateChange=function(state){
    if(state=="ok")
    {
        msgManagerReady=true;
        console.log("websocket创建成功");
    }
    else if(state="no"){
        msgManagerReady=false;
        console.log("websocket异常");
    }
};
//监听document加载状态
window.document.addEventListener("readystatechange",function(){
    console.log("加载状态:"+document.readyState);
});

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

//tag切换效果
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

//点击聊天框退出
function addClickExitListener(){
	var exit_node=document.querySelector("span.exit");
	exit_node.addEventListener("click",function(){
		var webim_page=document.getElementsByClassName("webim_page")[0];
		webim_page.style.display="none";
	});
}

//显示我的话题下的子模块,显示某一个，隐藏其他的
var timer=null;
var currentView=document.getElementById("writeTopic");
function showMyTopicSubpage(id) {
    if(currentView.id==id)
    {
        console.log("返回");
        return;
    }
    var body_content_node = document.getElementsByClassName("body_content")[0];
    var myTopic_node = body_content_node.getElementsByClassName("myTopic")[0];
    var myTopic_childrenNodes = myTopic_node.children;
    var show_target = document.getElementById(id);
    currentView=show_target;
    for (var i = 0; i < myTopic_childrenNodes.length; i++) {
        var child_node = myTopic_childrenNodes[i];
        child_node.style.opacity = 0;
        child_node.style.display = "none";
    }
    show_target.style.display = "block";
    clearInterval(timer);
    var alpha=0;
    timer = setInterval(function () {
        alpha += 2;
        alpha > 100 && (alpha = 100);
        show_target.style.opacity = alpha / 100;
        show_target.style.filter = "alpha(opacity=" + alpha + ")";
        alpha == 100 && clearInterval(timer);
    },5)
}

//发起话题监听器
function addPublishTopicListener() {
    //div.topic_input_box a
    var topic_box_node = document.getElementsByClassName("topic_input_box")[0];
    var button_node = topic_box_node.getElementsByTagName("button")[0];
    button_node.addEventListener("click",pt);
    var textArea_node = document.querySelector("div.topic_input_box textarea");
    //textArea_node.focus();//让输入框获取焦点
    textArea_node.addEventListener("keyup",function(){
        //获取输入框里的字符数……
        if(event.keyCode==13)
        {
            pt(event);
        }
    });

    function pt(e) {
        //获取用户输入的话题内容
        var textArea_node = document.querySelector("div.topic_input_box textarea");
        var content_value = textArea_node.value;//TODO :过滤敏感字符
        console.log("用户输入的内容：" + content_value);
        if(content_value=="")
        {
            textArea_node.focus();
            return;
        }
        //将数据发往数据库
        console.log("开始发送数据");
        var userName=document.getElementsByName("userName")[0].value;
        var userId=document.getElementsByName("userId")[0].value;
        fqht(content_value,userName,userId);
    }
    
    //发起话题
    function fqht(mytopic,topicAuthorName,authorId){
         //发送请求
        var parameters={
            mytopic:mytopic,
            topicAuthorName:topicAuthorName,
            authorId:authorId,
            cmd:'fqht'
        };
        
        doRequestUsingPOST("http://"+document.domain+":8080/xunta/servlet/topic?"+toDomString(parameters),mycallback);
        
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
                    //将topicList里的数据清空,将textArea用户输入的内容清空
                    var textArea_node = document.querySelector("div.topic_input_box textarea");

                    var topic_list_node=document.getElementsByClassName("topic-list")[0];
                    topic_list_node.innerHTML="";

                    var t=new Date();
                    var current_datetime=new Date().format("yyyy-MM-dd hh:mm:ss");

                    var myTopic={name:"我",topicContent:textArea_node.value,datetime:current_datetime};
                  
                    //var topic_list_node=document.getElementsByClassName("topic-list")[0];
                    var topic_list_node  =document.querySelector("div#writeTopic div.topic-list");
                    createTopicList(myTopic,topic_list_node);
                    textArea_node.value="";
                    for(var i=0;i<topicListData.length;i++)
                    {
                        //创建dom
                        createTopicList(topicListData[i],topic_list_node);
                    }
                }
                else{
                    console.log("请求没有成功响应:"+xmlHttp.status);
                }
            }
        }
    }
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
	//搜索请求
	function htss(event){
		//获取用户输入的内容
		var search_content=search_input_box.value;
		
         //发送请求
        var parameters={
            mytopic:search_content,
            cmd:'htss'
        };
        
        doRequestUsingPOST("http://"+document.domain+":8080/xunta/servlet/topic?"+toDomString(parameters),mycallback);
        
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
                    //将topicList里的数据清空,将textArea用户输入的内容清空
                    var textArea_node = document.querySelector("div.topic_input_box textarea");

                    var topic_list_node = document.querySelector("div#searchTopic div.topic-list");
                    
                    topic_list_node.innerHTML="";

                    var t=new Date();
                    var current_datetime=new Date().format("yyyy-MM-dd hh:mm:ss");
                    textArea_node.value="";
                    for(var i=0;i<topicListData.length;i++)
                    {
                        createTopicList(topicListData[i],topic_list_node);
                    }
                }
                else{
                    console.log("请求没有成功响应:"+xmlHttp.status);
                }
            }
        }
	}
}


//话题记忆
function htjy(authorId){
    //发送请求
	
    var parameters={
        authorId:authorId,
        cmd:'htjy'
    };
    
    doRequestUsingPOST("http://"+document.domain+":8080/xunta/servlet/topic?"+toDomString(parameters),mycallback);
    
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
                //将topicList里的数据清空

                var topic_list_node  =document.querySelector("div#historicalTopic div.topic-list");
                topic_list_node.innerHTML="";
              
                for(var i=0;i<topicListData.length;i++)
                {
                	console.log(topicListData[i]);
                    //创建dom
                    createTopicList(topicListData[i],topic_list_node);
                }
            }
            else{
                console.log("请求没有成功响应:"+xmlHttp.status);
            }
        }
    }
}

//话题推荐
function httj(){
	
}

var xmlHttp=null;//声明一个XHR对象
//创建一个XHR对象
function createXMLHttpRequest() {
    console.log("创建xhr");
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
        console.log("xmlHttp==null");
        createXMLHttpRequest();//创建xhr
    }
    if(xmlHttp.readyState!=0) {
        console.log("readyState!=0 初始化");
        xmlHttp.abort();//初始化
    }
    xmlHttp.onreadystatechange = callback;
    xmlHttp.open("POST", url + "&timeStamp=" + new Date().getTime(), true);
    xmlHttp.send(null);
}

//创建话题列表
function createTopicList(topicItemData,topic_list_node){
    //创建一个table节点
    var table_node = document.createElement("table");
    //在table节点下创建三个tr节点
    var tr_node1=document.createElement("tr");
    var tr_node2=document.createElement("tr");
    var tr_node3=document.createElement("tr");
    var tr_node4=document.createElement("tr");
    //为tr_node1创建两个td节点
    var tr_node1_td_node1=document.createElement("td");
    var tr_node1_td_node2=document.createElement("td");
    tr_node1_td_node1.setAttribute("class","img");
    tr_node1_td_node2.setAttribute("class","name");
    //为tr_node2创建一个td节点
    var tr_node2_td_node1=document.createElement("td");
    tr_node2_td_node1.setAttribute("class","content");
    tr_node2_td_node1.setAttribute("colspan","2");
    //为tr_node3创建一个td 节点
    var tr_node3_td_node1=document.createElement("td");
    tr_node3_td_node1.setAttribute("colspan","2");
    //为tr_node3_td_node1下创建一个或两个button按钮,如果是自己的话题就创建一个，否则创建两个
    //获取当前用户的id
    var currentUserId=document.getElementsByName("userId")[0].value;
    if(topicItemData.userId==currentUserId||topicItemData.userId==null)
    {
        var btn_join=document.createElement("button");
        btn_join.innerHTML="进入";
        btn_join.addEventListener("click",startChat);
        tr_node3_td_node1.appendChild(btn_join);
    }
    else {
        var btn_invite=document.createElement("button");
        btn_invite.innerHTML="邀请";
        var btn_join=document.createElement("button");
        btn_join.innerHTML="参与";
        // 为邀请，参与两按钮添加事件
        btn_invite.addEventListener("click",startChat);
        btn_join.addEventListener("click",startChat);
        tr_node3_td_node1.appendChild(btn_invite);
        tr_node3_td_node1.appendChild(btn_join);
    }
    tr_node3.appendChild(tr_node3_td_node1);

    //为tr_node4创建一个td节点
    var tr_node4_td_node1=document.createElement("td");
    tr_node4_td_node1.setAttribute("colspan","2");
    tr_node4_td_node1.setAttribute("class","datetime");

    //为tr_node1_td_node1下创建一个img节点　
    var img_node=document.createElement("img");

    //为tr_node4_td_node1创建一个small节点　
    var small_node=document.createElement("small");

    table_node.appendChild(tr_node1);
    table_node.appendChild(tr_node2);
    table_node.appendChild(tr_node3);
    table_node.appendChild(tr_node4);

    tr_node1.appendChild(tr_node1_td_node1);
    tr_node1.appendChild(tr_node1_td_node2);

    tr_node2.appendChild(tr_node2_td_node1);

    tr_node4.appendChild(tr_node4_td_node1);

    tr_node1_td_node1.appendChild(img_node);

    tr_node4_td_node1.appendChild(small_node);

    //赋值
    img_node.src="images/1.jpg";
	/**
	* 	jsonObj.put("name",name);
	*	jsonObj.put("userId", userId);
	*	jsonObj.put("topicContent",content);
	*	jsonObj.put("topicId",topic_id);
	*	jsonObj.put("datetime",datetime);
	*/
    tr_node1_td_node2.innerHTML=topicItemData.name;//"贾乃亮";
    tr_node1_td_node2.setAttribute("name",topicItemData.name);
    tr_node1_td_node2.setAttribute("userId",topicItemData.userId);
    tr_node2_td_node1.innerHTML=topicItemData.topicContent;//"这是动态创建的文本";
    tr_node2_td_node1.setAttribute("topicId",topicItemData.topicId);
    tr_node2_td_node1.setAttribute("datetime",topicItemData.datetime);
    small_node.innerText=topicItemData.datetime;//时间

    //将table节点挂在.topic-list下
    //var topic_list_node=document.getElementsByClassName("topic-list")[0];
    topic_list_node.appendChild(table_node);
}



//开始聊天
function startChat(e){
	var webim_page=document.getElementsByClassName("webim_page")[0];
	webim_page.style.display="block";
    switch(this.innerHTML.trim()){
        case "邀请":
            console.log("invite..");
            //跳转到聊天页面，话题为自己的话题id
           // window.location="chat.html?cmd=invite";
            console.log("邀请聊天");
            break;
        case "参与":
            console.log("参与聊天");
            break;
        case "进入":
        	console.log("进入");
        	break;
    }
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








