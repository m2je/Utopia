package ir.utopia.common.doctype.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractCmDoctypeDimension 
 */
@MappedSuperclass
public abstract class AbstractCmDoctypeDimension extends AbstractUtopiaPersistent implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5254956782079116368L;
	private Long cmDoctypeDimensionId;
	private CmDoctypeDimension cmDoctypeDimension;
	private CmDoctype cmDoctype;
	
	private Set<CmDoctypeDimension> cmDoctypeDimensions = new HashSet<CmDoctypeDimension>(
			0);

	// Constructors

	/** default constructor */
	public AbstractCmDoctypeDimension() {
	}

	
	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
	generator="DocTypeDimensionSequenceGenerator")
	@Column(name = "CM_DOCTYPE_DIMENSION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmDoctypeDimensionId() {
		return this.cmDoctypeDimensionId;
	}

	public void setCmDoctypeDimensionId(Long cmDoctypeDimensionId) {
		this.cmDoctypeDimensionId = cmDoctypeDimensionId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_SYSTEM_DIMENSION_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmDoctypeDimension getCmDoctypeDimension() {
		return this.cmDoctypeDimension;
	}

	public void setCmDoctypeDimension(CmDoctypeDimension cmDoctypeDimension) {
		this.cmDoctypeDimension = cmDoctypeDimension;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_DOCTYPE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmDoctype getCmDoctype() {
		return this.cmDoctype;
	}

	public void setCmDoctype(CmDoctype cmDoctype) {
		this.cmDoctype = cmDoctype;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "cmDoctypeDimension")
	public Set<CmDoctypeDimension> getCmDoctypeDimensions() {
		return this.cmDoctypeDimensions;
	}

	public void setCmDoctypeDimensions(
			Set<CmDoctypeDimension> cmDoctypeDimensions) {
		this.cmDoctypeDimensions = cmDoctypeDimensions;
	}

}