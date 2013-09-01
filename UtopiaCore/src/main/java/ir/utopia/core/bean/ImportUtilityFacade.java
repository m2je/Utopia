package ir.utopia.core.bean;


import static ir.utopia.core.bean.ImportTransactionalRemote.SAVE_BATCH_SIZE;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants.IsActive;
import ir.utopia.core.importer.model.ImportDataHandler;
import ir.utopia.core.importer.setting.bean.CoImportLogFacadeRemote;
import ir.utopia.core.importer.setting.bean.CoImportLogMsgFacadeRemote;
import ir.utopia.core.importer.setting.bean.CoImporterSettingFacadeRemote;
import ir.utopia.core.importer.setting.bean.model.ImportScheduleModel;
import ir.utopia.core.importer.setting.persistent.CoImportLog;
import ir.utopia.core.importer.setting.persistent.CoImportLogMsg;
import ir.utopia.core.importer.setting.persistent.CoImporterSetting;
import ir.utopia.core.importer.setting.persistent.ImportStatus;
import ir.utopia.core.importer.settinginstance.persistent.CoImporterInstance;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.ProcessListener;
import ir.utopia.core.process.ProcessStatusChangeEvent;
import ir.utopia.core.process.ProcessStatusChangeEvent.EventType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ImportUtilityFacade implements ImportUtilityFacadeRemote,Serializable {//TODO refactor
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ImportUtilityFacade.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5544591178610729776L;
	@Resource
	private transient UserTransaction userTransaction;
	@Resource 
	private transient TimerService timerService;
	
	
	
	
	
//********************************************************************************************
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doImport(Class<? extends UtopiaBasicUsecaseBean<?,?>> facadeClass,ImportDataProvider dataProvider,int userFrom ,int userTo,Long importLog ,Map<String,Object>context,ImporDataProcessListener realListener)throws Exception{
		Exception e=null;
		int recordCount=0;
		Semaphore logSemaphore=new Semaphore(1);
		Semaphore logWriterSemaphore=new Semaphore(1);
		try {
				userTransaction.begin();
				int from,to;
				recordCount=userTo-userFrom;
				int loopCount=((recordCount)/SAVE_BATCH_SIZE)+((recordCount)%SAVE_BATCH_SIZE>0?1:0);
				
				UtopiaBasicUsecaseBean bean=(UtopiaBasicUsecaseBean)ServiceFactory.lookupFacade(facadeClass);
				UtopiaBasicPersistent []validRecord;
				UtopiaBasicPersistent []invalidPersistentsTemp;
				List<MessageNamePair>messages;
				for(int i=0;i<loopCount;i++){
					validRecord=null;
					invalidPersistentsTemp=null;
					messages=null;
					realListener.currentPage=i;
					from=i*SAVE_BATCH_SIZE+userFrom;
					to=from+SAVE_BATCH_SIZE;
					if(to>userTo){
						to=userTo;
					}
					
					UtopiaBasicPersistent []p=dataProvider.getPersistents(from, to, context);
					if(p==null||p.length==0){
						logger.log(Level.WARNING," data provider return null array for importing data from:"+from+" and to:"+to+" opration failed ");
						fireStatusChange(realListener,null,null,null,logWriterSemaphore);
					}else{
						Long t1=System.currentTimeMillis();
						logger.log(Level.WARNING,"validating "+p.length+" records to import !! ");
						BeanProcessExcutionResult result=bean.validatePersistent( p);
						logger.log(Level.INFO,"validating import data  took :"+(System.currentTimeMillis()-t1)+" ms ");
						if(result!=null){
							validRecord=result.getValidPersistents();
							invalidPersistentsTemp=result.getInvalidPersistents();
							messages=result.getMessages();
							if(invalidPersistentsTemp!=null){
								logger.log(Level.WARNING,invalidPersistentsTemp.length+ " of "+p.length+" was invalid recods!,check import log ");
							}
							if(validRecord!=null&&validRecord.length>0){
								logger.log(Level.INFO,validRecord.length+ " of "+p.length+" was valid recods");
								bean.saveAll(validRecord,  false, realListener);
							}
								realListener.currentPage++;
								fireStatusChange(realListener,validRecord,invalidPersistentsTemp,messages,logWriterSemaphore);
							
						}else{
								realListener.currentPage++;
							    fireStatusChange(realListener,null,null,null,logWriterSemaphore);
						}
					}
					if(importLog!=null){
						int validRecordCount=validRecord==null?0:validRecord.length;
						int invalidCount=invalidPersistentsTemp==null?0:invalidPersistentsTemp.length;
						updateLog(dataProvider.getCommand(context),importLog, i==loopCount-1, new Long(validRecordCount), new Long(validRecordCount+invalidCount), dataProvider.getMaximumPK(), messages, logSemaphore,context);
						}
				}
				dataProvider.clear();
				Long t1=System.currentTimeMillis();
				logger.log(Level.INFO,"Import process was successful,commiting data");
				userTransaction.commit();
				logger.log(Level.INFO,"commmit took "+(System.currentTimeMillis()-t1));
				realListener.notifyProcessFinished();
			} catch (Exception e1) {
				logger.log(Level.WARNING,"", e1);
				try {
					userTransaction.rollback();
				} catch (Exception e2) {
				
				}
				e=e1;
				
			}
			
			if(e !=null)throw e;
			
	}
//********************************************************************************************
	private void fireStatusChange(final ImporDataProcessListener realListener,final UtopiaBasicPersistent[] validRecord,final UtopiaBasicPersistent[] invalidRecord,final List<MessageNamePair>messages,final Semaphore logWriterSemaphore){
		if(realListener!=null){
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
						logWriterSemaphore.acquire();
						ProcessStatusChangeEvent ev=new ProcessStatusChangeEvent(EventType.processStatusChanged);
						ProcessStatusChangeEvent.ProcessPersistentInfo persistentInfo=new ProcessStatusChangeEvent.ProcessPersistentInfo(validRecord,invalidRecord,messages);
						ev.setPersistentInfo(persistentInfo);
						realListener.notifyStatusChanged(ev);		
						} catch (Exception e) {
							logger.log(Level.WARNING,"", e);
						}finally{
							logWriterSemaphore.release();
						}
					}}).start();
			
			
		}
	}
