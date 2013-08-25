<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ir.utopia.security.authentication.AuthenticateAction,ir.utopia.core.util.WebUtil,com.opensymphony.xwork2.ActionContext,ir.utopia.core.security.SecurityProvider"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<html xmlns="http://www.w3.org/1999/xhtml" 
xmlns:wairole="http://www.w3.org/2005/01/wai-rdf/GUIRoleTaxonomy#" xmlns:waistate="http://www.w3.org/2005/07/aaa"
>
<head>
<meta content="no-cache" http-equiv="Pragma" />
<meta content="no-cache" http-equiv="Cache-Control" />
<meta content="no-store" http-equiv="Cache-Control" />
<meta content="max-age=0" http-equiv="Cache-Control" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=MessageHandler.getMessage("applicationTitle",
					"ir.utopia.nejat2.Nejat2UI", WebUtil.getLanguage(session))%></title>
<link rel="stylesheet" type="text/css" href="<%=WebUtil.getCss("css_master.css") %>" />
<script type="text/javascript">
djConfig={
    "parseWidgets": false,
    "isDebug": false,
    "debugAtAllCosts": false
};
</script>
<script type="text/javascript">
function redirectParent(){
	var container=parent;
	var newLocation='logout<%=Constants.STRUTS_EXTENSION_NAME %>';
	
	if(this!=container){
		parent.location=newLocation;
	}
}
</script>
<script type="text/javascript" src="./js/json.js"></script>
<script type="text/javascript" src="./js/prototype.js"></script>
<script type="text/javascript" src="./js/com_sun_faces_ajax.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/css_ns6up.css" />
    <link rel="stylesheet" type="text/css" href="../../css/css_ns6up.css" />
<script id="sun_script3" type="text/javascript" src="./js/adminjsf.js"></script>
<script type="text/javascript">loadInTopWindow();</script>
<style>
	input,select,option {
		font-family: tahoma;font-size: 12px;
	}
