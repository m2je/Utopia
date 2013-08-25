<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
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
	String logoURL = WebUtil.getRootAddress (pageContext) + "/images/logos/logo4.jpg";
	/* String fromDate = request.getParameter("dateFrom");
	 String toDate = request.getParameter("dateTo");
	 String userId =request.getParameter("userId");
	 String orgId =request.getParameter("orgId");
	 String inventoryId =request.getParameter("inventoryId");
	 String merchanId =request.getParameter("merchanId"); */
	String fiscalyearId = request.getParameter ("fiscalyearId");
	/*if(fromDate == "")
	 objParameters.put("fromDate", null);
	 else
	 objParameters.put("fromDate",fromDate);
	 if(toDate == "")
	 objParameters.put("toDate", null);
	 else
	 objParameters.put("toDate",toDate);
	 objParameters.put("userId",Integer.parseInt(userId));
	 if(orgId == "")
	 objParameters.put("orgId", null);
	 else
	 objParameters.put("orgId",Integer.parseInt(orgId));
	 if(inventoryId == "")
	 objParameters.put("inventoryId",null);
	 else
	 objParameters.put("inventoryId",Integer.parseInt(inventoryId));
	 if(merchanId == "")
	 objParameters.put(merchanId, null);
	 else
	 objParameters.put("merchanId",Integer.parseInt(merchanId));*/
	if (fiscalyearId == "")
		objParameters.put ("fiscalyearId", null);
	else
		objParameters.put ("fiscalyearId", Integer.parseInt (fiscalyearId));
	objParameters.put ("logo", logoURL);
	objParameters.put ("SUBREPORT_DIR",
			application.getRealPath ("/WEB-INF/reports/nejat2/budget/ApprovedCredit/") + "\\");
	ReportHelper.runReport (request, response, application, out, objParameters, "budgetResource",
			"/WEB-INF/reports/nejat2/budget/ApprovedCredit/ApprovedCreditForDep", result);
%>
