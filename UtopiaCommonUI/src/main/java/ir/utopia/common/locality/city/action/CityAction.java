package ir.utopia.common.locality.city.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class CityAction extends AbstractUtopiaAction<CityForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected CityForm createModelInstance() {
		return new CityForm();
	}
}
