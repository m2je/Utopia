<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.DateUtil,ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
<%
Map<Object,Object> objParameters = new HashMap<Object,Object>();
System.out.println("<<<<<<<<i'm here");
//String max_date = request.getParameter("max_IssuanceDate");
//String min_date = request.getParameter("min_IssuanceDate");
String lang =WebUtil.getLanguage(session);
//max_date =DateUtil.getDateString(DateUtil.solarToDate(max_date),Locale.ENGLISH);
//min_date =DateUtil.getDateString(DateUtil.solarToDate(min_date),Locale.ENGLISH);
//max_date = "'"+max_date+"'";
//min_date = "'"+min_date+"'" ;
//objParameters.put("max_IssuanceDate",max_date);
//objParameters.put("min_IssuanceDate",min_date);
//objParameters.put("max_IssuanceDate_fa",request.getParameter("max_IssuanceDate"));
//objParameters.put("min_IssuanceDate_fa",request.getParameter("min_IssuanceDate"));

ReportHelper.runReport(request,response,application,out,objParameters,"assetResource","/WEB-INF/reports/nejat2/asset/as_bd_realestate");
%>