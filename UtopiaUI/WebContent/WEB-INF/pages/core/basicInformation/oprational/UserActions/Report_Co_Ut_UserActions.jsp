<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
Map<Object,Object> objParameters = new HashMap<Object,Object>();


ReportType result=ReportType.pdf;
String reportType=  request.getParameter("reportType");
 if(reportType!=null&&reportType.trim().length()>0){
	reportType=reportType.trim();
	if(ReportType.html.toString().equals(reportType)){
		result=ReportType.html;
	}else if(ReportType.excel.toString().equals(reportType)){
		result=ReportType.excel;
	}else if(ReportType.text.toString().equals(reportType)){
		result=ReportType.text;
	}
}
 
String logoURL = WebUtil.getRootAddress(pageContext)+"/images/logos/Nejat2logo.png";
String fromDate = request.getParameter("dateFrom");
String toDate = request.getParameter("dateTo");
String organizationId =request.getParameter("organizationId");
if(fromDate == "")
	objParameters.put("fromDate", null);
else
	objParameters.put("fromDate",fromDate);
if(toDate == "")
	objParameters.put("toDate", null);
else
	objParameters.put("toDate",toDate);
if(organizationId == "")
	objParameters.put("organizationId", null);
else
	objParameters.put("organizationId",Integer.parseInt(organizationId));

objParameters.put("logo", logoURL);

ReportHelper.runReport(request,response,application,out,objParameters,"coreResource","/WEB-INF/reports/core/Co_Ut_UserActions",result);
%>

