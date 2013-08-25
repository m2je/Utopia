<%@page import="ir.utopia.core.constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="ir.utopia.core.security.SecurityProvider" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% if(session.getAttribute(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME)==null){
%>
<META HTTP-EQUIV="Refresh" CONTENT="0;URL=Login<%=Constants.STRUTS_EXTENSION_NAME%>">
<%
}else{%>
<META HTTP-EQUIV="Refresh" CONTENT="0;URL=Menu<%=Constants.STRUTS_EXTENSION_NAME%>">
<%
	}
	%>
<title>wait a moment</title>
</head>

<body>
</body>
</html>