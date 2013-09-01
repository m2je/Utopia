package ir.utopia.common.locality.currency.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class CurrencyAction extends AbstractUtopiaAction<CurrencyForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected CurrencyForm createModelInstance() {
		return new CurrencyForm();
	}
}
