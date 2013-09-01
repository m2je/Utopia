package ir.utopia.common.systems.subsystem.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class SubSystemAction extends AbstractUtopiaAction<SubSystemForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected SubSystemForm createModelInstance() {
		return new SubSystemForm();
	}
}