</style>
</head>
<body id="body3" cclass="LogBdy" focus="loginform.j_username" onload="redirectParent()" bgcolor="#EFEFEF">
    <br><br><br>
    <form method="post" class="form" name="loginform" action="loginAction<%=Constants.STRUTS_EXTENSION_NAME %>" >

    <table cellpadding="0" cellspacing="0" valign="middle" align="center" bgcolor="#EFEFEF" width="700" height="470">
		
            <tr>
	    <td colspan="3" style="background-image: url('images/login-backimage_blue_withtext.jpg');background-repeat:no-repeat;background-position: left middle;width:730px" align="center">
	    <!-- img id="sun_image13" src="./images/logos/tic.gif" alt="" border="0" style="float: right" /> -->
	    <br><br>
		<table  background="dot.gif" cellpadding="0" cellspacing="0" width="100%">
			<!--  
			<tr>
				<td colspan="3" align="center">
		    	<br><br>
		    	<p><font size="8" face="B Arshia" >سیستم جامع مالی  </font></p>
		    	<p><font size="4" isBold="true" >دانشگاه علوم پزشکی و خدمات بهداشتی درمانی تهران </font></p>
				<br/><br/>
		    	</td>
		    </tr>
		    -->
		    <tr height="150px"><td></td></tr>
		    <tr><td width="220">
			</td>
			<td width="300" transparent="true" valign="top" align="left">
			<%
				Object fail = ActionContext.getContext().get(
						SecurityProvider.FAIL_TO_LOGIN_MESSAGE_NAME);
				fail = fail == null ? ActionContext.getContext().get(
						SecurityProvider.FILL_MANADTRY_LOGIN_FIELDS) : fail;
				if (fail != null) {
			%>
				<font family="tahoma" color="red">
				<%=WebUtil.write(fail)%>
				
				</font>
				<%} %>
			
			    <table border="0" cellspacing="0" cellpadding="0">
				<tr><td>&nbsp;</td>
				    <td></td></tr>
				<tr><td nowrap="nowrap"><div class="logLbl">
					&nbsp;<input type="text" name="username" "id="Login.username" tabindex="1" /></div></td>
				    <td><div class="logInp"><span class="LblLev2Txt">
						<span lang="en-us"><font color="#EBDCDF">:</font></span>
					<label for="Login.username"><span lang="fa"><font color="#EBDCDF">&#1606;&#1575;&#1605; &#1705;&#1575;&#1585;&#1576;&#1585;&#1740;</font></span></label></span></div></td></tr>
				<tr><td nowrap="nowrap"><div class="logLblLst">
					&nbsp;<input type="password" name="password" id="Login.password" onKeyPress="return submitenter(event, 'loginButton', 'Login')" tabindex="2" /><span class="LblLev2Txt">  
			
			
					</span><span class="LblLev2Txt"> &nbsp;</span></div></td>
				    <td><div class="logInpLst"><span class="LblLev2Txt"> 
						<span lang="fa"><font color="#EBDCDF">:&#1705;&#1604;&#1605;&#1607; &#1593;&#1576;&#1608;&#1585;</font></span></span></div></td></tr>
						<tr><td nowrap="nowrap"><div class="logLblLst">
					&nbsp;<span class="LblLev2Txt"><select name="loginLanguage">
					<option value='fa'>&#1601;&#1575;&#1585;&#1587;&#1610;</option>
					<option value='en'>English</option>
					</select>  
			
			
					</span><span class="LblLev2Txt"> &nbsp;</span></div></td>
				    <td><div class="logInpLst"><span class="LblLev2Txt"> 
						<span lang="fa"><font color="#EBDCDF">:&#1586;&#1576;&#1575;&#1606;</font></span></span></div></td></tr>

				<tr><td>
					<span lang="fa">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>
					<img id="sun_image22" src="dot.gif" alt="" height="15" width="1" border="0" />
				    
					
					    <input type="submit" class="Btn1"
						value="&#1608;&#1585;&#1608;&#1583; &#1576;&#1607; &#1587;&#1740;&#1587;&#1578;&#1605;"
						title="Log In to Sun Java System Application Server" tabindex="3"
						onmouseover="javascript: if (this.disabled==0) this.className='Btn1Hov'"
						onmouseout="javascript: if (this.disabled==0) this.className='Btn1'"
						onblur="javascript: if (this.disabled==0) this.className='Btn1'"
						onfocus="javascript: if (this.disabled==0) this.className='Btn1Hov'"
						onclick="javascript: submitAndDisable(this, 'Login');"
						onkeypress="javascript: submitenter(event, 'loginButton', Login');
						
						name="loginButton" id="loginButton"></td>
				    <td>  
			    <table border="0">
				<tr><td><div class="logBtn">
					    &nbsp;<input type="hidden" name="loginButton.DisabledHiddenField" value="true" /></div>
				    </td></tr></table>
				</td></tr>
			</table>
		    </td>

		    <td>&nbsp;</td></tr>
	    </table>
	</td>
	</tr>
	<tr bgcolor="#EFEFEF" cclass="LogBotBnd" style="bbackground-image: url('gradlogbot.jpg');background-repeat:repeat-x;background-position: left top;">
	<td>&nbsp;</td>
	<td dir=rtl align="center" nowrap="nowrap" valign="top">
		<table>
			<tr><td valign="middle" width="20px"><img src="./images/logos/utopia-logo-grey.jpg"/></td>
			<td valign="middle" align="right">
				<font face="Tahoma" size="1" color="white"> 
				<a href="http://www.utopia.ir" target="_blank"><font face=tahoma size=1 color="black">تیم نرم افزاری اتوپیا</font></a>
				</font>
			</td></tr>
		</table>
	</td>
	<td>&nbsp;</td></tr>
	
</table>
</form>
</body>
</html>