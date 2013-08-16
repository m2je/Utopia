package ir.utopia.core.basicinformation.menu.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class MenuAction extends AbstractUtopiaAction<MenuForm> {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8939087920482198119L;

	protected MenuForm createModelInstance() {
		return new MenuForm();
	}
}
