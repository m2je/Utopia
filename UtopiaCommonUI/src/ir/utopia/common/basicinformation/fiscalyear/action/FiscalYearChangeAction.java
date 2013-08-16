package ir.utopia.common.basicinformation.fiscalyear.action;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;

import ir.utopia.common.basicinformation.fiscalyear.FiscalYearUtil;
import ir.utopia.common.basicinformation.fiscalyear.bean.CmFiscalyearFacadeRemote;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.UserPreferencesInfo;
import ir.utopia.core.util.tags.process.action.AbstractUtopiaProcessAction;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.process.client.model.UtopiaProcessAction;

public class FiscalYearChangeAction extends AbstractUtopiaProcessAction<CmFiscalyearFacadeRemote> implements UtopiaProcessAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3190819628265233304L;

	private Long fiscalYear;
	@Override
	protected String getProcessName() {
		
		return CmFiscalyearFacadeRemote.CHANGE_FISCAL_YEAR_METHOD;
	}

	@Override
	public ProcessExecutionResult execute(String[] params, String[] values) {
		ProcessExecutionResult result= super.execute(params, values);
		if(result.isSuccess()){
			Map<String,Object> session= ActionContext.getContext().getSession();
			UserPreferencesInfo pref= ContextUtil.getUserPreferences(session);
			pref.setCurrentFiscalYearId(fiscalYear);
			session.put(ContextUtil.USER_PREFERENCES_PARAMETER,pref );
			FiscalYearUtil.initFiscalYear(session);
		}
		return result;
	}

	public Long getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(Long fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	
	
}
