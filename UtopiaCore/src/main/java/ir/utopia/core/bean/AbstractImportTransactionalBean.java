package ir.utopia.core.bean;

import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.process.BeanProcess;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.BeanProcessParameter;
import ir.utopia.core.process.ProcessListener;
import ir.utopia.core.process.ProcessStatusChangeEvent;
import ir.utopia.core.process.ProcessStatusChangeEvent.EventType;

import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class ImportTransactional
 */
public abstract class AbstractImportTransactionalBean<P extends UtopiaPersistent,S extends UtopiaPersistent> extends AbstractBasicUsecaseBean<P,S> 
implements ImportTransactionalRemote{
	
	@EJB
	ImportUtilityFacadeRemote importer;
    
	/**
     * Default constructor. 
     */
    public AbstractImportTransactionalBean() {
    }

	
//********************************************************************************************
	@Override
	public int getTotalStepCount(BeanProcess processBean,Map<String,Object>context) {
		try {
			int []fromTo=getFromTo(processBean.getParameters(),context);
			return fromTo[1]-fromTo[0];
		} catch (Exception e) {
		return 0;
		}
	}
//********************************************************************************************
	
	@SuppressWarnings("rawtypes")
	@Override
	public BeanProcessExcutionResult<?> startProcess(BeanProcess processBean,
			Map<String, Object> context, ProcessListener listener)
			throws Exception {
		try {
			BeanProcessParameter []params=processBean==null?null:processBean.getParameters();
			if(params==null)throw new IllegalArgumentException("invalid processparameter null");
			ImportDataProvider dataProvider= getDataProvider(params);
			if(dataProvider==null||dataProvider.getSize(context)==0){
				listener.notifyStatusChanged(new ProcessStatusChangeEvent(EventType.processFinished));
			}
			
			int []fromTo=getFromTo(params,context);
			importer.importData(getFacadeClass(), dataProvider,fromTo[0], fromTo[1],context,listener);
			return new BeanProcessExcutionResult();
		} catch (Exception e) {
			throw e;
		}
	}

//********************************************************************************************
	@SuppressWarnings("unchecked")
	protected Class<? extends UtopiaBasicUsecaseBean<?,?>> getFacadeClass(){
		Class<UtopiaBasicUsecaseBean<?,?>> facadeClass=(Class<UtopiaBasicUsecaseBean<?,?>>) 
		BeanUtil.findRemoteClassFromPersistent( getPersistentClass());
		return facadeClass;
	}
//********************************************************************************************
	private int [] getFromTo(BeanProcessParameter []params,Map<String,Object>context)throws Exception{
		ImportDataProvider dataProvider= getDataProvider(params);
		int userFrom=getFromIndex(params),to=getToIndex(params);
		int size=dataProvider.getSize(context);
		to=(to==0||to>size)?size:to;
		return new int[]{userFrom,to};
	}
//********************************************************************************************	
	protected ImportDataProvider getDataProvider(BeanProcessParameter[] params){
		for(BeanProcessParameter param:params){
			if(PERSITENTLIST_DATA_PROVIDER.equals(param.getName())){
				ImportDataProvider dataProvider=(ImportDataProvider) param.getValue();
				return dataProvider;
			}
		}
		return null;
	}
//********************************************************************************************
	private int getFromIndex(BeanProcessParameter[] params){
		for(BeanProcessParameter param:params){
			if(RECORD_START_INDEX.equals(param.getName())){
				return (Integer) param.getValue();
			}
		}
		return 0;
	}
//********************************************************************************************
	private int getToIndex(BeanProcessParameter[] params){
		for(BeanProcessParameter param:params){
			if(RECORD_END_INDEX.equals(param.getName())){
				return (Integer) param.getValue();
			}
		}
		return 0;
	}

}
