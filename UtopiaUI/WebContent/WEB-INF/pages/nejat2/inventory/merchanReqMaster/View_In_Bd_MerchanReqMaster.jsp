<%@page import="ir.utopia.core.form.UtopiaPageForm"%>
<%@page import="ir.utopia.nejat2.inventory.merchanreqmaster.action.MerchanReqMasterForm"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants.predefindedActions"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.nejat2.inventory.merchanreqdetail.persistent.InBdMerchanReqDetail"%>
<%@page import="ir.utopia.nejat2.inventory.merchanreqmaster.bean.InBdMerchanReqMasterFacadeRemote"%>
<%@page import="ir.utopia.nejat2.inventory.merchanreqmaster.persistent.InBdMerchanReqMaster"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@page import="ir.utopia.core.ContextUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="ir.utopia.nejat2.inventory.InventoryEnums.DocStatus"%>

<html dir=<%=WebUtil.getDirection(session) %>>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8">
  <TITLE></TITLE>
  <link href="../../css/Intcss.css" rel="stylesheet" type="text/css">
</head>
<%@taglib prefix="tag" uri="Util" %>
<%
	String disabled = "";
String errorMessage = "";
String language=WebUtil.getLanguage(session);
Map<String,Object>context=ContextUtil.createContext(session);
String path = "ir.utopia.nejat2.inventory.merchanreqmaster.action.MerchanReqMasterForm";
UtopiaPageForm pageForm= (UtopiaPageForm)request.getAttribute(Constants.PAGE_CONFIG_FORM_NAME);
%>
<script type="text/javascript">
function myFormAction(str)
{
	document.forms[0].action="";
	if(str=='accept')
		{
         document.forms[0].action="confirm_In_Bd_MerchanReqMaster.htm";
 		}
	if(str=='reject')
	    { 
		 document.forms[0].action="reject_In_Bd_MerchanReqMaster.htm";
	    }
    document.forms[0].submit();
	}
	
</script>
<body class=clsInputPage>

       <tag:ReportData></tag:ReportData>
       
<%
MerchanReqMasterForm form = (MerchanReqMasterForm)pageForm.getForm();
if(form.getDocStatus()==DocStatus.confirmed)
	disabled = "disabled='disabled'";
else
	disabled = "";
InBdMerchanReqMaster merchanReqMaster;
Set<InBdMerchanReqDetail> merchanReqDetails = null;
InBdMerchanReqMasterFacadeRemote bean=(InBdMerchanReqMasterFacadeRemote)ServiceFactory.lookupFacade(InBdMerchanReqMasterFacadeRemote.class);
Long recordId=0l;
try{
recordId=Long.parseLong(request.getParameter("editItem"));
}catch(Exception e){
	e.printStackTrace();
}
merchanReqMaster = bean.loadById(recordId, context, "inBdMerchanReqDetail");
merchanReqDetails = merchanReqMaster.getInBdMerchanReqDetail();
for(InBdMerchanReqDetail merchanReqDetail:merchanReqDetails){
	if(merchanReqDetail.getCountConfirms() == null || merchanReqDetail.getCountApprovales() == null){
		disabled = "disabled='disabled'";
		errorMessage = MessageHandler.getMessage("nullCount",path,language);
	}
}
%>
   
       <div align="center">
       <br/><%=errorMessage %><br/><br/>
            <form name="submitForm" id="submitForm" action="#">
    	        <input type="hidden" value=<%=recordId %> name="reqId">
                <input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Cancel_10622_fa.gif"%>);" type="button" value="<%=MessageHandler.getMessage("Reject",path, language)%>" name="B2"  onClick="javascript:myFormAction('reject');" <%=disabled%>>
                &nbsp;&nbsp;
                <input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Continue_10622_fa.gif"%>);" type="button" value="<%=MessageHandler.getMessage("Confirm",path, language)%>" name="B1" onClick="javascript:myFormAction('accept');" <%=disabled%>>
            </form> 
       </div>

</body>
