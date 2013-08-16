package ir.utopia.core.usecase.usecase.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoVUsecase entity provides the base persistence definition of the
 * CoVUsecase entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVUsecase extends AbstractUtopiaPersistent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1158592897797212121L;
	private String systemName;
	private Long coUsecaseId;
	private String name;
	private String uscsRemoteClass;
	public AbstractCoVUsecase(){
		
	}
	private String subSystemName;
	@Column(name = "systemName")
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	@Column(name = "subSystemName")
	public String getSubSystemName() {
		return subSystemName;
	}

	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}
	@Id
	@Column(name = "CO_USECASE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUsecaseId() {
		return this.coUsecaseId;
	}

	public void setCoUsecaseId(Long coUsecaseId) {
		this.coUsecaseId = coUsecaseId;
	}

	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USCS_REMOTE_CLASS", unique = false, nullable = false, insertable = true, updatable = true, length = 2000)
	public String getUscsRemoteClass() {
		return this.uscsRemoteClass;
	}
	public void setUscsRemoteClass(String uscsRemoteClass) {
		 this.uscsRemoteClass=uscsRemoteClass;
	}
	

}