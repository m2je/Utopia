<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.lang.Math"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.common.basicinformation.notification.bean.CmNotificationFacadeRemote" %>
<%@page import="ir.utopia.common.basicinformation.notification.persistent.CmNotification" %>
<%@page import="ir.utopia.common.basicinformation.notificationroles.persistent.CmNotificationRoles" %>
<%@page import="ir.utopia.common.basicinformation.notificationroles.bean.CmNotificationRolesFacadeRemote" %>
<%@page import="ir.utopia.core.ServiceFactory" %>
<%@page import="ir.utopia.core.ContextUtil" %>
<%@page import="com.opensymphony.xwork2.ActionContext" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.Set" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.Set" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%
	String index = "0";// + (Math.round(Math.floor(Math.random() * 2)) * Math.round(Math.floor(Math.random() * 1)));
%>
<style type="text/css">
	body{
		background-attachment: fixed;
		background-image: url(<%=WebUtil.getImage("logos/first_page_0.jpg")%>);
		background-repeat: no-repeat;
		background-position: center 30%;
		background-color: #EFEFEF;
	}
</style>
</head>
<script type="text/javascript">
	var i = <%=index%>;
	//window.setInterval('changeImage()', 60000);
	function changeImage(){
		if(++i > 1)
			i = 0;
		
		document.body.style.backgroundImage = url(<%=WebUtil.getImage("logos/first_page_0.jpg")%>);
	}
</script>
<body>
<div dir="rtl" style="font-family: Tahoma; font-size:16px; color:red;font-weight: bold;">
<%Map<String,Object>context= ContextUtil.createContext(session); 
CmNotificationFacadeRemote bean = (CmNotificationFacadeRemote)ServiceFactory.lookupFacade(CmNotificationFacadeRemote.class.getName());
CmNotificationRolesFacadeRemote rolesBean = (CmNotificationRolesFacadeRemote)ServiceFactory.lookupFacade(CmNotificationRolesFacadeRemote.class.getName());
List<CmNotification> cmNotifications= bean.findAll(true,  null);
Date curDate = new Date();
if(cmNotifications!=null){
	for(CmNotification cmNotification:cmNotifications){
		if(cmNotification.getEndDate().after(curDate) && cmNotification.getStartDate().before(curDate)){
			List<CmNotificationRoles> cmNotificationRoles = rolesBean.findByProperty("cmNotification", cmNotification,  null);
			if(cmNotificationRoles.isEmpty()){ %>
		    	<br>
		    	* <%=cmNotification.getNotification()%>
<%			}else{
            	Set<Long> roles = ContextUtil.getUserRoles(context);
                if(roles!=null){
                	for(CmNotificationRoles cmRoles :cmNotificationRoles ){
                		if(roles.contains(cmRoles.getCoRole().getCoRoleId())){%>
                		    <br>
                			* <%=cmNotification.getNotification()%>
<%  		            }
	                }
            	}  
            }
	    }
	}
} 

%>
</div>
</body>
</html>