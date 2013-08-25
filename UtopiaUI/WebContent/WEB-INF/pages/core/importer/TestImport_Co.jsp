<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Map,java.util.HashMap,java.util.Set,ir.utopia.core.messagehandler.MessageHandler"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.sun.corba.ee.org.apache.regexp.recompile"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.struts.AbstractImportableAction"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@page import="ir.utopia.core.security.SecurityProvider"%>
<%@page import="javax.security.auth.Subject"%>
<%@page import="ir.utopia.core.form.UtopiaPageForm"%>
<%@page import="ir.utopia.core.usecase.UsecaseUtil"%>
<%@page import="ir.utopia.core.ContextUtil"%><html>
<head>

<meta http-equiv="content-type" content="text/html;charset=UTF-8">
  <TITLE></TITLE>
  
	<script language="javascript">
	
	function callProcess(){
		window.parent.startProcess(this,"<%=request.getAttribute(AbstractImportableAction.USECASE_ACTION_PARAMAETER_NAME)%>");
	}
	function processFinished(processUI,usecaseActionId,status){
		addRow(processUI);
		}

	function addRow(processUI) {
		
		var table = document.getElementById('dataTable');
		
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		var types = new Array();
		var typesId = new Array();
		types[0] = "<%=Constants.ReportType.pdf.toString()%>" ;
		typesId[0]="<%=Constants.ReportType.pdf.ordinal()%>" ;
		types[1] = "<%=Constants.ReportType.excel.toString()%>" ;
		typesId[1]="<%=Constants.ReportType.excel.ordinal()%>" ;
		types[2] = "<%=Constants.ReportType.html.toString()%>" ;
		typesId[2]="<%=Constants.ReportType.html.ordinal()%>" ;
		types[3] = "<%=Constants.ReportType.text.toString()%>" ;
		typesId[3]="<%=Constants.ReportType.text.ordinal()%>" ;
		path="../../pages/core/importer/Report_Co_Ut_ImportLog.jsp?settinName=<%=WebUtil.write(request.getAttribute("settingName"))	%>";
		path=path+"&reportType=";
		for(i in types){
			var cell1 = row.insertCell(i);
			cell1.appendChild(createTd("<%=MessageHandler.getMessage("reportImportLog", "ir.utopia.core.struts.ImportAction",
					ContextUtil.getLoginLanguage(ContextUtil.createContext(session)))%> ("+ types[i]+")"
					,path+typesId[i]+"&processUID="+processUI));
			}
	}

	function createTd(name,path){
		
		newlink = document.createElement('a');
		tn = document.createTextNode(name);
		newlink.appendChild(tn);
			newlink.setAttribute('class', 'signature');
			path='javascript:showWindow(\''+path+'\')';
			newlink.setAttribute('href',path );
			return newlink;
	}

function showWindow(t){
		window.open(t);
}
	

	</script>
	
</head>
<body class=clsInputPage onload="callProcess()">
<%@taglib prefix="tag" uri="Util" %>
<tag:Header></tag:Header>
<p align="center">
	<%
		String language=WebUtil.isRigthToLeft(session)?"fa":"en"; 
			UtopiaPageForm<?> form=(UtopiaPageForm<?>)request.getAttribute(Constants.PAGE_CONFIG_FORM_NAME);
			String forwardUrl= UsecaseUtil.getActionUrl(form.getUscaseName(),Constants.predefindedActions.search.toString());
			String backUrl=Constants.REDIREDT_TO_PAGE_PREFIX+Constants.RIDIRECT_TO_PAGE_SEPERATOR+UsecaseUtil.getActionUrl(form.getUscaseName(),Constants.predefindedActions.importData.toString());
	%>
			  <tag:ActionButton imageIcon="<%="buttons/btn_Continue_10622_"+language+".gif"%>" forwardURL="<%=forwardUrl%>" redirect="true" text="continue"></tag:ActionButton>
			  <tag:ActionButton imageIcon="<%="buttons/btn_Cancel_10622_"+language+".gif"%>"  forwardURL="<%=backUrl%>" redirect="true" text="return"></tag:ActionButton>
    </p>
    <TABLE id="dataTable" width="100%" border="1" class="clsTableBody">
		
	</TABLE>
    
</body>
</html>