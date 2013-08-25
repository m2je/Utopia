<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="ir.utopia.core.util.WebUtil"%>

<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8">
  <TITLE></TITLE>
  <link href="../../css/Intcss.css" rel="stylesheet" type="text/css">
</head>
<body class=clsInputPage dir="<%=WebUtil.getDirection(session) %>">
<%
String jsfilePath=WebUtil.getJsFile(pageContext,"components/ir.utopia.core.util.tags.controlpanel.ControlPanel.nocache.js");
%>
<script type="text/javascript" language="javascript" src="<%=jsfilePath %>" ></script>
</body>
</html>