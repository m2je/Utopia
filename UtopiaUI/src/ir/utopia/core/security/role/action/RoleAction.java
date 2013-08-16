package ir.utopia.core.security.role.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class RoleAction extends AbstractUtopiaAction<RoleForm> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7600786768598695263L;

	protected RoleForm createModelInstance() {
		return new RoleForm();
	}
}
