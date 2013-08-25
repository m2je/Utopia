<%@page import="ir.utopia.common.basicinformation.fiscalyear.FiscalYearInfo"%>
<%@page import="ir.utopia.common.basicinformation.fiscalyear.FiscalYearUtil"%>
<%@page import="ir.utopia.core.security.user.persistence.CoUser"%>
<%@page import="ir.utopia.core.security.user.bean.CoUserFacadeRemote"%>
<%@page import="java.util.Locale"%>
<%@page import="ir.utopia.core.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="ir.utopia.nejat2.Nejat2Constants"%>
<%@page import="java.util.Map"%>
<%@page import="javax.security.auth.Subject"%>
<%@page import="ir.utopia.common.basicinformation.organization.persistent.CmOrganization"%>
<%@page import="ir.utopia.common.basicinformation.organization.bean.CmOrganizationFacadeRemote"%>
<%@page import="ir.utopia.core.security.SecurityProvider"%>
<%@page import="ir.utopia.core.ContextUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ir.utopia.core.util.WebUtil,ir.utopia.core.ServiceFactory"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="java.lang.Exception"%>

<html dir="rtl">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<style>
		td,tr,table{
			font-family:tahoma;
			font-size:12px;
			color:navy;
			padding-top: 0px;
			margin-top: :0px;
		}
		td.line{
			color:silver;
			font-size:14px;
			width:30px;
			font-family: arial;
		}
		.title{
			color:brown;
			
		}
		a{
			text-decoration: none;
			color: black;		
		}
		a:HOVER{
			color: red;
		}
	</style>
	<script type="text/javascript"> 
	function changeOrganization(){
		window.parent.startProcess(this,'129');
	}
	function changeFiscalYear(){
		window.parent.startProcess(this,'1112');
	}
	function processFinished(processUI,usecaseActionId,status){
		
		}
	function myTimer()
	{
	 myVar=setInterval(function(){var d=new Date();
		document.getElementById("demo").innerHTML=d.toLocaleTimeString() ;;
	
	 },1000);
	}
	
	</script>
</head>
<html dir="rtl">
<body style="margin-top:0px;padding-top:0px;background-color: #BBDFF7" onload="myTimer()">

	<table cellpadding="0" cellspacing="0" style="margin:0px;padding:0px;" width="100%" dir="<%=WebUtil.getDirection(session)%>">
	<tr>
		<td nowrap="nowrap" valign="top" width="35px">
			<img src="<%=WebUtil.getImage("logos/utopia-logo.jpg") %>"/>
		</td>
		<td nowrap="nowrap" align="<%=WebUtil.getAlign(session)%>" valign="middle">
			<small><%=MessageHandler.getMessage("copyRight","ir.utopia.core.ApplicationUI",WebUtil.getLanguage(session)) %></small>
			<small><%=MessageHandler.getMessage("version","ir.utopia.core.ApplicationUI",WebUtil.getLanguage(session)) %></small>
		</td>
		<td><%
		String lang =WebUtil.getLanguage(session);
		Date date =new Date();
		String dateStr =DateUtil.getDateString(date, new Locale(lang));
		Map<String,Object>context=ContextUtil.createContext(session);
		Long []availablesOrganizations=null;
		Long userId  ;
		if(ServiceFactory.isSupportOrganization()){
		SecurityProvider secPro=ServiceFactory.getSecurityProvider();
		Subject subject=(Subject)session.getAttribute(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
			Long orgId=secPro.getOrganization(subject,context);
			String organizationName;
			if(orgId==null||orgId.equals(0l)){
				 organizationName="*";
			}else{
				boolean showAllAccessibleOrgs= ContextUtil.isShowAllOrganizationData(context);
				
				CmOrganizationFacadeRemote orgFacade=(CmOrganizationFacadeRemote)ServiceFactory.lookupFacade(CmOrganizationFacadeRemote.class.getName());
				availablesOrganizations=secPro.getAvialableOrganizations(subject);	
				if(showAllAccessibleOrgs){
					organizationName=MessageHandler.getMessage("allAccessibleOrganizations", "ir.utopia.core.ApplicationUI",  WebUtil.getLanguage(session));						
				}else{
					CmOrganization orgnization= orgFacade.findById(orgId);
					organizationName=orgnization.getName();
				}
			
			}
			String orgInfoMessage=MessageHandler.getMessage("organizationInfo", "ir.utopia.core.ApplicationUI",  WebUtil.getLanguage(session));
			orgInfoMessage=orgInfoMessage.replaceAll("@organization@", organizationName);
			out.write(WebUtil.write(orgInfoMessage)); 
		}
		
		%></td>
		<td><%
		if(ServiceFactory.isSupportOrganization() && availablesOrganizations!=null&&availablesOrganizations.length>1){
				String changeOrganization=	MessageHandler.getMessage("changeOrganization", "ir.utopia.core.ApplicationUI",  ContextUtil.getLoginLanguage(context));
				out.write("<a href=\"#\" onclick=\"changeOrganization()\" >"+WebUtil.write(changeOrganization)+" </a> ");
		} 
		%></td>
		 <td>&nbsp; &nbsp;
		<%  Subject subject=WebUtil.getUser(session);
		SecurityProvider secPro=ServiceFactory.getSecurityProvider();
		userId =secPro.getUserId(subject);
		 String username ;
		if(userId==null){
			username="*";
		}else{
			CoUserFacadeRemote userFacade=(CoUserFacadeRemote)ServiceFactory.lookupFacade(CoUserFacadeRemote.class.getName());
			CoUser user= userFacade.findById(userId);
			username=user.getUsername();
		}
		String userInfoMessage=MessageHandler.getMessage("userInfo", "ir.utopia.core.ApplicationUI",  WebUtil.getLanguage(session));
		userInfoMessage=userInfoMessage.replaceAll("@user@", username);
		out.write(WebUtil.write(userInfoMessage)); 
		 
		%> </td>
		<td>&nbsp; &nbsp;<%
		String language=WebUtil.getLanguage(session);
		FiscalYearInfo info= FiscalYearUtil.getFiscalYear(session);
		String fiscalYearName=info!=null?info.getName():null;
		String fiscalYearMessage;
		if(fiscalYearName!=null){
			fiscalYearMessage=MessageHandler.getMessage("currentFiscalYear", "ir.utopia.nejat2.Nejat2UI",  language);
			fiscalYearMessage=fiscalYearMessage.replace("@fiscalYearName@", fiscalYearName);
			
		}else{
			fiscalYearMessage=MessageHandler.getMessage("unknownFiscalYear", "ir.utopia.nejat2.Nejat2UI",  language);
		}
		 %><td><a href="#" onclick="changeFiscalYear()" ><%= WebUtil.write(fiscalYearMessage,language)%></a></td>
		  
		<td><%=dateStr %></td> <td id="demo"></td>
		
		
		<%
		try{
		%>
		
		<td class="line">|</td>
		<%
		}catch(Exception e){
			out.write("<td class=\"title\" nowrap=\"nowrap\">" +MessageHandler.getMessage("error_fperiod_not_defined","ir.utopia.core.ApplicationUI",WebUtil.getLanguage(session)) + "</td>");
		}
		%>						
	</tr>
	</table>
</body>
</html>
