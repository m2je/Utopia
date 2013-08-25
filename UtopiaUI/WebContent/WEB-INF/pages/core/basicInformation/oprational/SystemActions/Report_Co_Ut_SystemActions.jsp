<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="ir.utopia.core.util.report.ReportHelper"%>
<% 
Map<Object,Object> objParameters = new HashMap<Object,Object>();
ReportType result=ReportType.pdf;
String logoURL = WebUtil.getRootAddress(pageContext)+"/images/logos/Nejat2logo.png";
objParameters.put("logo",logoURL);

ReportHelper.runReport(request,response,application,out,objParameters,"coreResource","/WEB-INF/reports/core/Co_Ut_SystemActions",result);

%>
