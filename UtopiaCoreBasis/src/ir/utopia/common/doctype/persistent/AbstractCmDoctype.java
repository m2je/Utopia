package ir.utopia.common.doctype.persistent;

import ir.utopia.common.basicinformation.fiscalyear.persistent.CmFiscalyear;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

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
 * AbstractCmDoctype 
 */
@MappedSuperclass
public abstract class AbstractCmDoctype extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8313836838224890091L;
	private Long cmDoctypeId;
	private String name;
	private CoUsecaseAction coUsecaseAction;
	private CmFiscalyear cmFiscalyear;
	private String code;
	private CoUsecase coUsecase;
	private Set<CmDocStatus> cmDocStatuses = new HashSet<CmDocStatus>(0);
	private Set<CmDoctypeDimension> cmDoctypeDimensions = new HashSet<CmDoctypeDimension>(
			0);

	// Constructors

	/** default constructor */
	public AbstractCmDoctype() {
	}

		// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
	generator="DocTypeSequenceGenerator")
	@Column(name = "CM_DOCTYPE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmDoctypeId() {
		return this.cmDoctypeId;
	}

	public void setCmDoctypeId(Long cmDoctypeId) {
		this.cmDoctypeId = cmDoctypeId;
	}

	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "CM_FISCALYEAR_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmFiscalyear getCmFiscalyear() {
		return this.cmFiscalyear;
	}
	
	public void setCmFiscalyear(CmFiscalyear cmFiscalyear) {
		this.cmFiscalyear = cmFiscalyear;
	}
	
	@ManyToOne
	@JoinColumn(name = "CO_USECASE_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoUsecaseAction getCoUsecaseAction() {
		return coUsecaseAction;
	}

	public void setCoUsecaseAction(CoUsecaseAction coUsecaseAction) {
		this.coUsecaseAction = coUsecaseAction;
	}

	

	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ManyToOne
	@JoinColumn(name = "CO_USECASE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUsecase getCoUsecase() {
		return this.coUsecase;
	}

	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "cmDoctype")
	public Set<CmDocStatus> getCmDocStatuses() {
		return this.cmDocStatuses;
	}

	public void setCmDocStatuses(Set<CmDocStatus> cmDocStatuses) {
		this.cmDocStatuses = cmDocStatuses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "cmDoctype")
	public Set<CmDoctypeDimension> getCmDoctypeDimensions() {
		return this.cmDoctypeDimensions;
	}

	public void setCmDoctypeDimensions(
			Set<CmDoctypeDimension> cmDoctypeDimensions) {
		this.cmDoctypeDimensions = cmDoctypeDimensions;
	}

}