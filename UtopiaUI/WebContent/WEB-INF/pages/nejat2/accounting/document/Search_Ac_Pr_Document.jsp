<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
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
<%
String lang = WebUtil.getLanguage(session);
String path = "ir.utopia.nejat2.accounting.AccountingEnums";
%>
<table width="100%">
  <tr>
    <td bgcolor="#72F3E3" width="30">&nbsp;</td>
    <td class="clsLabel"><%=MessageHandler.getMessage("DocStatus.revised",path,lang)%></td>
    <td>&nbsp;</td>
    <td bgcolor="#FFAD5B" width="30">&nbsp;</td>
    <td class="clsLabel"><%=MessageHandler.getMessage("DocStatus.draft",path,lang)%></td>
    <td>&nbsp;</td>
    <td bgcolor="#D1A4FF" width="30">&nbsp;</td>
    <td class="clsLabel"><%=MessageHandler.getMessage("DocStatus.reviewed",path,lang)%></td>
    <td>&nbsp;</td>
    <td bgcolor="#C7A38B" width="30">&nbsp;</td>
    <td class="clsLabel"><%=MessageHandler.getMessage("DocStatus.returned",path,lang)%></td>
    <td>&nbsp;</td>
    <td bgcolor="#FFFFBF" width="30">&nbsp;</td>
    <td class="clsLabel"><%=MessageHandler.getMessage("DocStatus.temporary",path,lang)%></td>
    <td>&nbsp;</td>
    <td bgcolor="#FF9F9F" width="30">&nbsp;</td>
    <td class="clsLabel"><%=MessageHandler.getMessage("DocStatus.rejected",path,lang)%></td>
    <td>&nbsp;</td>
    <td bgcolor="#A5C8EF" width="30">&nbsp;</td>
    <td class="clsLabel"><%=MessageHandler.getMessage("DocStatus.finalConfirm",path,lang)%></td>
    <td>&nbsp;</td>
    <td bgcolor="#A8D19A" width="30">&nbsp;</td>
    <td class="clsLabel"><%=MessageHandler.getMessage("DocStatus.permanent",path,lang)%></td>
  </tr>
</table>	
	<tag:SearchGrid forwardUrl="test"></tag:SearchGrid>
</body>
</HTML>


