<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.form.UtopiaPageForm"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@ page import="ir.utopia.core.constants.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
<TITLE></TITLE>
<link href="<%=WebUtil.getCss("Intcss.css")%>" rel="stylesheet"
	type="text/css">
</head>
<script type="text/javascript">
		function myFormAction(str){
			switch (str)
			{
				case 'Next' 	  	: document.forms[0].action="assetAction.htm";
									 	break;
				default : document.forms[0].action="";
			}
			document.forms[0].submit();
		}
	</script>
<body class=clsInputPage>
	<%@taglib prefix="tag" uri="Util"%>
	<%String language = WebUtil.getLanguage(session);
			String path = "ir.utopia.nejat2.asset.inventoryreceipt.action.InventoryReceiptForm";
			UtopiaPageForm pageForm = (UtopiaPageForm) request
					.getAttribute(Constants.PAGE_CONFIG_FORM_NAME);%>

	<tag:Header></tag:Header>
	<tag:InputForm></tag:InputForm>
	<%
				String disabled = "";
		%>
	<!-- 	
	<div align="center">
		<br/><br/><br/>
			<form name="submitForm" id="submitForm" action="#">
				<input type="hidden" value="1" name="transactionId1">
				<input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Continue_10622_fa.gif"%>);" 
						type="button" value="<%=MessageHandler.getMessage("Next", path, language)%>" name="AminConfirm" onclick="javascript:myFormAction('Next');" <%=disabled%>>
				&nbsp;&nbsp;
				
			</form>
		</div>
		
	 -->	
</body>
</html>