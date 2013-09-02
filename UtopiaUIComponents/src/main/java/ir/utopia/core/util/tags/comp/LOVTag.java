package ir.utopia.core.util.tags.comp;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants.DisplayTypes;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class LOVTag extends UtopiaWidgetsTag {
	
	protected String usecaseName;
	protected Class<?> formClassName;
	protected String searchTitle;
	protected String separator;
	protected String displayItems;
	protected boolean multiSelect=false;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8218471283190886793L;
	public static final String WIDGET_USECASE_NAME_KEY="__usecaseName__";
	public static final String WIDGET_FORM_CLASS_NAME_KEY="__formClassName__";
	public static final String WIDGET_SEARCH_TITLE_KEY="__searchTitle__";
	public static final String WIDGET_SEPARATOR_CHAR_KEY="__separator__";
	public static final String WIDGET_DISPLAY_ITEMS_KEY="__displayItems__";
	public static final String WIDGET_MULTI_SELECT_KEY="__multiSelect__";
	private static final Logger logger;
	static {
		logger = Logger.getLogger(LOVTag.class.getName());
	}
	public int  doStartTag()throws JspException{
		try {
		super.setDisplayType(DisplayTypes.LOV);
		super.doStartTag();
		StringBuffer result=new StringBuffer();
		Integer index=getWidgetIndex();
		result.append("<input type=\"hidden\" id=\"").append(WIDGET_USECASE_NAME_KEY).append(index).append("\" value=\"").append(ServiceFactory.getSecurityProvider().encrypt(getUsecaseName())).append("\">").append((char)13);
		result.append("<input type=\"hidden\" id=\"").append(WIDGET_FORM_CLASS_NAME_KEY).append(index).append("\" value=\"").append(ServiceFactory.getSecurityProvider().encrypt( getFormClassName().getName())).append("\">").append((char)13);
		result.append("<input type=\"hidden\" id=\"").append(WIDGET_SEARCH_TITLE_KEY).append(index).append("\" value=\"").append(getSearchTitle()).append("\">").append((char)13);
		result.append("<input type=\"hidden\" id=\"").append(WIDGET_SEPARATOR_CHAR_KEY).append(index).append("\" value=\"").append(getSeparator()).append("\">").append((char)13);
		result.append("<input type=\"hidden\" id=\"").append(WIDGET_DISPLAY_ITEMS_KEY).append(index).append("\" value=\"").append(getDisplayItems()).append("\">").append((char)13);
		result.append("<input type=\"hidden\" id=\"").append(WIDGET_MULTI_SELECT_KEY).append(index).append("\" value=\"").append(isMultiSelect()).append("\">").append((char)13);
		JspWriter writer =pageContext.getOut();
		writer.write(result.toString());
		} catch (Exception e) {
			logger.log(Level.WARNING,"faild to create LOV widget ",e);
		}
		return SKIP_BODY;
	}
	
//****************************************************************************************************************
	public String getUsecaseName() {
		return usecaseName;
	}
//****************************************************************************************************************
	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}
//****************************************************************************************************************
	public Class<?> getFormClassName() {
		return formClassName;
	}
//****************************************************************************************************************
	public void setFormClassName(Class<?> formClassName) {
		this.formClassName = formClassName;
	}
//****************************************************************************************************************
	public String getSearchTitle() {
		return searchTitle;
	}
//****************************************************************************************************************
	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}
//****************************************************************************************************************
	public String getSeparator() {
		return separator;
	}
//****************************************************************************************************************
	public void setSeparator(String separator) {
		this.separator = separator;
	}
//****************************************************************************************************************
	public String getDisplayItems() {
		return displayItems;
	}
//****************************************************************************************************************
	public void setDisplayItems(String displayItems) {
		this.displayItems = displayItems;
	}
//****************************************************************************************************************
	public boolean isMultiSelect() {
		return multiSelect;
	}
//****************************************************************************************************************
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	
}
