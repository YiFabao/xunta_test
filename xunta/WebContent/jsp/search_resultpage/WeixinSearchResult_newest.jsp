<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/_00meta.inc"%>
<html lang="zh-CN">

			<searchKeywords>${pageData.searchKeywords}<searchKeywords><br>
			<searchMode>newest</select><searchMode><br>
			<url>http://www.xunta.so/psearch?searchKeywords=${pageData.searchKeywords}&searchMode=newest<url><br><br>

			<c:forEach items="${pageData.getAuthorList()}" var="authorInfo">
			<person>
			<tab>	</tab>	<author>${authorInfo.author}</author><br>
			<tab>	</tab>	<site>${authorInfo.site}</site><br>
			<tab>	</tab>	<totalDocs>${authorInfo.getTotalAuthorDocs()}</totalDocs><br>
			<tab>	</tab>	<author_url>http://www.xunta.so/author_profile?author=${authorInfo.author}&site=${authorInfo.site}&searchMode=newest&searchKeywords=${pageData.searchKeywords}</author_url><br>
			<tab>	</tab>	<score>${authorInfo.totalScore}</score><br><br>
			</person>
			</c:forEach>
</html>
