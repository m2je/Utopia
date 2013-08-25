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
String path = "ir.utopia.nejat2.inventory.merchandraftmaster.action.MerchanDraftMasterForm";
%>
<script lnaguage=javascript>
var inventoryError="<%=MessageHandler.getMessage("inventoryError",path,lang)%>";
</script>
<script language=javascript>
/* function LoadPage(){
	var docTypeId = getGWTComponentId('inBdDocumentType');
    var docType = Ext.getCmp(docTypeId);
    var docTypeValue   = docType.getValue();
    var gridId = getGWTComponentId('merchanDraftDetail');
    var grid = Ext.getCmp(gridId);
    var gridToolbar = grid.getTopToolbar();
    var columns = grid.getColumnModel();
    if(docTypeValue=='1151' || docTypeValue=='1152'){
    	gridToolbar.setVisible(false);
        columns.setEditable(2,false);
        columns.setEditable(3,false);
    }
} */
</script>
<script type="text/javascript" SRC="<%=WebUtil.getJsFile(pageContext,"Nejat2UI.js")%>"></script>
</head>
<body class=clsInputPage>
<%@taglib prefix="tag" uri="Util" %>
<tag:Header></tag:Header>
<tag:InputForm></tag:InputForm>
</body>
</html>