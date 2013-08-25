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
 
 String OrganizationId = request.getParameter("OrganizationId");
 objParameters.put("OrganizationId",Integer.parseInt(OrganizationId));
 
 String HiringType = request.getParameter("HiringType");
 if(HiringType == "")
		objParameters.put("HiringType", null);
	else
		objParameters.put("HiringType",Integer.parseInt(HiringType));
 
 String MonthCalc = request.getParameter("MonthCalc");
 objParameters.put("MonthCalc",Integer.parseInt(MonthCalc));
 
 String workshop = request.getParameter("workshop");
 if(workshop == "")
		objParameters.put("workshop", null);
	else
		objParameters.put("workshop",Integer.parseInt(workshop));
 
 String kind = request.getParameter ("kind");
	if (Integer.parseInt (kind)==0){
		
		ReportHelper.runReport(request,response,application,out,objParameters,"payrollResource","/WEB-INF/reports/nejat2/payroll/tamin/taminReport2_current&out/TaminReport2",result); 	
	}
	
	 if (Integer.parseInt (kind)==1){
		 ReportHelper.runReport(request,response,application,out,objParameters,"payrollResource","/WEB-INF/reports/nejat2/payroll/tamin/taminReport2_current/TaminReport2",result); 
		 
	}
	 
	if(Integer.parseInt (kind)==2)
	{
		ReportHelper.runReport(request,response,application,out,objParameters,"payrollResource","/WEB-INF/reports/nejat2/payroll/tamin/taminReport2_outstanding/TaminReport2",result); 
	}
	

/* ReportHelper.runReport(request,response,application,out,objParameters,"payrollResource","/WEB-INF/reports/nejat2/payroll/tamin/TaminReport2",result); */
%>


