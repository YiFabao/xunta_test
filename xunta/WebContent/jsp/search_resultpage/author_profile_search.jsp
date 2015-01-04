<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/inc/_00meta.inc"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String ipaddress=request.getRemoteAddr();
%>

<!DOCTYPE html >
<html lang="zh-CN">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="xunta.so" content="寻TA网">
		<meta name="description" content="寻TA网_跨社区旅游搜人引擎">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>寻TA网_跨社区旅游搜人引擎|XunTa.so|输入"知识点"关键词,寻找可信赖的旅伴,团长,导游,店家,餐馆,车主,博主...</title>
		<!-- 为解决ie9不兼容的问题后加上的. -->
		<title>搜索结果</title>
		<!-- link具体指什么? include?  -->
		<link rel="stylesheet"
			href="<%=basePath%>assets/bootstrap/3.0.0/css/bootstrap.min.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=basePath%>assets/bootstrap/3.0.0/css/bootstrap-theme.min.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=basePath%>assets/stylesheets/application.css"
			type="text/css" />
		<script type="text/javascript" src="<%=basePath %>assets/d3/d3.js"></script>

	</head>
	<body>
		<%@ include file="top_nav_bar.jsp"%>
		<font class="switch" color="gray">..</font>
		<br>

		<div class="container">

			<div class="row result-item-user-row">
				<!-- 作者名/网站名/得分 -->
				<div class="col-md-2 col-user">
					<span class="glyphicon glyphicon-user"></span>
					<label class="data-author">${pageData.getAuthor()}</label>
					<p><span class="data-site">${pageData.getSite()}</span></p>
					<!--
					<div author="${pageData.getAuthor()}" site="${pageData.getSite()}">
					<font color="gray">原创度: </font>
					<font color="gray" class="leadPosters"> </font>
					<br />
					<font color="gray" >参与度: </font><font color="gray" class="followPosters"> </font>
					<br />
					<font color="gray" >灌水度: </font><font color="gray" class="dumplicatePosters"> </font>
					</div>
				  -->
				</div>
				<div class="col-md-10 col-content">
					<!-- 搜索/全部 -->
					<ul class="nav nav-tabs">
						<li id="tab1" class="active">
							<a href="javascript:js_post('<%=basePath%>author_profile?author=${pageData.author}&site=${pageData.site}&searchMode=newest&searchKeywords=${pageData.searchKeywords}')">搜索</a>
						</li>
						<li  id="tab2" class="">
							<!-- 下面的keywords在搜索all时本不需要,但为了保持兼容性而保留 -->
							<a href="javascript:js_post('<%=basePath%>author_profile?author=${pageData.author}&site=${pageData.site}&searchMode=all&searchKeywords=${pageData.searchKeywords}')">全部</a>
						</li>
						<li id="tab3" class="">
							<a>知识图谱</a>
						</li>
					</ul>
					<!-- /搜索/全部 -->


					<!-- 搜索条 -->
					<div id="content">
					<div class="top-search-bar">
						<div class="container container-fluid">

							<!-- 最新/最相关 -->
							<c:if test="${pageData.searchMode=='newest'}">
								<ol class="breadcrumb new_relevant_tabs">
									<li class="active newest">
										最新
									</li>
									<li class="relevant">
										<a
											href="javascript:newest_relevant_Post('relevant','${pageData.getSearchKeywords()}')">相关</a>
									</li>
								</ol>
							</c:if>
							<c:if test="${pageData.searchMode=='relevant'}">
								<ol class="breadcrumb new_relevant_tabs">
									<li class="newest">
										<a
											href="javascript:newest_relevant_Post('newest','${pageData.getSearchKeywords()}')">最新</a>
									</li>
									<li class="active relevant">
										相关
									</li>
								</ol>
							</c:if>
							<!-- /最新/最相关 -->

							<form class="form-inline search-form" role="form"
								action="<%=basePath%>author_profile">
								<input type="hidden" name="searchMode" value="${pageData.searchMode}">
								<input type="hidden" name="author" value="${pageData.author}">
								<input type="hidden" name="site" value="${pageData.site}">

								<div class="col-lg-4">
									<div class="input-group">
										<label class="sr-only" for="keyword">
											搜索关键词
										</label>
										<input style="width: 450px;"
											class="form-control search-keywords" type="text"
											name="searchKeywords" placeholder="请输入搜索关键词"
											value="${pageData.getSearchKeywords()}" />
										<span class="input-group-btn">
											<button class="btn btn-primary" type="submit">
												.SO
											</button> </span>
									</div>
									<!-- /input-group -->
								</div>
							</form>
						</div>
			
					</div>

					<!-- 搜索结果:作者文档的标题/正文 -->
					<div class="user-tab-content">
						<ul>
							<%@ include file="getMore.jsp"%>
						</ul>
						<div class="h-gap"></div>
						<div style="height: 60px; text-align: right;">
							<span style="float: left"><span
								class="label label-default displaydocsnum"></span> </span>
							<button type="button" class="btn btn-default btn-xs disabled"
								searchMode="${pageData.searchMode}" getmore="0" docNumShown=""
								thisAuthorRank=""
								thisAuthorTotalDocs="${pageData.getTotalDocs()}">
								更多
							</button>
							<button type="button" class="btn btn-default btn-xs"
								foldup="foldup">
								收起
							</button>
						</div>
					</div>
					</div>
					<!-- end of <div class="user-tab-content"> -->
				</div>
				<!-- end of <div class="col-md-10 col-content"> -->
			</div>
			<!-- end of <div class="row result-item-user-row" > -->
			<!-- /搜索结果:作者文档的标题/正文 -->
		</div>
		<!--end of <div class="container"> -->

		<hr />
		<footer>
		<p align="center">
			© XunTa.so 2013
		</p>
		</footer>

		<!-- /.container -->

		<!-- Placed at the end of the document so the pages load faster 为什么?-->
		<!-- 这里原来放了js库 -->
		<!-- Placed at the end of the document so the pages load faster 为什么?-->
		<script type="text/javascript"
			src="<%=basePath%>assets/javascripts/jquery-1.10.2.js">
