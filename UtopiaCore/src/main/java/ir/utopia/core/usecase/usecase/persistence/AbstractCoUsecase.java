package ir.utopia.core.usecase.usecase.persistence;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractCoUsecase entity provides the base persistence definition of the
 * CoUsecase entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoUsecase extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3178644408009107112L;
	private Long coUsecaseId;
	private String name;
	private String uscsRemoteClass;
	private CmSubsystem cmSubsystem;
	private Set<CoUsecaseAction> coUsecaseActions = new HashSet<CoUsecaseAction>(
			0);
	
	// Constructors

	/** default constructor */
	public AbstractCoUsecase() {
	}

	/** minimal constructor */
	public AbstractCoUsecase(Long coUsecaseId, String uscsRemoteClass) {
		this.coUsecaseId = coUsecaseId;
		this.uscsRemoteClass = uscsRemoteClass;
	}

	/** full constructor */
	public AbstractCoUsecase(Long coUsecaseId, String name, Date created,
			Long createdby, Date updated, Long updatedby,
			String uscsRemoteClass, Long isactive) {
		super(created,createdby,updated,updatedby,isactive.intValue());
		this.coUsecaseId = coUsecaseId;
		this.name = name;
		this.uscsRemoteClass = uscsRemoteClass;

	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="CoUsecaseSequenceGenerator")
	@Column(name = "CO_USECASE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUsecaseId() {
		return this.coUsecaseId;
	}

	public void setCoUsecaseId(Long coUsecaseId) {
		this.coUsecaseId = coUsecaseId;
	}

	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USCS_REMOTE_CLASS", unique = false, nullable = false, insertable = true, updatable = true, length = 2000)
	public String getUscsRemoteClass() {
		return this.uscsRemoteClass;
	}

	public void setUscsRemoteClass(String uscsRemoteClass) {
		this.uscsRemoteClass = uscsRemoteClass;
	}
	@ManyToOne	
	@JoinColumn(name = "CM_SUBSYSTEM_ID", referencedColumnName = "CM_SUBSYSTEM_ID" )
	public CmSubsystem getCmSubsystem() {
		return cmSubsystem;
	}
	public void setCmSubsystem(CmSubsystem cmSubsystem) {
		this.cmSubsystem = cmSubsystem;
	}
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coUsecase")
	public Set<CoUsecaseAction> getCoUsecaseActions() {
		return this.coUsecaseActions;
	}

	public void setCoUsecaseActions(Set<CoUsecaseAction> coUsecaseActions) {
		this.coUsecaseActions = coUsecaseActions;
	}
	


	
	 
	
	

}