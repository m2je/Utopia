package ir.utopia.common.locality.province.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmProvince entity provides the base persistence definition of the
 * CmProvince entity.
 * 
 * @author Jahani
 */
@MappedSuperclass
public abstract class AbstractCmVProvince extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1627911479633948887L;
	private Long cmProvinceId;
	private String name;
	private String description;
	private String stateName;
	private String code;

	// Constructors

	/** default constructor */
	public AbstractCmVProvince() {
	}

	// Property accessors
	@Id
	@Column(name = "CM_PROVINCE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmProvinceId() {
		return this.cmProvinceId;
	}

	public void setCmProvinceId(Long cmProvinceId) {
		this.cmProvinceId = cmProvinceId;
	}


	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 66)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "STATE_NAME")
	public String getStateName() {
		return this.stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}