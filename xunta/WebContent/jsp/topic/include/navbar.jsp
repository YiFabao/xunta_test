<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
                <li><a href="${pageContext.request.contextPath}/jsp/topic/httj.jsp" class="recommendTopic">话题推荐</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/topic/fqht.jsp" class="writeTopic">发起话题</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/topic/htjy.jsp" class="historicalTopic">话题记忆</a></li>
                <li class="top_profile">
                    <a href="#" class="userInfo">
                        <img src="${pageContext.request.contextPath}/jsp/topic/images/avatar.jpg">
                        <span>${sessionScope.user.xunta_username }</span>
                    </a>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/jsp/topic/myknowledge.jsp">我的知识</a></li>
                        <li><a href="${pageContext.request.contextPath}/jsp/topic/mymessage.jsp" class="message">消息</a></li>
                        <li><a href="javascript:void(0)" class="configure">设置</a></li>
                        <li><a href="javascript:void(0)" class="signOut">更换账号</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
<script>
	//全局环境变量
	var contextPath ="${pageContext.request.contextPath}";
</script>