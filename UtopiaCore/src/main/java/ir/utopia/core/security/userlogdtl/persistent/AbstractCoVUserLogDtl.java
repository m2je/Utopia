package ir.utopia.core.security.userlogdtl.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;
import ir.utopia.core.security.userlog.persistent.CoVUserLog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoVUserLogDtl entity provides the base persistence definition of the
 * CoVUserLogDtl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVUserLogDtl extends AbstractBasicPersistent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4099846443321222169L;
	private CoVUserLog coUserLog;
	private Long coUserId;
	private Long coUserLogDtlId;
	private LogActionStatus status=LogActionStatus.success;
	private String systemname;
	private String subsystemname;
	private String usecasename;
	private String actionName;
	private Long coUsecaseActionId;
	private Date actionDate;

	// Constructors

	// Property accessors
	@Id
	@Column(name="CO_USER_LOG_DTL_ID")
	public Long getCoUserLogDtlId() {
		return this.coUserLogDtlId;
	}

	public void setCoUserLogDtlId(Long coUserLogDtlId) {
		this.coUserLogDtlId = coUserLogDtlId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USER_LOG_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoVUserLog getCoUserLog() {
		return this.coUserLog;
	}

	public void setCoUserLog(CoVUserLog coUserLog) {
		this.coUserLog = coUserLog;
	}

	@Column(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserId() {
		return this.coUserId;
	}

	public void setCoUserId(Long coUserId) {
		this.coUserId = coUserId;
	}

	

	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public LogActionStatus getStatus() {
		return this.status;
	}

	public void setStatus(LogActionStatus status) {
		this.status = status;
	}

	@Column(name = "SYSTEMNAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getSystemname() {
		return this.systemname;
	}

	public void setSystemname(String systemname) {
		this.systemname = systemname;
	}

	@Column(name = "SUBSYSTEMNAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getSubsystemname() {
		return this.subsystemname;
	}

	public void setSubsystemname(String subsystemname) {
		this.subsystemname = subsystemname;
	}

	@Column(name = "USECASENAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getUsecasename() {
		return this.usecasename;
	}

	public void setUsecasename(String usecasename) {
		this.usecasename = usecasename;
	}

	@Column(name = "ACTION_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Column(name = "CO_USECASE_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUsecaseActionId() {
		return this.coUsecaseActionId;
	}

	public void setCoUsecaseActionId(Long coUsecaseActionId) {
		this.coUsecaseActionId = coUsecaseActionId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTION_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getActionDate() {
		return this.actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}


}