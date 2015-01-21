<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jsp/topic/css/chat_box.css">
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
        <div class="topic_group_list">
           <ul>
              <li class="active">
                  <div class="head">
                      <img src="http://tp3.sinaimg.cn/1298064414/50/5617529344/1">
                  </div>
                  <p class=topic_name>临界性与群体中的长程关联</p>
              </li>
              <li>
                  <div class="head">
                      <img src="http://tp4.sinaimg.cn/2074109747/50/5715079216/1">
                  </div>
                  <p class="topic_name">心理学上，如何解释喜欢做幼稚或极端行为引别人注意?</p>
              </li>
              <li>
                  <div class="head">
                      <img src="http://tp4.sinaimg.cn/2074109747/50/5715079216/1">
                  </div>
                  <p class="topic_name">国内外素描的区别怎样？</p>
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
					<li class="nickname">
						张三
					</li>
					<li class="topic">
						临界性与群体中的长程关联
					</li>
					<li>
						<input type="button" value=" X ">
					</li>
				</ul>        		
            </div>
            <div class="contacts">
	            <ul>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg" title="张三">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/2.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/3.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/4.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/5.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/2.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/3.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/4.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/5.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/2.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/3.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/4.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/5.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/2.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/3.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/4.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/5.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/2.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/3.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/4.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/5.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/1.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/2.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/3.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/4.jpg">
					</li>
					<li>
						<img src="${pageContext.request.contextPath}/jsp/topic/images/5.jpg">
					</li>
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
