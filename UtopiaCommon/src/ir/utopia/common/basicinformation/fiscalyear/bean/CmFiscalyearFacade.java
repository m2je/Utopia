package ir.utopia.common.basicinformation.fiscalyear.bean;

import ir.utopia.common.basicinformation.fiscalyear.persistent.CmFiscalyear;
import ir.utopia.common.basicinformation.fiscalyear.persistent.CmVFiscalyear;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UserPreferencesInfo;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.process.BeanProcess;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.BeanProcessParameter;
import ir.utopia.core.process.ProcessListener;
import ir.utopia.core.security.userpreferences.bean.CoUserPreferencesFacadeRemote;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity CmFiscalyear.
 * 
 * @see currency.CmFiscalyear
 * @author Jahani
 */
@Stateless
public class CmFiscalyearFacade extends AbstractBasicUsecaseBean<CmFiscalyear,CmVFiscalyear> implements CmFiscalyearFacadeRemote {
	// property constants
	public static final String FISCALYEAR_ID = "cmFiscalyearId";
	public static final String STARTDATE = "startdate";
	public static final String ENDDATE = "enddate";
	public static final String NAME = "name";
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CmFiscalyearFacade.class.getName());
	}


	public List<CmFiscalyear> findByStartdate(Object startdate,
			int... rowStartIdxAndCount) {
		return findByProperty(STARTDATE, startdate, rowStartIdxAndCount);
	}

	public List<CmFiscalyear> findByEnddate(Object enddate,
			int... rowStartIdxAndCount) {
		return findByProperty(ENDDATE, enddate, rowStartIdxAndCount);
	}

	public List<CmFiscalyear> findByName(Object name,
			int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	@Override
	public CmFiscalyear getFiscalYear(Date intendedDate) {
		try {
			Query q= entityManager.createNamedQuery(CmFiscalyear.FIND_CURRENT_FISCAL_YEAR_QUERY);
			q.setParameter("intendedDate", intendedDate);
			q.setParameter("isactive", Constants.IsActive.active);
			return (CmFiscalyear)q.getSingleResult();
		} catch (Exception e) {
			logger.log(Level.WARNING,"unable to find fiscalYear for date: "+intendedDate,e);
		}
		return null;
	}

	@Override
	public int getTotalStepCount(BeanProcess processBean,
			Map<String, Object> context) {
		return 1;
	}

	@Override
	public BeanProcessExcutionResult<CmFiscalyear> startProcess(BeanProcess processBean,
			Map<String, Object> context, ProcessListener listener)
			throws Exception {
		BeanProcessParameter []params= processBean.getParameters();
		BeanProcessExcutionResult<CmFiscalyear> result=new BeanProcessExcutionResult<CmFiscalyear>();
		if(params!=null&&params.length>0){
			Long fiscalyear=(Long)params[0].getValue();
			if(fiscalyear>0){
				UserPreferencesInfo userPreferences= ContextUtil.getUserPreferences(context);
				userPreferences.setCurrentFiscalYearId(fiscalyear);
				CoUserPreferencesFacadeRemote pref=(CoUserPreferencesFacadeRemote)ServiceFactory.lookupFacade(CoUserPreferencesFacadeRemote.class);
			    pref.savePreferences(ContextUtil.getUserId(context), userPreferences.exportPreferences());
			}
		}
		
		
		return result;
	}


}