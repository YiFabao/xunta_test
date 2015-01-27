//显示自己的未读消息
function showMyMessage()
{
	var parameters={
	        authorId:_currentUserId,
	        cmd:'viewMessage'
	    };
	window.location=mydomain+"servlet/topic?"+toDomString(parameters);
}


//显示最新的好友列表
function showTheNewestContacts(){
	searchTopicMemberList(tid);
}
//参与话题＝＝>保存话题成员，保存话题历史,返回话题成员列表

//在话题topicId下添加会话成员memberId
function addTopicMember(topicId,memberId,memberName)
{
	 //发送请求
    var parameters={
        topicId:topicId,
        memberId:memberId,
        memberName:memberName,
        cmd:'addTopicMember'
    };
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
  //以下是callback函数的定义
    function mycallback(){
        if(xmlHttp.readyState==4)
        {
            if(xmlHttp.status==200)
            {
            	var responseData=xmlHttp.responseText;
            	if(responseData!=""&&responseData!=null)
            	{
            		var contacts= JSON.parse(responseData);
     	           	var contacts_list=document.getElementsByClassName("contacts_list")[0];
     	        	var contacts_list_ul_node=contacts_list.getElementsByTagName("ul")[0];
     	        	contacts_list_ul_node.innerHTML="";//先赋空
                    for(var i=0;i<contacts.length;i++)
                    {
                       console.log(contacts[i]);
                 	   createContactsList(contacts[i]);
                    }
            	}
            }
            else{
                console.log("添加话题下的新成员，请求没有成功响应:"+xmlHttp.status);
            }
        }
    }
}

//添加消息提醒
//查询topicId对应的话题内容
function searchTopicContent(topicId)
{
	console.log("搜索话题"+topicId+"内容");
	 //发送请求
   var parameters={
       topicId:topicId,
       cmd:'searchTopicContent'
   };
   doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
   //以下是callback函数的定义
   function mycallback(){
       if(xmlHttp.readyState==4)
       {
           if(xmlHttp.status==200)
           {

           }
           else{
               console.log("请求没有成功响应:"+xmlHttp.status);
           }
       }
   }
}

//查询话题下的联系人列表
function searchTopicMemberList(topicId)
{
	console.log("搜索话题下的联系人");
	 //发送请求
   var parameters={
       topicId:topicId,
       cmd:'searchTopicMemberList'
   };
   doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
 //以下是callback函数的定义
   function mycallback(){
       if(xmlHttp.readyState==4)
       {
           if(xmlHttp.status==200)
           {
               var respoinseData=xmlHttp.responseText;
               var contacts= JSON.parse(respoinseData);
	           	var contacts_list=document.getElementsByClassName("contacts_list")[0];
	        	var contacts_list_ul_node=contacts_list.getElementsByTagName("ul")[0];
	        	contacts_list_ul_node.innerHTML="";//先赋空
               for(var i=0;i<contacts.length;i++)
               {
            	   //console.log(contacts[i]);
            	   createContactsList(contacts[i]);
               }
           }
           else{
               console.log("请求没有成功响应:"+xmlHttp.status);
           }
       }
   }
}

function addMessageAlert(_fromUserId,toUserId){
	//发送请求
    var parameters={
    	_fromUserId:_fromUserId,
    	authorId:toUserId,
        cmd:'addMessageAlert'
    };
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
    //以下是callback函数的定义
    function mycallback(){
        if(xmlHttp.readyState==4)
        {
            if(xmlHttp.status==200)
            {
                //如果有返回数据，在些接收处理
            }
            else{
                console.log("请求没有成功响应:"+xmlHttp.status);
            }
        }
    }
}


