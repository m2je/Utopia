/**
 * 
 */
package ir.utopia.core.util.tags.comp;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.util.WebUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;


/**
 * @author Salarkia
 *
 */
public class ActionButtonTagManager extends AbstractUtopiaTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2672945024363775221L;
	
	private Constants.predefindedActions predefindeAction;
	 private static final int DEFAULT_BUTTON_WIDTH=106;
	private long actionId;
	private String forwardURL;
	private String imageIcon;
	private String onClick;
	private boolean redirect=false;
	private String text;
	private String jrxml;
	public enum ButtonTypes{
		submit,reset,foward,nothing,report
	}
//****************************************************************************************	
	public Constants.predefindedActions getPredefindeAction() {
		return predefindeAction;
	}
//****************************************************************************************
	public void setPredefindeAction(Constants.predefindedActions predefindeAction) {
		this.predefindeAction = predefindeAction;
	}
//****************************************************************************************
	public long getActionId() {
		return actionId;
	}
//****************************************************************************************
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}
//****************************************************************************************
	 public int doStartTag() throws JspException{
		  try{
			  pageContext.getResponse().setCharacterEncoding("UTF-8");
		      JspWriter writer =pageContext.getOut();
		      writer.write(createButton());
		  }	
		   catch(Exception ioe){
		       ioe.printStackTrace();

		     }
		return SKIP_BODY;
		}
//****************************************************************************************
	/**
	 * 
	 * @return
	 */
	 private String createButton(){
		 if(predefindeAction==null||Constants.predefindedActions.noAction.equals(predefindeAction)){
			 if(onClick!=null&&onClick.trim().length()>0){
				 return createButton(imageIcon, onClick, text, super.getLanguage(), ButtonTypes.nothing).toString();
			 }
			 if(forwardURL!=null&&forwardURL.trim().length()>0&&redirect){
				 return createButton(imageIcon, forwardURL, text, super.getLanguage(), ButtonTypes.foward).toString();
			 }
		 }
		UtopiaPageForm configForm= super.getPageForm();
		String actionUrl=configForm.getUscaseName()+Constants.STRUTS_EXTENSION_NAME;
		actionUrl=predefindeAction==null?actionUrl:predefindeAction.toString()+Constants.JSP_SEPERATOR+actionUrl;
		Long []availableActions=configForm.getAvailableActions();
		actionUrl=isRedirect()?Constants.REDIREDT_TO_PAGE_PREFIX + Constants.RIDIRECT_TO_PAGE_SEPERATOR + actionUrl : actionUrl;
		if(availableActions!=null){
			for(Long action: availableActions){
				if (predefindeAction == Constants.predefindedActions.save&&Constants.predefindedActions.save.ordinal()==action.intValue()){
							return createButton("buttons/btn_Add_10622_fa.gif", actionUrl,"add", super.getLanguage(),ButtonTypes.foward,onClick).toString();
					}
				else if (predefindeAction == Constants.predefindedActions.importData&&Constants.predefindedActions.importData.ordinal()==action.intValue()){
						return createButton("buttons/btn_Add_10622_fa.gif", actionUrl,"importData", super.getLanguage(),ButtonTypes.foward,onClick).toString();
						}
				else if (predefindeAction == Constants.predefindedActions.report&&Constants.predefindedActions.report.ordinal()==action.intValue()){
					return createButton(imageIcon==null||imageIcon.trim().length()==0?"buttons/btn_Print_10622_fa.gif":imageIcon,
							(forwardURL==null||forwardURL.trim().length()==0)?actionUrl:forwardURL,"report",
									"ir.utopia.core.constants.Glossory",super.getLanguage(),
									redirect?ButtonTypes.foward:ButtonTypes.report,onClick,DEFAULT_BUTTON_WIDTH,getJrxml()).toString();
					}
			}
		}
		return "";
	}
