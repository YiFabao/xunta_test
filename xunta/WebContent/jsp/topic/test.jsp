<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%
	Map map = new HashMap<String,String>();
	map.put("name","fabao.yi");
	map.put("age","23");
	map.put("address","随州");
	pageContext.setAttribute("map",map);
	map.get("name");
%>   
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<c:forEach var="obj" items="${pageScope.map['name']}">
		<c:out value="${obj}"></c:out>|

		<br/>
	</c:forEach>

</body>
</html>