package ir.utopia.core.log.persistent;

// default package

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.basicinformation.action.persistent.CoAction;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.role.persistence.CoRole;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractCoCpLogConfig entity provides the base persistence definition of the
 * CoCpLogConfig entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoCpLogConfig extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7750441572943250735L;
	private Long coCpLogConfigId;
	private CoUser coUser;
	private CoRole coRole;
	private CoUsecase coUsecase;
	private CoUsecaseAction coUsecaseAction;
	private CoAction coAction;
	
	private CmSystem cmSystem;
	private CmSubsystem cmSubsystem;
	private Set<CoCpLogValues> coCpLogValueses = new HashSet<CoCpLogValues>(0);

	// Constructors

	/** default constructor */
	public AbstractCoCpLogConfig() {
	}

	

	

	// Property accessors
	@Id
	@Column(name = "CO_CP_LOG_CONFIG_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoCpLogConfigId() {
		return this.coCpLogConfigId;
	}

	public void setCoCpLogConfigId(Long coCpLogConfigId) {
		this.coCpLogConfigId = coCpLogConfigId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USER_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoUser getCoUser() {
		return this.coUser;
	}

	public void setCoUser(CoUser coUser) {
		this.coUser = coUser;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_ROLE_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoRole getCoRole() {
		return this.coRole;
	}

	public void setCoRole(CoRole coRole) {
		this.coRole = coRole;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USECASE_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoUsecase getCoUsecase() {
		return this.coUsecase;
	}

	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USECASE_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoUsecaseAction getCoUsecaseAction() {
		return this.coUsecaseAction;
	}

	public void setCoUsecaseAction(CoUsecaseAction coUsecaseAction) {
		this.coUsecaseAction = coUsecaseAction;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoAction getCoAction() {
		return this.coAction;
	}

	public void setCoAction(CoAction coAction) {
		this.coAction = coAction;
	}
	@ManyToOne
	@JoinColumn(name = "CM_SYSTEM_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmSystem getCmSystem() {
		return this.cmSystem;
	}

	public void setCmSystem(CmSystem cmSystem) {
		this.cmSystem = cmSystem;
	}
	@ManyToOne
	@JoinColumn(name = "CM_SUBSYSTEM_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmSubsystem getCmSubsystem() {
		return this.cmSubsystem;
	}

	public void setCmSubsystem(CmSubsystem cmSubsystem) {
		this.cmSubsystem = cmSubsystem;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coCpLogConfig")
	public Set<CoCpLogValues> getCoCpLogValueses() {
		return this.coCpLogValueses;
	}

	public void setCoCpLogValueses(Set<CoCpLogValues> coCpLogValueses) {
		this.coCpLogValueses = coCpLogValueses;
	}

}