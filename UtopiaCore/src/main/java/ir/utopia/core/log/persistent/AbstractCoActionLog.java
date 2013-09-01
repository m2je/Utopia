package ir.utopia.core.log.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoActionLog entity provides the base persistence definition of the
 * CoActionLog entity.
 * 
 * @author 
 */
@MappedSuperclass
public abstract class AbstractCoActionLog extends AbstractBasicPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4211464953239774249L;
	private Long coActionLogId;
	private CoUsecaseAction coUsecaseAction;
	private ActionLogger logger;
	private Date startTime;
	private Date endTime;
	private ActionLogStatus status;
	private Set<CoActionLogDtl> coActionLogDtls = new HashSet<CoActionLogDtl>(0);

	// Constructors

	/** default constructor */
	public AbstractCoActionLog() {
	}

	/** minimal constructor */


	// Property accessors
	@GeneratedValue(strategy=GenerationType.TABLE,generator="ActionLogSequenceGenerator")
	@Id
	@Column(name = "CO_ACTION_LOG_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoActionLogId() {
		return this.coActionLogId;
	}

	public void setCoActionLogId(Long coActionLogId) {
		this.coActionLogId = coActionLogId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USECASE_ACTION_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUsecaseAction getCoUsecaseAction() {
		return this.coUsecaseAction;
	}

	public void setCoUsecaseAction(CoUsecaseAction coUsecaseAction) {
		this.coUsecaseAction = coUsecaseAction;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "LOGGER")
	public ActionLogger getLogger() {
		return this.logger;
	}

	public void setLogger(ActionLogger logger) {
		this.logger = logger;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATUS")
	public ActionLogStatus getStatus() {
		return this.status;
	}

	public void setStatus(ActionLogStatus status) {
		this.status = status;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coActionLog")
	public Set<CoActionLogDtl> getCoActionLogDtls() {
		return this.coActionLogDtls;
	}

	public void setCoActionLogDtls(Set<CoActionLogDtl> coActionLogDtls) {
		this.coActionLogDtls = coActionLogDtls;
	}

}