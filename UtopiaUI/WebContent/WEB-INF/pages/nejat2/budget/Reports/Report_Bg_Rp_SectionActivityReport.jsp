<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%
	request.setCharacterEncoding ("UTF-8");
	response.setCharacterEncoding ("UTF-8");
	Map <Object, Object> objParameters = new HashMap <Object, Object> ();
	ReportType result = ReportType.pdf;
	String reportType = request.getParameter ("reportType");
	if (reportType != null && reportType.trim ().length () > 0) {
		reportType = reportType.trim ();
		if (ReportType.html.toString ().equals (reportType)) {
			result = ReportType.html;
		} else if (ReportType.excel.toString ().equals (reportType)) {
			result = ReportType.excel;
		} else if (ReportType.text.toString ().equals (reportType)) {
			result = ReportType.text;
		}
	}
	String logoURL = WebUtil.getRootAddress (pageContext) + "/images/logos/Nejat2logo.png";
	objParameters.put ("logo", logoURL);
	String activityType = request.getParameter ("activityType");
	//objParameters.put("fiscalyearId",Integer.parseInt(fiscalyearId));
	if (activityType == "")
		objParameters.put ("activityType", null);
	else
		objParameters.put ("activityType", Integer.parseInt (activityType));
	String SectionId = request.getParameter ("SectionId");
	//objParameters.put("departmentId",Integer.parseInt(departmentId));
	if (SectionId == "")
		objParameters.put ("SectionId", null);
	else
		objParameters.put ("SectionId", Integer.parseInt (SectionId));
	ReportHelper.runReport (request, response, application, out, objParameters, "budgetResource",
			"/WEB-INF/reports/nejat2/budget/BasicData/SectionActivityReport", result);
%>
