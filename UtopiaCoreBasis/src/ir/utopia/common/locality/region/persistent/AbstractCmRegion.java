package ir.utopia.common.locality.region.persistent;

import ir.utopia.common.locality.country.persistent.CmCountry;
import ir.utopia.common.locality.province.persistent.CmProvince;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmRegion entity provides the base persistence definition of the
 * CmRegion entity.
 * 
 * @author Jahani
 */
@MappedSuperclass
public abstract class AbstractCmRegion extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7083306791975034360L;
	private Long cmRegionId;
	private String name;
	private String description;
	private CmCountry cmCountry;
	private CmProvince cmProvince;
	private String code;

	// Constructors

	/** default constructor */
	public AbstractCmRegion() {
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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_COUNTRY_ID", referencedColumnName = "CM_COUNTRY_ID" )
	public CmCountry getCmCountry() {
		return this.cmCountry;
	}

	public void setCmCountry(CmCountry cmCountry) {
		this.cmCountry = cmCountry;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_PROVINCE_ID", referencedColumnName = "CM_PROVINCE_ID" )
		public CmProvince getCmProvince() {
		return this.cmProvince;
	}

	public void setCmProvince(CmProvince cmProvince) {
		this.cmProvince = cmProvince;
	}

	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}