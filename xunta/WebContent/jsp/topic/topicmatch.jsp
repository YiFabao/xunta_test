<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">

    <title>话题匹配</title>
    <link rel="stylesheet" type="text/css" href="topicmatch.css">
</head>
<body>
<div class="top">
    <div class="top_content">
        <!--logo-->
        <div class="top_logo">
                <img src="images/logo.jpg">
        </div>
        <!--全局导航-->
        <div class="top_global_nav">
            <ul class="menu">
                <li><a href="#">首页</a></li>
                <li class="myTopic">
                    <a href="javascript:void(0)">我的话题</a>
                    <ul>
                        <li><a href="javascript:void(0)" class="writeTopic">发起话题</a></li>
                        <li><a href="javascript:void(0)" class="recommendTopic">话题推荐</a></li>
                        <li><a href="javascript:void(0)" class="historicalTopic">话题记忆</a></li>
                        <li><a href="javascript:void(0)" class="searchTopic">话题搜索</a></li>
                    </ul>
                </li>
                <li><a href="#">我的知识</a></li>
                <li class="top_profile">
                    <a href="#" class="userInfo">
                        <img src="images/avatar.jpg">
                        <span>${sessionScope.user.xunta_username }</span>
                    </a>
                    <ul>
                        <li><a href="javascript:void(0)" class="homePage">我的主页</a></li>
                        <li><a href="javascript:void(0)" class="message">消息</a></li>
                        <li><a href="javascript:void(0)" class="configure">设置</a></li>
                        <li><a href="javascript:void(0)" class="signOut">退出</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>

<div class="body">
    <!--发起话题-->
    <div class="body_content">
        <div class="myTopic">

            <div class="myTopic_SubPage" id="writeTopic">

                <div  class="topic_input_box">
                    <span>有什么新鲜事告诉家？</span>
                    <textarea></textarea><br>
                    <button>发布</button>
                </div>
                <div class="line"></div>
                <div class="topic-list">
                    <table>
                        <tr>
                            <td class="img">
                                <img src="images/1.jpg">
                            </td>
                            <td class="name">贾乃亮</td>
                        </tr>
                        <tr>
                            <td class="content" colspan="2">
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <button>邀请</button><button>参与</button>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="datetime">
                                <small>2014-12-31 23:34:10</small>
                            </td>
                        </tr>
                    </table>
                 </div>
            </div>

            <div class="myTopic_SubPage" id="recommendTopic">
                <div class="topic-list">
                    <table>
                        <tr>
                            <td class="img">
                                <img src="images/1.jpg">
                            </td>
                            <td class="name">贾乃亮</td>
                        </tr>
                        <tr>
                            <td class="content" colspan="2">
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="datetime">
                                <small>2014-12-31 23:34:10</small>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div class="myTopic_SubPage" id="historicalTopic">
                <div class="topic-list">
                    <table>
                        <tr>
                            <td class="img">
                                <img src="images/1.jpg">
                            </td>
                            <td class="name">贾乃亮</td>
                        </tr>
                        <tr>
                            <td class="content" colspan="2">
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="datetime">
                                <small>2014-12-31 23:34:10</small>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div class="myTopic_SubPage" id="searchTopic">
                <div class="topic-list">
                    <table>
                        <tr>
                            <td class="img">
                                <img src="images/1.jpg">
                            </td>
                            <td class="name">贾乃亮</td>
                        </tr>
                        <tr>
                            <td class="content" colspan="2">
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                                如果你早认清你在别人心中没那么重要，你会快乐很多
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="datetime">
                                <small>2014-12-31 23:34:10</small>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="footing"></div>
<div class="webim_page">
    <div class="people_list">
        <div class="search webim_search">
            <span class="search_s">
                <input type="text" maxlength="10" value="查找话题或联系人">
                <span class="pos">
                    <a href="javascript:;" title="搜索">f</a>
                </span>

            </span>
        </div>
        <div class="contacts_list">
          <ul>
              <li class="active">
                  <div class="head">
                      <img src="http://tp3.sinaimg.cn/1298064414/50/5617529344/1">
                  </div>
                  <p class="nick_name">黄刚-物流与供应链物流与供应链</p>
              </li>
              <li>
                  <div class="head">
                      <img src="http://tp4.sinaimg.cn/2074109747/50/5715079216/1">
                  </div>
                  <p class="nick_name">毛启盈</p>
              </li>
              <li>
                  <div class="head">
                      <img src="http://tp4.sinaimg.cn/2074109747/50/5715079216/1">
                  </div>
                  <p class="nick_name">黎万强</p>
              </li>
          </ul>
      </div>
    </div>
    <div class="dialogue_box">
        <div class="private_dialogue_body">
            <div class="header">
                <span class="name"></span>
            </div>
            <div class="msg_bubble">
               <div id="mainBox">
                    <div id="content">
                        <div class="msg_bubble_list">

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="private_send_box">
            <div class="sendbox_area">
                <textarea class="msg_input" placeholder="按回车键发送信息"></textarea>
            </div>
        </div>
    </div>
</div>
自己的id：<input type="text" name="fromUserId"><br>
发送消息的 Id:<input type="text" name="toUserId"><br>
话题id:<input type="text" name="topicId"><br>
<button id="confirm">确认</button>

<script src="topicmatch.js"></script>
<script src="websocket.js"></script>
<script src="myscroll.js"></script>
<script>

</script>
</body>
</html>
