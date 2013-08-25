
<%@page import="ir.utopia.core.constants.Constants"%>
<%@page import="ir.utopia.core.constants.Constants.ReportType"%>
<%@page import="java.util.logging.Level"%>

<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.struts.AbstractImportableAction"%><%@page import="java.io.File"%><%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="ir.utopia.core.util.DateUtil,ir.utopia.core.util.report.ReportHelper,java.util.*,ir.utopia.core.util.WebUtil,ir.utopia.core.util.EnumUtil"%>

<%
String lang =WebUtil.getLanguage(session);
Map<Object,Object> objParameters = new HashMap<Object,Object>();
Long processUID=null;
Constants.ReportType reportType=ReportType.pdf;
try{
String pID=request.getParameter("processUID");
 processUID=Long.parseLong(pID);
 reportType =(ReportType)EnumUtil.getEnumValue(ReportType.class, Integer.parseInt(request.getParameter("reportType")));
 }
catch(Exception e){
	e.printStackTrace();
}

int from=0,to=0;
String reportError=MessageHandler.getMessage("ImportLogDoseNotExists","ir.utopia.core.struts.ImportAction",lang);
Map<String,String>map=(Map<String,String>) request.getParameterMap();
int pageCount=AbstractImportableAction.getLogPageCount(processUID,session);

if((map.containsKey("from")&&map.containsKey("to"))||pageCount==1){
	try{
		from=Integer.parseInt(request.getParameter("from"));
		to=Integer.parseInt(request.getParameter("to"));
	}catch(Exception e){
		
	}
	if(processUID==null){
		WebUtil.write(reportError);
	}else{
		Vector<Map<String,Object>>dataVector=AbstractImportableAction.getImportLog(processUID,from,to,WebUtil.write(request.getParameter("settinName")),session);
		if(dataVector!=null)
			ReportHelper.runReport(request,response,application,out,objParameters,dataVector,"/reports/core/Co_Ut_ImportLog_manualFill",reportType);
		else{
			WebUtil.write(reportError);
		}
	}
}else{
	int logCount=AbstractImportableAction.getLogCount(processUID,session);
	if(logCount==0){
		Vector<Map<String,Object>>dataVector=AbstractImportableAction.getImportLog(processUID,from,to,WebUtil.write(request.getParameter("settinName")),session);
		if(dataVector!=null)
			ReportHelper.runReport(request,response,application,out,objParameters,dataVector,"/reports/core/Co_Ut_ImportLog_manualFill",reportType);
		else{
			WebUtil.write(reportError);
		}
	}else{
	%>
	<html>
	<head>
	<title><%=MessageHandler.getMessage("applicationTitle","ir.utopia.nejat2.Nejat2UI",WebUtil.getLanguage(session)) %></title>
	<link type="text/css" rel="stylesheet" href="../../../css/Intcss.css">
	</head>
	<body>
	<%
	
	%>
	<table class="clsTableBody" width="100%"> 
	<%
	
	for(int i=0;i<pageCount;i++){
		int myFrom=i*AbstractImportableAction.MESSAGE_LOG_FILE_SIZE;
		int myTo=myFrom+AbstractImportableAction.MESSAGE_LOG_FILE_SIZE;
		myFrom++;
		if(myTo>logCount){
			myTo=logCount;
		}
		%>
		<tr>
		<td>
		<a href="#" onclick="javascript:window.open('../../core/importer/Report_Co_Ut_ImportLog.jsp?reportType=<%=request.getParameter("reportType")%>&processUID=<%=request.getParameter("processUID")%>&from=<%=myFrom%>&to=<%=myTo%>&settinName=<%=WebUtil.write(request.getParameter("settinName")) %>') ">
		<%= MessageHandler.getMessage("reportImportLog", "ir.utopia.core.struts.ImportAction",lang)+" "+myFrom+".."+myTo+"("+reportType.name()+")"%>
		</a>
		</td>
		</tr>
		<%}
}}
%>
</table>
</body>
</html>