
<%@page import="ir.utopia.core.util.report.ReportHelper"%>
<% 
ReportHelper.runReport(request,response,application,out,"coreResource","/reports/core/Co_Ut_ImportLog");
%>
