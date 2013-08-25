<%@page import="ir.utopia.nejat2.credit.financereqmaster.action.FinanceReqBuyReqForm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="tag" uri="Util" %>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.nejat2.credit.financereqmaster.bean.CdPrFinanceReqMasterFacadeRemote"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@page import="ir.utopia.core.ContextUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>
<%@page import="ir.utopia.core.util.DateUtil"%>
<%@page import="ir.utopia.nejat2.credit.financereqmaster.persistent.CdPrFinanceReqMaster"%>
<%@page import="ir.utopia.nejat2.inventory.buyreqmaster.bean.InBdBuyReqMasterFacadeRemote"%>
<%@page import="ir.utopia.nejat2.inventory.buyreqmaster.persistent.InBdBuyReqMaster"%>
<%@page import="ir.utopia.nejat2.inventory.buyreqdetail.persistent.InBdBuyReqDetail"%>
<%@page import="ir.utopia.nejat2.credit.creditconfig.bean.CdBdActivityDimensionFacadeRemote"%>
<%@page import="ir.utopia.nejat2.credit.creditconfig.persistent.CdBdActivityDimension"%>
<%@page import="ir.utopia.nejat2.inventory.InventoryEnums.BRDocStatus"%>
<%@page import="ir.utopia.nejat2.budget.depfiscalyear.persistent.BgBdDepFiscalyear" %>
<%@page import="ir.utopia.nejat2.budget.agreementsubdtl.persistent.BgPrAgreementSubdtl" %>
<!--<tag:ReportData></tag:ReportData>-->
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
  <TITLE></TITLE>
  <link href="<%=WebUtil.getCss("Intcss.css") %>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=WebUtil.getJsFile(pageContext,"components/js/adapter/ext/ext-base.js")%>"></script>
<script type="text/javascript" src="<%=WebUtil.getJsFile(pageContext,"components/js/ext-all.js")%>" ></script>
<script type="text/javascript" src="<%=WebUtil.getJsFile(pageContext,"components/js/ext-all-debug.js")%>" ></script>
<script type="text/javascript" src="<%=WebUtil.getJsFile(pageContext,"components/js/ext-core.js")%>" ></script>
<script type="text/javascript" src="<%=WebUtil.getJsFile(pageContext,"components/js/ext-core-debug.js")%>" ></script>
<%
String jsfilePath=WebUtil.getJsFile(pageContext,"components/js/ext-all.js");
%>
<script>
function calculateRemain(){
	var bgPrAgreementSubdtlValue = document.getElementById("bgPrAgreementSubdtl").value;
	Ext.Ajax.request({
	   url: 'loadFromActivity_Cd_Pr_FinanceReqMaster.json',
	   success: function(response, opts) {
	     var jsonObj = Ext.decode(response.responseText);
	     document.getElementById("remain").value = jsonObj.remain;
	   },
	   failure: function(response, opts) {
	      console.log('server-side failure with status code ' + response.status);
	   },
	   params:{'agreementSubdtlId':bgPrAgreementSubdtlValue}
	});
	
}
</script>
<script type="text/javascript">
function myFormAction(str)
{
	document.forms[0].action="";
	if(str=='accept')
		{
         document.forms[0].action="confirm_Cd_Pr_FinanceReqMaster.htm";
 		}
	if(str=='cancel')
	    { 
		 document.forms[0].action="search_Cd_Pr_FinanceReqMaster.htm";
	    }
    document.forms[0].submit();
}
	
