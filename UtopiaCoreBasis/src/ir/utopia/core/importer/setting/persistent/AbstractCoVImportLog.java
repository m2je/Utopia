package ir.utopia.core.importer.setting.persistent;

// default package

import ir.utopia.core.constants.Constants.BooleanType;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoVImportLog entity provides the base persistence definition of the
 * CoVImportLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVImportLog extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

/**
	 * 
	 */
	private static final long serialVersionUID = 5424112266210008173L;

private Long coImportLogId;
	
	private Date startTime;
	private Date endTime;
	private BooleanType status;
	private Long lastImportedPk;
	private Long recordCount;
	private Long coImporterSettingId;
	private String name;
	
	private Long totalRecordCount;
	
	// Constructors

	

	

	

	// Property accessors
	@Id
	@Column(name = "CO_IMPORT_LOG_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
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
	public BooleanType getStatus() {
		return this.status;
	}

	public void setStatus(BooleanType status) {
		this.status = status;
	}

	@Column(name = "LAST_IMPORTED_PK", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 0)
	public Long getLastImportedPk() {
		return this.lastImportedPk;
	}

	public void setLastImportedPk(Long lastImportedPk) {
		this.lastImportedPk = lastImportedPk;
	}

	@Column(name = "IMPORT_RECORD_COUNT", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 0)
	public Long getRecordCount() {
		return this.recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}

	@Column(name = "CO_IMPORTER_SETTING_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoImporterSettingId() {
		return this.coImporterSettingId;
	}

	public void setCoImporterSettingId(Long coImporterSettingId) {
		this.coImporterSettingId = coImporterSettingId;
	}

	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name="TOTAL_RECORD_COUNT")
	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	
}