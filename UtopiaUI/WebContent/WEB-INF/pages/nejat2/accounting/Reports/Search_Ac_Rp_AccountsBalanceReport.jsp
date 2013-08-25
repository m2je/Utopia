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
<%@page import="ir.utopia.common.basicinformation.fiscalyear.bean.CmFiscalyearFacadeRemote" %>
<%-- <%@page import="ir.utopia.common.basicinformation.organization.bean.CmOrganizationFacadeRemote" %> --%>
<%@page import="ir.utopia.nejat2.accounting.process.document.bean.AcDocumentsFacadeRemote"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="ir.utopia.core.constants.Constants.predefindedActions"%>
<%@page import="ir.utopia.core.util.report.ReportHelper" %>
<%@page import="ir.utopia.core.usecase.UsecaseUtil"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" uri="Util" %>
<%
	String lang = WebUtil.getLanguage(session);
System.out.println("session +"+session);
String path = "ir.utopia.nejat2.accounting.AccountingUsecase";
Map<String,Object>context=ContextUtil.createContext(session);
String message=MessageHandler.getMessage(predefindedActions.report.toString(),"ir.utopia.core.constants.Glossory",lang);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=MessageHandler.getMessage("AccountsBalanceReportTitle",path, lang)%></title>
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
function validateFromdateTo(){
	var dateFrom=document.getElementById('dateFrom').value;
	var dateTo=document.getElementById('dateTo').value;	
	if(validateDate(dateFrom,'/')<0){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("dateFrom",path, lang))%>');
		return false;
	}
	if(dateTo!=null&&validateDate(dateTo,'/')<0){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("dateTo",path, lang))%>');
		return false;
	}
	if(dateTo!=null&&!(isBeforeDate(dateFrom,dateTo,'/')||isEqual(dateFrom,dateTo,'/'))){
		alert('<%=WebUtil.write(MessageHandler.getMessage("startEndDateError","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replaceAll("\"@endDate@\"",MessageHandler.getMessage("dateTo",path, lang)).
				replace("\"@startDate@\"",MessageHandler.getMessage("dateFrom",path, lang))%>');
		return false;
	}
	return true;
	
}
function validate(){
	
 	if(document.getElementById('FiscalYear').value==""){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("FiscalYear",path, lang))%>');
		return false;
	} 
 	
	if(document.getElementById('dateFrom').value!="" || document.getElementById('dateTo').value!="")
		if(!validateFromdateTo())
		   return false;
	
	if(document.getElementById('AccountLevel').value==""){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("AccountLevel",path, lang))%>');
		return false;
	} 
	if(document.getElementById('BalanceColumn').value==""){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("BalanceColumn",path, lang))%>');
		return false;
	} 
	
