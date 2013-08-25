<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Calendar"%>
<%@page import="ir.utopia.core.util.DateUtil"%>
<%@page import="ir.utopia.core.persistent.lookup.model.NamePair"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.ContextUtil"%>
<%@page import="ir.utopia.nejat2.payroll.monthcalc.bean.PrPrMonthCalcFacadeRemote" %>
<%@page import="ir.utopia.nejat2.payroll.prvorganization.bean.PrVOrganizationFacadeRemote" %>
<%@page import="ir.utopia.nejat2.payroll.personnel.bean.PrBdPersonnelFacadeRemote"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="ir.utopia.core.constants.Constants.predefindedActions"%>
<%@page import="ir.utopia.core.util.report.ReportHelper" %>
<%@page import="ir.utopia.core.usecase.UsecaseUtil"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" uri="Util" %>
<%
	String lang = WebUtil.getLanguage(session);
System.out.println("session +"+session);
String path = "ir.utopia.nejat2.payroll.PayrollUsecase";
Map<String,Object>context=ContextUtil.createContext(session);
String message=MessageHandler.getMessage(predefindedActions.report.toString(),"ir.utopia.core.constants.Glossory",lang);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=MessageHandler.getMessage("OvertimeListTitle",path, lang)%></title>
<link href="<%=WebUtil.getCss("Intcss.css") %>" rel="stylesheet" type="text/css">
<link href="<%=WebUtil.getCss("lovIntcss.css") %>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<%=WebUtil.getRootAddress(pageContext)+"/js/CalendarPopup.js"%>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<%=WebUtil.getRootAddress(pageContext)+"/js/favautil.js"%>"></SCRIPT>
<SCRIPT language=JavaScript>
document.write(getCalendarStyles());
</SCRIPT>
<script language="JavaScript" id="js18">
var cal18 = new CalendarPopup("_dateDiv");
</script>
<script type="text/javascript">
function validate(){
	
	if(document.getElementById('OrganizationId').value==""){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("OrganizationId",path, lang))%>');
		return false;
	}
		if(document.getElementById('MonthCalc').value==""){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("MonthCalc",path, lang))%>');
		return false;
	}
		
		return true;
		
}
function report(reportType){
	if(validate()){
	    var OrganizationId=document.getElementById('OrganizationId').value;
		var MonthCalc=document.getElementById('MonthCalc').value;
		var personnel=document.getElementById('personnelId_C');
		selected = new Array(); 
		for (var i = 0; i < personnel.options.length; i++) 
				selected.push(personnel.options[i].value);
	    var url = "<%=UsecaseUtil.getActionUrl("Pr_Rp_OvertimeList",predefindedActions.report.toString())%>"+"?OrganizationId="+OrganizationId+"&MonthCalc="+MonthCalc+"&PersonnelId="+selected+"&reportType="+reportType;
		if(url.length == 0){
			alert("reportIsNotAvailable");
			return;
		}
		document.forms[0].action = url;
		document.forms[0].submit();

	}
}
</script>
</head>

<body class="clsInputPage">
<form name="OvertimeList_search" method="POST" target="_self" dir="rtl" action="--WEBBOT-SELF--">
<TABLE  CELLSPACING="1" CELLPADDING="1" width="800" align="center">
     <tr>
       <td colspan="6">&nbsp;
       </td>
     <tr>
     <tr>
       <td colspan="6">&nbsp;
       </td>
     <tr>
     <tr>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;" width="90";">
           <%=MessageHandler.getMessage("OrganizationId",path, lang)%>
           
       </td>
       <td align="right" class="clsLabel">
           <select name="OrganizationId" id="OrganizationId" class="clsSelect" style="width:200px;">
           <option value=""></option>
	       <%
	       PrVOrganizationFacadeRemote orgFacade=(PrVOrganizationFacadeRemote)ServiceFactory.lookupFacade(PrVOrganizationFacadeRemote.class);
	       	       List<NamePair>pairs= orgFacade.loadLookup(context, null);
	       	       for(NamePair pair:pairs){ 
	       %>
	       <option value=<%=pair.getKey()%>><%=WebUtil.write(pair,lang)%></option>
	       <%
	       	}
	       %>
	       </select>
       </td>
       <td>&nbsp;    
       </td>
     <td>&nbsp;
       </td>
     </tr>
     <tr>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;">
           <%=MessageHandler.getMessage("MonthCalc",path, lang)%>
           
       </td>
       <td align="right" class="clsLabel">
           <select name="MonthCalc" id="MonthCalc" class="clsSelect" style="width:200px;">
           <option value=""></option>
	       <%
	       PrPrMonthCalcFacadeRemote MonthCalcFacade=(PrPrMonthCalcFacadeRemote)ServiceFactory.lookupFacade(PrPrMonthCalcFacadeRemote.class);
	       	       List<NamePair>mcpairs= MonthCalcFacade.loadLookup(context, null);
	       	       for(NamePair mcPair:mcpairs){ 
	       %>
	       <option value=<%=mcPair.getKey()%>><%=WebUtil.write(mcPair,lang)%></option>
	       <%
	       	}
	       %>
	       </select>
       </td>
       <td>&nbsp;
       </td>
     </tr>
      <tr>
       <td colspan="6">&nbsp;
       </td>
     <tr>
     <tr>
       <td colspan="6">&nbsp;
       </td>
     <tr>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;" width="90">
           <%=MessageHandler.getMessage("PersonnelId",path, lang)%>
           
       </td>
       <td align="right" class="clsLabel" width="208">
           
           <tag:LOV usecaseName="Pr_Bd_Personnel" formClassName="<%=ir.utopia.nejat2.payroll.personnel.action.PersonnelForm.class%>" widgetName="personnelId" searchTitle="personnelSearch" displayItems="bpName,nationalCode" separator=" " multiSelect="true"></tag:LOV>
           </td>
       <td width="90">&nbsp;    
       </td>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;" width="90">
           &nbsp;
       </td>
       <td align="right" class="clsLabel" width="208">
           &nbsp;
	   </td>
       <td>&nbsp;
       </td>
     </tr>
  
     <tr>
       <td colspan="6">
           <table  width="100%" dir=<%=WebUtil.getDirection(lang) %> align="center">
	<tr>
		<td width=30%>&nbsp;</td>
		<td width=20% style="font-family: Tahoma;font-size:12px;"><%=WebUtil.write(MessageHandler.getMessage("reportType","ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants",lang)) %></td>
		<td width=20%>
		<select name="reportType" id="reportType" class="clsSelect" style="width:70px">
	 		<option value=<%= ReportType.pdf%>><%=WebUtil.write(MessageHandler.getMessage("reportTypePDF","ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants",lang)) %></option>
	 		<option value=<%= ReportType.excel%>><%=WebUtil.write(MessageHandler.getMessage("reportTypeExcel","ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants",lang)) %></option>
		</select>
		</td>
		<td width=30%>&nbsp;</td>

	</tr>
	

</table>



<p align="center">
<input style="border:0px;width: 106px; height: 22px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Print_10622_fa.gif"%>);" type="button" value="<%=message%>" name="B1" onclick="report(document.getElementById('reportType').value);">
</p>
       </td>
     </tr>
</TABLE>

	
	
	
	
</form>
<div id="_dateDiv" style="position: absolute; visibility: hidden; background-color: white;z-index:2"></div>
</body>
</html>