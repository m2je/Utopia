package ir.utopia.core.basicinformation.menu.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoMenu entity provides the base persistence definition of the CoMenu
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVMenu extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	private Long coMenuId;
	private String usecaseaction;
	private String name;
	private String parentmenuName;
	private String iconPath;
	private String subsystemName;
	private long precedence;

	// Constructors

	/** default constructor */
	public AbstractCoVMenu() {
	}


	// Property accessors
	@Id
	@Column(name = "CO_MENU_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoMenuId() {
		return this.coMenuId;
	}
	public void setCoMenuId(Long coMenuId) {
		this.coMenuId = coMenuId;
	}


	@Column(name = "USECASEACTION")
	public String getUsecaseaction() {
		return usecaseaction;
	}


	public void setUsecaseaction(String usecaseaction) {
		this.usecaseaction = usecaseaction;
	}


	@Column(name = "PARENTMENU_NAME")
	public String getParentmenuName() {
		return parentmenuName;
	}


	public void setParentmenuName(String parentmenuName) {
		this.parentmenuName = parentmenuName;
	}


	@Column(name = "SUBSYTEM_NAME")
	public String getSubsystemName() {
		return subsystemName;
	}


	public void setSubsystemName(String subsystemName) {
		this.subsystemName = subsystemName;
	}


	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="ICON")
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	@Column(name = "PRECEDENCE", unique = false, nullable = true, insertable = true, updatable = true, precision = 5, scale = 0)
	public long getPrecedence() {
		return precedence;
	}

	public void setPrecedence(long precedence) {
		this.precedence = precedence;
	}

	
	
}