//********************************************************************************************
public void importData(Class<? extends UtopiaBasicUsecaseBean<?,?>> facadeClass,ImportDataProvider dataProvider,int userFrom ,int userTo,Map<String,Object>context,ProcessListener listener)throws Exception{
	ImporDataProcessListener realListener=new ImporDataProcessListener(listener,context);
	doImport(facadeClass, dataProvider, userFrom, userTo, null,context, realListener);
	
	}
//********************************************************************************************
@SuppressWarnings("unchecked")
@Override
public void importData(CoImporterSetting setting,
		ImportDataProvider dataProvider, int userFrom, int userTo,
		Map<String, Object> context, ProcessListener listener)
		throws Exception {
	ImporDataProcessListener realListener=new ImporDataProcessListener(listener,context);
	try {
		String remoteClass=setting.getCoUsecase().getUscsRemoteClass();
		Long logId= createImportLog(setting.getCoImporterSettingId(),dataProvider.getCommand(context),   context);
       doImport((Class<UtopiaBasicUsecaseBean<?, ?>>)Class.forName(remoteClass), dataProvider, userFrom, userTo,logId, context, realListener);
	} catch (Exception e1) {
		logger.log(Level.WARNING, "",e1);
		
	}
	
	
	
}
//********************************************************************************************
//public void log(ProcessPersistentInfo pInfo){
//	try {
//		UtopiaPersistent []valids=pInfo!=null?pInfo.getValidPersistents():null;
//		UtopiaPersistent []invalids=pInfo!=null?pInfo.getInvlidPersistents():null;
		
//		if(!hasBeenLoged){
//			hasBeenLoged=true;
//			logTransaction.begin();
//			importLogId=createImportLog(importSettingId, lastPk,  new Date(System.currentTimeMillis()), context);
//			logTransaction.commit();
//		}
//			int validCount= valids==null?0:valids.length;
//			int invalidCount= invalids==null?0:invalids.length;
//			updateLog(importLogId,true,new Long(validCount),new Long(invalidCount+validCount),lastPk, pInfo.getMessages(), context);
//		
//	} catch (Exception e) {
//		logger.log(Level.WARNING, e);
//	}
	
