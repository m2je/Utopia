package ir.utopia.common.locality.currency.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmCurrency entity provides the base persistence definition of the
 * CmCurrency entity.
 * 
 * @author Jahani
 */
@MappedSuperclass
public abstract class AbstractCmVCurrency extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8118732073349121000L;
	private Long cmCurrencyId;
	private String description;
	private String isoCode;
	private String symbol;
	private String name;

	// Constructors

	/** default constructor */
	public AbstractCmVCurrency() {
	}

	// Property accessors
	@Id
	@Column(name = "CM_CURRENCY_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmCurrencyId() {
		return this.cmCurrencyId;
	}

	public void setCmCurrencyId(Long cmCurrencyId) {
		this.cmCurrencyId = cmCurrencyId;
	}


	@Column(name = "DESCRIPTION", unique = false, nullable = false, insertable = true, updatable = true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "ISO_CODE", unique = false, nullable = false, insertable = true, updatable = true, length = 3)
	public String getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	@Column(name = "SYMBOL", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}