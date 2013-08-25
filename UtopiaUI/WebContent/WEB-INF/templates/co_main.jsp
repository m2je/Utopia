<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.util.WebUtil"%>

<%@page import="java.util.Map"%>
<%@page import="ir.utopia.core.struts.ActionUtil"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<html dir="<%=WebUtil.getDirection(session)%>" xml:lang="fa">

<head>
<style type="text/css">
	</style>
	<meta name="gwt:module" content="com.google.gwt.examples.i18n.ColorNameLookupExample">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<title><%=MessageHandler.getMessage("applicationTitle","ir.utopia.nejat2.Nejat2UI",WebUtil.getLanguage(session)) %></title>
<script type="text/javascript">
var childwindow;
function startProcess(childwin,param){
		<%
		Map<Integer, String>map= ActionUtil.parseClassAndMethod(request.getRequestURI());
		boolean confirmDiscard=false;
		if(map!=null){
			String actionName=map.get(ActionUtil.METHOD);
			confirmDiscard=Constants.predefindedActions.save.toString().equalsIgnoreCase(actionName)||
				Constants.predefindedActions.update.toString().equalsIgnoreCase(actionName);
		}
		%>
		childwindow=childwin;
	   window.parent.startProcessConfirm(param,'<%=confirmDiscard%>',this);
	   }
function refreshPage(){
	var f1= window.document.getElementById("headerFrame");
	var f2=window.document.getElementById("menuFrame"); 
	var f3=window.document.getElementById("footerFrame");
	f1.src=f1.src;
	f3.src=f3.src;
	this.menuFrame.location=this.menuFrame.location;
	f3.src=f3.src;
	f1.location.reload();
	f2.location.reload();
	f3.location.reload();
}

function processFinished(processUI,usecaseActionId,status){
	childwindow.processFinished(processUI,usecaseActionId,status);	
	}

</script>
</head>
	
	<frameset rows="50px,*,30px" frameborder="no">
		<frame name="headerFrame" src="header<%=Constants.STRUTS_EXTENSION_NAME %>" id="headerFrame" scrolling="no" noresize="noresize">
		<frameset cols="*,260px" frameborder="no">
			<frame  name="menuFrame" id="menuFrame" src="firstPage<%=Constants.STRUTS_EXTENSION_NAME %>" style="border-right: 0px solid silver;border-bottom: 0px solid silver;"></frame>
			<frame src="deluxe-tree<%=Constants.STRUTS_EXTENSION_NAME %>" style="overflow: auto"></frame>
		</frameset>
		<frame name="footerFrame" id="footerFrame" src="footer<%=Constants.STRUTS_EXTENSION_NAME %>" scrolling="no"  frameborder="0"  noresize="noresize" ></frame>
	</frameset>
	
</html>
