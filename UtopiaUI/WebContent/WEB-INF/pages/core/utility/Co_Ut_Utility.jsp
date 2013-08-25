<%@page import="ir.utopia.core.util.WebUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
  <TITLE></TITLE>
  
	<link href="<%=WebUtil.getCss(pageContext,"Intcss.css") %>" type="text/css" rel="stylesheet">
	<%if(WebUtil.isRigthToLeft(session)){ %>	  
		<link href="<%=WebUtil.getCss(pageContext,"gwt/standard/standard_rtl.css") %>" type="text/css" rel="stylesheet">
	<%} else{%>
		<link href="<%=WebUtil.getCss(pageContext,"gwt/standard/standard.css") %>" type="text/css" rel="stylesheet">
	<%} %>
      <script language='javascript' src='<%=WebUtil.getJsFile(pageContext,"components/ir.utopia.core.util.tags.utilitypanel.UtilityPanel/ir.utopia.core.util.tags.utilitypanel.UtilityPanel.nocache.js") %>' ></script>
</head>
<body class=clsInputPage>
    <iframe src="javascript:''" id="__gwt_historyFrame" style="position:absolute;width:0;height:0;border:0"></iframe>
</body>
</html>