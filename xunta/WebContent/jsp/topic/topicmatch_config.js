window.mydomain="http://"+document.domain+":21280/xunta/";
//全局变量
var _currentUserId=document.getElementsByName("userId")[0].value;
var _currentUserName=document.getElementsByName("userName")[0].value;

//websocket是否可用,默认是不可用的
var msgManagerReady=false;