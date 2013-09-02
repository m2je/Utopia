package ir.utopia.core.usecase.usecase.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

/**
 * @author Salarkia
 *
 */
public class UseCaseActionAction extends AbstractUtopiaAction<UseCaseActionForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7698948298953201736L;

	/* (non-Javadoc)
	 * @see ir.utopia.core.struts.AbstractUtopiaAction#createModelInstance()
	 */
	@Override
	protected UseCaseActionForm createModelInstance() {
		return new UseCaseActionForm();
	}

	
	

}
