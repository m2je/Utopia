package ir.utopia.common.basicinformation.request.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class RequestAction extends AbstractUtopiaAction<RequestForm> {
	
	
	
	private static final long serialVersionUID = 8096221606060970439L;


	@Override
	public String execute() throws Exception {
		
		return super.execute();
	}
	
	
	@Override
	protected RequestForm createModelInstance(){
		
		return new RequestForm();
	}

}
