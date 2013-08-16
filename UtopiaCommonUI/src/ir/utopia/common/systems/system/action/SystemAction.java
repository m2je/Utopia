package ir.utopia.common.systems.system.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class SystemAction extends AbstractUtopiaAction<SystemForm> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 216372577481957923L;

	protected SystemForm createModelInstance() {
		return new SystemForm();
	}
}
