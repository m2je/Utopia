<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.form.UtopiaPageForm"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="tag" uri="Util" %>
<%
String disabled = "";
String language=WebUtil.getLanguage(session);
String path = "ir.utopia.nejat2.payroll.accDocumentMaster.action.PrPrAccDocumentMasterAction";
UtopiaPageForm pageForm= (UtopiaPageForm)session.getAttribute(Constants.PAGE_CONFIG_FORM_NAME);

%>
<script type="text/javascript">
function myFormAction(str)
{
	document.forms[0].action="";
	if(str=='accept')
		{
         document.forms[0].action="process_Pr_Bd_AccDocumentPayroll.htm";
 		}
	if(str=='reject')
	    { 
		 document.forms[0].action="search_Pr_Bd_AccDocumentPayroll.htm";
	    }
    document.forms[0].submit();
	}
	
</script>
<body class=clsInputPage>
<tag:ReportData></tag:ReportData>
<form name="submitForm" id="submitForm" action="#">
    	        <input type="hidden" value=<%=request.getParameter("editItem") %> name="prBdHiringTypeId">
                <input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Cancel_10622_fa.gif"%>);" type="button" value="<%=MessageHandler.getMessage("Reject",path, language)%>" name="B2"  onClick="javascript:myFormAction('reject');">
                &nbsp;&nbsp;
                <input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Continue_10622_fa.gif"%>);" type="button" value="<%=MessageHandler.getMessage("Confirm",path, language)%>" name="B1" onClick="javascript:myFormAction('accept');" >
            </form> 