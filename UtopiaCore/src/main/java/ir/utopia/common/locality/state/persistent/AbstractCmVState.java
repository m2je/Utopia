package ir.utopia.common.locality.state.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmState entity provides the base persistence definition of the
 * CmState entity.
 * 
 * @author Jahani
 */
@MappedSuperclass
public abstract class AbstractCmVState extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3874260852402357829L;
	private Long cmStateId;
	private String name;
	private String description;
	private String CountryName;
	private String code;

	// Constructors

	/** default constructor */
	public AbstractCmVState() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="StateSequenceGenerator")
	@Column(name = "CM_STATE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmStateId() {
		return this.cmStateId;
	}

	public void setCmStateId(Long cmStateId) {
		this.cmStateId = cmStateId;
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

	@Column(name = "COUNTRY_NAME")
	public String getCountryName() {
		return this.CountryName;
	}

	public void setCountryName(String CountryName) {
		this.CountryName = CountryName;
	}

	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}