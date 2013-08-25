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
<%@page import="ir.utopia.nejat2.budget.plan.bean.BgBdPlanFacadeRemote"%>
<%@page import="ir.utopia.nejat2.budget.project.bean.BgBdProjectFacadeRemote"%>
<%@page import="ir.utopia.common.basicinformation.organization.bean.CmOrganizationFacadeRemote"%>
<%@page import="ir.utopia.nejat2.budget.creditsource.bean.BgBdCreditSourceFacadeRemote"%>
<%@page import="ir.utopia.nejat2.budget.section.bean.BgBdSectionFacadeRemote"%>
<%@page import="ir.utopia.nejat2.budget.depfiscalyear.bean.BgBdDepFiscalyearFacadeRemote"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="ir.utopia.core.constants.Constants.predefindedActions"%>
<%@page import="ir.utopia.core.util.report.ReportHelper"%>
<%@page import="ir.utopia.core.usecase.UsecaseUtil"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="tag" uri="Util"%>
<%


	String lang = WebUtil.getLanguage(session);
System.out.println("session +"+session);
String path = "ir.utopia.nejat2.credit.CreditUsecase";
Map<String,Object>context=ContextUtil.createContext(session);
String message=MessageHandler.getMessage(predefindedActions.report.toString(),"ir.utopia.core.constants.Glossory",lang);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=MessageHandler.getMessage("ProjectsPlanReportTitle",path, lang)%></title>
<link href='<%=WebUtil.getCss("Intcss.css") %>' rel="stylesheet" type="text/css">
<link href='<%=WebUtil.getCss("lovIntcss.css") %>' type="text/css">
<script language="JavaScript" src='<%=WebUtil.getRootAddress(pageContext)+"/js/CalendarPopup.js"%>'
	type="text/javascript"
></script>
<script language="JavaScript" src='<%=WebUtil.getRootAddress(pageContext)+"/js/favautil.js"%>' type="text/javascript"></script>
<script language="JavaScript" type="text/javascript">
document.write(getCalendarStyles());
</script>
<script language="JavaScript" type="text/javascript">
var cal18 = new CalendarPopup("_dateDiv");
</script>
<script type="text/javascript">