var timer_recNumUnreadMsg=setInterval(searchUnreadMessageNum,5000);
//查询未读消息数
function searchUnreadMessageNum()
{
	console.log("未读消息数");
	 var parameters={
        authorId:_currentUserId,
        cmd:'searchUnreadMsgNum'
    };
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
    
  //以下是callback函数的定义
    function mycallback(){
        if(xmlHttp.readyState==4)
        {
            if(xmlHttp.status==200)
            {
                var num=xmlHttp.responseText;
                var num=parseInt(num);
                if(num>0)
                {
                	//alert("你好，收到消息,消息数"+num);
                	document.getElementById("unreadMessageNum").innerHTML="未读消息消息数:<i>"+num+"</i>";
                }
            }
            else{
                console.log("请求没有成功响应:"+xmlHttp.status);
            }
        }
    }
}
//创建联系人列表
function createContactsList(contact){
	var topicId=contact.topic_id;
	var memberId=contact.topic_memberId;
	var memberName=contact.topic_member_name;
	
	var li_node=document.createElement("li");
	//li_node 下div,p
	var div_node=document.createElement("div");
	div_node.setAttribute("class","head");
	//div_node 下 img
	var img_node=document.createElement("img");
	img_node.setAttribute("src","images/1.jpg")//此处应传入用户的图片，数据库要增加图片url
	
	var p_node=document.createElement("p");
	p_node.setAttribute("class","nick_name");
	
	div_node.appendChild(img_node);
	li_node.appendChild(div_node);
	li_node.appendChild(p_node);
	//设置动态值
	p_node.innerText=contact.topic_member_name;//用户昵称
	p_node.setAttribute("memberId",memberId);
	p_node.setAttribute("memberName",memberName);
	p_node.setAttribute("topicId",topicId);
	var contacts_list=document.getElementsByClassName("contacts_list")[0];
	var contacts_list_ul_node=contacts_list.getElementsByTagName("ul")[0];
	contacts_list_ul_node.appendChild(li_node);
}

//查询联系人列表中是否存在某个userId
function checkUserIdExistInList(userId)
{
	var contacts_list=document.getElementsByClassName("contacts_list")[0];
	var contacts_list_ul_node=contacts_list.getElementsByTagName("ul")[0];
	var p_nodes=contacts_list_ul_node.getElementsByClassName("nick_name");
	var flag=false;
	for(var i=0;i<p_nodes.length;i++)
	{
		if(p_nodes[i].getAttribute("memberId")==userId){
			flag = true;
			break;
		}
	}
	return flag;
}

