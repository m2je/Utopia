package ir.utopia.core.importer.setting.persistent;

// default package

import ir.utopia.core.importer.settinginstance.persistent.CoImporterInstance;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoImportLog entity provides the base persistence definition of the
 * CoImportLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoImportLog extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6393782746290836238L;
	private Long coImportLogId;
	private Date startTime;
	private Date endTime;
	private ImportStatus status;
	private Long recordCount;
	private Long lastImportedPk;
	private CoImporterInstance coImporterInstance;  
	

	private Long totalRecordCount;
	private String command;
	private Set<CoImportLogMsg> coImportLogMsg = new HashSet<CoImportLogMsg>(0);
	
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="ImporterLogSequenceGenerator")
	@Id
	@Column(name = "CO_IMPORT_LOG_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoImportLogId() {
		return this.coImportLogId;
	}

	public void setCoImportLogId(Long coImportLogId) {
		this.coImportLogId = coImportLogId;
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
	@Column(name = "END_TIME", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true, precision = 1, scale = 0)
	public ImportStatus getStatus() {
		return this.status;
	}

	public void setStatus(ImportStatus status) {
		this.status = status;
	}
	@Column(name="IMPORT_RECORD_COUNT")
	public Long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	@Column(name="LAST_IMPORTED_PK")
	public Long getLastImportedPk() {
		return lastImportedPk;
	}

	public void setLastImportedPk(Long lastImportedPk) {
		this.lastImportedPk = lastImportedPk;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "coImportLog",cascade=CascadeType.ALL)
	public Set<CoImportLogMsg> getCoImportLogMsg() {
		return coImportLogMsg;
	}

	public void setCoImportLogMsg(Set<CoImportLogMsg> coImportLogMsg) {
		this.coImportLogMsg = coImportLogMsg;
	}
	@Column(name="TOTAL_RECORD_COUNT")
	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	@Lob
	@Column(name="COMMAND")
	@Basic(fetch=FetchType.EAGER)
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	@ManyToOne(cascade={CascadeType.ALL})	
	@JoinColumn(name = "CO_IMPORTER_INSTANCE_ID", referencedColumnName = "CO_IMPORTER_INSTANCE_ID")
	public CoImporterInstance getCoImporterInstance() {
		return coImporterInstance;
	}

	public void setCoImporterInstance(CoImporterInstance coImporterInstance) {
		this.coImporterInstance = coImporterInstance;
	}
	
}