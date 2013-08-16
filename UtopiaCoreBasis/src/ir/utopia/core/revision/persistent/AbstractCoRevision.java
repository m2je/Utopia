package ir.utopia.core.revision.persistent;


import ir.utopia.core.persistent.AbstractBasicPersistent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoRevision 
 */
@MappedSuperclass
public abstract class AbstractCoRevision extends AbstractBasicPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5925430117500441033L;
	private Long coRevisionId;
	private Long coUsecaseId;
	private Long persistentRecordId;
	private Long createdby;
	private Date created;
	private String description;
	private String serializedObject;

	// Constructors

	/** default constructor */
	public AbstractCoRevision() {
	}

	

	// Property accessors
	@Id
	@Column(name = "CO_REVISION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	@GeneratedValue(strategy=GenerationType.TABLE,
	generator="RevisionSequenceGenerator")
	public Long getCoRevisionId() {
		return this.coRevisionId;
	}

	public void setCoRevisionId(Long coRevisionId) {
		this.coRevisionId = coRevisionId;
	}

	@Column(name = "CO_USECASE_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUsecaseId() {
		return this.coUsecaseId;
	}

	public void setCoUsecaseId(Long coUsecaseId) {
		this.coUsecaseId = coUsecaseId;
	}


	@Column(name = "CREATEDBY", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCreatedby() {
		return this.createdby;
	}


	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}

	
	@Column(name = "RECORD_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getPersistentRecordId() {
		return persistentRecordId;
	}

	
	public void setPersistentRecordId(Long persistentRecordId) {
		this.persistentRecordId = persistentRecordId;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true, length = 3000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "SERIALIZED_OBJECT", unique = false, nullable = false, insertable = true, updatable = true)
	public String getSerializedObject() {
		return this.serializedObject;
	}

	public void setSerializedObject(String serializedObject) {
		this.serializedObject = serializedObject;
	}

}