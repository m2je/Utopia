package ir.utopia.common.locality.region.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmRegion entity provides the base persistence definition of the
 * CmRegion entity.
 * 
 * @author Jahani
 */
@MappedSuperclass
public abstract class AbstractCmVRegion extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6511192883503547105L;
	private Long cmRegionId;
	private String name;
	private String description;
	private String countryName;
	private String provinceName;
	private String code;

	// Constructors

	/** default constructor */
	public AbstractCmVRegion() {
	}


	// Property accessors
	@Id
	@Column(name = "CM_REGION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmRegionId() {
		return this.cmRegionId;
	}

	public void setCmRegionId(Long cmRegionId) {
		this.cmRegionId = cmRegionId;
	}


	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
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

	@Column(name = "COUNTRY_NAME")
	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Column(name = "PROVINCE_NAME")
	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}