<%-- 	if(document.getElementById('OrganizationId').value==""){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("OrganizationId",path, lang))%>');
		return false;
	} --%>
	return true;
		
}
function report(reportType){
	if(validate()){
		var dateFrom=document.getElementById('dateFrom').value;
		var dateTo=document.getElementById('dateTo').value;
		var documentFrom=document.getElementById('documentFrom').value;
		var documentTo=document.getElementById('documentTo').value; 
		var AccountLevel=document.getElementById('AccountLevel').value;
		var BalanceColumn=document.getElementById('BalanceColumn').value;
	/* 	var OrganizationId=document.getElementById('OrganizationId').value;*/
		var FiscalYear=document.getElementById('FiscalYear').value; 
		var url = "<%=UsecaseUtil.getActionUrl("Ac_Rp_AccountsBalanceReport",predefindedActions.report.toString())%>"+"?dateFrom="+dateFrom+"&dateTo="+dateTo+"&documentFrom="+documentFrom+"&documentTo="+documentTo+"&AccountLevel="+AccountLevel+"&BalanceColumn="+BalanceColumn+"&FiscalYear="+FiscalYear+"&reportType="+reportType;
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
<form name="AccountsBalanceReport_search" method="POST" target="_self" dir="rtl" action="--WEBBOT-SELF--">
<TABLE  CELLSPACING="1" CELLPADDING="1" width="800" align="center">
     <tr>
       <td colspan="6">&nbsp;
       </td>
     <tr>
     <tr>
       <td colspan="6">&nbsp;
       </td>
     <tr>
       <td class="clsLabel" align="right" style="font-family: Tahoma;font-size:12px;" width="90">
           <%=MessageHandler.getMessage("dateFrom",path, lang)%>
           
       </td>
       <td align="right" width="208">
           <input  name="dateFrom" id="dateFrom" type="text" class="clsText"  size="15" value="">
       </td>
       <td width="90" align="right">
		   <a href="#"  onclick="cal18.select(document.forms[0].dateFrom,'anchor2','yyyy/MM/dd'); return false;" NAME="anchor2" ID="anchor2" >	
		   <img src="<%=WebUtil.getRootAddress(pageContext)+"/images/popupCalendarIcon.gif"%>" alt="&#1578;&#1602;&#1608;&#1610;&#1605;" width="21" height="16" border="0"></a>
            
       </td>
       <td class="clsLabel" align="right" style="font-family: Tahoma;font-size:12px;" width="90">
           <%=MessageHandler.getMessage("dateTo",path, lang)%>
       </td>
       <td align="right" width="208">
           <input id="dateTo" type="text" name="dateTo" class="clsText" size="15" value="">
       </td>
       <td align="right">
           <a href="#" onclick="cal18.select(document.forms[0].dateTo,'anchor3','yyyy/MM/dd'); return false;" NAME="anchor3" ID="anchor3" >	
           <img src="<%=WebUtil.getRootAddress(pageContext)+"/images/popupCalendarIcon.gif"%>" alt="&#1578;&#1602;&#1608;&#1610;&#1605;" width="21" height="16" border="0"></a>
       </td>
     </tr>
     <tr>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;">
           <%=MessageHandler.getMessage("documentFrom",path, lang)%>
           
       </td>
       <td align="right"  width="208">
           <input name="documentFrom" id="documentFrom" type="text" class="clsSelect" size="15" value="">
       </td>    
          
       <td>&nbsp;    
       </td>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;">
           <%=MessageHandler.getMessage("documentTo",path, lang)%>
       </td>
      
     <td align="right" class="clsLabel">
           <input name="documentTo" id="documentTo" type="text" class="clsSelect" size="15" value="">
     </td>      
          
       <td>&nbsp;    
       </td>
       </tr> 
       
       <tr>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;">
           <%=MessageHandler.getMessage("fiscalYear",path, lang)%>
           
       </td>
       <td align="right" class="clsLabel">
           <select name="FiscalYear" id="FiscalYear" class="clsSelect" style="width:200px;">
           <option value=""></option>
	       <%
	       CmFiscalyearFacadeRemote fiscalyearFacade=(CmFiscalyearFacadeRemote)ServiceFactory.lookupFacade(CmFiscalyearFacadeRemote.class);
	       	       List<NamePair>fyearpairs= fiscalyearFacade.loadLookup(context, null);
	       	       for(NamePair fyearPair:fyearpairs){ 
	       %>
	       <option value=<%=fyearPair.getKey()%>><%=WebUtil.write(fyearPair,lang)%></option>
	       <%
	       	}
	       %>
	       </select>
       </td>
       <td>&nbsp;    
       </td>
     <tr>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;" width="90">
           <%=MessageHandler.getMessage("AccountLevel",path, lang)%>
           
       </td>
       <td align="right" class="clsLabel" width="208">
           <select name="AccountLevel" id="AccountLevel" class="clsSelect">
                        <option value=""></option>
					    <option value="0"><%=MessageHandler.getMessage("Group Accounts",path, lang)%></option>
						<option value="1"><%=MessageHandler.getMessage("Total Accounts",path, lang)%></option>
						<option value="2"><%=MessageHandler.getMessage("Ledger Accounts",path, lang)%></option>
						<option value="3"><%=MessageHandler.getMessage("subSidiaryLedger Accounts",path, lang)%></option>
				</select></td>
				<td width="90">&nbsp;</td>
				<td align="right" class="clsLabel" style="font-family: Tahoma; font-size: 12px;" width="90">&nbsp;</td>
				<td align="right" class="clsLabel" width="208">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		
        <tr>
        <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;" width="90">
           <%=MessageHandler.getMessage("BalanceColumn",path, lang)%>
       </td>
       <td align="right" class="clsLabel" width="208">
           <select name="BalanceColumn" id="BalanceColumn" class="clsSelect">
                        <option value=""></option>
					    <option value="0"><%=MessageHandler.getMessage("balance with two column",path, lang)%></option>
						<option value="1"><%=MessageHandler.getMessage("balance with four column",path, lang)%></option>
						<option value="2"><%=MessageHandler.getMessage("balance with eight column",path, lang)%></option>
				</select></td>
				</tr>
     <tr>
       <td colspan="6"><input type="hidden" name="userId" id="userId" value="<%=ContextUtil.getUserId(context)%>"/>
       </td>
     <tr>
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