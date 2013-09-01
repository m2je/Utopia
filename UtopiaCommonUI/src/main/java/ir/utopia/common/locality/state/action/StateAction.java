package ir.utopia.common.locality.state.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class StateAction extends AbstractUtopiaAction<StateForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected StateForm createModelInstance() {
		return new StateForm();
	}
}
