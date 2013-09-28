package ir.utopia.core.basicinformation.menu.action;

import ir.utopia.core.basicinformation.menu.persistent.CoMenu;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.constants.Constants.MenuLinkTarget;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class MenuForm  extends AbstractUtopiaForm<CoMenu>{

	private Long coMenuId;
	private Long coUsecaseActionId;
	
	private String name;
	private Long parentMenuId;
	private String iconPath;
	private Long cmSubSystemId;
	private Long precedence;
	private MenuLinkTarget linkTarget;
	private String directUrl;
	private String usecaseaction;
	private String parentmenuName;
	private String subsystemName;


	public MenuForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoMenuId() {
		return coMenuId;
	}

	public void setCoMenuId(Long coMenuId) {
		this.coMenuId = coMenuId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=1,breakLineAfter=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoUsecaseAction")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=6)
	public Long getCoUsecaseActionId() {
		return coUsecaseActionId;
	}

	public void setCoUsecaseActionId(Long coUsecaseActionId) {
		this.coUsecaseActionId = coUsecaseActionId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getParentMenu")
	@InputItem(isManadatory=false,displayType=DisplayTypes.lookup,index=4)
	public Long getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmSubSystem")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=5)
	public Long getCmSubSystemId() {
		return cmSubSystemId;
	}

	public void setCmSubSystemId(Long cmSubSystemId) {
		this.cmSubSystemId = cmSubSystemId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=2)
	public Long getPrecedence() {
		return precedence;
	}

	public void setPrecedence(Long precedence) {
		this.precedence = precedence;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=3,breakLineAfter=true)
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=7,breakLineAfter=false)
	public String getDirectUrl() {
		return directUrl;
	}

	public void setDirectUrl(String directUrl) {
		this.directUrl = directUrl;
	}

	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUsecaseaction() {
		return usecaseaction;
	}

	public void setUsecaseaction(String usecaseaction) {
		this.usecaseaction = usecaseaction;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getParentmenuName() {
		return parentmenuName;
	}

	public void setParentmenuName(String parentmenuName) {
		this.parentmenuName = parentmenuName;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSubsystemName() {
		return subsystemName;
	}

	public void setSubsystemName(String subsystemName) {
		this.subsystemName = subsystemName;
	}

	@FormPersistentAttribute
	@InputItem(displayType=Constants.DisplayTypes.list,isManadatory=true,index=4,breakLineAfter=true,name="linkTarget")
	public MenuLinkTarget getLinkTarget() {
		return linkTarget;
	}
	public void setType(MenuLinkTarget linkTarget) {
		this.linkTarget = linkTarget;
	}


}
