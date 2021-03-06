package ir.utopia.core.persistent;

import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractOrganizationData extends AbstractUtopiaPersistent implements OrganizationData {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6381499175807559534L;
	private CmOrganization cmOrganization;
	
	@Override
	public void setCmOrganization(CmOrganization cmOrganization) {
		this.cmOrganization=cmOrganization;
		
	}

	@Override
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "CM_ORGANIZATION_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmOrganization getCmOrganization() {
		return cmOrganization;
	}

}
