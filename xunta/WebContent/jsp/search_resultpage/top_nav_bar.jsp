<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/_00meta.inc"%>

		<div class="navbar navbar-inverse navbar-fixed-top">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse"	data-target=".navbar-collapse">
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="<%=basePath%>index.jsp"><font size="2">首页</font></a>
				</div>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav">
						<li>
							<a href="http://www.aigine.com">关于语擎</a><%--
							<a href="servlet/Login?mark=qq&status=click">QQ</a>
						--%></li>
					</ul>
	
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
								<a href="#" data-toggle="dropdown">帮助/讨论<b class="caret"></b> </a>
								<!-- dropdown? -->
								<ul  class="dropdown-menu" >
									<li><a href="http://jq.qq.com/?_wv=1027&k=RKZJsB"  target="_blank">进QQ群提问</a></li>
									<li><a href="http://weibo.com/XuntaSo" target="_blank">关注微博官方账号</a></li>
									<li onmouseover="js_show_weixinImg()" onmouseout="js_hidden_weixinImg()">
										<a href="javascript:void(0);">关注微信官方账号</a>
									</li>
									<li id="img_weixin" style="display:none">
											<a href="javascript:void(0);"><img src="assets/images/qrcode_for_gh_43f17f8952ea_258.jpg" width="80px" height="80px"/></a>
									</li>
								</ul>
						</li>
					</ul>
			
			</div>
				<!--/.nav-collapse -->
			</div>
		</div>
		
									
	