<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@page import="ir.utopia.core.util.tags.importpage.client.ImportPage"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>

<%@taglib prefix="tag" uri="Util" %>
<tag:Header useCaseName="<%=request.getParameter(ImportPage.USECASE_PARAMETER)%>" actionName="<%=ServiceFactory.getSecurityProvider().encrypt(Constants.predefindedActions.importData.name())%>"></tag:Header>
<script type="text/javascript" language="javascript" src="<%=WebUtil.getJsFile(pageContext, "components/ir.utopia.core.util.tags.importpage.ImportPage/ir.utopia.core.util.tags.importpage.ImportPage.nocache.js") %>"></script>
<input type="hidden" name="<%=ImportPage.FORM_CLASS_PARAMETER%>" id="<%=ImportPage.FORM_CLASS_PARAMETER%>" value="<%=WebUtil.write(request.getParameter(ImportPage.FORM_CLASS_PARAMETER))%>">
<input type="hidden" name="<%=ImportPage.USECASE_PARAMETER%>" id="<%=ImportPage.USECASE_PARAMETER%>" value="<%=WebUtil.write(request.getParameter(ImportPage.USECASE_PARAMETER))%>">
<input type="hidden" name="<%=ImportPage.SETTING_INSTANCE_ID%>" id="<%=ImportPage.SETTING_INSTANCE_ID%>" value="<%=WebUtil.write(request.getParameter(ImportPage.SETTING_INSTANCE_ID))%>">
<input type="hidden" name="<%=ImportPage.IMPORT_TYPE%>" id="<%=ImportPage.IMPORT_TYPE%>" value="<%=WebUtil.write(request.getParameter(ImportPage.IMPORT_TYPE))%>">
<input type="hidden" name="<%=ImportPage.ACTION_NAME_PARAMETER%>" id="<%=ImportPage.ACTION_NAME_PARAMETER%>" value="<%=WebUtil.write(ServiceFactory.getSecurityProvider().encrypt(Constants.predefindedActions.importData.name()))%>">
<input type="hidden" name="<%=ImportPage.SELECTED_SETTING_ID%>" id="<%=ImportPage.SELECTED_SETTING_ID%>" value="<%=request.getParameter(ImportPage.SELECTED_SETTING_ID)%>">
</body>
</html>