package ir.utopia.core.basicinformation.action.action;

import java.util.logging.Logger;

import ir.utopia.core.UIServiceFactory;
import ir.utopia.core.struts.AbstractUtopiaAction;

public class ActionAction extends AbstractUtopiaAction<ActionForm> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8939087920482198119L;

	protected ActionForm createModelInstance() {
		return new ActionForm();
	}
}
