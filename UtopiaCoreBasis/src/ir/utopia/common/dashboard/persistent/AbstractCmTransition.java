package ir.utopia.common.dashboard.persistent;

import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.core.constants.Constants.BooleanType;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmTransition
 */
@MappedSuperclass
public abstract class AbstractCmTransition extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3102459757751912470L;
	private Long cmTransitionId;
	private CmDocStatus cmDocStatusByCmDocStatusTo;
	private CmDocStatus cmDocStatusByCmDocStatusFrom;
	private CoUsecaseAction coUsecaseAction;
	private String name;
	private String description;
	

	// Constructors

	/** default constructor */
	public AbstractCmTransition() {
	}

	
	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
	generator="TransitionSequenceGenerator")
	@Column(name = "CM_TRANSITION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmTransitionId() {
		return this.cmTransitionId;
	}

	public void setCmTransitionId(Long cmTransitionId) {
		this.cmTransitionId = cmTransitionId;
	}

	@ManyToOne(cascade = {})
	@JoinColumn(name = "CM_DOC_STATUS_TO", unique = false, nullable = false, insertable = true, updatable = true)
	public CmDocStatus getCmDocStatusByCmDocStatusTo() {
		return this.cmDocStatusByCmDocStatusTo;
	}

	public void setCmDocStatusByCmDocStatusTo(
			CmDocStatus cmDocStatusByCmDocStatusTo) {
		this.cmDocStatusByCmDocStatusTo = cmDocStatusByCmDocStatusTo;
	}

	@ManyToOne(cascade = {})
	@JoinColumn(name = "CM_DOC_STATUS_FROM", unique = false, nullable = false, insertable = true, updatable = true)
	public CmDocStatus getCmDocStatusByCmDocStatusFrom() {
		return this.cmDocStatusByCmDocStatusFrom;
	}

	public void setCmDocStatusByCmDocStatusFrom(
			CmDocStatus cmDocStatusByCmDocStatusFrom) {
		this.cmDocStatusByCmDocStatusFrom = cmDocStatusByCmDocStatusFrom;
	}
	@ManyToOne
	@JoinColumn(name = "CO_USECASE_ACTION_ID", unique = true, nullable = false, insertable = true, updatable = true)
	public CoUsecaseAction getCoUsecaseAction() {
		return this.coUsecaseAction;
	}

	public void setCoUsecaseAction(CoUsecaseAction coUsecaseAction) {
		this.coUsecaseAction = coUsecaseAction;
	}

	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 500)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true, length = 2000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	

}