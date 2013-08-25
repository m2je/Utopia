<%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="ir.utopia.core.constants.Constants"%>
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
	
	String sectionId =request.getParameter("sectionId");
	if(sectionId == "")
		 objParameters.put("sectionId", null);
		 else
		 objParameters.put("sectionId",Integer.parseInt(sectionId));
	
	
	 String creditsourceId =request.getParameter("creditsourceId");
	
	 if(creditsourceId == "")
		objParameters.put("creditsourceId", null);
		 else
		 objParameters.put("creditsourceId",Integer.parseInt(creditsourceId));
	 
	 
	 String organizationId =request.getParameter("organizationId"); 
	 if(organizationId == "")
		 objParameters.put("organizationId",null);
		 else
	  objParameters.put("organizationId",Integer.parseInt(organizationId));
	
	 
	 String planid =request.getParameter("planid");
	 if(planid == "")
		 objParameters.put(planid, null);
		 else
		 objParameters.put("planid",Integer.parseInt(planid));
	 
	 String depfiscalyearId = request.getParameter ("depfiscalyearId");
	 if (depfiscalyearId == "")
			objParameters.put ("depfiscalyearId", null);
		else
		objParameters.put ("depfiscalyearId", Integer.parseInt (depfiscalyearId));
	 
	 
	/* 
	Integer agreementId = Integer.parseInt (request.getParameter ("editItem"));

	objParameters.put ("agreementId", agreementId);
	 
	objParameters.put ("SUBREPORT_DIR", application.getRealPath ("/WEB-INF/reports/nejat2/budget/Agreement") + "\\");*/
	ReportHelper.runReport (request, response, application, out, objParameters, "creditResource",
			"//WEB-INF/reports/nejat2/credit/CreditReport/CreditReport", result);	
%>

	<!-- 
	 String sectionId =request.getParameter("sectionId");
	 String creditsourceId =request.getParameter("creditsourceId");
	 String organizationId =request.getParameter("organizationId"); 
	 String planid =request.getParameter("planid");
	String depfiscalyearId = request.getParameter ("depfiscalyearId");
	if(sectionId == "")
	 objParameters.put("sectionId", null);
	 else
	 objParameters.put("sectionId",Integer.parseInt(sectionId));
	 if(creditsourceId == "")
	 objParameters.put("creditsourceId", null);
	 else
	 objParameters.put("creditsourceId",Integer.parseInt(creditsourceId));
	 if(organizationId == "")
	 objParameters.put("organizationId",null);
	 else
	 objParameters.put("organizationId",Integer.parseInt(organizationId));
	 if(planid == "")
	 objParameters.put(planid, null);
	 else
	 objParameters.put("planid",Integer.parseInt(planid));
	if (depfiscalyearId == "")
		objParameters.put ("depfiscalyearId", null);
	else
		objParameters.put ("depfiscalyearId", Integer.parseInt (depfiscalyearId));
	objParameters.put ("logo", logoURL);
	/* objParameters.put ("SUBREPORT_DIR",
			application.getRealPath ("/WEB-INF/reports/nejat2/budget/ApprovedCredit/") + "\\"); */
	ReportHelper.runReport (request, response, application, out, objParameters, "creditResource",
			"/WEB-INF/reports/nejat2/credit/CreditReport/CreditReport", result); -->
