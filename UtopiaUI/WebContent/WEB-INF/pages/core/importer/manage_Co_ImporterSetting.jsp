<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ir.utopia.core.constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.ArrayList"%>

<%@page import="java.util.ArrayList"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%><html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8">
  <TITLE></TITLE>
  <link href="../../../css/Intcss.css" rel="stylesheet" type="text/css">

</head>
<body  class=clsTableBody style="spacing-left:0;padding-right:0;">
<script language="JavaScript" src="../../../js/CalendarPopup.js"></script>
<script language="JavaScript" src="../../../js/importer.js"></script>
<script language="JavaScript" src="../../../js/utopiautil.js"></script>
<script language="JavaScript" src="../../../js/components/ir.utopia.core.util.tags.importpage.ImportPage/ir.utopia.core.util.tags.importpage.ImportPage.nocache.js"></script>

<center>
<form method=post action=manage_Co_ImporterSetting<%=Constants.STRUTS_EXTENSION_NAME %>  enctype="multipart/form-data" method=post>

<br>
<table dir=<%=WebUtil.getDirection(session) %>  cellspacing=0 style="padding:0;" width="800px">
<tr><td>
<div id="importConfigFrame" ></div>
<div id="importConfigDynParamFrame"></div>
</td>

</tr>
<tr>
<td align="center" nowrap="nowrap" colspan="4">
	<button class="clsImageButton"  onclick="onSave();" style="width: 106px; height: 22px;background-image:url('../../../images/buttons/btn_Save_10622_fa.gif')" type=submit background-repeat:no-repeat >
		<%=MessageHandler.getMessage("save","ir.utopia.core.constants.Glossory",WebUtil.getLanguage(session))%></button>&nbsp;
	<button class="clsImageButton" onclick="deleteSetting();" style="width: 106px; height: 22px;background-image:url('../../../images/buttons/btn_Delete_10622_fa.gif')" type=submit background-repeat:no-repeat >
		<%=MessageHandler.getMessage("delete","ir.utopia.core.constants.Glossory",WebUtil.getLanguage(session))%>
	</button>
	
</td>
</tr>

</table>

</form>
</center>
</body>
</html>