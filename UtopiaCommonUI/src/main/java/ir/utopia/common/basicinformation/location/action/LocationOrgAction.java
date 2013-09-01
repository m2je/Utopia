package ir.utopia.common.basicinformation.location.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class LocationOrgAction extends AbstractUtopiaAction<LocationOrgForm> {

	private static final long serialVersionUID = -5872895959553833092L;

	@Override
	public String execute() throws Exception {
		
		return super.execute();
	}
	
	@Override
	protected LocationOrgForm createModelInstance() {
		return new LocationOrgForm();
	}

}
