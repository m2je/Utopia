<%@page import="ir.utopia.core.util.WebUtil"%>
<html dir=<%=WebUtil.getDirection(session)%>>
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
<TITLE></TITLE>
<link href="<%=WebUtil.getCss("Intcss.css")%>" rel="stylesheet"
	type="text/css">
</head>
<%@taglib prefix="tag" uri="Util"%>
<body class=clsInputPage>
	<tag:Header>
	</tag:Header>
	
	<tag:SearchGrid forwardUrl="test"></tag:SearchGrid>
</body>
</HTML>
