
<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%
//request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
Map<Object,Object> objParameters = new HashMap<Object,Object>();

ReportType reportType=ReportType.html;

ReportHelper.runReport(request,response,application,out,objParameters,"accSQLResource","/WEB-INF/reports/nejat2/ManagementReports/Nejat1Reports/Nejat1Report",reportType);
%>