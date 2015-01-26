<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jsp/topic/css/chat_box.css">
<style>
	.msgAlert{
		width:200px;
		height:48px;
		border-radius:4px;
		border:1px solid #ccc;
		box-shadow: 1px 1px 1px rgba(0,0,0,0.15);
		position:fixed;
		bottom:5px;
		right:5px;
	}
	
</style>
<div class="webim_page" style="display:block" id="webim_page">
    <div class="people_list">
        <div class="search webim_search">
            <span class="search_s">
                <input type="text" maxlength="10" value="查找话题或联系人">
                <span class="pos">
                    <a href="javascript:;" title="搜索">f</a>
                </span>

            </span>
        </div>
        <div class="topic_group_list">
           <ul>
              <li class="active">
                  <div class="head">
                      <img src="http://tp3.sinaimg.cn/1298064414/50/5617529344/1">
                  </div>
                  <p class=topic_name>${requestScope.topic.topicName }</p>
              </li>
          </ul>
      </div>
    </div>
    <div class="dialogue_box">
        <div class="private_dialogue_body" topicId="" msg_count="0">
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
						<input type="button" value=" X " id="btn_exit">
					</li>
				</ul>        		
            </div>
            <!-- 联系人图像 -->
            <div class="contacts">
            	参与人：
	            <ul>
	            	<c:forEach items="${requestScope.memberList }" var="member">
	            	<li>
						<button> ${member.xunta_username}</button>
						<%-- <img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg" title="张三"> --%>
					</li>
	            	</c:forEach>
	
				</ul>
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

<div class="msgAlert" id="bar_message" style="display:none">你有4条私信</div>
<script>
	var bar_message = document.getElementById("bar_message");
	bar_message.addEventListener("click",function(event){
		this.style.display = "none";
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "block";
	});
	
	//点击退出按钮
	var btn_exit = document.getElementById("btn_exit");
	btn_exit.addEventListener("click",function(event){
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "none";
		bar_message.style.display = "block";
	});
	
	function showBarMessage()
	{
		var bar_message = document.getElementById("bar_message");
		var webim_page = document.getElementById("webim_page");
		webim_page.style.display = "none";
		bar_message.style.display = "block";
	}
	

	 document.addEventListener("click",function(e){
		 if(e.target.tagName=="TD")return;
		//聊天框是否隐藏起来
		var webim_page = document.getElementById("webim_page");
		if(webim_page.style.display=="none"){
			return;
		}
		else{
			//计算鼠标点击的坐票
			var mouse_x = e.clientX;
			var mouse_y = e.clientY;
			console.log(mouse_x+"  "+mouse_y);
			//计算聊天框的左顶点坐标
			var webim_page_position = getPositionOnScreen(webim_page);
			console.log(webim_page_position.x +"  "+webim_page_position.y);
			//如果鼠标点击的地方在聊天窗口之外就隐藏聊天框，显示消息框
			if(!(mouse_x>webim_page_position.x&&mouse_y>webim_page_position.y))
			{
				showBarMessage();
			}
		}
	}); 
	
	function getPositionOnScreen(obj){
        /**
         * 获取元素4个角的坐标{x1:,x2,y1,y2}
         * @param obj 元素
         */
        var _this=obj;
        var x= 0,y=0;
        do{
            x+=obj.offsetLeft;
            y+=obj.offsetTop;
        }while(obj=obj.offsetParent);

        return {
            x:x,
            y:y
        }
	}
	
</script>