</script>
<%
String lang = WebUtil.getLanguage(session);
Map<String,Object>context= ContextUtil.createContext(session);
String path = "ir.utopia.nejat2.credit.financereqmaster.action.FinanceReqMasterForm";
String locale = ContextUtil.getLoginLanguage(context);
Locale l=new Locale(locale);
FinanceReqBuyReqForm form =(FinanceReqBuyReqForm)request.getAttribute("reqForm");
System.out.println("test   "+ form.getInventoryName());
int row = 0;
/* String invnetoryType = null;
String inventoryName = null;
String orgName = null;
String applicantOrg = null;

Long recordId=Long.parseLong(request.getParameter("editItem"));
CdPrFinanceReqMasterFacadeRemote bean = (CdPrFinanceReqMasterFacadeRemote)ServiceFactory.lookupFacade(CdPrFinanceReqMasterFacadeRemote.class.getName());
CdPrFinanceReqMaster cdPrFinanceReqMaster = bean.loadById(recordId, context);
InBdBuyReqMasterFacadeRemote reqBean = (InBdBuyReqMasterFacadeRemote)ServiceFactory.lookupFacade(InBdBuyReqMasterFacadeRemote.class.getName());
InBdBuyReqMaster inBdBuyReqMaster = reqBean.loadById(cdPrFinanceReqMaster.getRecordId(), context, "inBdBuyReqDetails");
Set<InBdBuyReqDetail> inBdBuyReqDetails = inBdBuyReqMaster.getInBdBuyReqDetails();
if(inBdBuyReqMaster.getInBdInventory()!=null){
	inventoryName = inBdBuyReqMaster.getInBdInventory().getCode() + " - " + inBdBuyReqMaster.getInBdInventory().getName();
	if(inBdBuyReqMaster.getInBdInventory().getInBdInventoryType()!=null){
		invnetoryType = inBdBuyReqMaster.getInBdInventory().getInBdInventoryType().getInventoryTypeName();
	}
}
if(inBdBuyReqMaster.getCmOrganization()!=null){
	orgName = inBdBuyReqMaster.getCmOrganization().getName();
}
if(inBdBuyReqDetails.size()>0){
	  for(InBdBuyReqDetail inBdBuyReqDetail:inBdBuyReqDetails){
		  if(inBdBuyReqDetail.getInBdMerchanReqMaster()!=null){
			  if(inBdBuyReqDetail.getInBdMerchanReqMaster().getCmOrganization()!=null){
				  applicantOrg = applicantOrg + inBdBuyReqDetail.getInBdMerchanReqMaster().getCmOrganization().getName() + ", ";
			  }
		  }
	  }
} */

%>
</head>
<body class=clsInputPage >
<%@taglib prefix="tag" uri="Util" %>
 <tag:Header></tag:Header> 

<form id="FinanceReqBuyReqForm" name="FinanceReqBuyReqForm" method="post" action="FinanceReqMasterAction">
 
