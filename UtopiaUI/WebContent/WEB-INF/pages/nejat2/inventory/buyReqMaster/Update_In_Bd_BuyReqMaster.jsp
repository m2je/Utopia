<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ir.utopia.core.util.WebUtil"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
  <TITLE></TITLE>
  <link href="<%=WebUtil.getCss("Intcss.css") %>" rel="stylesheet" type="text/css">
<script language=javascript>
/* function LoadPage(){
//	var merchanReqMasterId = getGWTComponentId('merchanReqMaster');
//	var req=document.getElementById(merchanReqMasterId);
//	reqId = new Array(); 
//	for (var i = 0; i < req.options.length; i++) 
//		reqId.push(req.options[i].value);
	var gridId = getGWTComponentId('inBdBuyReqDetails');
	var grid = Ext.getCmp(gridId);
	var gridToolbar = grid.getTopToolbar();
	var columns = grid.getColumnModel();
	var gridStore = grid.getStore();
	var count = gridStore.getCount();
	for(var i=0;i<count;i++){
		var record = getRowFromGrid('inBdBuyReqDetails',i);
		var merchanReqId=record[4];
		if(merchanReqId!=''&&merchanReqId!=null){
			gridToolbar.setVisible(false);
		    columns.setEditable(2,false);
		    columns.setEditable(5,false);
		}
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