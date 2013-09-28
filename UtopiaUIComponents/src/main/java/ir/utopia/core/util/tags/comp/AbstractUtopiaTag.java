package ir.utopia.core.util.tags.comp;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.form.annotation.NativeScriptType;
import ir.utopia.core.logic.util.LogicParser;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.struts.FormUtil;
import ir.utopia.core.struts.IncludedFormMetaData;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormNativeConfiguration;
import ir.utopia.core.struts.UtopiaFormNativeConfiguration.NativeScriptMessage;
import ir.utopia.core.util.WebUtil;

import java.util.List;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AbstractUtopiaTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6653435682089462114L;

	public AbstractUtopiaTag(){
		
	}
//**********************************************************************************	
	/**
	 * 
	 * @return
	 */
	protected UtopiaPageForm getPageForm(){
		Object configuration=null;
		  if(configuration instanceof UtopiaPageForm){
			  UtopiaPageForm configForm=(UtopiaPageForm) configuration;
		     return configForm;
		  }
		  return null;
		  }
//**********************************************************************************
	/**
	 * 
	 * @return
	 */
	protected String getLanguage(){
		return WebUtil.getLanguage(pageContext.getSession());
	}
//**********************************************************************************
	public static String getPageResources(PageContext pageContext ,UtopiaFormMetaData metaData,NativeScriptType scriptType){
		StringBuffer result=new StringBuffer();
		if(metaData.getCSSFiles()!=null){
			for(String css:metaData.getCSSFiles()){
				result.append("<link type=\"text/css\" rel=\"stylesheet\" href=\""+WebUtil.getCss(pageContext, css)+" \">").append((char)13);
			}
		}
		UtopiaFormNativeConfiguration nativeConfiguration= metaData.getNativeConfiguration(scriptType);
		if(nativeConfiguration!=null&&nativeConfiguration.getNativeScripts()!=null&&nativeConfiguration.getNativeScripts().trim().length()>0){
			String nativeScript=nativeConfiguration.getNativeScripts();
			nativeScript=LogicParser.getFlatExpression(nativeScript, ContextUtil.createContext(pageContext.getSession()));
			StringBuffer messages=new StringBuffer();
			List<NativeScriptMessage>messageList= nativeConfiguration.getNativeMessages();
			if(messageList!=null){
				String language= WebUtil.getLanguage( pageContext.getSession());
				for(NativeScriptMessage msg:messageList){
					if(msg.getMessage()!=null&&msg.getMessage().trim().length()>0){
						String message= MessageHandler.getMessage(msg.getMessage(), msg.getBundle(), language);
						message=LogicParser.getFlatExpression(message, ContextUtil.createContext(pageContext.getSession()));
						messages.append(" var ").append(msg.getMessageName()).append("='").append(message).append("';").append((char)13);
					}
				}
			}
			result.append((char)13).append("<script type=\"text/javascript\" language=\"javascript\"  >").append((char)13).append(messages)
			.append((char)13).append(nativeScript).append((char)13).append("</script>");
		}
		
		IncludedFormMetaData []includedForms =metaData.getIncludedForms();
		if(includedForms!=null){
			for(IncludedFormMetaData includedMeta:includedForms){
				UtopiaFormMetaData includedMetaData= FormUtil.getMetaData(includedMeta.getFormClass());
				UtopiaFormNativeConfiguration includedMetaDataNativeConfiguration= includedMetaData.getNativeConfiguration(scriptType);
				if(includedMetaDataNativeConfiguration!=null&&includedMetaDataNativeConfiguration.getNativeScripts()!=null&&includedMetaDataNativeConfiguration.getNativeScripts().trim().length()>0){
					String nativeScript=includedMetaDataNativeConfiguration.getNativeScripts();
					nativeScript=LogicParser.getFlatExpression(nativeScript, ContextUtil.createContext(pageContext.getSession()));
					result.append((char)13).append("<script type=\"text/javascript\" language=\"javascript\"  >").append(nativeScript).append("</script>");
				}
			}
		}
		return result.toString();
	} 
//**********************************************************************************
	/**
	 * 
	 */
	protected String getDirection(){
		return WebUtil.getDirection(pageContext.getSession());
	}
	
	protected String getAlign(){
		return WebUtil.getAlign(pageContext.getSession());
	}

	protected String getAlignMirror(){
		return WebUtil.getAlign(pageContext.getSession()) == "left"? "right" : "left";
	}

}
