package ir.utopia.core.util.tags.comp;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.bean.LookUpLoaderBean;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.importer.FieldSetup;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.util.EnumUtil;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.logic.LogicEvaluator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
@Deprecated
public class DataImportFormTag extends AbstractUtopiaTag {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(DataImportFormTag.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 674171435764524523L;

	private static final int LABEL_WEIGHT = 3;
	private static final int EDITOR_WEIGHT = 6;
	private static final int ICON_WEIGHT = 1;
	
	private static final String FILE_TYPE_HIDDEN_PARAM_NAME="fileType";
	private static final String REG_EXP_HIDDEN_PARAM_NAME="regexp";
	private static final String SQL_HIDDEN_PARAM_NAME="sqlText";
	private static final String RESOURCE_NAME_PARAM_NAME="resourceName";
	private static final String FORM_CLASS_PARAM_NAME="formClass";
	private int dateItemCounter = 0;
	protected Map<String,Object> context;

	// *************************************************************************
	public int doStartTag() throws JspException {
		try {
			
			pageContext.getResponse().setCharacterEncoding("UTF-8");
			JspWriter writer = pageContext.getOut();
			ArrayList<MessageNamePair>messages=new ArrayList<MessageNamePair>();
			String formStr=createForm(messages);
			writer.write(PageHeaderManager.getMessages(messages, getDirection(), getLanguage()));
			writer.write(formStr);
			writer.write((char)13);
		    writer.write("<meta name=\"gwt:property\" content=\"locale="+WebUtil.getLocale(pageContext.getSession())+ "\">");
			
			writer.flush();
		} catch (IOException e) {
			logger.log(Level.ALL,"error",  e);
		}
		return SKIP_BODY;
	}

	// *************************************************************************
	/**
	 * 
	 * @return
	 */
	protected String createForm(List<MessageNamePair>pairs) {
		UtopiaPageForm configForm = super.getPageForm();
		int columnCount = 0;
		

		for (Annotation annotation : configForm.getFormMetaData()
				.getAnnotations()) {
			if (DataInputForm.class.isInstance(annotation)) {
				columnCount = ((DataInputForm) annotation).columnCount();
				break;
			}
		}
		if (columnCount <= 0) {
			logger
					.log(Level.ALL,
							"fail to create import form ;  Form "
									+ configForm.getFormMetaData().getClazz()
									+ " is not a DataImportForm or invalid column is set for datainput form  "
							);
			return "";
		}
		context = ContextUtil.createContext(pageContext.getSession());
		List<UtopiaFormMethodMetaData> metaData = getInputItems(configForm);
		StringBuffer result = new StringBuffer();
		if (metaData.size() > 0) {
			result
					.append(getReuiredCssAndJs())
					.append("<div id=\"importConfigFrame\" >")
					.append("</div>")
					.append("<input type=hidden name=\"").append(FORM_CLASS_PARAM_NAME).append("\" id=\"").append(FORM_CLASS_PARAM_NAME).append("\"").append(" value=").append(WebUtil.encrypt(getPageForm().getFormMetaData().getClazz().getName())).append(" \">")
					.append("<BR>")
					.append("\n<form method=post action=")
					.append(configForm.getActionName())
					.append(Constants.JSP_SEPERATOR)
					.append(configForm.getUscaseName())
					.append(Constants.STRUTS_EXTENSION_NAME)
					.append(
							"  enctype=\"multipart/form-data\" method=post onsubmit=\"onSubmit(event)\" id=\"form\">\r\n")
					.append(
							"<table id=\"settingTable\" class=clsTableBody dir=")
					.append(super.getDirection())
					.append(">\r\n")
					.append("<input type=\"hidden\" value=\"")
					.append(ServiceFactory.getSecurityProvider().encrypt(String.valueOf(configForm.getUsecase().getUsecaseId())))
					.append("\" name=\"usecaseId\" id=\"usecaseId\" /> ")
					.append("<tr>")
					.append("<th width=\"100px\" class=\"clsTableHeader\">")
					.append(msg("distination")).append("</th>")
					.append("<th width=\"170px\" class=\"clsTableHeader\">")
					.append(msg("default"))
					.append("</th>")
					.append("<th width=\"200px\" class=\"clsTableHeader\">")
					.append(msg("fetchExpr")).append("</th>")
					.append("<th width=\"100px\" class=\"clsTableHeader\">")
					.append(msg("fetchExprType")).append("</th>")
					.append("</tr>\r\n")
					.append(createColumns(columnCount, configForm
									.getFormMetaData(), metaData, configForm))
					
					.append("\r\n")
					.append("</table>\r\n")
					
					.append("<table class=clsTableBody dir=")
					.append(WebUtil.getDirection(getLanguage()))
					.append(">")
					
					//************************** dynamic parameters tag **********************
					.append("<tr>")
					.append("<div id=\"importParamFrame\" dir=")
					.append(WebUtil.getDirection(getLanguage()))
					.append("></div>")
					.append("</tr>")
					//************************************************************************
					
					.append("<tr><td class=\"clsLabel\" width=\"100px\">")
					.append(msg("from"))
					.append(":</td><td width=\"100px\" >")
					.append("<input type=text value=0 name='from' class=\"clsText\"/>")
					.append("</td></tr>")
					
					.append("<tr><td class=\"clsLabel\" width=\"100px\">")
					.append(msg("to"))
					.append(":</td>")
					.append("<td width=\"100px\">")
					.append("<input type=text  name='to' class=\"clsText\"/>")
					.append("</td></tr>")
					//**************
					.append("<tr>\r\n")
					.append("<td class=\"clsLabel\" width=\"100px\">")
					.append(msg("file")).append(": </td>\r\n")
					.append(
							"<td  colspan=\"1\" align=\"right\" width=\"270px\"><input type=\"file\" size=\"30\" name=\"upload\" value=\"\" class=\"clsText\" id=\"upload\"/></td>\r\n")
					.append("\r\n")
					.append("</td>\r\n").append("</tr>\r\n").append(
							"</table>\r\n").append(getActionButtons() + "\r\n")
					.append("<input type=hidden name=\"").append(FILE_TYPE_HIDDEN_PARAM_NAME).append("\" id=\"").append(FILE_TYPE_HIDDEN_PARAM_NAME).append("\">")		
					.append("<input type=hidden name=\"").append(REG_EXP_HIDDEN_PARAM_NAME).append("\" id=\"").append(REG_EXP_HIDDEN_PARAM_NAME).append("\">")
					.append("<input type=hidden name=\"").append(SQL_HIDDEN_PARAM_NAME).append("\" id=\"").append(SQL_HIDDEN_PARAM_NAME).append("\">")
					.append("<input type=hidden name=\"").append(RESOURCE_NAME_PARAM_NAME).append("\" id=\"").append(RESOURCE_NAME_PARAM_NAME).append("\">")
					.append("</form>\r\n");
		}

		return result.toString();
	}
// ****************************************************************************
	protected StringBuffer getReuiredCssAndJs() {
		StringBuffer result = new StringBuffer();
		result
				.append("<script language=\"JavaScript\" src=\"")
				.append(WebUtil.getJsFile(pageContext,"CalendarPopup.js"))
				.append("\"></script>")
				.append((char) 13)
				.append("<script language=\"JavaScript\" src=\"")
				.append(WebUtil.getJsFile(pageContext,"importer.js"))
				.append("\"></script>")
				.append((char) 13)
				.append("<script language=\"JavaScript\" src=\"")
				.append(WebUtil.getJsFile(pageContext,"Utopiautil.js"))
				.append("\"></script>")
				.append((char) 13)
				.append(
						"<script language=\"JavaScript\">try{document.write(getCalendarStyles());}catch(e){}addEvent(window.document.getElementsByTagName('form'), 'submit', onSubmit);function onSubmit(e){testMandatory(e);}</script>")
				.append("<script language=\"JavaScript\" src=\"")
				.append(WebUtil.getJsFile(pageContext,"components/ir.utopia.core.util.tags.importpage.ImportPage/ir.utopia.core.util.tags.importpage.ImportPage.nocache.js"))
				.append("\"></script>")		
				.append("<script language=\"JavaScript\" src=\"")
				.append(WebUtil.getJsFile(pageContext,"Utopiautil.js"))
				.append("\"></script>")	
				.append("<link href=\"").append("../../js/components/js/resources/css/ext-all.css").append("\" rel=\"stylesheet\" type=\"text/css\">")
				.append("<link href=\"").append("../../js/components/js/columntree/column-tree.css").append("\" rel=\"stylesheet\" type=\"text/css\">"); ;
		
		return result;
	}

	// ****************************************************************************
	private StringBuffer getActionButtons() {
		StringBuffer result = new StringBuffer(" <p align=\"center\">");
		result.append(ActionButtonTagManager.createButton(
				"buttons/btn_Cancel_10622_fa.gif", "", "cancel", super.getLanguage(),
				ActionButtonTagManager.ButtonTypes.foward));
		result.append(ActionButtonTagManager.createButton(
				"buttons/btn_Refresh_10622_fa.gif", null, "reset", super
						.getLanguage(),
				ActionButtonTagManager.ButtonTypes.reset));
		result.append(ActionButtonTagManager.createButton(
				"buttons/btn_Save_10622_fa.gif", null, "save", super
						.getLanguage(),
				ActionButtonTagManager.ButtonTypes.submit));
		return result.append("</p>");
	}

	// ****************************************************************************
	/**
	 * 
	 * @param columnCount
	 * @param metaData
	 * @param configForm
	 * @return
	 */
	protected StringBuffer createColumns(int columnCount,
			UtopiaFormMetaData formMetaData,
			List<UtopiaFormMethodMetaData> metaData, UtopiaPageForm configForm) {
		StringBuffer simpleEdiors = new StringBuffer();
		
		int columnCounter = 0;
		int labelRatio, editorRatio, iconRatio;
		int columnWidth = 100 / columnCount;
		labelRatio = columnWidth * LABEL_WEIGHT / 10;
		editorRatio = columnWidth * EDITOR_WEIGHT / 10;
		iconRatio = columnWidth * ICON_WEIGHT / 10;
		
		
		for (UtopiaFormMethodMetaData meta : metaData) {
			if(!meta.displayOnImport())continue;
			InputItem item = (InputItem) meta.getAnnotation(InputItem.class);
			Object value = formMetaData.getHandler().getDefaultValueOf(meta , null,predefindedActions.save);
			String enabled=LogicEvaluator.evaluateReadonlyLogic(meta.getReadonlyLogic(predefindedActions.importData), context)?" disabled=true ":" ";
			simpleEdiors.append(createSpace(item, meta, formMetaData, value,
					columnCounter, configForm.getFormMetaData().getClazz()
							.getName(), labelRatio, editorRatio, iconRatio,enabled));
		}

		/*
		 * simpleEdiors.append(completeLine(lastcol, columnCount, labelRatio,
		 * editorRatio, iconRatio)).append("</tr>").
		 * append("</table>").append((char)13).append(largeEdiors);
		 */
		return simpleEdiors;
	}



	/**
	 * in case of item or meta is null create an empty space else creates label
	 * and icon and the editor
	 * 
	 * @param item
	 * @param meta
	 * @param i
	 * @param clazz
	 * @param labeRatio
	 * @param editorRatio
	 * @param iconRatio
	 * @return
	 */
	protected StringBuffer createSpace(InputItem item,
			UtopiaFormMethodMetaData meta, UtopiaFormMetaData formMetaData,
			Object value, int i, String clazz, int labelRatio, int editorRatio,
			int iconRatio,String enabled) {
		String fieldName;
		StringBuffer result = new StringBuffer();
		String[] edStr;
		String header;
		if (item != null && meta != null) {
			header = (item.name() == null || item.name().trim().length() == 0) ? AnnotationUtil
					.getPropertyName(meta.getMethodName())
					: item.name();
			fieldName = header;
			header = MessageHandler.getMessage(header, clazz, super
					.getLanguage());
			edStr = createEditorAndIcon(item, meta, formMetaData, value, header,enabled);
			if (item.isManadatory()) {
				header += "*";
			}
			header = header + " :";
		} else {
			header = "";
			edStr = new String[] { "", "" };
			return new StringBuffer();
		}
		
		result
				.append("<tr>\r\n")
				.append(
						"<td class=clsLabel nowrap align=\"left\" id=FieldNameTD_")
				.append(fieldName)
				.append(">")
				.append(header)
				.append("</td>\r\n")
				.append("<td >")
				.append(edStr[0])
				.append(WebUtil.write(edStr[1]))
				.append("</td>\r\n")
				.append("<td ><input name=\"")
				.append(fieldName)
				.append("Excel\" id=\"")
				.append(fieldName)
				.append("Excel\"  type=text class=clsText  ").
				append(enabled).
				append(" value=\"\" align=\"left\" dir=ltr></td>")
				.append("<td><select name=\"").append(fieldName).append(
						"Type\" id=\"").append(fieldName).append(
						"Type\" class=clsSelect dir=rtl").append(enabled).append(" >");
		for (FieldSetup.Types type : FieldSetup.Types.values()) {
			result.append("<option value=\"")
					.append(type.name()).append("\">")
					.append(MessageHandler.getMessage(type.name(), MessageHandler.IMPORTER_GLOSSOARY_BUNDLE, getLanguage()))
					.append("</option>");
		}
		result.append("</select></td></tr>\r\n");

		return result;
	}

	// ****************************************************************************
	/**
	 * 
	 * @param item
	 * @param meta
	 * @return
	 */
	protected String[] createEditorAndIcon(InputItem item,
			UtopiaFormMethodMetaData meta, UtopiaFormMetaData formMetaData,
			Object value, String alt,String enabled) {
		StringBuffer editorStr = new StringBuffer();
		StringBuffer iconStr = new StringBuffer();
		String fieldName = AnnotationUtil.getPropertyName(meta.getMethodName());// "form." added because: action get persistent form and then set this field of it
		
		if (Constants.DisplayTypes.Integer.equals(item.displayType())
				|| Constants.DisplayTypes.String.equals(item.displayType())) {
			editorStr.append("<input name=\"form.").append(fieldName).append(
					"\" id=").append(fieldName).append(
					" type=text class=clsText   value=\"").append(
					WebUtil.write(value).trim()).append("\" ").append(enabled).append("\">");

		} else if (Constants.DisplayTypes.lookup.equals(item.displayType())
				|| Constants.DisplayTypes.list.equals(item.displayType())) {
			editorStr.append(createLookup(meta, formMetaData, item
					.displayType(), value,enabled));
		} else if (Constants.DisplayTypes.LargeString
				.equals(item.displayType())) {
			editorStr.append(
					"<textarea class=clsTextarea  style=\"width: 100%;\"")
					.append(" name =\"form.").append(fieldName).append(
							"\" id=\"").append(fieldName).append(enabled).append("\""). append(
							" >").append(WebUtil.write(value).trim()).append(
							"</textarea>");
		} else if (Constants.DisplayTypes.checkBox.equals(item.displayType())) {
			editorStr.append("<input type=checkbox class=clsCheckBox").append(
					" name=\"form.").append(fieldName).append("\"").append(
					" id=\"").append(fieldName).append("\"").append(
					" value=\"true\" ").append(enabled);
			if (Boolean.class.isInstance(value)
					&& ((Boolean) value).booleanValue())
				editorStr.append(" checked ");
			editorStr.append(" />");
		} else if (Constants.DisplayTypes.Date.equals(item.displayType())) {
			editorStr.append("<script language=\"JavaScript\" id=\"jscal1x3\">")
				.append("var cal1x").append(dateItemCounter).append(" = new CalendarPopup(\"testdiv1\");")
				.append("</script>").append("<nobr><input name=\"form.").append(fieldName).append(enabled)
				.append("\"").append(" id=\"").append(fieldName)
				.append("\" type=\"text\" class=\"clsText\"  ")
				.append("style=\"width:90%\" dir=\"ltr\" value =")
				.append(WebUtil.write(value)).append(">");
			iconStr.append("<a href=\"#\" onclick=\"cal1x")
				.append(dateItemCounter).append(".select(document.forms[0].")
				.append(fieldName).append(",'").append(fieldName).append("','yyyy/MM/dd');")
				.append("return false;\" title=\"").append(alt).append("\" name=\"")
				.append(fieldName).append("\" id=\"").append(fieldName)
				.append("\"><img src=\"").append(WebUtil.getImage("popupCalendarIcon.gif"))
				.append("\" alt=\"").append(alt).append("\" width=\"16\" height=\"16\" border=\"0\"></a>")
				.append("<div id=\"testdiv1\" style=\"position: absolute; visibility: hidden; background-color: white;\"></div><nobr>");
			dateItemCounter++;
		}
		return new String[] { editorStr.toString(), iconStr.toString() };
	}

	// ****************************************************************************
	/**
	 * 
	 * @param meta
	 * @return
	 */
	protected StringBuffer createLookup(UtopiaFormMethodMetaData meta,
			UtopiaFormMetaData formMetaData, DisplayTypes displayType,
			Object value,String enable) {
		StringBuffer result = new StringBuffer("<select name=\"form.").append(
				meta.getFieldName()).append("\" id=\"").append(
				meta.getFieldName()).append("\" class=\"clsSelect\" dir=\"")
				.append(super.getDirection()).append("\""). append(enable).append(" >");
		List<NamePair> pairs = loadLookup(meta, formMetaData, displayType);
		long selectedItem = -1;
		try {
			if (Enum.class.isInstance(value)) {
				selectedItem = ((Enum<?>) value).ordinal();
			} else {
				selectedItem = value != null ? Long.valueOf(value.toString())
						: -1;
			}
		} catch (NumberFormatException e) {
		}
		result.append("<option value=\"-1\" >---</option>");
		if (pairs != null) {
			for (NamePair pair : pairs) {
				String key = String.valueOf(pair.getKey());
				result.append("<option value=").append(key);
				if (pair.getKey() == selectedItem) {
					result.append(" selected ");
				}
				result.append(">");
				if(meta.getReturnType().isEnum())
					result.append(WebUtil.write(pair.getKey() + " : " + pair.getName()));
				else
					result.append(WebUtil.write(pair.getName()));
				result.append("</option>");
			}
		}
		result.append("</select>");
		return result;
	}

	// ****************************************************************************
	/**
	 * 
	 * @param item
	 * @param meta
	 * @param formMetaData
	 * @return
	 */
	protected List<NamePair> loadLookup(UtopiaFormMethodMetaData meta,
			UtopiaFormMetaData formMetaData, DisplayTypes displayType) {
		if (DisplayTypes.lookup.equals(displayType)) {
			Class<? extends UtopiaPersistent> persitentClass = findPersitedClass(
					meta, formMetaData);
			if (persitentClass != null) {
				Class<? extends UtopiaBean> bean = BeanUtil
						.findRemoteClassFromPersistent(persitentClass);
				if (LookUpLoaderBean.class.isAssignableFrom(bean)) {
					try {
						LookUpLoaderBean remote = (LookUpLoaderBean) ServiceFactory.lookupFacade(bean.getName());
						return remote.loadLookup( null);
					} catch (NamingException e) {
						logger.log(Level.ALL,"unable to load facade class ", 
								e);
					}
				}
			}
		} else if (DisplayTypes.list.equals(displayType)) {
			return EnumUtil.getEnumLookups(formMetaData.getEnumClass(meta), getLanguage());
		}
		return null;
	}

	// ****************************************************************************
	/**
	 * 
	 * @param meta
	 * @param formMetaData
	 * @return
	 */

	protected Class<? extends UtopiaPersistent> findPersitedClass(
			UtopiaFormMethodMetaData meta, UtopiaFormMetaData formMetaData) {
		return formMetaData.getLookupPersistentClass(meta);
	}

	// ****************************************************************************
	private List<UtopiaFormMethodMetaData> getInputItems(
			UtopiaPageForm configForm) {
		UtopiaFormMetaData metaData = configForm.getFormMetaData();
		UtopiaFormMethodMetaData[] methodMetaDatas = metaData.getMethodMetaData();
		List<UtopiaFormMethodMetaData> result = new ArrayList<UtopiaFormMethodMetaData>();
		for (UtopiaFormMethodMetaData methodMetaData : methodMetaDatas) {
			if (methodMetaData.getAnnotation(InputItem.class) != null) {
				result.add(methodMetaData);
			}

		}
		Collections.sort(result, new Comparator<UtopiaFormMethodMetaData>() {
			public int compare(UtopiaFormMethodMetaData o1,
					UtopiaFormMethodMetaData o2) {
				InputItem i1 = (InputItem) o1.getAnnotation(InputItem.class);
				InputItem i2 = (InputItem) o2.getAnnotation(InputItem.class);
				return i1.index() - i2.index();
			}
		});
		return result;
	}
	// ****************************************************************************
	protected String msg(String name) {
		String ret = MessageHandler.getMessage(name, this.getClass().getName(), super.getLanguage());
		return ret;
	}

}
