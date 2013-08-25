<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ir.utopia.core.form.UtopiaPageForm"%>
<%@ page import="ir.utopia.nejat2.asset.inventorydraft.action.InventoryDraftForm"%>
<%@ page import="ir.utopia.core.constants.Constants"%>
<%@ page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@ page import="ir.utopia.core.util.WebUtil"%>

<html dir=<%=WebUtil.getDirection(session)%> >
	<head>
		<meta http-equiv="content-type" content="text/html;charset=UTF-8">
		<title></title>
		<link href="../../css/Intcss.css" rel="stylesheet" type="text/css">
	</head>
	
	<%@taglib prefix="tag" uri="Util" %>
	<%
		String aminDisabled = "";
			String managerDisabled = "";
			String language = WebUtil.getLanguage(session);
			String path = "ir.utopia.nejat2.asset.inventorydraft.action.InventoryDraftForm";
			UtopiaPageForm pageForm = (UtopiaPageForm)request.getAttribute(Constants.PAGE_CONFIG_FORM_NAME);
	%>
	<script type="text/javascript">
		function myFormAction(str){
			switch (str)
			{
				case 'AminConfirm' 	  : document.forms[0].action="AminConfirm_As_Pr_InventoryDraft.htm";
									 	break;
				case 'AminReject'  	  : document.forms[0].action="AminReject_As_Pr_InventoryDraft.htm";
									 	break;
				case 'ManagerConfirm' : document.forms[0].action="ManagerConfirm_As_Pr_InventoryDraft.htm";
										break;
				case 'ManagerReject'  : document.forms[0].action="ManagerReject_As_Pr_InventoryDraft.htm";
										break;
				default : document.forms[0].action="";
			}
			document.forms[0].submit();
		}
	</script>
	<body class="clsInputPage">
		<tag:ReportData></tag:ReportData>
		<%
			InventoryDraftForm form = (InventoryDraftForm)pageForm.getForm();
			/* if (form.getAssetStatus()==Constants.AssetStatus.aminConfirm) 
				aminDisabled = "disabled='disabled'";
			else  */
				aminDisabled = "";
			/* if (form.getAssetStatus()==Constants.AssetStatus.managerConfirm)
				managerDisabled = "disabled='disabled'";
			else */
				managerDisabled = "";
		%>
		<div align="center">
		<br/><br/><br/>
			<form name="submitForm" id="submitForm" action="#">
				<input type="hidden" value=<%=request.getParameter("editItem")%> name="draftDetailId">
				<input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Continue_10622_fa.gif"%>);" 
						type="button" value="<%=MessageHandler.getMessage("AminConfirm", path, language)%>" name="AminConfirm" onclick="javascript:myFormAction('AminConfirm');" <%=aminDisabled%>>
				&nbsp;&nbsp;
				<input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Cancel_10622_fa.gif"%>);" 
						type="button" value="<%=MessageHandler.getMessage("AminReject", path, language)%>" name="AminReject" onclick="javascript:myFormAction('AminReject');" <%=aminDisabled%>>
				&nbsp;&nbsp;
				<input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Continue_10622_fa.gif"%>);" 
						type="button" value="<%=MessageHandler.getMessage("ManagerConfirm", path, language)%>" name="ManagerConfirm" onclick="javascript:myFormAction('ManagerConfirm');" <%=managerDisabled%>>
				&nbsp;&nbsp;
				<input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Cancel_10622_fa.gif"%>);" 
						type="button" value="<%=MessageHandler.getMessage("ManagerReject", path, language)%>" name="ManagerReject" onclick="javascript:myFormAction('ManagerReject');" <%=managerDisabled%>>
			</form>
		</div>
	</body>
</html>