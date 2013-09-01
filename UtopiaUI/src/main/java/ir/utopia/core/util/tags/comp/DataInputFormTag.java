package ir.utopia.core.util.tags.comp;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.form.annotation.NativeScriptType;
import ir.utopia.core.security.SaveTokenController;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.struts.UtopiaForm;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.util.WebUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
public class DataInputFormTag  extends AbstractUtopiaTag{
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(DataInputFormTag.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 674171435764524523L;
		 
	private static String DATA_INPUT_FORM_SCRIPT;
//*************************************************************************
	public int doStartTag()throws JspException{
		  try {
            DATA_INPUT_FORM_SCRIPT="<script type=\"text/javascript\" language=\"javascript\" src=\""+WebUtil.getJsFile(pageContext,"components/ir.utopia.core.util.tags.datainputform.DataInputForm/ir.utopia.core.util.tags.datainputform.DataInputForm.nocache.js")+" \"></script>";
			pageContext.getResponse().setCharacterEncoding("UTF-8");
			JspWriter writer =pageContext.getOut();
//			writer.write(createFormOld());
			writer.write(createFormNew());
			writer.write((char)13);
		    writer.write("<meta name=\"gwt:property\" content=\"locale="+WebUtil.getLocale(pageContext.getSession())+ "\">");
		} catch (IOException e) {
			logger.log(Level.ALL,"error",  e);
		}
		return SKIP_BODY;
	}
//*************************************************************************
	private String createFormNew(){
		UtopiaPageForm configForm=super.getPageForm();
		long windowNo=configForm.getWindowNumber();
		UtopiaFormMetaData metaData= configForm.getFormMetaData();
		StringBuffer result=new StringBuffer(DATA_INPUT_FORM_SCRIPT).append((char)13);
		result.append("<link type=\"text/css\" rel=\"stylesheet\" href=\""+WebUtil.getCss(pageContext, "gwt/standard/standard"+(WebUtil.isRigthToLeft(pageContext.getSession())? "":"_rtl")+".css")+" \">").append((char)13);
		
		result.append(getPageResources(pageContext, metaData, NativeScriptType.dataInputPage));
		long currentRecordId=0;
		try {
			currentRecordId=Long.parseLong(pageContext.getRequest().getParameter("editItem"));
		} catch (Exception e) {
		}
		String pageSaveToken;
		boolean isSuccessPage=true;
		if(pageContext.getRequest().getAttribute(UtopiaBasicAction.LAST_UNSUCCESSFULL_TOKEN_KEY)!=null){
			pageSaveToken=(String)pageContext.getRequest().getAttribute(UtopiaBasicAction.LAST_UNSUCCESSFULL_TOKEN_KEY);
			UtopiaForm<?>form=(UtopiaForm<?>) pageContext.getSession().getAttribute(pageSaveToken);
			if(form!=null){
				currentRecordId=form.getRecordId();
			}
			isSuccessPage=false;
		}else{
			pageSaveToken=SaveTokenController.getToken(pageContext.getSession(),configForm.getUsecaseActionId());
		}
		
		result.append((char)13).append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME).append("\" id=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(configForm.getUscaseName())).append("'/>").append((char)13).
		append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.USE_ACTION_NAME_PARAMETER_NAME).append("\" id=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.USE_ACTION_NAME_PARAMETER_NAME).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(configForm.getActionName())+"' />").append((char)13).
		append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME).append("\" id=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME).append("\" value='").append(
				ServiceFactory.getSecurityProvider().encrypt(metaData.getClazz().getName())+"' />").append((char)13).
		append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.RECORDID_PARAMETER_NAME).append("\" id=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.RECORDID_PARAMETER_NAME).append("\" value='"+String.valueOf(currentRecordId)+"' />").append((char)13).
		append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.WINDOW_NO_PARAMETER_NAME).append("\" id=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.WINDOW_NO_PARAMETER_NAME).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(String.valueOf(windowNo))+"' />").append((char)13).
		append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.SUCCESS_PARAMETER_NAME).append("\" id=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.SUCCESS_PARAMETER_NAME).append("\" value='"+isSuccessPage+"' />").append((char)13).
		append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.SAVE_TOKEN_PARAMETER_NAME).append("\" id=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.SAVE_TOKEN_PARAMETER_NAME).append("\" value='"+pageSaveToken+"' />").append((char)13);
		return result.toString();
	}
//*************************************************************************

}