<table width="800" align="center">
   <tr>
    <td height="49" colspan="4" align="center" class="clsHeaderCell"><%=MessageHandler.getMessage("title",path,lang)%></td>
  </tr>
  <tr>
    <td colspan="2" align="left" class="clsLabel"><%=MessageHandler.getMessage("inventoryType",path,lang)%>:</td>
    <td colspan="2" class="clsLabel"><%=form.getInvnetoryType() %></td>
  </tr>
  <tr>
    <td height="63" colspan="4" align="center" class="clsHeaderCell"><%=MessageHandler.getMessage("buyRequest",path,lang)%></td>
  </tr>
  <tr>
    <td height="30" colspan="2" align="right" class="clsLabel"><%=MessageHandler.getMessage("orgName",path,lang)%>: <%=form.getOrgName() %></td>
    <td width="25%" height="30" align="left" class="clsLabel"><%=MessageHandler.getMessage("code",path,lang)%>:</td>
    <td width="25%" height="30" class="clsLabel"><%=form.getCode() %></td>
  </tr>
  <tr>
    <td height="30" colspan="2" align="right" class="clsLabel"><%=MessageHandler.getMessage("inventoryName",path,lang)%>: <%=form.getInventoryName() %></td>
    <td height="30" align="left" class="clsLabel"><%=MessageHandler.getMessage("requestDate",path,lang)%>:</td>
    <td height="30" class="clsLabel"><%=form.getRequestDate() %></td>
  </tr>
  <tr>
    <td height="30" colspan="4" align="right" class="clsLabel"><%=MessageHandler.getMessage("applicantOrg",path,lang)%>: <%=form.getApplicantOrg() %></td>
  </tr>
  <tr>
    <td height="30" colspan="4" align="right" class="clsLabel"><%=MessageHandler.getMessage("description",path,lang)%>: <%=form.getDescription() %></td>
  </tr>
  <tr>
    <td colspan="4"><table width="100%" border="1" cellspacing="0">
      <tr class="normal">
        <td width="5%" align="center" bgcolor="#999999" class="clsTableHeader"><%=MessageHandler.getMessage("row",path,lang)%></td>
        <td width="16%" align="center" bgcolor="#999999" class="clsTableHeader"><%=MessageHandler.getMessage("merchandiseCode",path,lang)%></td>
        <td width="24%" align="center" bgcolor="#999999" class="clsTableHeader"><%=MessageHandler.getMessage("merchandiseName",path,lang)%></td>
        <td width="10%" align="center" bgcolor="#999999" class="clsTableHeader"><%=MessageHandler.getMessage("unit",path,lang)%></td>
        <td width="8%" align="center" bgcolor="#999999" class="clsTableHeader"><%=MessageHandler.getMessage("count",path,lang)%></td>
        <td width="14%" align="center" bgcolor="#999999" class="clsTableHeader"><%=MessageHandler.getMessage("priceEstimate",path,lang)%></td>
        <td width="23%" height="27" align="center" bgcolor="#999999" class="clsTableHeader"><%=MessageHandler.getMessage("description",path,lang)%></td>
      </tr>
      <%if(form.getInBdBuyReqDetails().size()>0){
    	  for(InBdBuyReqDetail inBdBuyReqDetail:form.getInBdBuyReqDetails()){%>
    		  <tr>
                  <td height="27" class="clsLabel"><%row=row+1;%>
                  <%=WebUtil.write(row,locale) %></td>
                  <td class="clsLabel"><%=WebUtil.write(inBdBuyReqDetail.getInBdMerchandise().getCode(),locale) %></td>
                  <td class="clsLabel"><%=WebUtil.write(inBdBuyReqDetail.getInBdMerchandise().getName(),locale) %></td>
                  <td class="clsLabel"><%if(inBdBuyReqDetail.getInBdMerchandise().getInBdUnit()!=null){ %>
                  <%=WebUtil.write(inBdBuyReqDetail.getInBdMerchandise().getInBdUnit().getName(),locale) %>
                  <%} %></td>
                  <td class="clsLabel"><%=WebUtil.write(inBdBuyReqDetail.getCount(),locale) %></td>
                  <td class="clsLabel"><%=WebUtil.write(inBdBuyReqDetail.getEstimatePrice(),locale) %></td>
                  <td class="clsLabel"><%=WebUtil.write(inBdBuyReqDetail.getDescription(),locale) %></td>
              </tr>
      <%  }
      }
      %>
    </table></td>
  </tr>
  <tr>
    <td colspan="4"><hr>
    </td>
    
  </tr>
  <tr>
    <td width="25%"><%=MessageHandler.getMessage("value",path,lang)%></td>
    <td width="25%"><input type="text" name="value" id="value" tabindex="1" value="<%=WebUtil.write(form.getValue(),locale)%>" class="clsText" readonly="readonly"/></td>
    <td width="25%"><%=MessageHandler.getMessage("remain",path,lang)%>:</td>
    <td width="25%"><input type="text" name="remain" id="remain" tabindex="3"  class="clsText" readonly="readonly"/></td>
  </tr>
  <tr>
    <td width="25%"><%=MessageHandler.getMessage("bgBdActivity",path,lang)%>:</td>
    <td width="25%" colspan="3">
  <select name="bgPrAgreementSubdtl" id="bgPrAgreementSubdtl" tabindex="2" class="clsSelect" onchange="calculateRemain()">
           <option value=""></option>
    <%
	
    if(form.getBgPrAgreementSubdtls().size()>0){
    	for(BgPrAgreementSubdtl bgPrAgreementSubdtl:form.getBgPrAgreementSubdtls()){
    		%>
 	       <option value=<%=bgPrAgreementSubdtl.getBgPrAgreementSubdtlId()%>>
 	       <%=bgPrAgreementSubdtl.getBgPrAgreementDetail().getBgPrAgreementMaster().getBgBdDepFiscalyear().getName()%> - 
 	       <%=bgPrAgreementSubdtl.getBgPrAgreementDetail().getBgBdPlan().getName()%> -
 	       <%=bgPrAgreementSubdtl.getBgPrAgreementDetail().getBgBdCreditSource().getName()%> -
 	       <%=bgPrAgreementSubdtl.getBgBdSection().getName()%> -
 	       <%=bgPrAgreementSubdtl.getBgBdActivity().getName()%>
 	       </option>
 	       <%
    	}
    }        	
    %>
  </select>
  </td>
    
  </tr>
  
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="4" align="center">
         <input type="hidden" value=<%=form.getCdPrFinanceReqMasterId() %> name="cdPrFinanceReqMasterId">
         <%
         String disabled = "";
         InBdBuyReqMasterFacadeRemote buyReqBean = (InBdBuyReqMasterFacadeRemote)ServiceFactory.lookupFacade(InBdBuyReqMasterFacadeRemote.class.getName());
 		 InBdBuyReqMaster inBdBuyReqMaster = buyReqBean.loadById(form.getUsecaseRecordId(), context);
 		 if(inBdBuyReqMaster.getDocStatus()==BRDocStatus.financed)
 	  		disabled = "disabled='disabled'";
 		 else
 			disabled = "";
         %>
         <input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Continue_10622_fa.gif"%>);" type="button" value="<%=MessageHandler.getMessage("Confirm",path, lang)%>" name="B1" onClick="javascript:myFormAction('accept');" <%=disabled%>>
         &nbsp;&nbsp;
         <input style="border:0px;width: 106px; height: 22px; font-family: Tahoma; font-size:12px; background-image: url(<%=WebUtil.getRootAddress(pageContext)+"/images/buttons/btn_Cancel_10622_fa.gif"%>);" type="button" value="<%=MessageHandler.getMessage("Cancel",path, lang)%>" name="B2"  onClick="javascript:myFormAction('cancel');">
    </td>
  </tr>
</table> 

</form>
</body>
</html>
