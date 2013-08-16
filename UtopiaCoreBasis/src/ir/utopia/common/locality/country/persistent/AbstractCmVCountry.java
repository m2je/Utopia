package ir.utopia.common.locality.country.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmCountry entity provides the base persistence definition of the
 * CmCountry entity.
 * 
 * @author Jahani
 */
@MappedSuperclass
public abstract class AbstractCmVCountry extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1126499293045438134L;
	private Long cmCountryId;
	private String name;
	private String description;
	private String currencyName;
	private String code;

	// Constructors

	/** default constructor */
	public AbstractCmVCountry() {
	}

	// Property accessors
	@Id
	@Column(name = "CM_COUNTRY_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmCountryId() {
		return this.cmCountryId;
	}

	public void setCmCountryId(Long cmCountryId) {
		this.cmCountryId = cmCountryId;
	}


	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
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

	@Column(name = "CURRENCY_NAME")
	public String getCurrencyName() {
		return this.currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}