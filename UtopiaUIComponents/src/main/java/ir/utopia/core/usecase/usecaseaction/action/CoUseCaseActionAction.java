package ir.utopia.core.usecase.usecaseaction.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

/**
 * @author Salarkia
 *
 */
public class CoUseCaseActionAction extends AbstractUtopiaAction<CoUseCaseActionForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7887397458696741690L;

	@Override
	protected CoUseCaseActionForm createModelInstance() {
		return new CoUseCaseActionForm();
	}

	
	

}
