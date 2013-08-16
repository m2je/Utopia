package ir.utopia.common.systems.subsystem.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmSubsystem entity provides the base persistence definition of the
 * CmSubsystem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCmVSubsystem extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8110969930121438665L;
	private Long cmSubsystemId;
	private String systemName;
	private String name;
	private String iconPath;
	private String abbreviation;
	// Constructors


	/** default constructor */
	public AbstractCmVSubsystem() {
	}

	// Property accessors
	@Id
	@Column(name = "CM_SUBSYSTEM_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmSubsystemId() {
		return this.cmSubsystemId;
	}

	public void setCmSubsystemId(Long cmSubsystemId) {
		this.cmSubsystemId = cmSubsystemId;
	}

	@Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true, length = 200)
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
	@Column(name = "ABBREVIATION", unique = true, nullable = false, insertable = true, updatable = true, length = 2)
	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	@Column(name="SYSTEM_NAME")
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

}