package ir.utopia.core.importer.setting.persistent;

// default package

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoImportLogMsg entity provides the base persistence definition of the
 * CoImportLogMsg entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoImportLogMsg extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 189119056341128138L;
	private Long coImportLogMsgId;
	private CoImportLog coImportLog;
	private int type;
	private String referenceKey1;
	private String referenceKey2;
	private String referenceKey3;
	
	private String msg;
	

	// Constructors

	/** default constructor */
	public AbstractCoImportLogMsg() {
	}

	

	// Property accessors
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="ImporterLogMessageSequenceGenerator")
	@Id
	@Column(name = "CO_IMPORT_LOG_MSG_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoImportLogMsgId() {
		return this.coImportLogMsgId;
	}

	public void setCoImportLogMsgId(Long coImportLogMsgId) {
		this.coImportLogMsgId = coImportLogMsgId;
	}
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "CO_IMPORT_LOG_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoImportLog getCoImportLog() {
		return this.coImportLog;
	}

	public void setCoImportLog(CoImportLog coImportLog) {
		this.coImportLog = coImportLog;
	}

	

	@Column(name = "MSG", unique = false, nullable = false, insertable = true, updatable = true, length = 3000)
	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	@Column(name="type")
	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}


	@Column(name="REFERENCE_KEY_1")
	public String getReferenceKey1() {
		return referenceKey1;
	}



	public void setReferenceKey1(String referenceKey1) {
		this.referenceKey1 = referenceKey1;
	}


	@Column(name="REFERENCE_KEY_2")
	public String getReferenceKey2() {
		return referenceKey2;
	}



	public void setReferenceKey2(String referenceKey2) {
		this.referenceKey2 = referenceKey2;
	}


	@Column(name="REFERENCE_KEY_3")
	public String getReferenceKey3() {
		return referenceKey3;
	}



	public void setReferenceKey3(String referenceKey3) {
		this.referenceKey3 = referenceKey3;
	}

	

}