//}
//********************************************************************************************
private Long createImportLog(Long importSettingId,String command,Map<String,Object>context)throws Exception{
	try {
		CoImportLogFacadeRemote importLog=(CoImportLogFacadeRemote) ServiceFactory.lookupFacade(CoImportLogFacadeRemote.class.getName());
		CoImportLog log=new CoImportLog();
		ArrayList<LookupInfo>infos=new ArrayList<LookupInfo>();
		infos.add(new LookupInfo(CoImporterSetting.class,importSettingId));
		log.setLookupInfos(infos);
		log.setCommand(command);
		log.setIsactive(IsActive.active);
		Date startTime=new Date(System.currentTimeMillis());
		log.setStartTime(startTime);
		log.setEndTime(startTime);
		log.setStatus(ImportStatus.successfull);
		log= importLog.save(log, true);
		return log.getRecordId();
	} catch (Exception e) {
		
		logger.log(Level.WARNING, "",e);
	}
	return -1l;
}
//********************************************************************************************
private void updateLog(final String command,final Long importLogId,final boolean status,final Long validRecordCount,final Long totalRecordCount,final Long lastPk,final List<MessageNamePair>messages,final Semaphore logSemaphore ,final Map<String,Object>context){
	
	new Thread(new Runnable(){

		@Override
		public void run() {
			try {
				logSemaphore.acquire();
				CoImportLogFacadeRemote importLog=(CoImportLogFacadeRemote) ServiceFactory.lookupFacade(CoImportLogFacadeRemote.class.getName());
				CoImportLog log=importLog.findById(importLogId); 
				if(validRecordCount!=null)
					log.setRecordCount((log.getRecordCount()==null?0:log.getRecordCount())+validRecordCount);
				
				if(totalRecordCount!=null)
					log.setTotalRecordCount((log.getTotalRecordCount()==null?0:log.getTotalRecordCount())+totalRecordCount);
				ImportStatus previousStatus=log.getStatus();
				boolean haswarning=totalRecordCount-validRecordCount>0;
				
				
				logImportLogDetail(importLogId, messages, context);
				CoImportLog newLog=new CoImportLog();
				if(status){
					if(haswarning){
						if(ImportStatus.successfull.equals(previousStatus)){
							newLog.setStatus(ImportStatus.sucessfullWithWarning);		
						}else{
							newLog.setStatus(previousStatus);
						}
					}	
				}else{
					newLog.setStatus(ImportStatus.unsuccessfull);
				}
				
				newLog.setCoImportLogId(log.getCoImportLogId());
				newLog.setStartTime(log.getStartTime());
				newLog.setEndTime(new Date(System.currentTimeMillis()));
				newLog.setStatus(log.getStatus());
				newLog.setRecordCount(log.getRecordCount());
				
				List<LookupInfo>lookups=new ArrayList<LookupInfo>();
				lookups.add(new LookupInfo(CoImporterInstance.class,log.getCoImporterInstance().getRecordId()));
				newLog.setLookupInfos(lookups)  ;
				newLog.setTotalRecordCount(log.getTotalRecordCount());
				newLog.setCoImportLogId(log.getCoImportLogId());
				newLog.setIsactive(log.getIsactive());
				newLog.setCreated(log.getCreated());
				newLog.setUpdated(log.getUpdated());
				newLog.setUpdatedby(log.getUpdatedby());
				newLog.setCommand(command);
				newLog.setLastImportedPk(lastPk);
				importLog.update(newLog);
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
			}
			logSemaphore.release();
		}}).start();
	
}
//********************************************************************************************
private void logImportLogDetail(Long importLogId,List<MessageNamePair>messages,Map<String,Object>context){
		if(messages!=null){
			try {
				HashSet<CoImportLogMsg >logMessages=new HashSet<CoImportLogMsg>();
				LookupInfo info=new LookupInfo(CoImportLog.class,importLogId);
				ArrayList<LookupInfo>infoss=new ArrayList<LookupInfo>();
				infoss.add(info);
				for(MessageNamePair message:messages){
					CoImportLogMsg msg=new CoImportLogMsg();
					msg.setMsg(message.getMessage());
					msg.setReferenceKey1(message.getReferenceKey1());
					msg.setReferenceKey2(message.getReferenceKey2());
					msg.setReferenceKey3(message.getReferenceKey3());
					msg.setLookupInfos(infoss);
					msg.setType(message.getErrorCode());
					logMessages.add(msg);
				}
				CoImportLogMsgFacadeRemote bean=(CoImportLogMsgFacadeRemote)ServiceFactory.lookupFacade(CoImportLogMsgFacadeRemote.class);
				CoImportLogMsg[]messagesArray= logMessages.toArray(new CoImportLogMsg[logMessages.size()]);
				bean.saveAll(messagesArray,  false,null);
				
			} catch (Exception e) {
				
				logger.log(Level.WARNING,"", e);
			}
			}
	} 
