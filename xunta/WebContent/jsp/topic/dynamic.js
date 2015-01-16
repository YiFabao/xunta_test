//显示我的话题下的子模块,显示某一个，隐藏其他的
var timer=null;
var currentView=document.getElementById("writeTopic");
function showMyTopicSubpage(id) {
    if(currentView.id==id)
    {
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