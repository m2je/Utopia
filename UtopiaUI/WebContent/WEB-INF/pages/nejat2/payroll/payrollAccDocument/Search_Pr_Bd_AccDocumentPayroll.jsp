<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="ir.utopia.core.util.WebUtil"%>

<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8">
  <TITLE></TITLE>
    <link href="../../css/Intcss.css" rel="stylesheet" type="text/css">
  <link href="<%=WebUtil.getCss(pageContext,"AccountingTree.css") %>" type="text/css" rel="stylesheet">
   <link href="<%=WebUtil.getCss(pageContext,"AccountingDocument.css") %>" type="text/css" rel="stylesheet">
	<%if(WebUtil.isRigthToLeft(session)){ %>	  
		<link href="<%=WebUtil.getCss(pageContext,"gwt/standard/standard_rtl.css") %>" type="text/css" rel="stylesheet">
	<%} else{%>
		<link href="<%=WebUtil.getCss(pageContext,"gwt/standard/standard.css") %>" type="text/css" rel="stylesheet">
	<%} %>
</head>
<body class=clsInputPage dir="<%=WebUtil.getDirection(session) %>">
<%
String jsfilePath=WebUtil.getJsFile(pageContext,"components/ir.utopia.nejat2.grid.ExtendedGrid/ir.utopia.nejat2.grid.ExtendedGrid.nocache.js");
System.out.print(jsfilePath);
%>
<script type="text/javascript" language="javascript" src="<%=jsfilePath %>" ></script>

	<input type="hidden" name="usecaseName" value="load_Pr_Bd_AccDocumentPayroll" id="usecaseName">  
</body>
</html>