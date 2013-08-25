<%@page import="ir.utopia.core.constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.util.WebUtil"%>

<%@page import="ir.utopia.core.security.SecurityProvider"%>
<%@page import="javax.security.auth.Subject"%>
<%@page import="java.security.Principal"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@page import="ir.utopia.core.ContextUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Map"%><html dir="rtl">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String title=MessageHandler.getMessage("applicationTitle","ir.utopia.core.ApplicationUI",WebUtil.getLanguage(session));
%>
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="<%=WebUtil.getCss("Intcss.css") %>">
<link rel="stylesheet" type="text/css" href="<%=WebUtil.getCss("Header.css") %>">
<script>
function help(){
	window.open('../User_Guide.pdf');
}
function logout(){
parent.location='logout<%=Constants.STRUTS_EXTENSION_NAME %>';
}
</script>
<style type="text/css">
p{
	padding: 5px;
	margin: 0px;
}
body {
	background-image: url('<%=WebUtil.getImage("logos/header_bg.jpg")%>');background-repeat:repeat-x;background-position: left top;
	margin-top:0;
	padding-top:0;
	padding-right: 0px;	
}
td#link{
	text-align: center;
	height: 10px;
}
.line{
	font-family:arial;
	font-size:14px;
	color: silver;
	padding-left: 3px; 
	padding-right: 3px; 
}
</style>

</head>

<body>
<table  width="100%" cellpadding="0" cellspacing="0" border="0" style="padding:0;margin:0">
<tr>
<%
Subject userProfile=(Subject) session.getAttribute(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
Map<String,Object>context=ContextUtil.createContext(session);
String userFullName=ServiceFactory.getSecurityProvider().getFullUserName(userProfile,context);
int hour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

String time;
if(hour<=5){
	time="goodNight";
}else if(hour<=14){
	time="goodMorning";
}else if(hour<=18){
	time="goodAfterNoon";
}else {
	time="goodNight";
}
String timeMessage=MessageHandler.getMessage(time,"ir.utopia.core.constants.Glossory",ContextUtil.getLoginLanguage(context));
%>
<td width="100px" align="left" valign="middle">
		<table  style="margin-top: 0px" height="100%" dir="<%=WebUtil.getDirection(session) %>">
		<!--  
		<tr><td nowrap="nowrap" class="welcomeMessage"><%=WebUtil.write(timeMessage)%> <%=WebUtil.write(userFullName)%></td></tr>
		<tr><td id="link"><a class=clsHeaderPageLink href=# ><%=MessageHandler.getMessage("about","ir.utopia.core.ApplicationUI",WebUtil.getLanguage(session)) %></a></td></tr>
		-->
		<tr><td id="link"><a class=clsHeaderPageLink href=# onclick=help()><%=MessageHandler.getMessage("help","ir.utopia.core.ApplicationUI",WebUtil.getLanguage(session)) %></a></td></tr>
		<tr><td id="link"><a class=clsHeaderPageLink href=# onclick=logout()><%=MessageHandler.getMessage("logout","ir.utopia.core.ApplicationUI",WebUtil.getLanguage(session)) %></a></td></tr>
	</table>
</td>
 
<td align="center" valign="middle" >
	<%
		if(WebUtil.isRigthToLeft(session))
			out.write("<img src=\""+WebUtil.getImage("logos/nejat_fillback_blue_center.jpg")+"\">");
		else
			out.write("<img src=\""+WebUtil.getImage("logos/title_fillback_en_blue.jpg")+"\">");
	%>
</td>

<td width="220px" valign="middle" align="right">
	<img src="<%= WebUtil.getImage("logos/nejat_fillback_blue.jpg")%>">
</td>

<tr>
</table>
</body>

</html>