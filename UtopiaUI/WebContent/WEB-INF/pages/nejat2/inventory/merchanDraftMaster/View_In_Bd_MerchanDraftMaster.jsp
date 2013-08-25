<%@page import="ir.utopia.core.form.UtopiaPageForm"%>
<%@page import="ir.utopia.nejat2.inventory.merchandraftmaster.action.MerchanDraftMasterForm"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants.predefindedActions"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.nejat2.inventory.InventoryEnums.MDDocStatus"%>

<html dir=<%=WebUtil.getDirection(session) %>>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8">
  <TITLE></TITLE>
  <link href="../../css/Intcss.css" rel="stylesheet" type="text/css">
</head>
<%@taglib prefix="tag" uri="Util" %>
<%
	String disabled = "";
String language=WebUtil.getLanguage(session);
String path = "ir.utopia.nejat2.inventory.merchandraftmaster.action.MerchanDraftMasterForm";
UtopiaPageForm pageForm= (UtopiaPageForm)request.getAttribute(Constants.PAGE_CONFIG_FORM_NAME);
%>
<script type="text/javascript">
function myFormAction(str)
{
	document.forms[0].action="";
	if(str=='accept')
		{
         document.forms[0].action="confirm_In_Bd_MerchanDraftMaster.htm";
 		}
	if(str=='reject')
	    { 
		 document.forms[0].action="reject_In_Bd_MerchanDraftMaster.htm";
	    }
    document.forms[0].submit();
	}
	
</script>
<body class=clsInputPage>

       <tag:ReportData></tag:ReportData>
       
<%
MerchanDraftMasterForm form = (MerchanDraftMasterForm)pageForm.getForm();
if(form.getDocStatus()==MDDocStatus.developmentmanagerconfirmed)
	disabled = "disabled='disabled'";
else
	disabled = "";
%>
   
       <div align="center">
       <br/><br/><br/>
            <form name="submitForm" id="submitForm" action="#">
    	        <input type="hidden" value=<%=request.getParameter("editItem") %> name="draftId">
                <input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Cancel_10622_fa.gif"%>);" type="button" value="<%=MessageHandler.getMessage("Reject",path, language)%>" name="B2"  onClick="javascript:myFormAction('reject');" <%=disabled%>>
                &nbsp;&nbsp;
                <input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Continue_10622_fa.gif"%>);" type="button" value="<%=MessageHandler.getMessage("Confirm",path, language)%>" name="B1" onClick="javascript:myFormAction('accept');" <%=disabled%>>
            </form> 
       </div>

</body>
