<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<html dir="<%=WebUtil.getDirection (session)%>">
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
<title></title>
<link href='<%=WebUtil.getCss ("Intcss.css")%>' rel="stylesheet" type="text/css">
</head>
<%@taglib prefix="tag" uri="Util"%>
<body class="clsInputPage">
	<tag:Header />
	<!--  <p align=center> <tag:ActionButton predefindeAction="<%=Constants.predefindedActions.save%>" redirect="true"></tag:ActionButton></p>  -->
	<tag:TreeView />
</body>
</html>
