<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/inc/_00meta.inc"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String timepoint=(String)request.getAttribute("timepoint");
%>
<!DOCTYPE html >
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="xunta.so" content="寻TA网">
	<meta name="description" content="寻TA网_跨社区旅游搜人引擎">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>寻TA网_跨社区旅游搜人引擎|XunTa.so|输入"知识点"关键词,寻找可信赖的旅伴,团长,导游,店家,餐馆,车主,博主...</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- 为解决ie9不兼容的问题后加上的. -->
	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
	<title>搜索结果</title>
	<link rel="stylesheet"
		href="<%=basePath%>assets/bootstrap/3.0.0/css/bootstrap.min.css"
		type="text/css" />
	<link rel="stylesheet"
		href="<%=basePath%>assets/bootstrap/3.0.0/css/bootstrap-theme.min.css"
		type="text/css" />
	<link rel="stylesheet"
		href="<%=basePath%>assets/stylesheets/application.css"
		type="text/css" />
</head>
<body>
<%@ include file="top_nav_bar.jsp"%>
<div class="top-search-bar">
	<div class="container">

		<!-- 最新/最相关 -->
		<ol class="breadcrumb">
			<li class="">
				<a href="javascript:js_post('<%=basePath%>psearch?searchMode=newest&searchKeywords=${pageData.searchKeywords}')">最新</a>
			</li>
			<li class="active">
				达人
			</li>
		</ol>
		<!-- /最新/最相关 -->
		<%@ include file="search_form.jsp"%>
	</div>
</div>
<font class="switch" color="gray">..</font>
<br>
<div class="container">

	<!-- 搜索结果 -->
	<div class="h-gap"></div>

	<!-- 上端翻页 -->
	<%@ include file="pagination.jsp"%>
	<!-- /上端翻页 -->
	<br>
	<br>
	<!-- 作者循环 -->
	<c:forEach items="${pageData.getAuthorList()}" var="authorInfo">
		<div class="row result-item-user-row"
			data-author="${authorInfo.author}" data-site="${authorInfo.site}">

			<!-- 作者名/网站名/得分 -->
			<a href="javascript:js_post_open('author_profile?author=${authorInfo.author}&site=${authorInfo.site}&searchMode=relevant&searchKeywords=${pageData.searchKeywords}')">
				<div class="col-md-2 col-user">
					<font color="black">
						<span class="glyphicon glyphicon-user"></span>
					</font>
					<label>
						<font color="black">${authorInfo.author}</font>
					</label>
					<span class="site">${authorInfo.site}</span>
					<span class="total-score" style="display:none">(${authorInfo.totalScore})</span>
					<br />
					<!-- 
					<div author="${authorInfo.author}" site="${authorInfo.site}">
						<font color="green">头帖数: </font>
						<font color="orange"><fmt:formatNumber
								value="${authorInfo.leadPosters}" pattern="#" />
						</font>
						<br />
						<font color="green">跟帖数: </font><font color="orange"><fmt:formatNumber
								value="${authorInfo.followPosters}" pattern="#" />
						</font>
						<br />
						<font color="green">重复帖: </font><font color="orange"><fmt:formatNumber
								value="${authorInfo.duplicatePosters}" pattern="#" />
						</font>
					</div>
					 -->
				</div>
			</a>

			<!-- 作者文章 -->
			<div class="col-md-10 col-content">
				<!-- div class="h-gap"></div-->

				<!-- 某作者的文章标题/正文 -->
				<div class="user-tab-content">
					<ul>
						<!-- 遍历某作者的文章标题/正文 -->
						<c:forEach items="${authorInfo.getAuthorDocData_Set()}"
							var="docData">
							<li>
								<span class="date">${docData.date}</span>
								<div
									style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-right: 15%">
									<a href="${docData.url}" target="_blank">
										<font class="display_switch" style="color:gray;display:none">(${docData.docID})</font>${docData.title}
									</a>
								</div>
								<p>
									${docData.getHighlightedContent()} 
									<font class="display_switch" color=gray style="display:none">(${docData.score})</font>
								</p>
							</li>
						</c:forEach>
					</ul>

					<div class="h-gap"></div>

					<div style="height: 60px;">
						<span style="float: left"><span
							class="label label-default displaydocsnum"></span> </span>
						<button style="float: right" type="button"
							class="btn btn-default btn-xs disabled" getmore="0"
							docNumShown="" thisAuthorRank=""
							thisAuthorTotalDocs="${authorInfo.getTotalAuthorDocs()}">
							更多
						</button>
						<button style="float: right" type="button"
							class="btn btn-default btn-xs" foldup="foldup">
							收起
						</button>
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
	<!-- /搜索结果 -->

	<!-- 下端翻页 -->
	<%@ include file="pagination.jsp"%>
	<!-- /下端翻页 -->
	<br>
	<br>

	<hr />
	<footer>
	<p align="center">
		© XunTa.so 2013
	</p>
	</footer>

