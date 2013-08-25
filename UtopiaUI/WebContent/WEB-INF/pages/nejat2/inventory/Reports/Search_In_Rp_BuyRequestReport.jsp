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
<%@page import="ir.utopia.nejat2.inventory.inventory.bean.InBdInventoryFacadeRemote"%>
<%@page import="ir.utopia.nejat2.inventory.merchandise.bean.InBdMerchandiseFacadeRemote"%>
<%@page import="ir.utopia.common.basicinformation.fiscalyear.bean.CmFiscalyearFacadeRemote" %>
<%@page import="ir.utopia.common.basicinformation.organization.bean.CmOrganizationFacadeRemote" %>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="ir.utopia.core.constants.Constants.predefindedActions"%>
<%@page import="ir.utopia.core.util.report.ReportHelper" %>
<%@page import="ir.utopia.core.usecase.UsecaseUtil"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" uri="Util" %>
<%
	String lang = WebUtil.getLanguage(session);
System.out.println("session +"+session);
String path = "ir.utopia.nejat2.inventory.InventoryUsecase";
Map<String,Object>context=ContextUtil.createContext(session);
String message=MessageHandler.getMessage(predefindedActions.report.toString(),"ir.utopia.core.constants.Glossory",lang);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=MessageHandler.getMessage("BuyRequestTitle",path, lang)%></title>
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
	
	if(document.getElementById('fiscalyearId').value==""){
		alert('<%=WebUtil.write(MessageHandler.getMessage("inValidDate","ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants",
				lang),lang).replace("\"@dateItem@\"",MessageHandler.getMessage("fiscalyearId",path, lang))%>');
		return false;
	}
	if(document.getElementById('dateFrom').value!="" || document.getElementById('dateTo').value!="")
		if(!validateFromdateTo())
		   return false;
	return true;
		
}
function report(reportType){
	if(validate()){
		var dateFrom=document.getElementById('dateFrom').value;
		var dateTo=document.getElementById('dateTo').value;
		var inventoryId=document.getElementById('inventoryId').value;
		var merchanId=document.getElementById('merchanId_C').value;
		var userId=document.getElementById('userId').value;
		var orgId=document.getElementById('orgId').value;
		var fiscalyearId=document.getElementById('fiscalyearId').value;
		var url = "<%=UsecaseUtil.getActionUrl("In_Rp_BuyRequestReport",predefindedActions.report.toString())%>"+"?dateFrom="+dateFrom+"&dateTo="+dateTo+"&inventoryId="+inventoryId+"&merchanId="+merchanId+"&userId="+userId+"&orgId="+orgId+"&fiscalyearId="+fiscalyearId+"&reportType="+reportType;
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
<form name="kardks_search" method="POST" target="_self" dir="rtl" action="--WEBBOT-SELF--">
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
           <%=MessageHandler.getMessage("inventory",path, lang)%>
           
       </td>
       <td align="right" class="clsLabel">
           <select name="inventoryId" id="inventoryId" class="clsSelect" style="width:200px;">
           <option value=""></option>
	       <%
	       	InBdInventoryFacadeRemote inventoryFacade=(InBdInventoryFacadeRemote)ServiceFactory.lookupFacade(InBdInventoryFacadeRemote.class);
	       	       List<NamePair>pairs= inventoryFacade.loadLookup(context, null);
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
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;">
           <%=MessageHandler.getMessage("merchandise",path, lang)%>
       </td>
       <td align="right" class="clsLabel">
                      <tag:LOV usecaseName="In_Bd_Merchandise" formClassName="<%=ir.utopia.nejat2.inventory.merchandise.action.MerchandiseForm.class%>" widgetName="merchanId" searchTitle="merchandiseSearch" displayItems="name"></tag:LOV>

	   </td>
       <td>&nbsp;
       </td>
     </tr>
     <tr>
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;">
           <%=MessageHandler.getMessage("fiscalYear",path, lang)%>
           
       </td>
       <td align="right" class="clsLabel">
           <select name="fiscalyearId" id="fiscalyearId" class="clsSelect" style="width:200px;">
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
       <td align="right" class="clsLabel" style="font-family: Tahoma;font-size:12px;">
           <%=MessageHandler.getMessage("organization",path, lang)%>
       </td>
       <td align="right" class="clsLabel">
           <select name="orgId" id="orgId" class="clsSelect" style="width:200px;">
           <option value=""></option>
	       <%
	       	CmOrganizationFacadeRemote orgFacade=(CmOrganizationFacadeRemote)ServiceFactory.lookupFacade(CmOrganizationFacadeRemote.class);
	       	       List<NamePair>orgPairs= orgFacade.loadLookup(context, null);
	       	       for(NamePair orgPair:orgPairs){
	       %>
	       <option value=<%= orgPair.getKey()%>><%=WebUtil.write(orgPair,lang) %></option>
	       <%} %>
	       </select>
	   </td>
       <td>&nbsp;
       </td>
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