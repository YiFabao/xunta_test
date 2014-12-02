<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/_00meta.inc"%>
<html lang="zh-CN">

			<searchKeywords>${pageData.searchKeywords}<searchKeywords>
			<searchMode>pageData.get</select><searchMode>
			<url>http://www.xunta.so/psearch?searchKeywords=${pageData.searchKeywords}&searchMode=relevant<url>
			<!-- 作者循环 -->
			<c:forEach items="${pageData.getAuthorList()}" var="authorInfo">
			<person>
				<author>-xxyy0419-${authorInfo.author}-xxyy0419-</author>
				<site>${authorInfo.site}</site>
				<totalDocs>authorInfo.getTotalAuthorDocs()</totalDocs>
				<author_url>http://www.xunta.so/author_profile?author=${authorInfo.author}&site=${authorInfo.site}&searchMode=relevant&searchKeywords=${pageData.searchKeywords}')"></author_url>
				<score>(${authorInfo.totalScore})</score>
			</person>
			</c:forEach>
</html>
