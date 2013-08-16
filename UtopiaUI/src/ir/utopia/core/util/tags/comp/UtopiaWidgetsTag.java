package ir.utopia.core.util.tags.comp;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.util.GWTUtil;
import ir.utopia.core.util.WebUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public abstract class UtopiaWidgetsTag extends BodyTagSupport {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UtopiaWidgetsTag.class.getName());
	}
	public static final String WIDGET_ANCHOR_KEY="__UtopiaWidget__";
	public static final String WIDGET_DISPLAY_TYPE_KEY="__widgetType__";
	public static final String WIDGET_NAME_KEY="__widgetName__";
	private static final String UTOPIA_WIDGET_COUNT_PARAMETER="_UtopiaWidgetCount";
	
	protected Constants.DisplayTypes displayType;
	protected String widgetName;
	
	Integer index;
	/**
	 * 
	 */
	private static final long serialVersionUID = 9219212636217394196L;
//****************************************************************************************************************	
	public Constants.DisplayTypes getDisplayType() {
		return displayType;
	}
//****************************************************************************************************************
	public void setDisplayType(Constants.DisplayTypes displayType) {
		this.displayType = displayType;
	}
//****************************************************************************************************************
	public String getWidgetName() {
		return widgetName;
	}
//****************************************************************************************************************
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}
//****************************************************************************************************************
	public int doStartTag()throws JspException{
		try {
			pageContext.getResponse().setCharacterEncoding("UTF-8");
			JspWriter writer =pageContext.getOut();
			Integer index=getWidgetIndex();
			ServletRequest request= pageContext.getRequest();
			request.setAttribute(UTOPIA_WIDGET_COUNT_PARAMETER, ++index);
			writer.write("<script type=\"text/javascript\" language=\"javascript\" src=\""+WebUtil.getJsFile(pageContext,"components/ir.utopia.core.util.tags.components.wigetsexport.WidgetExporter/ir.utopia.core.util.tags.components.wigetsexport.WidgetExporter.nocache.js")+" \"></script>"+(char)13);
			writer.write(createWidget());
			writer.write((char)13);
			writer.write("<meta name=\"gwt:property\" content=\"locale="+WebUtil.getLocale(pageContext.getSession())+ "\">");
		} catch (Exception e) {
			logger.log(Level.WARNING,"fail to create Utpoia widget",e);
		}
		return SKIP_BODY;
	}
//****************************************************************************************************************
	protected Integer getWidgetIndex(){
		if(index==null){
			ServletRequest request= pageContext.getRequest();
			Object widgetCount= request.getAttribute(UTOPIA_WIDGET_COUNT_PARAMETER);
			if(widgetCount instanceof Integer){
				index=(Integer)widgetCount;
			}
			else index=0;
		}
		
		return index;
	}
//****************************************************************************************************************
	protected String createWidget(){
		StringBuffer result=new StringBuffer();
		Constants.DisplayTypes displayType=getDisplayType();
		result.append("<input type=\"hidden\" id=\"").append(WIDGET_DISPLAY_TYPE_KEY).append(index).append("\" value=\"").append(GWTUtil.convertDisplayType( displayType)).append("\">").append((char)13);
		result.append("<input type=\"hidden\" id=\"").append(WIDGET_NAME_KEY).append(index).append("\" value=\"").append(getWidgetName()).append("\">").append((char)13);
		result.append("<div id=\"").append(WIDGET_ANCHOR_KEY).append(getWidgetIndex().toString()).append("\"></div>");
		return result.toString();
	}
}
