<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=MessageHandler.getMessage("UnAuthorized", "ir.utopia.core.constants.Glossory", WebUtil.getLanguage(session)) %></title>
<link href="<%=WebUtil.getCss(pageContext, "Intcss.css") %>" rel="stylesheet" type="text/css">
</head>
<body>
 <p align="center" class="warningMessageCapital"><%=MessageHandler.getMessage("accessDenied", "ir.utopia.core.security.security", WebUtil.getLanguage(session)) %></p>
 
<p align="center"><img alt="<%=MessageHandler.getMessage("accessDenied", "ir.utopia.core.security.security", WebUtil.getLanguage(session)) %>" src="images/Acess_Denied.png" align="center"></p>

</body>
</html>