package ir.utopia.common.basicinformation.notification.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class NotificationAction extends AbstractUtopiaAction<NotificationForm> {
	
	
	
	private static final long serialVersionUID = 8096221606060970439L;


	@Override
	public String execute() throws Exception {
		
		return super.execute();
	}
	
	
	@Override
	protected NotificationForm createModelInstance(){
		
		return new NotificationForm();
	}

}
