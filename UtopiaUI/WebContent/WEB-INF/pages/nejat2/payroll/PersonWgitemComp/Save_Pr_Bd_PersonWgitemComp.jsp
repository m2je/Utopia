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
String path = "ir.utopia.nejat2.payroll.personadditionalinsurance.action.PersAdditionInsrancForm";
%>
<script language=javascript>
var persError="<%=MessageHandler.getMessage("persError",path,lang)%>";
function LoadPage(){
	var gridId = getGWTComponentId('prBdContractItems');
    var grid = Ext.getCmp(gridId);
    var gridToolbar = grid.getTopToolbar();
    gridToolbar.setVisible(false); 
    var columns = grid.getColumnModel();
    columns.setEditable(1,false);
    columns.setEditable(2,false);
    //var gridTempId = getGWTComponentId('prBdPersAdditionInsrancs');
    //var gridTemp = Ext.getCmp(gridTempId);
    //var record = getRowFromGrid('prBdPersAdditionInsrancs',0);
	//var merchanId=record[0];
	//alert(merchanId);
}
</script>
<script type="text/javascript" SRC="<%=WebUtil.getJsFile(pageContext,"Nejat2UI.js")%>"></script>
</head>
<body class=clsInputPage >
<%@taglib prefix="tag" uri="Util" %>
<tag:Header></tag:Header>
<tag:InputForm></tag:InputForm>
</body>
</html>