</div>
<!-- /.container -->

<!-- Placed at the end of the document so the pages load faster 为什么?-->
<!-- 这里原来放了js库 -->
<!-- Placed at the end of the document so the pages load faster 为什么?-->
<script type="text/javascript" src="<%=basePath%>assets/javascripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/javascripts/application.js"></script>

<!-- 显示badge元素中的数字 -->
<script type="text/javascript">
$(document).ready(function() {
	var rank = 0;
	$('.result-item-user-row').each(//加载后一次执行: 计算未显示的文档数还有多少: 作者文档总数(通过jsp置于badge的属性中)-页面显示数(通过循环各楼层来统计已显文档数)
			function() {
				rank = rank + 1;
				updateBadgeNum(this, rank);
			})
})
function updateBadgeNum(resultItemUserRow_Element, currentAuthorRank) {//更新getmore中的两个属性值(已显文档数及作者当前楼层)以及badge中显示的剩余文档数.
	var docNumShown = $(resultItemUserRow_Element).find('ul li').size();
	//alert("已显文档数:"+docNumShown);
	var getmoreElement = $(resultItemUserRow_Element).find('[getmore]');
	//alert("getmoreElement.size():  "+getmoreElement.size());
	var totalDocs = getmoreElement.attr('thisAuthorTotalDocs');
	getmoreElement.attr('docNumShown', docNumShown);
	getmoreElement.attr('thisAuthorRank', currentAuthorRank);
	var remainDocs = totalDocs - docNumShown;
	//$(resultItemUserRow_Element).find('div .badge').text("本页排名:"+currentAuthorRank+"|"+getmoreElement.attr('thisAuthorRank')+"|还有:"+remainDocs + '已显:' + docNumShown + ' 全部:'	+ totalDocs);
	$(resultItemUserRow_Element).find('div .displaydocsnum').text(
			docNumShown + "/" + totalDocs);
	//diable 更多 和 收起按钮.
	if (remainDocs > 0) {
		getmoreElement.attr('class', 'btn btn-default btn-xs');
	} else {
		getmoreElement.attr('class', 'btn btn-default btn-xs disabled');
	}
	var foldUpEle = $(resultItemUserRow_Element).find('[foldup]');
	//alert( "foldupE size:"+$(resultItemUserRow_Element).find('[foldup]').size()); 
	if (docNumShown > 1) {
		foldUpEle.attr('class', 'btn btn-default btn-xs');
	} else {
		foldUpEle.attr('class', 'btn btn-default btn-xs disabled');
	}

}
</script>
<!-- /显示badge元素中的数字 -->

