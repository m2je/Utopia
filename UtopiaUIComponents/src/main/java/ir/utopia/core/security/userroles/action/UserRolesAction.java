package ir.utopia.core.security.userroles.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class UserRolesAction extends AbstractUtopiaAction<UserRolesForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected UserRolesForm createModelInstance() {
		return new UserRolesForm();
	}
}