//********************************************************************************************
@Override
public void scheduleImport(CoImporterSetting setting,
		ImportDataProvider dataProvider,Map<String,Object>context) {
//	if(setting.getScdStartDate()!=null){
//		ImportScheduleModel model=new ImportScheduleModel(setting.getScdEndDate(),setting.getCoImporterSettingId(),
//				 dataProvider,context);
		Collection<Timer>timers= timerService.getTimers();
		if(timers!=null){
			for(Timer timer:timers){
				Object info=timer.getInfo();
				if(ImportScheduleModel.class.isInstance(info)){
					ImportScheduleModel modelw=(ImportScheduleModel)timer.getInfo();
					if(modelw.getImportSettingId()==setting.getCoImporterSettingId()){
						logger.log(Level.WARNING,"duplicate scedule for import setting -->"+setting.getCoImporterSettingId()+" older schedule will be cancel and the new one replace it" );
						timer.cancel();
					}
				}
			}
		}
//		 timerService.createTimer(setting.getScdStartDate(),setting.getScdInterval()*1000, 
//				model);	
//	}
}
//***********************************************************************
@Timeout
public void timeout(Timer timer){
	Long now=System.currentTimeMillis();
	ImportScheduleModel info=(ImportScheduleModel)timer.getInfo();
	if(info.getEndDate()!=null){
			 Long endTime=info.getEndDate().getTime();
			 if(now<=endTime){
				 timer.cancel();
				 return;
			 }
	}
	
	try {
	CoImporterSettingFacadeRemote bean=(CoImporterSettingFacadeRemote)ServiceFactory.lookupFacade(CoImporterSettingFacadeRemote.class);
	CoImporterSetting setting=	bean.findById(info.getImportSettingId());
	if(setting==null){
		timer.cancel();
		return;
	}
	
	
	Calendar settingTime=Calendar.getInstance();
//	settingTime.setTime(setting.getScdStartDate());
	Calendar currentTime=Calendar.getInstance(); 
	if(settingTime.getTimeZone().inDaylightTime(currentTime.getTime())){
		currentTime.add(Calendar.MILLISECOND, -currentTime.getTimeZone().getDSTSavings());
	}
	if(settingTime.getTimeZone().inDaylightTime(settingTime.getTime())){
		settingTime.add(Calendar.MILLISECOND, -settingTime.getTimeZone().getDSTSavings());
	}
	int hour=Math.abs(settingTime.get(Calendar.HOUR_OF_DAY)-currentTime.get(Calendar.HOUR_OF_DAY))*60;
	int minute=Math.abs(settingTime.get(Calendar.MINUTE)-currentTime.get(Calendar.MINUTE));
	if(Math.abs(hour-minute)>5){//more than 5 minute difference  is not acceptable 
		logger.log(Level.WARNING,"invalid time to run scheduler with startTime:\""+settingTime.get(Calendar.HOUR)+":"+settingTime.get(Calendar.MINUTE)+ "\" at \""+currentTime.get(Calendar.HOUR)+":"+currentTime.get(Calendar.MINUTE)+
				"\" (may be application server startup) . schedule process ignored!");
		return;
	}
	CoImportLogFacadeRemote logBean=(CoImportLogFacadeRemote)ServiceFactory.lookupFacade(CoImportLogFacadeRemote.class.getName());
	Map<String, Object>context=info.getContext();
	Long lastImportedRecord= logBean.getLastImportedPrimaryKey(setting.getCoImporterSettingId(), context);
	lastImportedRecord=lastImportedRecord==null?-1:lastImportedRecord;
	context.put(ImportDataHandler.LAST_IMPORTED_RECORD_PK_PARAMETER_NAME, lastImportedRecord);
	 ImportDataProvider dataProvider=info.getDataProvider();
	 int to= dataProvider.getSize(context);
	 importData(setting,dataProvider , 0, to, info.getContext(), null);
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
	}

}
//********************************************************************************************
	class ImporDataProcessListener implements ProcessListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1694579245920652409L;
		ProcessListener adaptee; 
		int currentPage=0;
		boolean hasBeenLoged=false;
		
		
		Map<String,Object>context;
		
		
		public ImporDataProcessListener(ProcessListener adaptee,Map<String,Object>context){
			this.adaptee=adaptee;
			this.context=context;
		
		} 
		@Override
		public void notifyStatusChanged(ProcessStatusChangeEvent e) {
			if(!EventType.processFinished.equals( e.getEventType())){
				if(EventType.processStatusChanged.equals(e.getEventType())){
					e.setProcessStatus((currentPage*SAVE_BATCH_SIZE)+e.getProcessStatus() );	
				}
				if(adaptee!=null)
					adaptee.notifyStatusChanged(e);	
			}
			
		}
		
		public void notifyProcessFinished(){
			if(adaptee!=null){
				ProcessStatusChangeEvent res=new ProcessStatusChangeEvent(EventType.processFinished);
				adaptee.notifyStatusChanged(res);
				}
		}
		
		
		
		
		
	}
	
	

	
}
