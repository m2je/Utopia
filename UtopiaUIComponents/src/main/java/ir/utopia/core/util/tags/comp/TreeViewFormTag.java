package ir.utopia.core.util.tags.comp;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.form.annotation.NativeScriptType;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.util.WebUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class TreeViewFormTag  extends AbstractUtopiaTag{
		private static final Logger logger;
		
		static {
			logger = Logger.getLogger(TreeViewFormTag.class.getName());
		}
		/**
		 * 
		 */
		private static final long serialVersionUID = 674171435764524523L;
		private String rootNodeText;	 
		private static String DATA_INPUT_FORM_SCRIPT;
	//*************************************************************************
		public int doStartTag()throws JspException{
			  try {
				 DATA_INPUT_FORM_SCRIPT="<script type=\"text/javascript\" language=\"javascript\" src=\""+WebUtil.getJsFile(pageContext,"components/ir.utopia.core.util.tags.treeview.Treeview/ir.utopia.core.util.tags.treeview.Treeview.nocache.js")+" \"></script>";
				pageContext.getResponse().setCharacterEncoding("UTF-8");
				JspWriter writer =pageContext.getOut();
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
			String usecaseName= getPageForm().getUscaseName();
			HttpSession session=((HttpServletRequest)pageContext.getRequest()).getSession();
			String bundelPath=ServiceFactory.getSubsystemConfiguration(getPageForm().getSubSystemId()).getUsecaseBundelName();
			bundelPath=bundelPath==null? super.getPageForm().getFormMetaData().getClazz().getName():bundelPath;
			 String title= MessageHandler.getMessage(usecaseName,bundelPath, getLanguage());
			 String actionName=MessageHandler.getMessage(predefindedActions.save.toString(),"ir.utopia.core.basicinformation.action.action" , WebUtil.getLanguage(session));
			 title= WebUtil.isRigthToLeft(session)?(actionName+" "+title):(title+" "+actionName);
			
			long windowNo=configForm.getWindowNumber();
			UtopiaFormMetaData metaData= configForm.getFormMetaData();
			StringBuffer result=new StringBuffer(DATA_INPUT_FORM_SCRIPT);
			result.append((char)13);
			result.append(getPageResources(pageContext, metaData, NativeScriptType.dataInputPage));
			result.append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.USECASE_PARAMETER).append("\" id=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.USECASE_PARAMETER).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(configForm.getUscaseName())).append("'/>").append((char)13).
			append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.ACTION_PARAMETER).append("\" id=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.ACTION_PARAMETER).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(configForm.getActionName())).append("'/>").append((char)13).
			append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.SAVE_ACTION_PARAMETER).append("\" id=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.SAVE_ACTION_PARAMETER).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(predefindedActions.save.toString())).append("'/>").append((char)13).
			append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.FORM_TITLE).append("\" id=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.FORM_TITLE).append("\" value='"+title).append("'/>").append((char)13).
			append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.FORM_CLASS_PARAMETER).append("\" id=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.FORM_CLASS_PARAMETER).append("\" value='").append(
					ServiceFactory.getSecurityProvider().encrypt(metaData.getClazz().getName())+"' />").append((char)13);
			if(rootNodeText!=null&&rootNodeText.trim().length()>0){
				result.append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.ROOT_NODE_TEXT).append("\" id=\"").append(ir.utopia.core.util.tags.treeview.client.TreeView.ROOT_NODE_TEXT).append("\" value='"+"getRootNodeText()"+"' />").append((char)13);	
			}
			result.append("<input type=hidden name=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.WINDOW_NO_PARAMETER_NAME).append("\" id=\"").append(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.WINDOW_NO_PARAMETER_NAME).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(String.valueOf(windowNo))+"' />").append((char)13);			

			return result.toString();
		}
	//*************************************************************************
		public String getRootNodeText() {
			return rootNodeText;
		}
   //*************************************************************************
		public void setRootNodeText(String rootNodeText) {
			this.rootNodeText = rootNodeText;
		}

	}

