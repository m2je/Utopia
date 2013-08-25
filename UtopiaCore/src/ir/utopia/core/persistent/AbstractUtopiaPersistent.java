package ir.utopia.core.persistent;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.IsActive;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class AbstractUtopiaPersistent extends SoftDeletePersistentSupport implements UtopiaPersistent,Serializable {
	/**
	 * 
	 */
	public static final String IS_ACTIVE_FIELD_NAME = "isactive";
	private static final long serialVersionUID = -5376839244673906173L;
	private Date created;
	private Long createdby;
	private Date updated;
	private Long updatedby;
	
	//************************************************************************************************	
	public AbstractUtopiaPersistent(){
	}
//************************************************************************************************	
	public AbstractUtopiaPersistent(Date created, Long createdby, Date updated,
			Long updatedby, IsActive isactive) {
		this.created = created;
		this.createdby = createdby;
		this.updated = updated;
		this.updatedby = updatedby;
		this.isactive = isactive;
	}
//************************************************************************************************
	public AbstractUtopiaPersistent(Date created, Long createdby, Date updated,
			Long updatedby, int isactive) {
		this.created = created;
		this.createdby = createdby;
		this.updated = updated;
		this.updatedby = updatedby;
		this.isactive = isactive==1?Constants.IsActive.active:Constants.IsActive.disActive;
	}
//************************************************************************************************	
	public AbstractUtopiaPersistent(Date created, Long createdby, Date updated,
			Long updatedby, long isactive) {
		this(created,createdby,updated,updatedby,(int)isactive);
		
	}
//************************************************************************************************	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getCreated() {
		return this.created;
	}
//************************************************************************************************
	public void setCreated(Date created) {
		this.created = created;
	}
//************************************************************************************************
	@Column(name = "CREATEDBY", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCreatedby() {
		return this.createdby;
	}
//************************************************************************************************
	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}
//***********************************************************************************************
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getUpdated() {
		return this.updated;
	}
//************************************************************************************************
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
//************************************************************************************************
	@Column(name = "UPDATEDBY", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getUpdatedby() {
		return this.updatedby;
	}
//************************************************************************************************
	public void setUpdatedby(Long updatedby) {
		this.updatedby = updatedby;
	}


	
}
