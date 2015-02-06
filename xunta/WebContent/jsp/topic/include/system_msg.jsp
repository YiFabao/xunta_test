<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="msg" items="${requestScope.sysMsgList}">
   <div>
	  <span>${msg.sysmsg }</span>
	  <br>
	  ${msg.dateTime }
   </div>
</c:forEach>
