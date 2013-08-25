<%@page import="ir.utopia.core.constants.Constants" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%><html xml:lang="fa">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<meta name="gwt:property" content="locale=fa_IR">
<link rel="shortcut icon" href="./images/logos/icon.gif" type="image/gif">
<link type="text/css" rel="stylesheet" href="./css/Intcss.css">
<title><%=MessageHandler.getMessage("applicationTitle","ir.utopia.nejat2.Nejat2UI",WebUtil.getLanguage(session)) %></title>
<script type="text/javascript">
var childwin;
function startProcessConfirm(param,confirm,childWindow){
	childwin=childWindow;
		startProcess(param,confirm);
	
	   }
function refreshPage(){
	childwin.refreshPage();
}
function processFinished(processUI,usecaseActionId,status){
	childwin.processFinished(processUI,usecaseActionId,status);	
	}
</script>
<script type="text/javascript" language="javascript" src="<%=WebUtil.getJsFile(pageContext,"components/ir.utopia.core.util.tags.process.ProcessHandler/ir.utopia.core.util.tags.process.ProcessHandler.nocache.js") %> "></script>

</head>
<body style="margin-top:0;margin-right:0;margin-left:0;margin-bottom: 0" dir="<%=WebUtil.getDirection(session)%>">
    <input type=hidden name="lastProcessUID" id="lastProcessUID" >
	<iframe width="100%" height="100%" src="co_main<%=Constants.STRUTS_EXTENSION_NAME %>" marginheight="0" marginwidth="0" frameborder="0" id="pageFrame" ></iframe>
</body>
</html>
