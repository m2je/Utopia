<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.DateUtil,ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
<%
Map<Object,Object> objParameters = new HashMap<Object,Object>();
System.out.println("barcode");
String lang =WebUtil.getLanguage(session);
String armURL = WebUtil.getRootAddress(pageContext)+"/images/logos/arm.jpg";
Long transactionid =Long.parseLong(request.getParameter("editItem"));
objParameters.put("transactionid",transactionid);
objParameters.put("Arm",armURL);
ReportHelper.runReport(request,response,application,out,objParameters,"assetResource","/WEB-INF/reports/nejat2/asset/as_bd_barcode");
%>