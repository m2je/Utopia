<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.util.report.ReportHelper"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="ir.utopia.core.usecase.UsecaseUtil"%>
<%@page import="ir.utopia.core.constants.Constants.predefindedActions"%>
<!--<%@page contentType="text/html;charset=UTF-8" %>-->
<%@page import="ir.utopia.core.constants.Constants"%>
<%
//request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
String lang = WebUtil.getLanguage(session);
String path = "ir.utopia.nejat2.managementreports.statistical.action.Statistical";
String message=MessageHandler.getMessage(predefindedActions.report.toString(),"ir.utopia.core.constants.Glossory",lang);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=MessageHandler.getMessage("title",path, lang)%></title>
 
<link href="<%=WebUtil.getRootAddress(pageContext)+"/css/Intcss.css"%>" rel="stylesheet" type="text/css">
<script type="text/javascript">
function report(){
		
			var checkDate=document.getElementById('checkDate').value;
			var url ="<%= UsecaseUtil.getActionUrl("Mr_St_Statistical",predefindedActions.report.toString())%>"+"?checkDate="+checkDate;
			if(url.length == 0){
				alert("قابل نمايش نيست!");
				return;
			}
			document.forms[0].action = url;
			document.forms[0].submit();
	}
</script>
</head>
<body class="clsInputPage">

<form name="payup_search" method="POST" target="_self" dir="rtl" action="--WEBBOT-SELF--">
<TABLE class="clsTableBody"  CELLSPACING="1" CELLPADDING="1" class="clsTableBody" width=100%>

	<tr><td width="60">&nbsp;</td><td width="76">&nbsp;</td><td width="208">&nbsp;</td>
		<td width="90">&nbsp;</td><td width="115">&nbsp;</td><td width="94">&nbsp;</td>
		<td width="208">&nbsp;</td><td width="200">&nbsp;</td><td width="144">&nbsp;</td></tr>
	<tr><td width="60">&nbsp;</td><td width="76">&nbsp;</td><td width="208">&nbsp;</td>
		<td width="90">&nbsp;</td><td width="115">&nbsp;</td><td width="94">&nbsp;</td>
		<td width="208">&nbsp;</td><td width="200">&nbsp;</td><td width="144">&nbsp;</td></tr>
	<tr><td width="60">&nbsp;</td><td width="76" class="clsLabel"><%=MessageHandler.getMessage("checkDate",path, lang)%></td>
	<td width="208">
	
	<select name="checkDate" id="checkDate" class="clsSelect">
	 
	     <option value="1389">1389</option>
         <option value="1390">1390</option>
         <option value="1391">1391</option>
	</select>
	</td>
		<td width="90">&nbsp;</td><td width="115">&nbsp;</td><td width="94">&nbsp;</td>
		<td width="208">&nbsp;</td><td width="200">&nbsp;</td><td width="144">&nbsp;</td></tr>
		
	</TABLE>
	</form>	
<p>&nbsp;</p>
<table align="center" width="300px">
<tr>
<td>




<p align="center">
<input style="border:0px;width: 106px; height: 22px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Print_10622_fa.gif"%>);" type="button" value="<%=message%>" name="B1" onclick="report();">
</p>
</td>
</tr>
</table>
</body>
</html>