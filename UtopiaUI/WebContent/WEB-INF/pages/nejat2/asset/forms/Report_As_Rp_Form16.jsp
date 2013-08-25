<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.DateUtil,ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
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
System.out.println("Form16");
String location_id =request.getParameter("location_id");
if(location_id == "")
	objParameters.put("location_id",null);
else
	objParameters.put("location_id",Long.parseLong(location_id));
String lang =WebUtil.getLanguage(session);

ReportHelper.runReport(request,response,application,out,objParameters,"assetResource","/WEB-INF/reports/nejat2/asset/as_rp_form16",result);
%>