package ir.utopia.common.basicinformation.supplier.persistent;

import java.util.Date;

import ir.utopia.common.CommonConstants.SupplierType;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmCompanyBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * AbstractCmSupplier entity provides the base persistence definition of the 
 * CmSupplier entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public abstract class AbstractCmSupplier extends AbstractUtopiaPersistent implements java.io.Serializable {

	//Fields
	
	private static final long serialVersionUID = 1730528884553231698L;
	private Long cmSupplierId;
	private CmCompanyBpartner cmCompanyBpartner;
	private CmPersonBpartner cmPersonBpartner;
	private String code;
	private Date arrivalDate;
	private SupplierType supplierType;
	
	
	
	// Constructors

	/** default constructor */
	public AbstractCmSupplier() {
		
	}


	
	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="SupplierSequenceGenerator")
	@Column(name = "CM_SUPPLIER_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmSupplierId() {
		return cmSupplierId;
	}
	public void setCmSupplierId(Long cmSupplierId) {
		this.cmSupplierId = cmSupplierId;
	}


	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})	
	@JoinColumn(name = "CM_COMPANY_BPARTNER_ID", referencedColumnName = "CM_COMPANY_BPARTNER_ID")
	public CmCompanyBpartner getCmCompanyBpartner() {
		return cmCompanyBpartner;
	}
	public void setCmCompanyBpartner(CmCompanyBpartner cmCompanyBpartner) {
		this.cmCompanyBpartner = cmCompanyBpartner;
	}


	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})	
	@JoinColumn(name = "CM_PERSON_BPARTNER_ID", referencedColumnName = "CM_PERSON_BPARTNER_ID")
	public CmPersonBpartner getCmPersonBpartner() {
		return cmPersonBpartner;
	}
	public void setCmPersonBpartner(CmPersonBpartner cmPersonBpartner) {
		this.cmPersonBpartner = cmPersonBpartner;
	}


	
	@Column(name = "CODE", unique = false, nullable = false, insertable = true, updatable = true)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	
	@Temporal(TemporalType.DATE)
	@Column(name = "ARRIVAL_DATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}


	
	@Enumerated(value= EnumType.ORDINAL)
	@Column(name = "SUPPLIER_TYPE", unique = false, nullable = false, insertable = true, updatable = true, precision = 1, scale = 0)
	public SupplierType getSupplierType() {
		return supplierType;
	}
	public void setSupplierType(SupplierType supplierType) {
		this.supplierType = supplierType;
	}

}
