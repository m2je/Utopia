package ir.utopia.common.dashboard.persistent;

import ir.utopia.core.persistent.SoftDeletePersistentSupport;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 */
@MappedSuperclass
public abstract class AbstractCmTransitionHistory extends SoftDeletePersistentSupport implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4632554784386788116L;
	private Long cmTransitionHistoryId;
	private CmTransition cmTransition;
	private Long recordId;
	private CoUser coUser;
	private CoUsecaseAction coUsecaseAction;
	private Date actionDate;
	private CoUsecase coUsecase;

	// Constructors

	/** default constructor */
	public AbstractCmTransitionHistory() {
	}


	// Property accessors
	@Id
	@Column(name = "CM_TRANSITION_HISTORY_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	@GeneratedValue(strategy=GenerationType.TABLE,
	generator="TransitionHistorySequenceGenerator")
	public Long getCmTransitionHistoryId() {
		return this.cmTransitionHistoryId;
	}

	public void setCmTransitionHistoryId(Long cmTransitionHistoryId) {
		this.cmTransitionHistoryId = cmTransitionHistoryId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_TRANSITION_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmTransition getCmTransition() {
		return this.cmTransition;
	}

	public void setCmTransition(CmTransition cmTransition) {
		this.cmTransition = cmTransition;
	}

	@Column(name = "RECORD_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getRecordId() {
		return this.recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	

	@ManyToOne
	@JoinColumn(name = "CO_USECASE_ACTION_ID", unique = true, nullable = false, insertable = true, updatable = true)
	public CoUsecaseAction getCoUsecaseAction() {
		return this.coUsecaseAction;
	}

	public void setCoUsecaseAction(CoUsecaseAction coUsecaseAction) {
		this.coUsecaseAction = coUsecaseAction;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTION_DATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getActionDate() {
		return this.actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USECASE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUsecase getCoUsecase() {
		return coUsecase;
	}

	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}
	
	@ManyToOne
	@JoinColumn(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true)
	
	public CoUser getCoUser() {
		return this.coUser;
	}

	public void setCoUser(CoUser coUser) {
		this.coUser = coUser;
	}
}