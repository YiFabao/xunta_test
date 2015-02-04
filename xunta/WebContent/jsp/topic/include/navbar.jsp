<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/topic/css/navbar.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/topic/css/navbar_msg_tab.css"> --%>
<div class="top">
    <div class="top_content">
        <!--logo-->
        <div class="top_logo">
                <img src="${pageContext.request.contextPath}/jsp/topic/images/logo.jpg">
        </div>
        <!--全局导航-->
        <div class="top_global_nav">
            <ul class="menu">
                <li>
                   <a href="javascript:void(0)"> 
	                   <input type="text" id="search_word">
	                   <button id="btn_search">搜索</button>
                   </a>
                </li>
                <li><a href="javascript:void(0)" class="recommendTopic">话题推荐</a></li>
                <li><a href="javascript:void(0)" class="writeTopic">发起话题</a></li>
                <li><a href="javascript:void(0)" class="historicalTopic">话题记忆</a></li>
<%--                 <li><a href="${pageContext.request.contextPath}/jsp/topic/httj.jsp" class="recommendTopic">话题推荐</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/topic/fqht.jsp" class="writeTopic">发起话题</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/topic/htjy.jsp" class="historicalTopic">话题记忆</a></li> --%>
                <li class="top_profile">
                    <a href="#" class="userInfo">
                        <img src="${pageContext.request.contextPath}/jsp/topic/images/avatar.jpg">
                        <span>${sessionScope.user.xunta_username }</span>
                    </a>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/jsp/topic/myknowledge.jsp">我的知识</a></li>
                        <li><a href="javascript:void(0)" class="message">消息</a></li>
<%--                         <li><a href="${pageContext.request.contextPath}/servlet/topic_service?cmd=msgalert" class="message">消息</a></li> --%>
                        <li><a href="javascript:void(0)" class="configure">设置</a></li>
                        <li><a href="${pageContext.request.contextPath}/servlet/topic_service?cmd=exit" class="signOut">更换账号</a></li>
                    </ul>
                </li>
            </ul>
        	<ul class="request_msg_num_menu">
        		<li>
				 <div id="topic_request_msg_num" class="nav_bar_msgalert">
				 	<em id="topic_invite_msg_num" num="">0</em>
				 </div>
				 <div class="alert_msg_pull_down" style="display:none">
                		<div class="msg_tab">
							<div class="tab">
						    	<a href="javascript:;" class="on" type="topic_request">话题请求</a>
						        <a href="javascript:;" type="sys_msg">系统消息</a>
						    </div>
						    <div class="content">
						    	<ul>
						        	<li style="display:block;">
						        		<!-- 话题请求内容 -->
<!-- 						        		<div>
						        			<p>张三 邀请您参与话题　#黄山哪好玩#</p>
						        			<button>同意</button>
						        			<button>不同意</button>
						        		</div>
						        		<div>
						        			<span>张三 邀请您参与话题　#黄山哪好玩#</span>
						        			<span>已同意</span>
						        		</div> -->
						        	</li>
						            <li>
						            	<!--系统消息内容  -->
<!-- 						            	<div>
						        			<span>张三 接受了参与话题　#黄山哪好玩#　的邀请</span>
						        		</div> -->
						            </li>
						        </ul>
						    </div>
						</div>
                	</div>
        		</li>
        	</ul>
        </div>
    </div>
</div>
<%-- <script src="${pageContext.request.contextPath }/assets/javascripts/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath }/jsp/topic/js/nav_bar_msg_tab.js"></script> --%>
<script>
	//全局环境变量
	var contextPath ="${pageContext.request.contextPath}";
	var currentUserId ="${sessionScope.user.id}";
	var currentUserName="${sessionScope.user.xunta_username}";
</script>