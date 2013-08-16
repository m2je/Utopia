package ir.utopia.common.basicinformation.jobtitle.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class JobTitleAction extends AbstractUtopiaAction<JobTitleForm> {
	
	

	private static final long serialVersionUID = -3043240838342919205L;

	protected JobTitleForm createModelInstance() {
		
		return new JobTitleForm();
		
	}

}
