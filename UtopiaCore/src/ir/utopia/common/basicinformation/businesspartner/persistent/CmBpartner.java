package ir.utopia.common.basicinformation.businesspartner.persistent;

import ir.utopia.common.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

/**
 * CmBpartner entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CM_BPARTNER")
@TableGenerator(name = "CmBpartnerSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_BPARTNER")
public class CmBpartner extends AbstractCmBpartner  {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -5396081736918825168L;

	/** default constructor */
	public CmBpartner() {
	}
	@Transient
	public CommonConstants.BusinessPartnerType getPartnerType(){
//		Set<CmCompanyBpartner>companies= getCmCompanyBpartner();
//		return companies==null||companies.size()==0?CommonConstants.BusinessPartnerType.person:
//			CommonConstants.BusinessPartnerType.company;
		return CommonConstants.BusinessPartnerType.person;
	} 
	

}