<!-- 点击"更多" -->
<script type="text/javascript">
$(document).ready(
		function() {
			$('[getmore]').click(function() {
				//alert($(this).parent().parent().parent().parent().attr('data-author')+"|"+$(this).parent().parent().parent().parent().attr('data-site')+"|已显文档:"+$(this).attr('docNumShown')+"全部文档:"+$(this).attr('thisAuthorTotalDocs'));
					//alert($(this).parents().find('.result-item-user-row').attr('data-author')+"|"+$(this).parents().find('.result-item-user-row').attr('data-site'));
					//用parents().find('.result-item-user-row')总是定位第一楼的作者名.为什么
					currentElement = this; //下面的回调函数不能使用this,因此这里先声明一下.
					$.post("getmoredocs", {//这里是json格式的数据值对.
							searchMode : "relevant",
							searchKeywords : $('.search-keywords')
									.attr('value'),
							timepoint: <%=timepoint%>,
							thisAuthorRank : $(this).attr(
									'thisAuthorRank'), //thisAuthorRank采用完全重新搜索后这个排序号就用不上了.
							docNumShown : $(this).attr(
									'docNumShown'),
							moreDocsFetchNum : "10",
							author : $(this).parent().parent()
									.parent().parent().attr(
											'data-author'),
							site : $(this).parent().parent()
									.parent().parent().attr(
											'data-site')
						}, function(data, status) {//callback函数.//其它方法:function(responseTxt,statusTxt,xhr)
							//alert("testing this!!! "+$(currentElement).parent().parent().parent().parent().attr('data-site'));
						//alert("data:"+data+"|status:"+status);
						$(currentElement).parent().parent()
								.parent().parent().find('ul li')
								.last().after(data);
						//$('div ul li').after(data);
						tmpE = $(currentElement).parent().parent()
								.parent().parent();
						//alert(tmpE[0].tagName+"|"+tmpE.attr('class'));
						updateBadgeNum(tmpE, $(currentElement)
								.attr('thisAuthorRank'))
						//上面第一个参数指向的元素要回溯到$('.result-item-user-row'),以便同上面的调用参数指向保持一致.
					});
				})
			$('[foldup]').click(function() {
				var currentE = this;
				var topNode = $(currentE).parent().parent().parent().parent();
				var docsShown = topNode.find('ul li');
				var foldUpTo = parseInt(docsShown.size() / 2) - 1;//收起到哪一个文档:先除2,取整,再加一.eq(i)是从0算起的.每次收起一半,最后剩一个时不允许收了.
					$(currentE).parent().parent().parent().parent().find(
							'ul li:gt(' + foldUpTo + ')').remove();
					updateBadgeNum(topNode, $(currentE).parent().find(
							'[getmore]').attr('thisAuthorRank'));
				})
		})
</script>

<!-- /点击"更多" -->
<script type="text/javascript">
//该函数用于对提交的url进行编码然后请求,并刷新页面.其目的是解决url中直接嵌入中文参数引起的乱码问题.2014.4.27
function js_post(url) {
	codedUrl = encodeURI(url);
	//alert(codedUrl);
	window.location.replace(codedUrl);
	//$.post(codedUrl,
	//	function(data,status){
	//	document.write(data);
	//	}
	//);
}
function js_post_open(url) {//用处理过的url发请求,打开一个新页面.2014.6.10
	codedUrl = encodeURI(url);
	window.open(codedUrl);
}

//微信图片的显示
function js_show_weixinImg()
{
	$("#img_weixin").fadeIn();
}
function js_hidden_weixinImg()
{
	$("#img_weixin").fadeOut();
}
//文档id,分数等的显示与隐藏
$(".switch").click(function(){
		$(".display_switch").toggle();
		$(".total-score").toggle();
});

function stringToJson(stringValue) 
{ 
	eval("var theJsonValue = "+stringValue); 
	return theJsonValue; 
}
function json2str(o) { 
	var arr = []; 
	var fmt = function(s) { 
	if (typeof s == 'object' && s != null) return json2str(s); 
		return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s; 
	};
	for (var i in o) arr.push("'" + i + "':" + fmt(o[i])); 
	return '{' + arr.join(',') + '}'; 
} 
</script>
</body>
</html>
