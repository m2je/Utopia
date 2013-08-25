<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
  <TITLE></TITLE>
  <link href="<%=WebUtil.getCss("Intcss.css") %>" rel="stylesheet" type="text/css">
<%
String lang = WebUtil.getLanguage(session);
String path = "ir.utopia.nejat2.payroll.groupitem.action.GroupItemAssignmentForm";
%>
</head>
<body class=clsInputPage>
<%@taglib prefix="tag" uri="Util" %>
<script>
var itemError="<%=MessageHandler.getMessage("itemError",path,lang)%>";
var fromDateError="<%=MessageHandler.getMessage("fromDateError",path,lang)%>";
var toDateError="<%=MessageHandler.getMessage("toDateError",path,lang)%>";
function gridButtonEnable(){
	return true;
}
</script>
<tag:Header></tag:Header>
<tag:InputForm></tag:InputForm>
</body>
</html>