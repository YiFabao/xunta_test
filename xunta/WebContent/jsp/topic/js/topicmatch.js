//所有文档完全加载完成后执行

window.onload=function(){
    console.log("所有页面资源包括图片加载完成");
    window._userId=document.getElementsByName("userId")[0].value;//发送消息人id
}
//dom结构加载后执行
window.addEventListener("DOMContentLoaded",function(){
    console.log("文档内容加载完毕");
    //创建websocket
    var fromUserid=document.getElementsByName("userId")[0].value;//发送消息人id;
    //var userName=document.getElementsByName("userName")[0].value;
    createWebsocketConnect(fromUserid);
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
    //添加聊天框发送消息监听事件
    addChatSendMessageListener();
});

//监听document加载状态
window.document.addEventListener("readystatechange",function(){
    console.log("加载状态:"+document.readyState);
});

