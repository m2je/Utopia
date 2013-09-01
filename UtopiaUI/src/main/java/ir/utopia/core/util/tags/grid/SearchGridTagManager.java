package ir.utopia.core.util.tags.grid;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.tags.comp.AbstractUtopiaTag;
import ir.utopia.core.util.tags.searchpage.client.SearchPage;

import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;


public class SearchGridTagManager extends AbstractUtopiaTag {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(SearchGridTagManager.class.getName());
	}
    /**
	 * 
	 */
	private static final long serialVersionUID = 5308845853272803650L;
	private String forwardUrl;
	
	private String defaultConditions;
	private boolean searchEnabled=true;
	private boolean selectable=false;
	private boolean actionEnabled=true;
	
	private UtopiaPageForm configForm;
	
	private  String SEARCH_FORM_SCRIPT;
    public int doStartTag() throws JspException{
  try{
	  SEARCH_FORM_SCRIPT=
				"<script type=\"text/javascript\" language=\"javascript\" src=\""+WebUtil.getJsFile(pageContext,"components/js/GwtExt.js")+" \"></script>"+(char)13+
				"<script type=\"text/javascript\" language=\"javascript\" src=\""+WebUtil.getJsFile(pageContext,"components/ir.utopia.core.util.tags.searchpage.SearchPage/ir.utopia.core.util.tags.searchpage.SearchPage.nocache.js")+" \"></script>"+(char)13;
	  	  pageContext.getResponse().setCharacterEncoding("UTF-8");
	      JspWriter writer =pageContext.getOut();
	      configForm=super.getPageForm();
	      writer.write(createSearchPage());
	      writer.write((char)13);
		  writer.write("<meta name=\"gwt:property\" content=\"locale="+WebUtil.getLocale(pageContext.getSession())+ "\">");
  }	
   catch(Exception ioe){
       ioe.printStackTrace();

     }
return SKIP_BODY;
}
//******************************************************************************
    private String  createSearchPage(){
    	StringBuffer result=new StringBuffer(SEARCH_FORM_SCRIPT);
    	UtopiaFormMetaData metaData= configForm.getFormMetaData();
    	if(metaData.getCSSFiles()!=null){
			for(String css:metaData.getCSSFiles()){
				result.append("<link type=\"text/css\" rel=\"stylesheet\" href=\""+WebUtil.getCss(pageContext, css)+" \">").append((char)13);
			}
		}
    	result.append("<input type=hidden name=\"").append(SearchPage.FORM_CLASS_PARAMETER).append("\" id=\"").append(SearchPage.FORM_CLASS_PARAMETER).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(configForm.getFormMetaData().getClazz().getName())).append("'/>").append((char)13).
    	append("<input type=hidden name=\"").append(SearchPage.USECASE_PARAMETER).append("\" id=\"").append(SearchPage.USECASE_PARAMETER).append("\" value='"+ServiceFactory.getSecurityProvider().encrypt(configForm.getUscaseName())).append("'/>").append((char)13).
    	append("<input type=hidden name=\"").append(SearchPage.IS_SEARCH_ENABLED).append("\" id=\"").append(SearchPage.IS_SEARCH_ENABLED).append("\" value='").append(searchEnabled).append("'/>").append((char)13);
    	if(defaultConditions!=null&&defaultConditions.trim().length()>0){
    		defaultConditions=ServiceFactory.getSecurityProvider().encrypt(defaultConditions);
	    		result.append("<input type=hidden name=\"").append(SearchPage.SEARCH_DEFAULT_CONDITIONS).append("\" id=\"").append(SearchPage.SEARCH_DEFAULT_CONDITIONS).append("\" value='").append(defaultConditions).append("'/>").append((char)13);
    	}
    	result.append("<input type=hidden name=\"").append(SearchPage.IS_SELECTABLE).append("\" id=\"").append(SearchPage.IS_SELECTABLE).append("\" value='").append(isSelectable()).append("'/>").append((char)13).
    	append("<input type=hidden name=\"").append(SearchPage.IS_ACTION_ENABLED).append("\" id=\"").append(SearchPage.IS_ACTION_ENABLED).append("\" value='").append(isActionEnabled()).append("'/>").append((char)13);
    	result.append("<div id=\"searchDiv\"></div>").append((char)13);
    	return result.toString();
    }
//******************************************************************************
	public String getForwardUrl() {
		return forwardUrl;
	}
//******************************************************************************
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
//******************************************************************************
public String getDefaultConditions() {
	return defaultConditions;
}
//******************************************************************************
public void setDefaultConditions(String defaultConditions) {
	this.defaultConditions = defaultConditions;
}
//******************************************************************************
public boolean getSearchEnabled() {
	return searchEnabled;
}
//******************************************************************************
public void setSearchEnabled(boolean searchEnabled) {
	this.searchEnabled = searchEnabled;
}
//******************************************************************************
public boolean isSelectable() {
	return selectable;
}
//******************************************************************************
public void setSelectable(boolean selectable) {
	this.selectable = selectable;
}
//******************************************************************************
public boolean isActionEnabled() {
	return actionEnabled;
}
//******************************************************************************
public void setActionEnabled(boolean actionEnabled) {
	this.actionEnabled = actionEnabled;
}

}

