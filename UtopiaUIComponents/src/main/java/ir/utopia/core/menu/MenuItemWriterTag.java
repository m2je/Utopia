/**
 * 
 */
package ir.utopia.core.menu;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.basicinformation.menu.MenuItem;
import ir.utopia.core.basicinformation.menu.persistent.CoMenuType;
import ir.utopia.core.basicinformation.menu.persistent.CoVSystemMenu;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.model.SubSystem;
import ir.utopia.core.model.System;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.util.WebUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;



/**
 * @author salarkia
 *
 */
public class MenuItemWriterTag extends BodyTagSupport {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(MenuItemWriterTag.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -167354299116330949L;
	private Comparator<Object> comparator = new MenuItemComparator();

//***********************************************************************************************	
	/**
	 * 
	 */
	public MenuItemWriterTag() {

	}
//***********************************************************************************************
	@Override
	public int doStartTag() throws JspException {
		
		JspWriter out = pageContext.getOut(); 
		try {
			 Object user= WebUtil.getUser(pageContext.getSession());
			 
			 if(!(user instanceof Subject)){
				 throw new IllegalArgumentException("invalid user attribute");
			 }
			 String language=WebUtil.getLanguage(pageContext.getSession());
			 String userMenuString;
			 Collection<? extends CoVSystemMenu> items= ServiceFactory.getSecurityProvider().getAccessibleMenus((Subject)user);
			 userMenuString=createMenus(items, new HashMap<Long, MenuItem>(),language);
			 out.write(userMenuString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
//***********************************************************************************************
	private String createMenus(Collection<? extends CoVSystemMenu> items,Map<Long,MenuItem>loadModel,String language){
			if(items.size()==0)return createMenu(loadModel.values(),language);
		Vector<CoVSystemMenu> missedItems=new Vector<CoVSystemMenu>();
		
		CoVSystemMenu[] itemsArray = items.toArray(new CoVSystemMenu[items.size()]);
		Arrays.sort(itemsArray, comparator );
		for(CoVSystemMenu item:itemsArray){
			long currentKey=item.getCurrentId();
			long parentKey=item.getParentId();
			if(parentKey==0 ){
				loadModel.put(currentKey,new MenuItem(item));
			}else {
				MenuItem currentItem=new MenuItem(item);
				MenuItem parent=getParentof(currentItem, loadModel);
				if(parent!=null){
					parent.addChild(currentItem);
				}
				else{
					missedItems.add(item);
				}
			}
		}
		if(items.size()==missedItems.size()){
			CoVSystemMenu item= missedItems.get(0);
			if(logger.isLoggable(Level.FINE))
				logger.log(Level.FINE,"could not find parent node for node : "+item.getName());
			return createMenu(loadModel.values(),language);
		}
		
		return createMenus(missedItems,loadModel,language);
	}
//***********************************************************************************************
	private MenuItem getParentof(MenuItem item,Map<Long,MenuItem>loadModel){
		long parentKey=item.getParentId();
		if(loadModel.containsKey(parentKey)){
			return loadModel.get(parentKey);
		}
		for(MenuItem citem: loadModel.values()){
			MenuItem res= getParentof(item,citem);
			if(res!=null)return res;
			} 
		return null;
	}
//***********************************************************************************************
	private MenuItem getParentof(MenuItem item,MenuItem condidate){
		if(item.getParentId()==condidate.getCurrentId())return condidate;
		for(MenuItem child:condidate.getChildren()){
			if(child.getCurrentId()==item.getParentId()){
				return child;
			}else{
				MenuItem res= getParentof(item, child);
				if(res!=null){
					return res;
				}
			}
			
			}
		return null;
	}
//***********************************************************************************************
	private String createMenu(Collection<MenuItem> items,String language){
		StringBuffer result=new StringBuffer();
		MenuItem[] itemsArray = items.toArray(new MenuItem[items.size()]);
		Arrays.sort(itemsArray, comparator);
		for(MenuItem item:itemsArray){
			Vector<MenuItem>children= item.getChildren();
			if(children.size()==0){
				if(logger.isLoggable(Level.FINE)){
					logger.log(Level.WARNING,"no subMenu accessible for menu "+item.getName());
				}
				continue;
			}
			Collections.sort(children, comparator);				
			if(!isValidItem(item)){
				continue;
			}
			long systemId=item.getSystemId();
			System system=null;
			SubSystem subSystem=null;
			system=ServiceFactory.getSystemConfiguration(systemId);
			if(system==null&&systemId>0){
				 continue;
			}
			subSystem=ServiceFactory.getSubsystemConfiguration(item.getSubsystemId());
			String superMenuBundel=(system!=null?system.getMenuBundelName():null);
			String usecaseBundelPath=(subSystem!=null?subSystem.getUsecaseBundelName():null);
			String itemName=MessageHandler.getMessage(item.getName(), superMenuBundel,usecaseBundelPath, language) ;
			result.append("[\"+").append(itemName).append("\",\"\", \"").append(WebUtil.getImage(item.getIcon())).
			append("\", \"\", \"\", \"").append(WebUtil.write(itemName)).append("\", \"\", \"0\", \"\", \"\", ],");
			for(MenuItem subMenu:children){
				createSubElements(result, 0, subMenu,language);
			}
			item.removeAllChildren();
			
		}
	return result.toString();
	}
//***********************************************************************************************	
private void createSubElements(StringBuffer source,int level,MenuItem item,String language){
	StringBuffer itemStr=new StringBuffer();
	for(int i=level+1;i>0;i--){
		itemStr.append("|");
	}
	
	String link=null;
	System system=ServiceFactory.getSystemConfiguration(item.getSystemId());
	String bundelName=system==null?null:system.getMenuBundelName();
	if(item.getSystemId()>0&&system==null){
		return;
	}
	String actionName=item.getActionName();
	link = item.getDirectUrl();
	if(link==null||link.trim().length()==0){
		if(actionName!=null &&actionName.trim().length()>0&& 
				item.getUseCaseName()!=null &&item.getUseCaseName().trim().length()>0){
			if(CoMenuType.window.equals(item.getMenuType())){
				actionName=Constants.predefindedActions.search.name().equals(actionName)?
						actionName:Constants.REDIREDT_TO_PAGE_PREFIX+Constants.RIDIRECT_TO_PAGE_SEPERATOR+actionName;
				link =WebUtil.write(actionName)+Constants.USECASE_SEPERATOR+
				WebUtil.write(item.getUseCaseName())+Constants.STRUTS_EXTENSION_NAME;
				link+="?"+SecurityProvider.LOGIN_LOCALE_PARAMETER_NAME+"="+language;
			}else if(CoMenuType.process.equals(item.getMenuType())){
				Long usactId= item.getUsecaseActionId();
				if(usactId!=null&&usactId>0)
					link="javascript:startProcess('"+usactId+"')";
			}
		}
		}
		if(isValidItem(item)){
			String itemName=MessageHandler.getMessage(item.getName(),bundelName , language) ;
			StringBuffer currentItem=new StringBuffer("[\"").append(itemStr).append(WebUtil.write(itemName)).
			append("\",\"").append(WebUtil.write(link)).append("\", \"").append(WebUtil.write(WebUtil.getImage(item.getIcon()))).
			append("\", \"").append(WebUtil.write(WebUtil.getImage(item.getIcon()))).
			append("\", \"").append(WebUtil.write(link)).append("\", \"").
			append(WebUtil.write(itemName)).append("\", \"").append(item.getLinkTargetString()).append("\", \"\", \"\", \"\", ],").append((char)13);
			source.append(currentItem);
			Vector<MenuItem>children= item.getChildren();
			Collections.sort(children, comparator);
			for(MenuItem subMenu:children){
				createSubElements(source, level+1, subMenu,language);
			}
			item.removeAllChildren();
		}
	
	}
//***********************************************************************************************
private boolean isValidItem(MenuItem item){
	if(item.getDirectUrl() != null && item.getDirectUrl().length() > 0)
		return true;
	if(item!=null){
		if(item.getActionName()!=null &&item.getActionName().trim().length()>0){
			return true;
		}else if(item.getChildren()==null||item.getChildren().size()==0) {
			return false;
		}else{
			for(MenuItem curItem :item.getChildren()){
				if(isValidItem(curItem))return true;
			}
		}
	}
	return false;
}
//***********************************************************************************************
}
