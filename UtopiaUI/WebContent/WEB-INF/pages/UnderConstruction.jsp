<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<meta http-equiv="content-type" content="text/html;charset=UTF-8">
<body>
	<br/>
	<center>
	<img src="<%=WebUtil.getImage(pageContext, "common/under-construction.jpg") %>"/>
	<br/>
		<font face="Arial" size="5" color="brown"><b><%=MessageHandler.getMessage("underConstruction", "ir.utopia.core.constants.Glossory", WebUtil.getLanguage(session)) %></b></font>
	</center>
	
	<jsp:scriptlet>
	Exception e= ((Exception)request.getAttribute("javax.servlet.error.exception"));
	if(e!=null)
		e.printStackTrace();
</jsp:scriptlet>
	
</body>
</html>