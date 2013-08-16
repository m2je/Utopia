package ir.utopia.core.bean;

import ir.utopia.common.basicinformation.fiscalyear.persistent.FiscalYearSupport;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.lookup.model.Condition;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.persistent.parentchild.ParentChildPair;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class AbstractFiscalYearFacade<P extends UtopiaBasicPersistent,S extends UtopiaBasicPersistent> extends AbstractBasicUsecaseBean<P,S>{

	protected String fiscalYearColumn=null;
	protected String fiscalYearViewColumn=null;
	
	protected String getFiscalYearColumn(Class<?> persistentClass){
		if(fiscalYearColumn==null){
			FiscalYearSupport fiscalYearSupport=getPersistentClass().getAnnotation(FiscalYearSupport.class);
			fiscalYearViewColumn=fiscalYearSupport.viewFiscalYearField();
			fiscalYearColumn=fiscalYearSupport.fiscalYearField();
	    }	
		if(getSearchModelClass().isAssignableFrom(persistentClass)){
			return fiscalYearViewColumn;
		}
		return fiscalYearColumn;
		}
//********************************************************************************************************************************	
	public Condition getFiscalYearCondition(Class<?> persistenObject,
			Condition condition) {
			Map<String,Object>context=ContextHolder.getContext().getContextMap();
			Long fiscalyearId= ContextUtil.getCurrentFiscalYear(context);
			if(fiscalyearId!=null){
				StringBuffer cond=new StringBuffer(persistenObject.getSimpleName()).append(".").append(getFiscalYearColumn(persistenObject)).append("=:__cmFiscalyearId");
				if(condition==null || condition.getCondition()==null|| condition.getCondition().trim().length()==0){
					condition=new Condition(cond.toString(), context);
				}else{
					condition.setCondtionString(condition.getCondition()+" and ("+cond+")");
				}
				condition.setParameterValue("__cmFiscalyearId",fiscalyearId );
				return condition;
			}
		
		return condition;
	}
//**********************************************************************************************************************************
	public List<ParentChildPair> loadChildren(LookupConfigurationModel model,Long parentId,
			int... rowStartEndIndex) {
		Condition condition=getFiscalYearCondition(getPersistentClass(), null);
		if(condition!=null)
			model.addCondition(condition);
		return super.loadChildren(model, parentId,  rowStartEndIndex);
	}
//**********************************************************************************************************************************	
	@Override
	public List<P> findByProperty(String propertyName,
			Object value, 
			int... rowStartIdxAndCount) {
		Map<String,Object>context=ContextHolder.getContext().getContextMap();
		Long fiscalyearId= ContextUtil.getCurrentFiscalYear(context);
		if(fiscalyearId!=null){
			return super.findByProperties(new String[]{propertyName, fiscalYearColumn}, new Object[]{value,fiscalyearId },
					 rowStartIdxAndCount);	
		}
		return super.findByProperty(propertyName, value,  rowStartIdxAndCount);	
	}
//**********************************************************************************************************************************
	@Override
	protected Query getSearchQuery(SearchConditionItem []items,EntityPair []orderBy, Class<? extends UtopiaBasicPersistent>searchClass,Condition defaultCondition,boolean count){
		Condition cond= getFiscalYearCondition(getSearchModelClass(),defaultCondition);
		return super.getSearchQuery(items,orderBy, searchClass, cond,
				count);
	}
//**********************************************************************************************************************************	
	@Override
	public List<NamePair> loadLookup(
			LookupConfigurationModel lookupConfigModel, int... rowStartEndIndex) {
		Condition condition=getFiscalYearCondition(getPersistentClass(), null);
		if(condition!=null)
			lookupConfigModel.addCondition(condition);
		return super.loadLookup( lookupConfigModel, rowStartEndIndex);
	}
}
