<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.DateUtil,ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
<%
Map<Object,Object> objParameters = new HashMap<Object,Object>();
System.out.println("Form19");
String lang =WebUtil.getLanguage(session);

ReportHelper.runReport(request,response,application,out,objParameters,"assetResource","/WEB-INF/reports/nejat2/asset/as_rp_form19");
%>