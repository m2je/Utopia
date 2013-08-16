package ir.utopia.common.locality.province.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class ProvinceAction extends AbstractUtopiaAction<ProvinceForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected ProvinceForm createModelInstance() {
		return new ProvinceForm();
	}
}
