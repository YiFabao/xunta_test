<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<div class="dialogue_box" boxId = "${requestScope.topic.topicId }" style ="display:none,overflow:auto">
        <div class="private_dialogue_body" topicId="${requestScope.topic.topicId}" msg_count="0">
            <div class="header">
				<ul>
					<li class="people_logo">
						<img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg" title="张三">
					</li>
					<li class="xunta_username">
						${requestScope.publisher.xunta_username }
					</li>
					<li class="topicName">
						话题:${requestScope.topic.topicName }
					</li>
					<li>
						<input type="button" value=" X " class="btn_exit">
					</li>
				</ul>        		
            </div>
            <!-- 联系人图像 -->
            <div class="contacts">
            	参与人：
	            <ul>
	            	<c:forEach items="${requestScope.memberList }" var="member">
	            	<li>
						<button userId = ${member.id } class="userId"> ${member.xunta_username}</button>
						<%-- <img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg" title="张三"> --%>
					</li>
	            	</c:forEach>
	
				</ul>
            </div>
            <div class="msg_bubble">
               <div class="mainBox">
                    <div class="content">
                        <div class="msg_bubble_list">
							${requestScope.publisher.xunta_username }的话题聊天框							
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
