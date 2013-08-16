package ir.utopia.common.basicinformation.bank.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class BankAction extends AbstractUtopiaAction<BankForm> {

	
	private static final long serialVersionUID = 8526198961761413518L;

	
	protected BankForm createModelInstance() {
		
		return new BankForm();
		
	}
}
