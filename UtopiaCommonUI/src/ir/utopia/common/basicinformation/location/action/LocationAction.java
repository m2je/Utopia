package ir.utopia.common.basicinformation.location.action;


import ir.utopia.core.struts.AbstractUtopiaAction;

public class LocationAction extends AbstractUtopiaAction<LocationForm> {
	
	private static final long serialVersionUID = 1321497408163047671L;

	
	@Override
	public String execute() throws Exception {
		
		return super.execute();
	}
	
	@Override
	protected LocationForm createModelInstance() {
		
		return new LocationForm();
		
	}

}
