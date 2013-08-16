package ir.utopia.common.locality.city.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmCity entity provides the base persistence definition of the CmCity
 * entity.
 * 
 * @author JAHANI
 */
@MappedSuperclass
public abstract class AbstractCmVCity extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -9003482842706790677L;
	private Long cmCityId;
	private String name;
	private String areacode;
	private String code;
	private String description;
	private String countryName;
	private String regionName;

	// Constructors

	/** default constructor */
	public AbstractCmVCity() {
	}


	// Property accessors
	@Id
	@Column(name = "CM_CITY_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmCityId() {
		return this.cmCityId;
	}

	public void setCmCityId(Long cmCityId) {
		this.cmCityId = cmCityId;
	}

	
	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AREACODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getAreacode() {
		return this.areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}


	@Column(name = "COUNTRY_NAME")
	public String getCountryName() {
		return countryName;
	}


	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}



	@Column(name = "REGION_NAME")
	public String getRegionName() {
		return this.regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}