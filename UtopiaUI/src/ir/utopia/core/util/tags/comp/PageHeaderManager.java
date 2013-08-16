package ir.utopia.core.util.tags.comp;


import ir.utopia.core.ServiceFactory;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class PageHeaderManager extends AbstractUtopiaTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -416057224535148409L;
	
	String actionName;
	String useCaseName;
	
	public int doStartTag() throws JspException{
		  try{
			  	 pageContext.getResponse().setCharacterEncoding("UTF-8");
				 String usecaseName=extractUsecaseName() ;
				 HttpSession session=((HttpServletRequest)pageContext.getRequest()).getSession();
				 String bundelPath=ServiceFactory.getSubsystemConfiguration(UsecaseUtil.getUsecaseWithName(usecaseName).getSubSystemId()).getUsecaseBundelName();
				 if(bundelPath==null&&super.getPageForm()!=null){
					 bundelPath= super.getPageForm().getFormMetaData().getClazz().getName();
				 }
				 String resultHeader= MessageHandler.getMessage(usecaseName,bundelPath, getLanguage());
				 String resultActionName=MessageHandler.getMessage(extractActionName(),"ir.utopia.core.basicinformation.action.action" , WebUtil.getLanguage(session));
				 resultHeader= WebUtil.isRigthToLeft(session)?(resultActionName+" "+resultHeader):(resultHeader+" "+resultActionName);
			     writeHeader(resultHeader);
		  }
		   catch(Exception ioe){
		       ioe.printStackTrace();

		     }
		return SKIP_BODY;
		}
//*************************************************************************************************************
	@SuppressWarnings("unchecked")
	private void writeHeader( String header)
			throws IOException {
		String REQUIRED_CSS= "<link href=\""+WebUtil.getCss("Intcss.css")+"\" rel=\"stylesheet\" type=\"text/css\">"+(char)13+
				"<link href=\""+WebUtil.getCss("Calendar.css")+"\" rel=\"stylesheet\" type=\"text/css\">"+(char)13+
				"<link href=\""+WebUtil.getJsFile(pageContext, "components/ir.utopia.core.util.tags.process.ProcessHandler/gwt/standard/")+
				(WebUtil.isRigthToLeft(pageContext.getSession())?"standard_rtl.css":"standard.css")+
				"\" rel=\"stylesheet\" type=\"text/css\">";
		JspWriter writer =pageContext.getOut();
		 writer.write(REQUIRED_CSS);
		 writer.write("<p class=\"clsPageHeader\">"+header+"</p>");
		 writer.write((char)13);
		 writer.write("<meta name=\"gwt:property\" content=\"locale="+WebUtil.getLocale(pageContext.getSession())+ "\">");
		 writer.write((char)13);
		 UtopiaPageForm form=getPageForm();
		 List<MessageNamePair>messages=(List<MessageNamePair>) pageContext.getSession().getAttribute(UtopiaBasicAction.ACTION_MESSAGES_SESSION_KEY);
		 if(form!=null&&messages!=null&&messages.size()>0){
		     writer.write(getMessages(messages,WebUtil.getDirection(pageContext.getSession()),getLanguage()));
		     pageContext.getSession().removeAttribute(UtopiaBasicAction.ACTION_MESSAGES_SESSION_KEY);
		 }
	}
//*************************************************************************************************************	
	public static String getMessages(List<MessageNamePair> pairs,String direction,String language){
		int heigth=pairs!=null?pairs.size()*25:0;
		heigth=heigth>100?100:heigth;
		StringBuffer result=new StringBuffer("<div style=\"overflow: auto; position: relative; width: 100%; height: ").append(heigth)
		.append("px;\">");
			result.append("<table width=100% ").append("dir=").append(direction).append(">");
			HashSet<String>messages=new HashSet<String>();
			for(MessageNamePair pair:pairs){
				String newMessages=pair.getMessage();
				newMessages=newMessages==null?null:newMessages.trim();
				if(newMessages==null||newMessages.trim().length()==0)continue;
				if(messages.contains(newMessages))continue;
				messages.add(newMessages);
				result.append("<tr>").append("<td> <img src=").append(WebUtil.getImage(pair.getType().toString()+".png")).
				append("></td>").
				append("<td width=10% class=\"").append(getClassPair(pair)).append("\">").
				append(MessageHandler.getMessage(pair.getType().toString(), "ir.utopia.core.constants.Glossory", language)).
				append("</td>");
				result.append("<td width=90% class=\"clsLabel\">").append(newMessages).append("</td>").append("</tr>");
								
			}
			result.append("</table></div>");
		return result.toString();
	}
//*************************************************************************************************************	
	private static String getClassPair(MessageNamePair pair){
		if(pair.getExceptionType().equals(MessageType.ERROR)){
			return "clsErrorMessage";
		}
		if(pair.getExceptionType().equals(MessageType.WARNING)){
			return "clsWarningMessage";
		}
			return "clsInfoMessage";
	}
//*************************************************************************************************************	
	public String getActionName() {
		return actionName;
	}
//*************************************************************************************************************	
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
//*************************************************************************************************************	
	public String getUseCaseName() {
		return useCaseName;
	}
//*************************************************************************************************************	
	public void setUseCaseName(String useCaseName) {
		this.useCaseName = useCaseName;
	}
//*************************************************************************************************************	
	public String extractUsecaseName(){
		String result;
		if(useCaseName!=null&&useCaseName.trim().length()>0){
			result=ServiceFactory.getSecurityProvider().decrypt(useCaseName);
		}else if(getPageForm()!=null){
			result=getPageForm().getUscaseName();
		}else{
			result=ServiceFactory.getSecurityProvider().decrypt(pageContext.getRequest().getParameter(UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME));
		}
		return result;
	}
//*************************************************************************************************************
	public String extractActionName(){
		String result;
		if(actionName!=null&&actionName.trim().length()>0){
				result=ServiceFactory.getSecurityProvider().decrypt(actionName);
		}else if(getPageForm()!=null){
			result=getPageForm().getActionName();
		}else{
			result=ServiceFactory.getSecurityProvider().decrypt(pageContext.getRequest().getParameter(UIComponentsConstants.USE_ACTION_NAME_PARAMETER_NAME));
		}
		return result;
	}
//*************************************************************************************************************
}
