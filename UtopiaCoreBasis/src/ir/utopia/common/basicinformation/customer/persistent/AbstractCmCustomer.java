package ir.utopia.common.basicinformation.customer.persistent;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * AbstractBpBill entity provides the base persistence definition of the BpBill
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCmCustomer extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7925739839182937974L;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private Long cmCustomerId;
	private CmBpartner cmBpartner;
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="CustomerSequenceGenerator")
	@Column(name = "CM_CUSTOMER_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 20, scale = 0)
	public Long getCmCustomerId() {
		return cmCustomerId;
	}


	public void setCmCustomerId(Long cmCustomerId) {
		this.cmCustomerId = cmCustomerId;
	}
	
	@OneToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "CM_BPARTNER_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmBpartner getCmBpartner() {
		return this.cmBpartner;
	}

	public void setCmBpartner(CmBpartner cmBpartner) {
		this.cmBpartner = cmBpartner;
	}

	

}