package ir.utopia.common.basicinformation.businesspartner.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class BusinesPartnerAction extends AbstractUtopiaAction<BusinessParnerForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9170801878823832671L;
	
	@Override
	public String execute() throws Exception {
		
		return super.execute();
	}

	@Override
	protected BusinessParnerForm createModelInstance() {
		
		return new BusinessParnerForm();
	}

	

}
