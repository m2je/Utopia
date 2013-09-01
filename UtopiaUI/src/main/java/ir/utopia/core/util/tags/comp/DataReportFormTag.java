package ir.utopia.core.util.tags.comp;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.user.bean.CoUserFacadeRemote;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.struts.UtopiaBasicForm;
import ir.utopia.core.struts.UtopiaForm;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaSoftDeleteForm;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridMetaData;
import ir.utopia.core.util.tags.datainputform.client.model.AttachmentInfo;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.converter.UtopiaUIHandler;
import ir.utopia.core.util.tags.model.ViewPageModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;


public class DataReportFormTag extends AbstractUtopiaTag {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(DataReportFormTag.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -2968916045788257042L;
	
	int ratio = 2;
	int width= 800;
	String REQUIRED_CSS= "<link href=\""+WebUtil.getCss("Intcss.css")+"\" rel=\"stylesheet\" type=\"text/css\">";
	Map<String,String>LOV_DSIPLAY_CACHE=new HashMap<String, String>();
	// *************************************************************************
	public int doStartTag() throws JspException{
		JspWriter writer = null;
		try {
			pageContext.getResponse().setCharacterEncoding("UTF-8");
			writer = pageContext.getOut();
			
			writer.write(createPage());
			writer.flush();
		}catch (Exception e) {
			logger.log(Level.ALL,"error",  e);
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
//************************************************************************************************************	
	private String createPage(){
		try {
			UtopiaPageForm pageForm=getPageForm();
			UtopiaFormMetaData meta= pageForm.getFormMetaData();
			UtopiaUIHandler UIHandler=meta.getHandler();
			Map<String,Object>context= ContextUtil.createContext(pageContext.getSession());
			String lang=ContextUtil.getLoginLanguage(context);
			String usecaseName = pageForm.getUscaseName();
			UseCase usecase= UsecaseUtil.getUsecaseWithName(usecaseName);
			Subject user= ContextUtil.getUser(context);
			ViewPageModel model= UIHandler.getViewPageModel( lang, usecase, user);
			long currentRecordId=0;
			try {
				currentRecordId=Long.parseLong(pageContext.getRequest().getParameter("editItem"));
			} catch (Exception e) {
			}
			UtopiaBasicForm<?>form= UIHandler.getViewPageData(currentRecordId);
			pageForm.setForm(form);
			return createPage(model,form ,UIHandler,usecase,lang,context);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return "";
	}
//************************************************************************************************************	
	protected String createPage(ViewPageModel model,UtopiaBasicForm<? extends UtopiaBasicPersistent>form,UtopiaUIHandler UIHandler,UseCase usecase,String language,Map<String,Object>context){
		StringBuffer result=new StringBuffer();
		String pageHeader=getPageHeader();
		result.append("<HTML>").append((char)13).
		append("<HEAD>").append((char)13).append(REQUIRED_CSS).append((char)13).
		append("<TITLE>").append(pageHeader).append("</TITLE>")
		.append("</HEAD>").append((char)13).
		append("<BODY>").append((char)13);
		result.append("<p class=\"clsPageHeader\">").append(pageHeader).append("</p>");
		result.append((char)13);
		ArrayList<InputItem>largeObjects=new ArrayList<InputItem>();
		ArrayList<InputItem>currentObjects=new ArrayList<InputItem>();
		InputItem[] items=model.getReportItems();
		Arrays.sort(items,new Comparator<InputItem>(){
			@Override
			public int compare(InputItem i0, InputItem i1) {
				return i0.getDisplayType()-i1.getDisplayType();
			}});
		for(InputItem item:items){
			if(item.getDisplayType()==InputItem.DISPLAY_TYPE_LARGE_STRING||
					item.getDisplayType()==InputItem.DISPLAY_TYPE_GRID
					||item.getDisplayType()==InputItem.DISPLAY_TYPE_SEARCH_GRID){
				largeObjects.add(item);
				continue;
			}else{
				currentObjects.add(item);
			}
		}
		
		String smallItems = createSmalItemsTable(form, UIHandler,
				language, context,  currentObjects.toArray(new InputItem[currentObjects.size()]));
		
		result.append(smallItems);
		
		for(InputItem item:largeObjects){
			if(item.getDisplayType()==InputItem.DISPLAY_TYPE_GRID
					||item.getDisplayType()==InputItem.DISPLAY_TYPE_SEARCH_GRID){
				GridMetaData gridMeta=item.getGridMetaData();
				if(gridMeta!=null){
					result.append("<div style=\"border: 2px solid rgb(0, 0, 0); width: 100%; overflow: auto;\">").append((char)13);
					result.append("<p align=center class=\"clsPageHeader\">").append(WebUtil.write(item.getHeader()==null?gridMeta.getGridTitle():"&nbsp;")).append("</p>").append((char)13);
					@SuppressWarnings("unchecked")
					Collection<UtopiaBasicForm<?>>value=(Collection<UtopiaBasicForm<?>>)UIHandler.getValueOf(item, form) ;
					if(value!=null&&value.size()>0){
						UtopiaBasicForm<?> cform= value.iterator().next();
						UtopiaUIHandler cUIHandler =cform.getMetaData().getHandler();
						int row=0;
						InputItem []gridCols= gridMeta.getColumns();
						Arrays.sort(gridCols,new Comparator<InputItem>(){

							@Override
							public int compare(InputItem c1,
									InputItem c2) {
								
								return c1.getIndex()-c2.getIndex();
							}});

						for(UtopiaBasicForm<?> cur:value){
							result.append("<Table width=\"100%\" class=\"clsBorderedBody\" dir=").append(WebUtil.getDirection(language)).append(">"). append((char)13); 
							result.append("<Tr><Td width=\"3%\" class=\"clsLable\">").append(WebUtil.write( ++row,language)).append("</Td>").append((char)13);
							result.append("<Td width=\"97%\"  >").append(createSmalItemsTable(cur, cUIHandler, language, context,gridCols)).append("</Td>").append((char)13);
							result.append("</Table>").append((char)13);
						}
					}
					result.append("</div>").append((char)13);
				}else{
					logger.log(Level.WARNING, "Fail to load detail form for view");
				}
				
			}else{
					Object value= UIHandler.getValueOf(item, form) ;
					result.append(createLargeItemRow(item.getHeader(),value,language)).append((char)13);	
			}
		}
		result.append(createRecordInfoBox(form, context));
		result.append(createAttachmentBox(form, UIHandler, usecase, language)).append((char)13);
		result.append("<p align=\"center\"> <input type=\"Button\" class=\"clsReportButton\" value=\"").append(WebUtil.write(MessageHandler.getMessage("print", "ir.utopia.core.constants.Glossory", language) , language)).append("\" onclick=window.print()></p> ");
		result.append((char)13).append("</BODY>").append((char)13).append("</HTML>");
		return result.toString();
	}
//************************************************************************************************************
private String getPageHeader() {
	String usecaseName= getPageForm().getUscaseName();
	 HttpSession session=((HttpServletRequest)pageContext.getRequest()).getSession();
	 String bundelPath=ServiceFactory.getSubsystemConfiguration(getPageForm().getSubSystemId()).getUsecaseBundelName();
	 bundelPath=bundelPath==null? super.getPageForm().getFormMetaData().getClazz().getName():bundelPath;
	 String header= MessageHandler.getMessage(usecaseName,bundelPath, getLanguage());
	 String actionName=MessageHandler.getMessage(getPageForm().getActionName(),"ir.utopia.core.basicinformation.action.action" , WebUtil.getLanguage(session));
	 header= WebUtil.isRigthToLeft(session)?(actionName+" "+header):(header+" "+actionName);
	return header;
}
//************************************************************************************************************
	private String createSmalItemsTable(
			UtopiaBasicForm<? extends UtopiaBasicPersistent> form,
			UtopiaUIHandler UIHandler, String language,
			Map<String, Object> context, 
			InputItem[] items) {
		StringBuffer smallItems=new StringBuffer("<Table  width=\"100%\" class=\"clsBorderedBody\" dir=").append(WebUtil.getDirection(language)).append(">"). append((char)13); 
		int index=0;
		HashMap<String,String>createdLabels=new HashMap<String,String>();
		for(InputItem item:items ){
			if(InputItem.DISPLAY_TYPE_PASSWORD ==item.getDisplayType()){
				continue;
			}
			 Object value= UIHandler.getValueOf(item, form) ;
			 UILookupInfo lookupInfo= item.getLookupInfo();
			 if(lookupInfo!=null && value!=null){
				 String[][] data=lookupInfo.getData();
				 if(data!=null){
					 for(String []row:data){
						 if(value.toString().equals(row[0])){
							 value=row[1];
							 break;
						 }
					 }
				 }
			 }else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_LOV&&value!=null){
				 String key=form.getClass().getName()+"|"+value;
				 if(!LOV_DSIPLAY_CACHE.containsKey(key)){//LOV_DSIPLAY_CACHE.clear()
					 value=UIHandler.loadLOVValue(value, item.getColumnName(), null);
					 if(value instanceof String){
							int seperatorIndex=((String) value).indexOf("|");
							if(seperatorIndex>0){
								value=((String) value).substring(seperatorIndex+1,((String)value).length());
						}
							}
					 LOV_DSIPLAY_CACHE.put(key, value==null?"":value.toString());
				 }
				 value=LOV_DSIPLAY_CACHE.get(key);
						 
			 }
			 if(createdLabels.containsKey(item.getHeader())){
				 String currentValue=createdLabels.get(item.getHeader());
				 if(currentValue!=null&&currentValue.trim().length()>0){
					 continue;
				 }else{
					 createdLabels.put(item.getHeader(), WebUtil.write(value, language));
					 continue;
				 }
			 }
			 
			StringBuffer cur=createSmallItemsRow(item.getHeader(),language);
			createdLabels.put(item.getHeader(),WebUtil.write(value, language));
			if(index%2==0){
				smallItems.append("<Tr>").append((char)13);
				smallItems.append(cur);
			}else{
				smallItems.append(cur);
				smallItems.append("</Tr>").append((char)13);
			}
			
			index++;
		}
		if(index%2!=1&&index>0){
			smallItems.append(createSmallItemsRow(null,  language));
		}
		smallItems.append("</Table>");
		String result=smallItems.toString();
		for(String key:createdLabels.keySet()){
			String value=createdLabels.get(key);
			result=result.replaceAll("@"+key+"@", value);
		}
		return result;
	}
	
//************************************************************************************************************	
	protected StringBuffer createSmallItemsRow(String header,String language){
		StringBuffer result= new StringBuffer("<td width=10% class=\"clsLabel\">").append(WebUtil.write(header,language));
		if(header!=null){
			result.append(":");
		}
		result.append("&nbsp;").append("</td>").append((char)13);
		result.append("<td width=\"40%\" class=\"clsLabel\">");
		if(header==null){
			result.append("&nbsp;");
		}else{
			result.append("@").append(header).append("@");
		}
		result.append("&nbsp;</td>").append((char)13);

		return result;
	}
//************************************************************************************************************	
	protected StringBuffer createLargeItemRow(String header,Object value,String language){
		StringBuffer result=new StringBuffer();
		result.append("<table border=\"1\" width=\"100%\" class=\"clsTableBody\" dir=").append(WebUtil.getDirection(language)).append(">"). append((char)13); 
		result.append("<tr>").append((char)13);	
		result.append("<td width=10% class=\"clsLabel\">").append(WebUtil.write( header,language)).append("&nbsp;</td>").append((char)13);		
		result.append("<td width=90% class=\"clsLabel\">").append(WebUtil.write( value,language)).append("&nbsp;</td>").append((char)13);
		result.append("</tr>").append((char)13).
		append("</table>").append((char)13);
		
		return result;
	}
//************************************************************************************************************
	protected StringBuffer createAttachmentBox(UtopiaBasicForm<?>form,UtopiaUIHandler UIHandler,UseCase usecase,String language){
		
		List<AttachmentInfo> attachments= UIHandler.getAttachments(form.getRecordId(), usecase);
		StringBuffer result=new StringBuffer();
		if(attachments!=null&&attachments.size()>0){
			result.append("<Table class=\"clsBorderedBody\" dir=").append(WebUtil.getDirection(language)).append(">").append((char)13)
			 .append("<Tr><Td width=\"25%\" class=\"clsLabel\">").append(MessageHandler.getMessage("attachmentFilesList", "ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants", language)).append("</td> ").append((char)13)
			 .append("<Td width=\"50%\">").append((char)13).
			 append("<Table  class=\"clsTableBody\" width=\"100%\">").append((char)13);
			int index=0;
			for(AttachmentInfo attachmentInfo:attachments){
				try {
					result.append("<Tr class=\"").append(index%2==0?"clsTableOddRow":"clsTableEvenRow").append("\">").append((char)13);
					result.append("<Td class=\"clsLabel\" withd=\"20%\">").append(WebUtil.write(index+1,language)).append("</Td>").append((char)13);
					result.append("<Td class=\"clsLabel\" withd=\"90%\">").append("<a href=\"").append(WebUtil.getRootAddress(pageContext)).append("/download_Co_Ut_Attachment").
					append(Constants.STRUTS_EXTENSION_NAME).append("?recordId=").append(URLEncoder.encode(attachmentInfo.getAttachmentId(), "UTF-8") ).
					append("&").append(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME).append("=").append(URLEncoder.encode(ServiceFactory.getSecurityProvider().encrypt( usecase.getFullName()),"UTF-8")).
					append("\">").append(attachmentInfo.getFileName()).append("</Td>").append((char)13).
					append("</Tr>");
					index++;
				} catch (UnsupportedEncodingException e) {
						logger.log(Level.WARNING,"",e);
				}
			}
			 result.append("</Table>").
			 append("</td> ").append((char)13)
			 .append("<Td width=\"25%\">&nbsp;</td> ").append((char)13).append("<Tr>")
			.append("</Table>");
		
		}
		return result;
	}
//************************************************************************************************************
	protected StringBuffer createRecordInfoBox(UtopiaBasicForm<?>form,Map<String, Object> context){
		StringBuffer result=new StringBuffer();
		String language=ContextUtil.getLoginLanguage(context);
		result.append("<div style=\"border: 2px solid rgb(0, 0, 0); width: 100%; overflow: auto;\">").append((char)13);
		result.append("<Table  width=\"100%\" class=\"clsBorderedBody\" dir=").append(WebUtil.getDirection(language)).append(">"). append((char)13);
		result.append("<Tr>");
		result.append("<td width=10% class=\"clsLabel\">").append(MessageHandler.getMessage("recordId", "ir.utopia.core.constants.Glossory", language)).append(":</td>").append((char)13);
		result.append("<td width=\"40%\" class=\"clsLabel\">").append(WebUtil.write(form.getRecordId(), language)).append("&nbsp;</td>").append((char)13);
		if(UtopiaSoftDeleteForm.class.isInstance(form)){
			result.append("<td width=10% class=\"clsLabel\">").append(MessageHandler.getMessage("ISACTIVE", "ir.utopia.core.constants.Glossory", language)).append(":</td>").append((char)13);
			result.append("<td width=\"40%\" class=\"clsLabel\">").append(WebUtil.write(((UtopiaSoftDeleteForm<?>)form).isActive(), language)).append("&nbsp;</td>").append((char)13);
		}else{
			result.append("<td width=10% class=\"clsLabel\">").append("&nbsp;").append("</td>").append((char)13);
			result.append("<td width=\"40%\" class=\"clsLabel\">").append("&nbsp;</td>").append((char)13);
		}
		result.append("</Tr>");
		if(UtopiaForm.class.isInstance(form)){
			String creator="",upDater="";
			try {
				CoUserFacadeRemote userBean=(CoUserFacadeRemote)ServiceFactory.lookupFacade(CoUserFacadeRemote.class);
				CoUser creatorObject= userBean.findById(((UtopiaForm<?>)form).getCreatedby());
				creator=creatorObject!=null?creatorObject.getUsername():"";
				CoUser updaterObject=userBean.findById(((UtopiaForm<?>)form).getUpdatedby());
				upDater=updaterObject!=null?updaterObject.getUsername():"";
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
			}
			result.append("<Tr>");
			result.append("<td width=10% class=\"clsLabel\">").append(MessageHandler.getMessage("CREATED", "ir.utopia.core.constants.Glossory", language)).append(":</td>").append((char)13);
			result.append("<td width=\"40%\" class=\"clsLabel\">").append(WebUtil.write(DateUtil.getDateAndTimeString(((UtopiaForm<?>)form).getCreated(),new Locale(language)), language)).append("&nbsp;</td>").append((char)13);
			result.append("<td width=10% class=\"clsLabel\">").append(MessageHandler.getMessage("CREATEDBY", "ir.utopia.core.constants.Glossory", language)).append(":</td>").append((char)13);
			result.append("<td width=\"40%\" class=\"clsLabel\">").append(WebUtil.write(creator, language)).append("&nbsp;</td>").append((char)13);
			result.append("</Tr>");
			
			result.append("<Tr>");
			result.append("<td width=10% class=\"clsLabel\">").append(MessageHandler.getMessage("updated", "ir.utopia.core.constants.Glossory", language)).append(":</td>").append((char)13);
			result.append("<td width=\"40%\" class=\"clsLabel\">").append(WebUtil.write(DateUtil.getDateAndTimeString(((UtopiaForm<?>)form).getUpdated(),new Locale(language)), language)).append("&nbsp;</td>").append((char)13);
			result.append("<td width=10% class=\"clsLabel\">").append(MessageHandler.getMessage("updatedby", "ir.utopia.core.constants.Glossory", language)).append(":</td>").append((char)13);
			result.append("<td width=\"40%\" class=\"clsLabel\">").append(WebUtil.write(upDater, language)).append("&nbsp;</td>").append((char)13);
			result.append("</Tr>");
		}
		result.append("</Table><div>");
		return result;
	}
}
