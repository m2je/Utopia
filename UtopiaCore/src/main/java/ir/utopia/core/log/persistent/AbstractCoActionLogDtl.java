package ir.utopia.core.log.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;

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

/**
 * AbstractCoActionLogDtl entity provides the base persistence definition of the
 * CoActionLogDtl entity.
 * 
 * @author 
 */
@MappedSuperclass
public abstract class AbstractCoActionLogDtl extends AbstractBasicPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8193629377806642520L;
	private Long coActionLogDtlId;
	private CoActionLog coActionLog;
	private ActionLogMessageType type;
	private int extendedType;
	private String referenceKey1;
	private String referenceKey2;
	private String referenceKey3;
	private String message;

	// Constructors

	/** default constructor */
	public AbstractCoActionLogDtl() {
	}

	/** minimal constructor */
	public AbstractCoActionLogDtl(Long coActionLogDtlId,
			CoActionLog coActionLog, ActionLogMessageType type) {
		this.coActionLogDtlId = coActionLogDtlId;
		this.coActionLog = coActionLog;
		this.type = type;
	}

	/** full constructor */
	public AbstractCoActionLogDtl(Long coActionLogDtlId,
			CoActionLog coActionLog, ActionLogMessageType type, int extendedType,
			String referenceKey1, String referenceKey2, String referenceKey3,
			String message) {
		this.coActionLogDtlId = coActionLogDtlId;
		this.coActionLog = coActionLog;
		this.type = type;
		this.extendedType = extendedType;
		this.referenceKey1 = referenceKey1;
		this.referenceKey2 = referenceKey2;
		this.referenceKey3 = referenceKey3;
		this.message = message;
	}

	// Property accessors
	@GeneratedValue(strategy=GenerationType.TABLE,generator="ActionDtlLogSequenceGenerator")
	@Id
	@Column(name = "CO_ACTION_LOG_DTL_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoActionLogDtlId() {
		return this.coActionLogDtlId;
	}

	public void setCoActionLogDtlId(Long coActionLogDtlId) {
		this.coActionLogDtlId = coActionLogDtlId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_ACTION_LOG_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoActionLog getCoActionLog() {
		return this.coActionLog;
	}

	public void setCoActionLog(CoActionLog coActionLog) {
		this.coActionLog = coActionLog;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TYPE", unique = false, nullable = false, insertable = true, updatable = true, precision = 1, scale = 0)
	public ActionLogMessageType getType() {
		return this.type;
	}

	public void setType(ActionLogMessageType type) {
		this.type = type;
	}

	@Column(name = "EXTENDED_TYPE", unique = false, nullable = true, insertable = true, updatable = true, precision = 2, scale = 0)
	public int getExtendedType() {
		return this.extendedType;
	}

	public void setExtendedType(int extendedType) {
		this.extendedType = extendedType;
	}

	@Column(name = "REFERENCE_KEY_1", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getReferenceKey1() {
		return this.referenceKey1;
	}

	public void setReferenceKey1(String referenceKey1) {
		this.referenceKey1 = referenceKey1;
	}

	@Column(name = "REFERENCE_KEY_2", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getReferenceKey2() {
		return this.referenceKey2;
	}

	public void setReferenceKey2(String referenceKey2) {
		this.referenceKey2 = referenceKey2;
	}

	@Column(name = "REFERENCE_KEY_3", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getReferenceKey3() {
		return this.referenceKey3;
	}

	public void setReferenceKey3(String referenceKey3) {
		this.referenceKey3 = referenceKey3;
	}

	@Column(name = "MESSAGE", unique = false, nullable = true, insertable = true, updatable = true)
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}