//****************************************************************************************
	 /**
	  * 
	  * @param imageName
	  * @param actionUrl
	  * @param text
	  * @param language
	  * @param actionType
	  * @return
	  */
	 public static StringBuffer createButton(String imageName,String actionUrl,String text,String language,ButtonTypes actionType){
		return createButton(imageName, actionUrl, text, "ir.utopia.core.constants.Glossory", language, actionType,null);
	 }
	 /**
	  * 
	  * @param imageName
	  * @param actionUrl
	  * @param text
	  * @param language
	  * @param actionType
	  * @param onClick
	  * @return
	  */
	 public static StringBuffer createButton(String imageName,String actionUrl,String text,String language,ButtonTypes actionType,String onClick){
			return createButton(imageName, actionUrl, text, "ir.utopia.core.constants.Glossory", language, actionType,onClick);
	 }

	 public static StringBuffer createButton(String imageName,String actionUrl,String text,String language,ButtonTypes actionType,int width){
			return createButton(imageName, actionUrl, text, "ir.utopia.core.constants.Glossory", language, actionType,null,width);
	 }
 //****************************************************************************************	 
	 /**
	  * 
	  * 
	  * @param imageName
	  * @param actionUrl
	  * @param text
	  * @param textBundel
	  * @param language
	  * @param actionType
	  * @return
	  */
	 public static StringBuffer createButton(String imageName,String actionUrl,String text,String textBundel,
			 String language,ButtonTypes actionType,String onClick, int width){
		return  createButton(imageName,actionUrl,text,textBundel,language,actionType,onClick,width,null);
	 }
//****************************************************************************************	 
	 /**
	  * 
	  * 
	  * @param imageName
	  * @param actionUrl
	  * @param text
	  * @param textBundel
	  * @param language
	  * @param actionType
	  * @return
	  */
	 public static StringBuffer createButton(String imageName,String actionUrl,String text,String textBundel,
			 String language,ButtonTypes actionType,String onClick, int width,String jrxml){
		 StringBuffer result=new StringBuffer("<button class=\"clsImageButton\" style=\"width: " + width +"px; height: 22px;"); 
		 result.append("background-image:url('").append(WebUtil.getImage(imageName)).append("')\"");
		 if(ButtonTypes.nothing.equals(actionType)||onClick!=null&&onClick.trim().length()!=0){
			 result.append(" onclick=\"").append(onClick).append("\"");
		 }
		 else if(ButtonTypes.foward.equals(actionType)){
			 result.append(" type=button ").append(" onClick=\"document.location.href='").append(actionUrl);
			 String localeStr="locale="+language;
			 if(result.indexOf(localeStr)<0)
				 result.append("&").append(localeStr);
			 
			 result.append("' \" ");
		 }else if(ButtonTypes.reset.equals(actionType)){
			 result.append(" type=reset");
		 }else if(ButtonTypes.submit.equals(actionType)){
			 result.append(" type=submit");
		 }else if(ButtonTypes.report.equals(actionType)){
			 result.append(" onclick=\"").append("showReportDialog('").append(jrxml==null||jrxml.trim().length()==0?"":jrxml.trim()).append("')").append("\"");
		 }
		result.append(" background-repeat:no-repeat >").
		append(WebUtil.write(MessageHandler.getMessage(text, textBundel, language))).
		append("</button>");
		 return result;
	 }
	 
//****************************************************************************************
	 public static StringBuffer createButton(String imageName,String actionUrl,String text,String textBundel,	 
			 String language,ButtonTypes actionType,String onClick){
		 return createButton(imageName,actionUrl, text,textBundel, language,actionType,onClick,DEFAULT_BUTTON_WIDTH);
	 }
//****************************************************************************************
public boolean isRedirect() {
	return redirect;
}
//****************************************************************************************
public void setRedirect(boolean redirect) {
	this.redirect = redirect;
}
public String getForwardURL() {
	return forwardURL;
}
public void setForwardURL(String forwardURL) {
	this.forwardURL = forwardURL;
}
public String getImageIcon() {
	return imageIcon;
}
public void setImageIcon(String imageIcon) {
	this.imageIcon = imageIcon;
}
public String getOnClick() {
	return onClick;
}
public void setOnClick(String onClick) {
	this.onClick = onClick;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public String getJrxml() {
	return jrxml;
}
public void setJrxml(String jrxml) {
	this.jrxml = jrxml;
} 

}
