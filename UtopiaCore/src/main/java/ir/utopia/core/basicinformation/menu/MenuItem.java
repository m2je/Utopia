package ir.utopia.core.basicinformation.menu;

import ir.utopia.core.basicinformation.menu.persistent.CoMenuType;
import ir.utopia.core.basicinformation.menu.persistent.CoVSystemMenu;
import ir.utopia.core.constants.Constants.MenuLinkTarget;

import java.util.Vector;

public class MenuItem {
private Vector<MenuItem> children=new Vector<MenuItem>();

	private long currentId;
	private long parentId;
	private String name;
	private String icon;
	private String actionName;
	private String useCaseName;
	private long subsystemId; 
	private long systemId;
	private MenuLinkTarget linkTarget;
	private String directUrl;
	private Long precedence;	
	private CoMenuType menuType; 
	private Long usecaseActionId;
	public MenuItem(CoVSystemMenu menu){
		this.name=menu.getName();
		this.currentId=menu.getCurrentId();
		this.parentId=menu.getParentId();
		this.icon=menu.getIcon();
		this.actionName=menu.getActionName();
		this.useCaseName=menu.getUseCaseName();
		this.subsystemId=menu.getCmSubsystemId();
		this.systemId=menu.getCmSystemId();
		this.linkTarget = menu.getLinkTarget();
		this.directUrl = menu.getDirectUrl();
		this.precedence = menu.getPrecedence();
		this.menuType=menu.getMenuType();
		this.usecaseActionId=menu.getCoUsecaseActionId();
	} 
	public Long getPrecedence() {
		return precedence;
	}
	public void setPrecedence(Long precedence) {
		this.precedence = precedence;
	}
	public MenuLinkTarget getLinkTarget() {
		return linkTarget;
	}
	public void setLinkTarget(MenuLinkTarget linkTarget) {
		this.linkTarget = linkTarget;
	}
	public String getDirectUrl() {
		return directUrl;
	}
	public void setDirectUrl(String directUrl) {
		this.directUrl = directUrl;
	}
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getUseCaseName() {
		return useCaseName;
	}

	public void setUseCaseName(String useCaseName) {
		this.useCaseName = useCaseName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public long getCurrentId() {
		return currentId;
	}

	public void setCurrentId(long currentId) {
		this.currentId = currentId;
	}

	

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChildren(Vector<MenuItem> children) {
		this.children = children;
	}

	public Vector<MenuItem> getChildren() {
		return children;
	}
	
	public void addChild(MenuItem item){
		children.add(item);
	}
	
	public void removeAllChildren(){
		children.removeAllElements();
	}
	public long getSubsystemId() {
		return subsystemId;
	}
	public void setSubsystemId(long subsystemId) {
		this.subsystemId = subsystemId;
	}
	public long getSystemId() {
		return systemId;
	}
	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}
	public String getLinkTargetString() {
		if(linkTarget == MenuLinkTarget.frame)
			return "menuFrame";
		if(linkTarget == MenuLinkTarget.top)
			return "_top";
		if(linkTarget == MenuLinkTarget.blank)
			return "_blank";
		return null;
	}
	public CoMenuType getMenuType() {
		return menuType;
	}
	public void setMenuType(CoMenuType menuType) {
		this.menuType = menuType;
	}
	public Long getUsecaseActionId() {
		return usecaseActionId;
	}
	public void setUsecaseActionId(Long usecaseActionId) {
		this.usecaseActionId = usecaseActionId;
	}
	
}
