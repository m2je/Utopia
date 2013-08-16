package ir.utopia.common.locality.region.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class RegionAction extends AbstractUtopiaAction<RegionForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected RegionForm createModelInstance() {
		return new RegionForm();
	}
}
