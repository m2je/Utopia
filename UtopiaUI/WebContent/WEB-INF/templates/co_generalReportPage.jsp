
<%@page import="ir.utopia.core.constants.Constants"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="java.util.logging.Level"%>
<%--  <%@page import="ir.utopia.core.log.LogUtil"%>  --%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.struts.AbstractImportableAction"%><%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.DateUtil,ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil,ir.utopia.core.util.EnumUtil"%>

<%
	String lang =WebUtil.getLanguage(session);
Map<Object,Object> objParameters = new HashMap<Object,Object>();
Long processUID=null;
Constants.ReportType reportType=ReportType.pdf;
try{
String pID=request.getParameter("reportUID");
 processUID=Long.parseLong(pID);
 reportType =(ReportType)EnumUtil.getEnumValue(ReportType.class, Integer.parseInt(request.getParameter("reportType")));
 }
catch(Exception e){
	/* LogUtil.log(Level.WARNING,e); */
	e.printStackTrace();
}

int from=0,to=0;
String reportError=MessageHandler.getMessage("ImportLogDoseNotExists","ir.utopia.core.struts.ImportAction",lang);
Map<String,String>map=(Map<String,String>) request.getParameterMap();
int pageCount=UtopiaUIServiceProxy.getReportPageCount(processUID,session);
int logCount=UtopiaUIServiceProxy.getReportRecordCount(processUID,session);
to=logCount;
if((map.containsKey("from")&&map.containsKey("to"))||pageCount<=1){
	try{
		from=Integer.parseInt(request.getParameter("from"));
		to=Integer.parseInt(request.getParameter("to"));
	}catch(Exception e){
		
	}
	if(processUID==null){
		WebUtil.write(reportError);
	}else{
		Vector<Map<String,Object>>dataVector=UtopiaUIServiceProxy.getReportData(processUID,from,to,session);
		if(dataVector!=null){
	 String jrxml=request.getParameter("jrxml");
	 String filename=UtopiaUIServiceProxy.getReportName(processUID,session)+" "+from+".."+to;
	 if(Constants.ReportType.excel.equals(reportType)||Constants.ReportType.text.equals(reportType)){
		 
		response.setHeader("Content-Disposition", "attachment;filename="+filename+"."+reportType.name());
		 
	 }else{
		 
	 }
	ReportHelper.runReport(request,response,application,out,objParameters,dataVector,jrxml,reportType);
	}
		else{
	WebUtil.write(reportError);
		}
	}
}else{
%>
	
<%@page import="ir.utopia.core.util.tags.datainputform.server.UtopiaUIServiceProxy"%><html>
	<head>
	<title><%=MessageHandler.getMessage("applicationTitle","ir.utopia.nejat2.Nejat2UI",WebUtil.getLanguage(session))%></title>
	<link type="text/css" rel="stylesheet" href="../../css/Intcss.css">
	</head>
	<body>
	<table class="clsTableBody" width="100%"> 
	<%
 		for(int i=0;i<pageCount;i++){
 			int myFrom=i*UtopiaUIServiceProxy.REPORT_PAGE_SIZE;
 			int myTo=myFrom+UtopiaUIServiceProxy.REPORT_PAGE_SIZE;
 			myFrom++;
 			if(myTo>logCount){
 		myTo=logCount;
 			}
 	%>
		<tr>
		<td>
		<a href="#" onclick="javascript:window.open('./SearchReport<%=Constants.STRUTS_EXTENSION_NAME%>?reportType=<%=request.getParameter("reportType")%>&reportUID=<%=request.getParameter("reportUID")%>&from=<%=myFrom%>&to=<%=myTo%>&jrxml=<%=request.getParameter("jrxml")%>') ">
		<%=UtopiaUIServiceProxy.getReportName(processUID,session)+" "+myFrom+".."+myTo+"("+reportType.name()+")"%>
		</a>
		</td>
		</tr>
		<%}
}
%>
</table>
</body>
</html>