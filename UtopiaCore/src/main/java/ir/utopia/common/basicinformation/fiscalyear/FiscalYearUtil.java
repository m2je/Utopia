package ir.utopia.common.basicinformation.fiscalyear;

import ir.utopia.common.basicinformation.fiscalyear.bean.CmFiscalyearFacadeRemote;
import ir.utopia.common.basicinformation.fiscalyear.persistent.CmFiscalyear;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UserPreferencesInfo;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

public class FiscalYearUtil {
	public static final String FISCAL_YEAR_INFO_KEY="__fiscalYearInfo";
	public static final String FISCAL_YEAR_ID_KEY="fiscalYearId";
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(FiscalYearUtil.class.getName());
	}
	public static Long getFiscalYearId(Map<String,Object>context){
		FiscalYearInfo fiscalyear=getFiscalYear(context);
		return fiscalyear==null?null:fiscalyear.getId();
	}
	
	public static FiscalYearInfo getFiscalYear(Map<String,Object>context){
		return (FiscalYearInfo)context.get(FISCAL_YEAR_INFO_KEY);
	}
	public static FiscalYearInfo getFiscalYear(HttpSession session){
		return (FiscalYearInfo)session.getAttribute(FISCAL_YEAR_INFO_KEY);
	}
	public static void initFiscalYear(Map<String,Object> session){
		CmFiscalyear fiscalYear=null;
		fiscalYear = findFiscalYear(ContextUtil.createContext(session));
		if(fiscalYear!=null){
			FiscalYearInfo info=new FiscalYearInfo();
			info.setEndDate(fiscalYear.getEnddate());
			info.setStartDate(fiscalYear.getStartdate());
			info.setName(fiscalYear.getName());
			info.setId(fiscalYear.getCmFiscalyearId());
			session.put(FISCAL_YEAR_INFO_KEY, info);
			session.put(FISCAL_YEAR_ID_KEY, info.getId());
		}
	}
	public static void initFiscalYear(HttpSession session){
		CmFiscalyear fiscalYear=null;
		fiscalYear = findFiscalYear(ContextUtil.createContext(session));
		if(fiscalYear!=null){
			FiscalYearInfo info=new FiscalYearInfo();
			info.setEndDate(fiscalYear.getEnddate());
			info.setStartDate(fiscalYear.getStartdate());
			info.setName(fiscalYear.getName());
			info.setId(fiscalYear.getCmFiscalyearId());
			session.setAttribute(FISCAL_YEAR_INFO_KEY, info);
			session.setAttribute(FISCAL_YEAR_ID_KEY, info.getId());
		}
	}
	public static void initContextFiscalYear(Map<String,Object> context,Long currentFiscalYear){
		CmFiscalyear fiscalYear=null;
		if(currentFiscalYear!=null){
			try {
				CmFiscalyearFacadeRemote fiscalYearFacade=(CmFiscalyearFacadeRemote)ServiceFactory.lookupFacade(CmFiscalyearFacadeRemote.class);
				fiscalYear= fiscalYearFacade.findById(currentFiscalYear);
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
			}
			
		}else{
			fiscalYear = findFiscalYear(context);
		}
		if(fiscalYear!=null){
			FiscalYearInfo info=new FiscalYearInfo();
			info.setEndDate(fiscalYear.getEnddate());
			info.setStartDate(fiscalYear.getStartdate());
			info.setName(fiscalYear.getName());
			info.setId(fiscalYear.getCmFiscalyearId());
			UserPreferencesInfo pref=new UserPreferencesInfo();
			pref.setCurrentFiscalYearId(fiscalYear.getCmFiscalyearId());
			context.put(ContextUtil.USER_PREFERENCES_PARAMETER, pref);
			context.put(FISCAL_YEAR_INFO_KEY, info);
			context.put(FISCAL_YEAR_ID_KEY, info.getId());
		}
	}
	private static CmFiscalyear findFiscalYear(Map<String,Object> context
			) {
		CmFiscalyear fiscalYear=null;
		try {
			UserPreferencesInfo pref=  ContextUtil.getUserPreferences(context);
			CmFiscalyearFacadeRemote fiscalYearFacade=(CmFiscalyearFacadeRemote)ServiceFactory.lookupFacade(CmFiscalyearFacadeRemote.class);
			if(pref==null||pref.getCurrentFiscalYearId()==null){
				 fiscalYear= fiscalYearFacade.getFiscalYear(new Date());
			}else{
				fiscalYear=fiscalYearFacade.findById(pref.getCurrentFiscalYearId());
			}
		
			
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return fiscalYear;
	}
}