</script>
		<script type="text/javascript"
			src="<%=basePath%>assets/bootstrap/3.0.0/js/bootstrap.min.js">
</script>
		<script type="text/javascript"
			src="<%=basePath%>assets/javascripts/application.js">
</script>
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
	var docNumShown = $(resultItemUserRow_Element).find('.user-tab-content')
			.find('ul li').size();
	//alert("已显文档数:"+docNumShown);
	var getmoreElement = $(resultItemUserRow_Element).find('[getmore]');
	//alert("getmoreElement.size():  "+getmoreElement.size());
	var totalDocs = getmoreElement.attr('thisAuthorTotalDocs');
	getmoreElement.attr('docNumShown', docNumShown);
	getmoreElement.attr('thisAuthorRank', currentAuthorRank);
	var remainDocs = totalDocs - docNumShown;
	//$(resultItemUserRow_Element).find('div .badge').text("本页排名:" + currentAuthorRank + "|"	+ getmoreElement.attr('thisAuthorRank') + "|还有:" + remainDocs + '已显:' + docNumShown + ' 全部:' + totalDocs);
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
		function getMore_Click() {
			$('[getmore]').click(function() {
				//alert($(this).parent().parent().parent().parent().attr('data-author')+"|"+$(this).parent().parent().parent().parent().attr('data-site')+"|已显文档:"+$(this).attr('docNumShown')+"全部文档:"+$(this).attr('thisAuthorTotalDocs'));
					//alert($('.data-author').text()+"|"+$('.data-site').text());
					//用parents().find('.result-item-user-row')总是定位第一楼的作者名.为什么
					currentElement = this; //下面的回调函数不能使用this,因此这里先声明一下.
					$
							.post("getmoredocs", {//这里是json格式的数据值对.
										searchMode : $('[getmore]').attr('searchMode'),
										searchKeywords : $('.search-keywords').attr('value'),
										thisAuthorRank : $(this).attr('thisAuthorRank'), //thisAuthorRank采用完全重新搜索后这个排序号就用不上了.
										docNumShown : $(this).attr('docNumShown'),
										moreDocsFetchNum :"50",
										author:$('.data-author').text(),
										site:$('.data-site').text()
									}, function(data, status) {//callback函数.//其它方法:function(responseTxt,statusTxt,xhr)
										//alert("testing this!!! "+$(currentElement).parent().parent().parent().parent().attr('data-site'));
									//alert("data:"+data+"|status:"+status);
									$(currentElement).parent().parent()
											.parent().parent().find('ul li')
											.last().after(data);
									//$('div ul li').after(data);
									topNode = $(currentElement).parent()
											.parent().parent().parent();
									//alert(tmpE[0].tagName+"|"+tmpE.attr('class'));
									updateBadgeNum(topNode, $(currentElement)
											.attr('thisAuthorRank'));
									//上面第一个参数指向的元素要回溯到$('.result-item-user-row'),以便同上面的调用参数指向保持一致.
								});
				})
			$('[foldup]').click(function() {

				var currentE = this;
				var topNode = $(currentE).parent().parent();
				var docsShown = topNode.find('ul li');
				//alert("docsshown: "+docsShown.size());
					var foldUpTo = parseInt(docsShown.size() / 2) - 1;//收起到哪一个文档:先除2,取整,再加一.eq(i)是从0算起的.每次收起一半,最后剩一个时不允许收了.
					//alert("foldupto: "+foldUpTo);
					$(currentE).parent().parent().find(
							'ul li:gt(' + foldUpTo + ')').remove();
					updateBadgeNum($('.result-item-user-row').last(), $(
							currentE).parent().find('[getmore]').attr(
							'thisAuthorRank'));
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
</script>

		<script type="text/javascript">
function newest_relevant_Post(searchMode, searchKeywords) { //更改搜索方式: 最新还是相关. 此外没有采用刷页的方式,而是动态更新.moreDocsFetchNum应该和AuthorProfileResponse里的初显文档数一样:apV.moreDocsFetchNum
	//alert('dfa'); 
	$.post("getmoredocs", {//这里是json格式的数据值对.//
				searchMode : searchMode,
				searchKeywords : searchKeywords,
				docNumShown : "0",
				moreDocsFetchNum : "100",
				author : "${pageData.getAuthor()}",
				site : "${pageData.getSite()}"
			}, function(data, status) {//callback函数.//其它方法:function(responseTxt,statusTxt,xhr)
				//alert("data:"+data+"|status:"+status);
			//$('.user-tab-content').parent().parent().parent().parent().find('ul li').last().after(data);
			$('.user-tab-content').find('ul').html(data);
			//$('div ul li').after(data);
			topNode = $('.result-item-user-row').last();
			//alert(tmpE[0].tagName+"|"+tmpE.attr('class'));
			
			updateBadgeNum(topNode, '0');

			//将"最新"设为active,"相关"的active去掉
			setNavBar_NewestRelevant(searchMode, searchKeywords);
			changeSearchMode();

		});
}

function changeSearchMode() {
	$('[getmore]').attr('searchmode',function(index,value){
		//searchmode
		if(value=="newest")
		{
			$(this).attr("searchmode","relevant");
		}
		else
		{
			$(this).attr("searchmode","newest");
		}
	});
	$("[type=hidden][name='searchMode']").attr("value",function(index,value){
		if(value=="newest")
		{
			$(this).attr("value","relevant");	
		}
		else
		{
			$(this).attr("value","newest");
		}
	});
}
//改变最新和最相关按钮的活跃值:
function setNavBar_NewestRelevant(searchMode, searchKeywords) {
	//alert('changeing the N R'); 
	var NR_element = $('.new_relevant_tabs');
	if (searchMode == 'relevant') {
		NR_element.find('.newest').attr('class', 'newest');
		NR_element.find('.newest').empty();
		NR_element.find('.newest').html('<a href="javascript:newest_relevant_Post(\'newest\',\'' + searchKeywords + '\')">最新</a>');
		NR_element.find('.relevant').attr('class', 'active relevant');
		NR_element.find('.relevant').find('a').remove();
		NR_element.find('.relevant').html('相关');

	} else {
		NR_element.find('.newest').attr('class', 'active newest');
		NR_element.find('.newest').find('a').remove();
		NR_element.find('.newest').html('最新');
		NR_element.find('.relevant').attr('class', 'relevant');
		NR_element.find('.relevant').empty();
		NR_element.find('.relevant').html('<a href="javascript:newest_relevant_Post(\'relevant\',\'' + searchKeywords + '\')">相关</a>');
	}
}
//原创度　参与度　灌水度
$(function() {
	$("[author][site]").each(function() {
		var aa = $(this);
		//-------------------------------start post
			$.post("servlet/AuthorEvaluationResponse", {
				author : aa.attr("author"),
				site : aa.attr("site")
			}, function(data, stas) {
				//alert(data.water_degree+" "+data.original_degree+" "+data.participation_degree);
					aa.eq(0).find(".leadPosters").eq(0).text(
							data.original_degree);
					aa.eq(0).find(".followPosters").eq(0).text(
							data.participation_degree);
					aa.eq(0).find(".dumplicatePosters").eq(0).text(
							data.water_degree);
				});

			//---------------------------------end post
		});
})
//文档id,分数等的显示与隐藏
$(".switch").click(function() {
	$(".display_switch").toggle();
})

$("#tab3").click(function(){
	console.log("点击 知识图谱按钮");
	//将知识图谱按钮设为高亮显示
	//将搜索高亮显示去掉
	$("#tab1").attr("class","");
	console.log(this);
	$("#tab3").attr("class","active");
	//将显示内容清空
	$(".user-tab-content").html("");
	//显示某个人的知识图谱
	//1.加载数据
	$("#content").html("<div id='tip'><b>知识图谱正在生成中...</b></div>");
	$("#div3").remove();
	$("hr").before("<div id='div3' width='100%'></div>");
	showWordCloud("${pageData.author}","${pageData.site}");
	console.log("词云图生成完毕");
});



</script>


	</body>

</html>
