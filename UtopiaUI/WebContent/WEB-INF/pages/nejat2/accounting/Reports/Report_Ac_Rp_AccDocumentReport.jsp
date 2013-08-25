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
 objParameters.put("logo",logoURL);

 String fromDate = request.getParameter("dateFrom");
 String toDate = request.getParameter("dateTo");

 if(fromDate == "")
 	objParameters.put("fromDate", null);
 else
 	objParameters.put("fromDate",fromDate);
 if(toDate == "")
 	objParameters.put("toDate", null);
 else
 	objParameters.put("toDate",toDate);
 
 
 String documentFrom = request.getParameter("documentFrom");
 String documentTo = request.getParameter("documentTo");
 

 if(documentFrom == "")
	objParameters.put("documentFrom",null);
else
	objParameters.put("documentFrom",Integer.parseInt(documentFrom));

if(documentTo == "")
	objParameters.put("documentTo",null);
else
	objParameters.put("documentTo",Integer.parseInt(documentTo)); 

 String OrganizationId =request.getParameter("OrganizationId");
 objParameters.put("OrganizationId",Integer.parseInt(OrganizationId));


 String FiscalYear =request.getParameter("FiscalYear");
 objParameters.put("FiscalYear",Integer.parseInt(FiscalYear));
 
 
ReportHelper.runReport(request,response,application,out,objParameters,"AccountingResource","/WEB-INF/reports/nejat2/accounting/AccDocumentReport",result);
%>


