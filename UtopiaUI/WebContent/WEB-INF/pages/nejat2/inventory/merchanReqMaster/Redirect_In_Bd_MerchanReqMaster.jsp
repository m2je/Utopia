<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.ContextUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="ir.utopia.core.security.userrole.persistence.CoUserRoles"%>
<%@page import="ir.utopia.core.security.userrole.bean.CoUserRolesFacadeRemote"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
  <TITLE></TITLE>
  <link href="<%=WebUtil.getCss("Intcss.css") %>" rel="stylesheet" type="text/css">
</head>
<body class=clsInputPage>
<%
Map<String,Object>context=ContextUtil.createContext(session);
List<CoUserRoles> userRoles = new ArrayList<CoUserRoles>(0);
List<Long> roleIds = new ArrayList<Long>(0);
try {
	CoUserRolesFacadeRemote userRolesBean = (CoUserRolesFacadeRemote)ServiceFactory.lookupFacade(CoUserRolesFacadeRemote.class.getName());
	userRoles = userRolesBean.findByProperty("coUser.coUserId", (Long) context.get("userId"), context, null);
} catch (NamingException e) {
	e.printStackTrace();
}
if(userRoles!=null && userRoles.size()>0){
	for(CoUserRoles userRole:userRoles){
		roleIds.add(userRole.getCoRole().getCoRoleId());
	}
}
Long ordinaryUserRoleId = (Long) context.get("In_OrdinaryUserRole");
Long inventoryManagerRole = (Long) context.get("In_InventoryManagerRole");
Long developmentManagerRole = (Long) context.get("In_DevelopmentManagerRole");
Long iTManagerRole = (Long) context.get("In_ITManagerRole");
Long inventoryUserRole = (Long) context.get("In_InventoryUserRole");
Long inventoryExpertRole = (Long) context.get("In_InventoryExpertRole");
Long unitManagerRole = (Long) context.get("In_UnitManagerRole");

if(roleIds!=null && roleIds.size()>0){
	if(roleIds.contains(ordinaryUserRoleId) && 
			!roleIds.contains(inventoryManagerRole) && 
			!roleIds.contains(developmentManagerRole) && 
			!roleIds.contains(iTManagerRole) && 
			!roleIds.contains(inventoryUserRole) && 
			!roleIds.contains(inventoryExpertRole) && 
			!roleIds.contains(unitManagerRole)){
		response.sendRedirect("go-save_In_Bd_MerchanReqMaster.htm");
	} else
		response.sendRedirect("search_In_Bd_MerchanReqMaster.htm");
} else
	response.sendRedirect("search_In_Bd_MerchanReqMaster.htm");
%>

</body>
</html>