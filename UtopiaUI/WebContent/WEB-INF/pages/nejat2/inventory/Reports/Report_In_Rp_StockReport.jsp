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
String logoURL = WebUtil.getRootAddress(pageContext)+"/images/logos/logo4.jpg";
String userId =request.getParameter("userId");
String inventoryId =request.getParameter("inventoryId");
String merchanId =request.getParameter("merchanId");

objParameters.put("userId",Integer.parseInt(userId));
if(inventoryId == "")
	objParameters.put("inventoryId",null);
else
	objParameters.put("inventoryId",Integer.parseInt(inventoryId));
if(merchanId == "")
	objParameters.put(merchanId, null);
else
	objParameters.put("merchanId",Integer.parseInt(merchanId));
objParameters.put("logo", logoURL);

ReportHelper.runReport(request,response,application,out,objParameters,"inventoryResource","/WEB-INF/reports/nejat2/inventory/StockReport",result);
%>

