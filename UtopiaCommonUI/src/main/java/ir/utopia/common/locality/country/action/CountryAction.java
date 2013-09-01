package ir.utopia.common.locality.country.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class CountryAction extends AbstractUtopiaAction<CountryForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected CountryForm createModelInstance() {
		return new CountryForm();
	}
}
