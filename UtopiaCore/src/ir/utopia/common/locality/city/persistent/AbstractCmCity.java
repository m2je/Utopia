package ir.utopia.common.locality.city.persistent;

import ir.utopia.common.locality.country.persistent.CmCountry;
import ir.utopia.common.locality.region.persistent.CmRegion;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmCity entity provides the base persistence definition of the CmCity
 * entity.
 * 
 * @author JAHANI
 */
@MappedSuperclass
public abstract class AbstractCmCity extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5389682100603380363L;
	private Long cmCityId;
	private String name;
	private String areacode;
	private CmCountry cmCountry;
	private CmRegion cmRegion;
	private String code;
	private String description;

	// Constructors

	/** default constructor */
	public AbstractCmCity() {
	}
	

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="CitySequenceGenerator")
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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_COUNTRY_ID", referencedColumnName = "CM_COUNTRY_ID" )
	public CmCountry getCmCountry() {
		return this.cmCountry;
	}

	public void setCmCountry(CmCountry cmCountry) {
		this.cmCountry = cmCountry;
	}


	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_REGION_ID", referencedColumnName = "CM_REGION_ID" )
	public CmRegion getCmRegion() {
		return this.cmRegion;
	}

	public void setCmRegion(CmRegion cmRegion) {
		this.cmRegion = cmRegion;
	}


}