//从数据库中查询联系人昵称
function searchUser(userId)
{
	var ret_nickname="";
	 var parameters={
		        userId:userId,
		        cmd:'searchnicknameByUserId'
		    };
		    
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
    
  //以下是callback函数的定义
    function mycallback(){
        if(xmlHttp.readyState==4)
        {
            if(xmlHttp.status==200)
            {
                console.log("请求成功响应");
                var data = xmlHttp.responseText;
                var nickname_obj= JSON.parse(data);
                console.log("昵称："+nickname_obj.nickname);
                
                ret_nickname = nickname_obj.nickname;
                console.log("赋值在前："+ret_nickname);
            }
            else{
                console.log("获取用户昵称 请求没有成功响应:"+xmlHttp.status);
            }
        }
    }
    console.log("返回在后："+ret_nickname);
    return ret_nickname;
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
        btn_join.setAttribute("topicId",topicItemData.topicId);//添加属性topicId
        tr_node3_td_node1.appendChild(btn_join);
    }
    else {
        var btn_invite=document.createElement("button");
        btn_invite.innerHTML="邀请";
        btn_invite.setAttribute("topicId",topicItemData.topicId);
        btn_invite.setAttribute("authorId",topicItemData.userId);
        var btn_join=document.createElement("button");
        btn_join.setAttribute("topicId",topicItemData.topicId);//添加属性id
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

//创建话题列表2
function createTopicList2(topicItemData,topic_list_node){
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
        btn_join.setAttribute("topicId",topicItemData.topicId);//添加属性topicId
        tr_node3_td_node1.appendChild(btn_join);
    }
    else {
        var btn_join=document.createElement("button");
        btn_join.setAttribute("topicId",topicItemData.topicId);
        btn_join.innerHTML="参与";
        btn_join.addEventListener("click",startChat);
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


//发起话题
function fqht(e){
	//获取用户输入的话题内容
    var textArea_node = document.querySelector("div.topic_input_box textarea");
    var content_value = textArea_node.value;//TODO :过滤敏感字符
    console.log("用户输入的内容：" + content_value);
    if(content_value=="")
    {
        return;
    }
    //将数据发往数据库
    var userName=document.getElementsByName("userName")[0].value;
    var userId=document.getElementsByName("userId")[0].value;
    
     //发送请求的请求参数
    var parameters={
        mytopic:content_value,
        topicAuthorName:userName,
        authorId:userId,
        cmd:'fqht'
    };
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
  //以下是callback函数的定义
    function mycallback(){
        if(xmlHttp.readyState==4)
        {
            if(xmlHttp.status==200)
            {
                var topicListData=xmlHttp.responseText;
                topicListData= JSON.parse(topicListData);
                //将topicList里的数据清空,将textArea用户输入的内容清空
                var textArea_node = document.querySelector("div.topic_input_box textarea");

                var topic_list_node=document.getElementsByClassName("topic-list")[0];
                topic_list_node.innerHTML="";//话题列表清空
                
                var topic_list_node  =document.querySelector("div#writeTopic div.topic-list");
                textArea_node.value="";//输入框内容清空
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

//话题搜索
function htss(event){
	//获取用户输入的内容
	var search_content=event.target.value;
	
     //发送请求
    var parameters={
        mytopic:search_content,
        cmd:'htss'
    };
    
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
    
    function mycallback(){
        if(xmlHttp.readyState==4)
        {
            if(xmlHttp.status==200)
            {
                var topicListData=xmlHttp.responseText;
                topicListData= JSON.parse(topicListData);
                //将topicList里的数据清空,将textArea用户输入的内容清空
                var textArea_node = document.querySelector("textarea.search_input_box");

                var topic_list_node = document.querySelector("div#searchTopic div.topic-list");
                
                topic_list_node.innerHTML="";
                textArea_node.innerHTML="";
                var t=new Date();
                var current_datetime=new Date().format("yyyy-MM-dd hh:mm:ss");
                textArea_node.value="";
                for(var i=0;i<topicListData.length;i++)
                {
                    createTopicList2(topicListData[i],topic_list_node);
                }
            }
            else{
                console.log("请求没有成功响应:"+xmlHttp.status);
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
    
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
    
  //以下是callback函数的定义
    function mycallback(){
        if(xmlHttp.readyState==4)
        {
            if(xmlHttp.status==200)
            {
                var topicListData=xmlHttp.responseText;
                topicListData= JSON.parse(topicListData);
                //将topicList里的数据清空

                var topic_list_node  =document.querySelector("div#historicalTopic div.topic-list");
                topic_list_node.innerHTML="";
              
                for(var i=0;i<topicListData.length;i++)
                {
                	//console.log(topicListData[i]);
                    //创建dom
                    createTopicList2(topicListData[i],topic_list_node);
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

//点击参与话题按钮后，用户参与话题
/**
 * topicId:话题Id
 */
function joinTopic(topicId)
{
	//请求参数
    var parameters={
        topicId:topicId,
        userId:_currentUserId,
        userName:_currentUserName,
        cmd:'joinTopic'
    };
    
    console.log("发送的用户名:"+_currentUserName);
  
    doRequestUsingPOST("servlet/topic?"+toDomString(parameters),mycallback);
  //以下是callback函数的定义
    function mycallback(){
        if(xmlHttp.readyState==4)
        {
            if(xmlHttp.status==200)
            {
            	//接收的是当前话题下的，联系人列表包括自己
            	var responseData=xmlHttp.responseText;
            	if(responseData!=""&&responseData!=null)
            	{
            		var contacts= JSON.parse(responseData);
     	           	var contacts_list=document.getElementsByClassName("contacts_list")[0];
     	        	var contacts_list_ul_node=contacts_list.getElementsByTagName("ul")[0];
     	        	contacts_list_ul_node.innerHTML="";//先赋空
                    for(var i=0;i<contacts.length;i++)
                    {
                       console.log(contacts[i]);
                 	   createContactsList(contacts[i]);
                    }
            	}
            }
            else{
                console.log("添加话题下的新成员，请求没有成功响应:"+xmlHttp.status);
            }
        }
    }
}

