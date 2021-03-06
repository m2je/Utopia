package ir.utopia.common.locality.province.persistent;

import ir.utopia.common.locality.state.persistent.CmState;
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
 * AbstractCmProvince entity provides the base persistence definition of the
 * CmProvince entity.
 * 
 * @author Jahani
 */
@MappedSuperclass
public abstract class AbstractCmProvince extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5303357894586637161L;
	private Long cmProvinceId;
	private String name;
	private String description;
	private CmState cmState;
	private String code;

	// Constructors

	/** default constructor */
	public AbstractCmProvince() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="ProvinceSequenceGenerator")
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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_STATE_ID", referencedColumnName = "CM_STATE_ID" )
	public CmState getCmState() {
		return this.cmState;
	}

	public void setCmState(CmState cmState) {
		this.cmState = cmState;
	}

	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}