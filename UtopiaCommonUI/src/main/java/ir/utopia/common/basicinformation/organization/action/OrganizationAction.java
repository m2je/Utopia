package ir.utopia.common.basicinformation.organization.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class OrganizationAction extends AbstractUtopiaAction<OrganizationForm> {


	
	private static final long serialVersionUID = 8193214439239146562L;

	protected OrganizationForm createModelInstance() {
		
		return new OrganizationForm();
		
	}
}
