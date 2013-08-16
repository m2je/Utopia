package ir.utopia.common.basicinformation.employeesignature.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class EmployeeSignatureAction extends AbstractUtopiaAction<EmployeeSignatureForm> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8127796818591040412L;

	@Override
	public String execute() throws Exception {
		
		return super.execute();
	}
	
	@Override
	protected EmployeeSignatureForm createModelInstance() {
		
		return new EmployeeSignatureForm();
		
	}
}
