package ir.utopia.common.basicinformation.jobtitle.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmJobTitle entity provides the base persistence definition of the
 * CmJobTitle entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public abstract class AbstractCmJobTitle extends AbstractUtopiaPersistent implements java.io.Serializable  {

	// Fields
	private static final long serialVersionUID = 8706304554315871965L;
	private Long cmJobTitleId;
	private String code;
	private String name;
	private String description;
	
	
	// Constructors
	/** default constructor */
	public AbstractCmJobTitle() {
	}

	
	// Property accessors

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="JobTitleSequenceGenerator")
	@Column(name = "CM_JOBTITLE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmJobTitleId() {
		return cmJobTitleId;
	}
	public void setCmJobTitleId(Long cmJobTitleId) {
		this.cmJobTitleId = cmJobTitleId;
	}


	
	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	
	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@Column(name = "DESCRIPTION", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
