package ir.utopia.core.security.userlog.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class UserLogAction extends AbstractUtopiaAction<UserLogForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5357811591534145449L;

	@Override
	protected UserLogForm createModelInstance() {
		return new UserLogForm();
	}

}
