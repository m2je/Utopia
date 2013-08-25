<!doctype html>



<%@page import="ir.utopia.core.util.tags.comp.AbstractUtopiaTag"%>
<%@page import="ir.utopia.core.form.annotation.NativeScriptType"%>
<%@page import="javax.security.auth.Subject"%>
<%@page import="java.util.Map"%>
<%@page import="ir.utopia.common.basicinformation.fiscalyear.FiscalYearInfo"%>
<%@page import="ir.utopia.core.struts.UtopiaFormMetaData"%>
<%@page import="ir.utopia.core.logic.util.LogicParser"%>
<%@page import="ir.utopia.core.struts.UtopiaFormNativeConfiguration"%>
<%@page import="ir.utopia.core.model.UsecaseUIInfo"%>
<%@page import="ir.utopia.core.UIServiceFactory"%>
<%@page import="java.util.Set"%>
<%@page import="ir.utopia.common.basicinformation.fiscalyear.FiscalYearUtil"%>
<%@page import="ir.utopia.core.ContextUtil"%>
<%@page import="ir.utopia.core.ServiceFactory"%>
<%@page import="ir.utopia.common.dashboard.bean.CmTransitionFacadeRemote"%>
<%@page import="ir.utopia.core.messagehandler.MessageHandler"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%pageContext.getResponse().setCharacterEncoding("UTF-8"); %>
<%request.setCharacterEncoding("UTF-8") ;%>
<%response.setCharacterEncoding("UTF-8") ;%>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title><%=WebUtil.write(MessageHandler.getMessage("Dashboard", "ir.utopia.core.ApplicationUI",WebUtil.getLanguage(session)))%></title>
    <script type="text/javascript" language="javascript" src="<%=WebUtil.getJsFile(pageContext, "components/Dashboard/Dashboard.nocache.js") %>"></script>
    <script type="text/javascript" SRC="<%=WebUtil.getJsFile(pageContext,"Nejat2UI.js")%>"></script>
    <link href="<%=WebUtil.getCss(pageContext, "Intcss.css") %>" rel="stylesheet" type="text/css">
	<link href="<%=WebUtil.getJsFile(pageContext, "components/ir.utopia.core.util.tags.process.ProcessHandler/gwt/standard/")+(WebUtil.isRigthToLeft(pageContext.getSession())?"standard_rtl.css":"standard.css")%>" rel="stylesheet" type="text/css">
    <link href="<%=WebUtil.getCss(pageContext,"Calendar.css")%>" rel="stylesheet" type="text/css">
    <%
    StringBuffer resources=new StringBuffer();
    
    FiscalYearInfo fiscalYear= FiscalYearUtil.getFiscalYear(session);
    if(fiscalYear!=null){
    	Long fiscalYearId=fiscalYear.getId();
	    CmTransitionFacadeRemote facade=(CmTransitionFacadeRemote)ServiceFactory.lookupFacade(CmTransitionFacadeRemote.class.getName());
	    Map<String,Object>context= ContextUtil.createContext(session);
	    Subject user= ContextUtil.getUser(context);
	    Long userId= ServiceFactory.getSecurityProvider().getUserId(user);
	    Set<Long>accessibleUsecases= facade.getDashboardAccessibleUsecases(userId, fiscalYearId, context);
	    if(accessibleUsecases!=null){
	    	
	    	for(Long usecaseId:accessibleUsecases){
	    		 UsecaseUIInfo UIInfo= UIServiceFactory.getUsecase(usecaseId);
	    		 UtopiaFormMetaData metaData=UIInfo.getMeta();
	    		 resources.append(AbstractUtopiaTag.getPageResources(pageContext, metaData, NativeScriptType.dataInputPage));
	    	}
	    }
    }
    %>
    <%=resources%>
    
  </head>
  <body>
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color:white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>
  </body>
</html>
