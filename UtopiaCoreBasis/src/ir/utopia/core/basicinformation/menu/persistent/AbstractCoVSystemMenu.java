package ir.utopia.core.basicinformation.menu.persistent;

import ir.utopia.core.constants.Constants.MenuLinkTarget;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoVSystemMenu entity provides the base persistence definition of the
 * CoVValidMenus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVSystemMenu implements java.io.Serializable {

	
	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 2641336187529585506L;

	/** default constructor */
	public AbstractCoVSystemMenu() {
	}
	private long cmSubsystemId;
	private long cmSystemId;
	
	private long coUserId;
	private long currentId;
	private long parentId;
	private String name;
	
	private String icon;
	private long coUsecaseActionId;
	private long precedence;
	private String useCaseName;
	private String actionName;
	private String directUrl;
	private MenuLinkTarget linkTarget;
	private CoMenuType menuType;

	@Column(name = "CO_USER_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public long getCoUserId() {
		return coUserId;
	}
	
	public void setCoUserId(long coUserId) {
		this.coUserId = coUserId;
	}
	@Id
	@Column(name = "CURRENT_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public long getCurrentId() {
		return currentId;
	}
	public void setCurrentId(long currentId) {
		this.currentId = currentId;
	}
	@Column(name = "PARENT_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "ICON", unique = false, nullable = true, insertable = true, updatable = true, length = 2000)
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Column(name = "CO_USECASE_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public long getCoUsecaseActionId() {
		return coUsecaseActionId;
	}
	public void setCoUsecaseActionId(long coUsecaseActionId) {
		this.coUsecaseActionId = coUsecaseActionId;
	}
	@Column(name = "PRECEDENCE", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public long getPrecedence() {
		return precedence;
	}
	public void setPrecedence(long precedence) {
		this.precedence = precedence;
	}
	@Column(name = "USECASENAME", unique = false, nullable = true, insertable = true, updatable = true)
	public String getUseCaseName() {
		return useCaseName;
	}

	public void setUseCaseName(String useCaseName) {
		this.useCaseName = useCaseName;
	}
	@Column(name = "ACTIONNAME", unique = false, nullable = true, insertable = true, updatable = true)
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	@Column(name="CM_SUBSYSTEM_ID")
	public long getCmSubsystemId() {
		return cmSubsystemId;
	}

	public void setCmSubsystemId(long cmSubsystemId) {
		this.cmSubsystemId = cmSubsystemId;
	}
	@Column(name="CM_SYSTEM_ID")
	public long getCmSystemId() {
		return cmSystemId;
	}

	public void setCmSystemId(long cmSystemId) {
		this.cmSystemId = cmSystemId;
	}
	
	@Column(name = "DIRECT_URL", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getDirectUrl() {
		return this.directUrl;
	}

	public void setDirectUrl(String direcUrl) {
		this.directUrl = direcUrl;
	}
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "LINK_TARGET", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public MenuLinkTarget getLinkTarget() {
		return this.linkTarget;
	}

	public void setLinkTarget(MenuLinkTarget linkTarget) {
		this.linkTarget = linkTarget;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "MENU_TYPE")
	public CoMenuType getMenuType() {
		return menuType;
	}

	public void setMenuType(CoMenuType menuType) {
		this.menuType = menuType;
	}
	
}