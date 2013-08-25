<%@page import="ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.util.tags.searchpage.client.SearchPage"%>
<%@page import="ir.utopia.core.util.tags.importpage.client.ImportPage"%>
<%@page import="ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<%@taglib prefix="tag" uri="Util" %>
<tag:Header useCaseName="<%=request.getParameter(SearchPage.USECASE_PARAMETER)%>" actionName="<%=ServiceFactory.getSecurityProvider().encrypt(request.getParameter("actionName"))%>"></tag:Header>
<script type="text/javascript" language="javascript" src="<%=WebUtil.getJsFile(pageContext, "components/ir.utopia.core.util.tags.importpage.ImExDefinitionPage/ir.utopia.core.util.tags.importpage.ImExDefinitionPage.nocache.js") %>"></script>
<input type="hidden" name="<%=ImExDefinitionPage.FORM_CLASS_PARAMETER%>" id="<%=ImExDefinitionPage.FORM_CLASS_PARAMETER%>" value="<%=request.getParameter(SearchPage.FORM_CLASS_PARAMETER)%>">
<input type="hidden" name="<%=ImExDefinitionPage.USECASE_PARAMETER%>" id="<%=ImExDefinitionPage.USECASE_PARAMETER%>" value="<%=request.getParameter(ImExDefinitionPage.USECASE_PARAMETER)%>">
<input type="hidden" name="<%=ImExDefinitionPage.ACTION_NAME %>" id="<%=ImExDefinitionPage.ACTION_NAME%>" value="<%=ServiceFactory.getSecurityProvider().encrypt(request.getParameter(UIComponentsConstants.USE_ACTION_NAME_PARAMETER_NAME))%>">
<input type="hidden" name="<%=ImExDefinitionPage.PREVIOUS_PAGE_PARAMETER_NAME%>" id="<%=ImExDefinitionPage.PREVIOUS_PAGE_PARAMETER_NAME%>" value="<%=request.getHeader("referer")%>">
<input type="hidden" name="<%=ImExDefinitionPage.SETTING_ID%>" id="<%=ImExDefinitionPage.SETTING_ID%>" value="<%=request.getParameter(ImExDefinitionPage.SETTING_ID)%>">
<input type="hidden" name="<%=ImExDefinitionPage.AFTER_SAVE_URL%>" id="<%=ImExDefinitionPage.AFTER_SAVE_URL%>" value="<%=request.getParameter("forwardURL")%>">
<input type="hidden" name="<%=ImExDefinitionPage.IMPORT_TYPE%>" id="<%=ImExDefinitionPage.IMPORT_TYPE%>" value="<%=request.getParameter("importType")%>">
<input type="hidden" name="<%=ImExDefinitionPage.SETTING_INSTANCE_ID%>" id="<%=ImExDefinitionPage.SETTING_INSTANCE_ID%>" value="<%=WebUtil.write(request.getParameter(ImExDefinitionPage.SETTING_INSTANCE_ID))%>">
</body>
</html>