package ir.utopia.common.basicinformation.branch.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class BranchAction extends AbstractUtopiaAction<BranchForm> {

	private static final long serialVersionUID = -613949831457412093L;

	@Override
	protected BranchForm createModelInstance() {
		return new BranchForm();
	}

}
