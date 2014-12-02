<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String author = request.getParameter("author") == null ? null
			: new String(request.getParameter("author").getBytes(
					"ISO-8859-1"), "utf-8");

	String site = request.getParameter("site") == null ? null
			: new String(request.getParameter("site").getBytes(
					"ISO-8859-1"), "utf-8");
%>

<h1><%=site%> : <%=author%></h1>

<div>
	这是从远程获取的用户最相关数据
</div>