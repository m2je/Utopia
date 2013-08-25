package ir.utopia.core.security.userlogdtl.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;
import ir.utopia.core.security.userlog.persistent.CoUserLog;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoUserLogDtl entity provides the base persistence definition of the
 * CoUserLogDtl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoUserLogDtl extends AbstractBasicPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8886772381601677160L;
	private Long coUserLogDtlId;
	private CoUsecaseAction coUsecaseAction;
	private CoUserLog coUserLog;
	private String actionName;
	private LogActionStatus status=LogActionStatus.success;
	private Date actionDate;
	// Constructors

	/** default constructor */
	public AbstractCoUserLogDtl() {
	}

	

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="UserSequenceLogDTLGenerator")
	@Column(name = "CO_USER_LOG_DTL_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserLogDtlId() {
		return this.coUserLogDtlId;
	}

	public void setCoUserLogDtlId(Long coUserLogDtlId) {
		this.coUserLogDtlId = coUserLogDtlId;
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
	@JoinColumn(name = "CO_USER_LOG_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUserLog getCoUserLog() {
		return this.coUserLog;
	}

	public void setCoUserLog(CoUserLog coUserLog) {
		this.coUserLog = coUserLog;
	}

	@Column(name = "ACTION_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public LogActionStatus getStatus() {
		return this.status;
	}

	public void setStatus(LogActionStatus status) {
		this.status = status;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTION_DATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

}