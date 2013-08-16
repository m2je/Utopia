package ir.utopia.common.basicinformation.fiscalyear.bean;

import ir.utopia.common.basicinformation.fiscalyear.FiscalYearUtil;
import ir.utopia.common.basicinformation.fiscalyear.persistent.CmFiscalyear;
import ir.utopia.common.basicinformation.fiscalyear.persistent.CmVFiscalyear;
import ir.utopia.core.bean.ActionParameterTypes;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.bean.UtopiaProcessBean;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.process.annotation.Process;
import ir.utopia.core.process.annotation.ProcessParameter;
import ir.utopia.core.process.annotation.ProcessUIConfiguration;
import ir.utopia.core.process.annotation.Processes;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmFiscalyearFacade.
 * 
 * @author Jahani
 */
@Remote
@Processes(processList={@Process(UIConfiguration=@ProcessUIConfiguration(refreshPageAfterProcess=true), name=CmFiscalyearFacadeRemote.CHANGE_FISCAL_YEAR_METHOD, 
parameters={
		@ProcessParameter(name="fiscalYear",type=ActionParameterTypes.processParameter,paramDisplayType=DisplayTypes.lookup,
				lookupConfiguration=@LookupConfiguration(persistentClass=CmFiscalyear.class,displayColumns={"name"},orderby="CmFiscalyear.startdate desc")
				,displayIndex=0,index=0,isMandatory=true,defaultValue="@"+FiscalYearUtil.FISCAL_YEAR_ID_KEY+"@")})})
public interface CmFiscalyearFacadeRemote extends UtopiaBasicUsecaseBean<CmFiscalyear, CmVFiscalyear>,UtopiaProcessBean {
	
	public static final String CHANGE_FISCAL_YEAR_METHOD= "changeFiscalYear";
	public List<CmFiscalyear> findByStartdate(Object startdate,
			int... rowStartIdxAndCount);

	public List<CmFiscalyear> findByEnddate(Object enddate,
			int... rowStartIdxAndCount);

	public List<CmFiscalyear> findByName(Object name,
			int... rowStartIdxAndCount);
	
	public CmFiscalyear getFiscalYear(Date intendedDate);
}