function report(reportType){
	{
	    var planid=document.getElementById('planid').value;
	    var organizationId=document.getElementById('organizationId').value;
	    var creditsourceId=document.getElementById('creditsourceId').value;
	   
	   // var ProjectId=document.getElementById('ProjectId').value;
	   
	    var sectionId=document.getElementById('sectionId').value;
	    var depfiscalyearId=document.getElementById('depfiscalyearId').value;
        var url = "<%=UsecaseUtil.getActionUrl("Cd_Rp_CreditReport",predefindedActions.report.toString())%>"+"?planid="+planid+"&organizationId="+organizationId+"&creditsourceId="+creditsourceId+"&sectionId="+sectionId+"&depfiscalyearId="+depfiscalyearId+"&reportType="+reportType;
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
	<form name="DepartmentPlanReport_search" method="POST" target="_self" dir="rtl" action="--WEBBOT-SELF--">
		<table cellspacing="1" cellpadding="1" width="800" align="center">
			<tr>
				<td colspan="6">&nbsp;</td>
			</tr>
			<tr>
			</tr>
			<tr>
				<td colspan="6">&nbsp;</td>
			</tr>
			<tr>
			</tr>
			<tr>
				<td align="right" class="clsLabel" style="font-family: Tahoma; font-size: 12px;" width="90"><%=MessageHandler.getMessage("planid",path, lang)%>
				</td>
				<td align="right" class="clsLabel"><select name="planid" id="planid" class="clsSelect"
					style="width: 200px;"
				>
						<option value=""></option>
						
						<%
					
						BgBdPlanFacadeRemote prFacade=(BgBdPlanFacadeRemote)ServiceFactory.lookupFacade(BgBdPlanFacadeRemote.class);
			       	       List<NamePair>prpairs= prFacade.loadLookup(context, null);
			       	       for(NamePair prPair:prpairs){ 
						
	       %>
						<option value="<%=prPair.getKey()%>"><%=WebUtil.write(prPair,lang)%></option>
						<%
	       	}
	       %>
						
						<%-- <option value="0"><%=MessageHandler.getMessage("program",path, lang)%></option>
						<option value="1"><%=MessageHandler.getMessage("plan",path, lang)%></option> --%>
				</select></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			
			
			
			
		<!-- --------------------------------------------------------------------------------->
				<tr>
				<td align="right" class="clsLabel" style="font-family: Tahoma; font-size: 12px;" width="90"><%=MessageHandler.getMessage("organizationId",path, lang)%>
				</td>
				<td align="right" class="clsLabel"><select name="organizationId" id="organizationId" class="clsSelect"
					style="width: 200px;"
				>
						<option value=""></option>
						<%
					
						CmOrganizationFacadeRemote paFacade=(CmOrganizationFacadeRemote)ServiceFactory.lookupFacade(CmOrganizationFacadeRemote.class);
			       	       List<NamePair>papairs= paFacade.loadLookup(context, null);
			       	       for(NamePair paPair:papairs){ 
						
	       %>
						<option value="<%=paPair.getKey()%>"><%=WebUtil.write(paPair,lang)%></option>
									<%
	       	}
	       %>
						
					<%--  <option value="0"><%=MessageHandler.getMessage("program",path, lang)%></option>
						<option value="1"><%=MessageHandler.getMessage("organization",path, lang)%></option> --%>
				</select></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<!---------------------------------------------------------------------------------->
				<tr>
				<td align="right" class="clsLabel" style="font-family: Tahoma; font-size: 12px;" width="90"><%=MessageHandler.getMessage("creditsourceId",path, lang)%>
				</td>
				<td align="right" class="clsLabel"><select name="creditsourceId" id="creditsourceId" class="clsSelect"
					style="width: 200px;"
				>
						<option value=""></option>
						
						<%
					
				
						BgBdCreditSourceFacadeRemote pbFacade=(BgBdCreditSourceFacadeRemote)ServiceFactory.lookupFacade(BgBdCreditSourceFacadeRemote.class);
	       	       List<NamePair>pbpairs= pbFacade.loadLookup(context, null);
	       	       for(NamePair pbPair:pbpairs){ 
	       %>
						<option value="<%=pbPair.getKey()%>"><%=WebUtil.write(pbPair,lang)%></option>
									<%
	       	}
	       %>
						
						<%-- <option value="0"><%=MessageHandler.getMessage("program",path, lang)%></option>
						<option value="1"><%=MessageHandler.getMessage("creditsource",path, lang)%></option> --%>
				</select></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			
			<!-- -------------------------------------------------------------------------------- -->
			
			
				<tr>
				<td align="right" class="clsLabel" style="font-family: Tahoma; font-size: 12px;" width="90"><%=MessageHandler.getMessage("depfiscalyearId",path, lang)%>
				</td>
				<td align="right" class="clsLabel"><select name="depfiscalyearId" id="depfiscalyearId" class="clsSelect"
					style="width: 200px;"
				>
						<option value=""></option>
						
						<%
					
	      
						BgBdDepFiscalyearFacadeRemote pdFacade=(BgBdDepFiscalyearFacadeRemote)ServiceFactory.lookupFacade(BgBdDepFiscalyearFacadeRemote.class);
	       	       List<NamePair>pdpairs= pdFacade.loadLookup(context, null);
	       	       for(NamePair pdPair:pdpairs){ 
	       %>
						<option value="<%=pdPair.getKey()%>"><%=WebUtil.write(pdPair,lang)%></option>
									<%
	       	}
	       %>
						
						<%-- <option value="0"><%=MessageHandler.getMessage("program",path, lang)%></option>
						<option value="1"><%=MessageHandler.getMessage("depfiscalyear",path, lang)%></option> --%>
				</select></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
						
						
<!-- ---------------------------------------------------------------------------------------->	
<tr>
				<td align="right" class="clsLabel" style="font-family: Tahoma; font-size: 12px;"><%=MessageHandler.getMessage("sectionId",path, lang)%>
				</td>
				<td align="right" class="clsLabel"><select name="sectionId" id="sectionId" class="clsSelect"
					style="width: 200px;"
				>
						<option value=""></option>
							<%
					
						
						BgBdSectionFacadeRemote pcFacade=(BgBdSectionFacadeRemote)ServiceFactory.lookupFacade(BgBdSectionFacadeRemote.class);
	       	       List<NamePair>pcpairs= pcFacade.loadLookup(context, null);
	       	       for(NamePair pcPair:pcpairs){ 
	       %>
	       
						<option value="<%=pcPair.getKey()%>"><%=WebUtil.write(pcPair,lang)%></option>
						
				<%
							}
						%>
						
					
					
					
				</select></td>
				<td>&nbsp;</td>
			</tr>

	
			
			
			
			<tr>
				<td colspan="6">
					<table width="100%" dir="<%=WebUtil.getDirection(lang) %>" align="center">
						<tr>
							<td width="30%">&nbsp;</td>
							<td width="20%" style="font-family: Tahoma; font-size: 12px;"><%=WebUtil.write(MessageHandler.getMessage("reportType","ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants",lang)) %></td>
							<td width="20%"><select name="reportType" id="reportType" class="clsSelect" style="width: 70px">
									<option value="<%= ReportType.pdf%>"><%=WebUtil.write(MessageHandler.getMessage("reportTypePDF","ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants",lang)) %></option>
									<option value="<%= ReportType.excel%>"><%=WebUtil.write(MessageHandler.getMessage("reportTypeExcel","ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants",lang)) %></option>
							</select></td>
							<td width="30%">&nbsp;</td>
						</tr>
					</table>
				
					<p align="center"><input
						style='border: 0px; width: 106px; height: 22px; background-image: url(<%= WebUtil.getRootAddress ( pageContext)+"/images/buttons/btn_Print_10622_fa.gif"%>);'
				
							type="button" 
							value="<%=message%>" name="B1" onclick="report(document.getElementById('reportType').value);"
					></p> 
				</td>
			</tr>
		</table>
</form>
	<div id="_dateDiv" style="position: absolute; visibility: hidden; background-color: white; z-index: 2"></div>
</body>
</html>
