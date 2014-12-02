<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/_00meta.inc"%>
<form class="form-inline search-form" role="form"
	action="<%=basePath%>psearch">
	<input type="hidden" name="searchTime" value="30days">
	<input type="hidden" name="searchMode" value="${pageData.searchMode}">
	<div class="row">
		<div class="col-lg-4">
			<div class="input-group">
				<label class="sr-only" for="keyword">
					搜索关键词
				</label>
				<input style="width: 450px;" class="form-control search-keywords"
					type="text" name="searchKeywords"
					placeholder="输入(多个)知识点关键词，寻找旅游达人、组织者、导游及结伴消息。"
					value="${pageData.searchKeywords}" />
				<span class="input-group-btn">
					<button id="search" class="btn btn-primary" type="submit">
						.SO
					</button> </span>
			</div>
			<!-- /input-group -->
		</div>
		<!-- /.col-lg-6 -->
	</div>

</form>

