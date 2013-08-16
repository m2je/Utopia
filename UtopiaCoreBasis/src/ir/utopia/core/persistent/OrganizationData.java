package ir.utopia.core.persistent;

import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;

public interface OrganizationData extends UtopiaBasicPersistent {
	/**
	 * 
	 * @param organization
	 */
	public void setCmOrganization(CmOrganization organization);
	/**
	 * 
	 * @return
	 */
	public CmOrganization getCmOrganization();
}
