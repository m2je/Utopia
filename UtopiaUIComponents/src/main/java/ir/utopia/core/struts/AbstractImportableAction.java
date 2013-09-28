package ir.utopia.core.struts;

import static ir.utopia.core.importer.model.DefaultImportDataProvider.distinct;
import static ir.utopia.core.importer.model.DefaultImportDataProvider.getLookupValue;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.bean.ImportDataProvider;
import ir.utopia.core.bean.ImportTransactionalRemote;
import ir.utopia.core.bean.UtopiaProcessBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.importer.FieldSetup;
import ir.utopia.core.importer.ImportSetup;
import ir.utopia.core.importer.model.DefaultImportDataProvider;
import ir.utopia.core.importer.model.ImportDataHandler;
import ir.utopia.core.importer.model.exception.ImportDataException;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.process.BeanProcessParameter;
import ir.utopia.core.process.ProcessStatusChangeEvent;
import ir.utopia.core.process.ProcessStatusChangeEvent.ProcessPersistentInfo;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.exceptionhandler.ExceptionResult;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.process.action.AbstractUtopiaProcessAction;
import ir.utopia.core.util.tags.process.action.ProcessBeanInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.process.client.model.UtopiaProcessAction;
import ir.utopia.core.utilities.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Golnari
 * @param <F>
 * @param <P>
 * 
 */
@Deprecated
public abstract class AbstractImportableAction<T extends UtopiaImportableForm<?, ?>>
		extends AbstractUtopiaProcessAction<UtopiaProcessBean> implements  UtopiaProcessAction{
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AbstractImportableAction.class.getName());
	}
	/**
	 * 
	 */
	public static final int MAXIMUM_ERROR_COUNT=10;
	protected abstract T createModelInstance();
	private static final long serialVersionUID = 6939331883362639983L;
	protected T importableForm;
	protected Subject subject;
	protected Locale locale;
	private HttpServletRequest request;
//	private static boolean IS_IMPORTING=false; 
	private static HashMap<Long, int[]>confirmMap=new HashMap<Long, int[]>();
	private static HashMap<Long, ProcessExecutionResult>confirmResultMap=new HashMap<Long, ProcessExecutionResult>();
	public static String USECASE_ACTION_PARAMAETER_NAME="uscaseActionId";
	public static final String IMPORT_SETTING_ID="importSettingId";
	protected Class<? extends UtopiaProcessBean> facadeClass;
	private final String SESSION_IMPORT_DATA_HANDLER=getClass().getName()+"_dataHandlerResult";
	private final String SESSION_IMPORT_DATA_PROVIDER= getClass().getName()+"_dataProvider";
	private final String SESSION_IMPORT_SETUP= getClass().getName()+"_importSetup";
	protected static Cache<Long, BeanExecInfo>importBeanCache=new Cache<Long, BeanExecInfo>();
	protected static final Timer timer2=new Timer();
	public static final int MESSAGE_LOG_FILE_SIZE=10000; 
	int lastIndex=0;
	Map<int[],String>logMap=new HashMap<int[], String>();
	
	static {
		
		timer2.schedule(new TimerTask(){

			@Override
			public void run() {
				Long time=System.currentTimeMillis();
				ArrayList<Long>removeItems=new ArrayList<Long>();
				for(Long key: importBeanCache.keySet()){
					BeanExecInfo res=importBeanCache.get(key);
					if(res.startTime-time>MAX_CACHING_BEAN_TIME_MILLISECOUND){
						removeItems.add(key);
						logger.log(Level.WARNING," import data log removed from beanMapCache by timeout ");
					}
				}
				for(Long r:removeItems){
					BeanExecInfo info=importBeanCache.remove(r);
					if(info.LogMap!=null){
						for(String f:info.LogMap.values()){
							try {
								File tf=new File(f);
								if(tf.exists()){
									tf.delete();
								}
							} catch (Exception e) {
								logger.log(Level.WARNING, "",e);
							}
						}
					}
				}
			}
			
		}, MAX_CACHING_BEAN_TIME_MILLISECOUND);
	}
	String saveRecordMessage;
	private static final int LOAD_PAGE_SIZE=10000; 
	
//*********************************************************************************************	
	@Override
	public ProcessExecutionResult confirm(String[] params, String[] values) {
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_DATA_HANDLER);
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_DATA_PROVIDER);
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_SETUP);
		ProcessExecutionResult result=new ProcessExecutionResult()	;
		Locale locale= getUserLocale();
		String language=locale==null?"en": locale.getLanguage();
		Map<String,Object>session=null;//ActionContext.getContext().getSession();
		ImportDataHandler importHandler=(ImportDataHandler)session.get(SESSION_IMPORT_DATA_HANDLER);
		Long processId=getProcessIdentifier();
		
		try {
			ImportSetup importSetup = (ImportSetup) session.get(SESSION_IMPORT_SETUP);
		if(result==null||importSetup==null||importHandler==null){//session expires or invalid access 
			result=new ProcessExecutionResult(false,ProcessExecutionResult.PROCESS_STATUS_FINISHED);
			
			result.addErrorMessage(MessageHandler.getMessage("invalidSessionValues", "ir.utopia.core.struts.ImportAction",language));
			result.addInfoMessage(MessageHandler.getMessage("relogin", "ir.utopia.core.struts.ImportAction", language));
			return result;
		}
			DefaultImportDataProvider provider=new DefaultImportDataProvider(importHandler, importSetup) ;
			session.put( SESSION_IMPORT_DATA_PROVIDER, provider);
			
			result.setProcessStatus(ProcessExecutionResult.PROCESS_STATUS_PROCESSING);
			validateData(processId,importSetup, importHandler,provider,  session);
		} catch (Exception e) {
			ExceptionResult exRes= getExceptionHandler().handel(e, createContext());
			logger.log(Level.WARNING, "",e);
			appendMessages(exRes, result);
			result.setSuccess(false);
			result.setProcessStatus(ProcessExecutionResult.PROCESS_STATUS_FINISHED);		
		}
		
		result.setProcessIdentifier(processId);
		return result;
	}
//*********************************************************************************************
	@Override
	protected ProcessExecutionResult updateConfirmExecResult(
			Long processUniqueId) {
		ProcessExecutionResult result ;
		if(confirmResultMap.containsKey(processUniqueId)){
			result=confirmResultMap.get(processUniqueId);
			confirmResultMap.remove(processUniqueId);
			confirmMap.remove(processUniqueId);
			result.setProcessStatus(ProcessExecutionResult.PROCESS_STATUS_FINISHED);
			
		}else{
			result=super.updateConfirmExecResult(processUniqueId);
			if(confirmMap.containsKey(processUniqueId)){
				int fromTo[]=confirmMap.get(processUniqueId);
				result.setCurrentTask(fromTo[2]);
				result.setTotalTaskCount(fromTo[3]);
				result.setProcessStatus(ProcessExecutionResult.PROCESS_STATUS_PROCESSING);
				String message=MessageHandler.getMessage("controllingData", "ir.utopia.core.struts.ImportAction", getUserLocale().getLanguage());
				NumberFormat formater= DecimalFormat.getInstance();
				message=message.replace("@record@",formater.format(fromTo[0])+" ... "+formater.format(fromTo[1]));
				result.setCurrentTaskName(message);
			}
		}
		result.setProcessIdentifier(processUniqueId);
		return result;
	}
//*********************************************************************************************
	protected ProcessExecutionResult updateExecResult(Long processUniqueId) {
		ProcessExecutionResult res=super.updateExecResult(processUniqueId);
		if(res.isSuccess()&&res.getProcessStatus()==ProcessExecutionResult.PROCESS_STATUS_FINISHED){
			try {
				Map<String,Object>session= null;//ActionContext.getContext().getSession();
				ImportDataProvider dataProvider=(ImportDataProvider)session.get(SESSION_IMPORT_DATA_PROVIDER);
				ImportSetup importSetup = (ImportSetup) session.get(SESSION_IMPORT_SETUP);
				Map<String,Object>context=createContext();
				int recordCount=dataProvider.getSize(context);
				recordCount=importSetup.isFirstLineTitle()?recordCount-1:recordCount;
				BeanExecInfo re=importBeanCache.get( processUniqueId);
				Long time=(re==null||re.startTime==null)?System.currentTimeMillis():re.startTime;
				if(time!=null){
					time=System.currentTimeMillis()-time;
				}
				String timeStr=null;
				if(time!=null){
					int secs=(new Long(time/1000)).intValue();
					int hours = secs / 3600,
					remainder = secs % 3600,
					minutes = remainder / 60,
					seconds = remainder % 60;

					String disHour = (hours < 10 ? "0" : "") + hours,
					disMinu = (minutes < 10 ? "0" : "") + minutes ,
					disSec = (seconds < 10 ? "0" : "") + seconds ;
					timeStr=disHour+":"+disMinu+":"+disSec;
				}
				String message=MessageHandler.getMessage("importPrompt", "ir.utopia.core.struts.ImportAction", getUserLocale().getLanguage());
				message=message.replaceAll("@time@", timeStr==null?"":timeStr);
				message=message.replaceAll("@recordCount@", String.valueOf( recordCount));
				res.addInfoMessage(message);
			} catch (Exception e) {
			}
		}
		res.setProcessIdentifier(processUniqueId);
		return res;
	}
//*********************************************************************************************
	@Override
	protected String getTaskName(int step, String language) {
		if(saveRecordMessage==null){
			saveRecordMessage=MessageHandler.getMessage("savingRecordMessage", "ir.utopia.core.struts.ImportAction", getUserLocale().getLanguage());
		}
		return saveRecordMessage.
			replaceAll("@record@", DecimalFormat.getInstance().format(step+1));
	}
//********************************************************************************************************
	private void validateData(final Long processId,final ImportSetup importSetup,final ImportDataHandler importHandler,final DefaultImportDataProvider dataProvider,final Map<String,Object> session){
		final Map<String,Object>context=createContext();
		Thread t= new Thread(new Runnable() {
			@Override
			public void run() {
				ProcessExecutionResult result=new ProcessExecutionResult();
				try {
					int []fromTo=getUserFromAndTo(importSetup, importHandler,context);
					int recordCount=fromTo[1]-fromTo[0];
					int pageCount=(recordCount/LOAD_PAGE_SIZE)+1;
					int from,to;
					
					for(int i=0;i<pageCount;i++){
						Long startTime=System.currentTimeMillis();
						from=i*LOAD_PAGE_SIZE+fromTo[0];
						to=from+LOAD_PAGE_SIZE;
						if(to>recordCount){
							to=recordCount;
						}
						confirmMap.put(processId, new int[]{from,to,i,pageCount});
						Map<FieldSetup,Vector<Object>>fieldSetupValue=dataProvider.getFormatedValue(from,to,context,result);
						logger.log(Level.INFO,"Loading data from : "+from+" to :"+to+" took ... "+(System.currentTimeMillis()-startTime)+" ms ");
						startTime=System.currentTimeMillis();
						result= checkValue(importSetup, fieldSetupValue,context, result);
						if(!result.isSuccess()){
							break;
						}
						 logger.log(Level.INFO,"validating data  from : "+from+" to :"+to+" took ... "+(System.currentTimeMillis()-startTime)+" ms ");
						 System.gc();
					}
					dataProvider.clear();
				} catch (Exception e) {
					ExceptionResult exRes= getExceptionHandler().handel(e,context);
					if(e instanceof ImportDataException){
						String message=MessageHandler.getMessage("invalidRecorDataAtLine", "ir.utopia.core.struts.ImportAction", getUserLocale().getLanguage());
						message=message.replaceAll("@line@", String.valueOf(((ImportDataException)e).getBadDataLineNumber()));
						MessageNamePair error=new MessageNamePair(MessageType.error,
								message);
						exRes.addMessage(error);
					}
					result.setSuccess(false);
					logger.log(Level.WARNING,"", e);
					appendMessages(exRes, result);
				}	
				result.setProcessStatus(ProcessExecutionResult.PROCESS_STATUS_FINISHED);
				confirmResultMap.put(processId, result);
//				setImporting(false);
			}
		}
		,"Data Test Thread");
		t.start();
	}
//*********************************************************************************************
	@Override
	protected Map<String, Object> createContext() {
		Map<String,Object> context=super.createContext();
		context.remove(SESSION_IMPORT_DATA_HANDLER);
		context.remove(SESSION_IMPORT_DATA_PROVIDER);
		context.remove(SESSION_IMPORT_SETUP);
		return context;
	}
//*********************************************************************************************
	@Override
	public ProcessExecutionResult execute(String[] params, String[] values) {
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_DATA_HANDLER);
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_DATA_PROVIDER);
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_SETUP);
		ProcessExecutionResult result;
		Long startTime=System.currentTimeMillis();
		result=super.execute(params, values);
		BeanExecInfo info=new BeanExecInfo(startTime);
		importBeanCache.put( result.getProcessIdentifier(), info);
		return result;
	}	
	
//*********************************************************************************************
	@Override
	public void notifyProcessStatusChanged(ProcessStatusChangeEvent e) {
		ProcessPersistentInfo pinfo=e.getPersistentInfo();
		ProcessBeanInfo beanInfo= beanMap.get(e.getKey());
		logExecutionResult(beanInfo.getKey(), pinfo);
		super.notifyProcessStatusChanged(e);
	}
//*********************************************************************************************
	private ProcessExecutionResult checkValue(ImportSetup importSetup,Map<FieldSetup,Vector<Object>>fieldSetupValue,Map<String,Object> context,ProcessExecutionResult result)throws Exception{
		int errorCount=0;
		for(FieldSetup setup: fieldSetupValue.keySet()){
			errorCount+=testValue(importSetup, setup, fieldSetupValue.get(setup),context, result);
			if(errorCount>MAXIMUM_ERROR_COUNT){
				break;
			}
		}
		return result;
	}

//******************************************************************************************************
	private int testValue(ImportSetup importSetup ,FieldSetup setup,Vector<Object> columnData,Map<String,Object> context,ProcessExecutionResult result){
		String formula=null;// TODO DELETE //setup.getImportFormula();
		int errorCount=0;
		if(formula!=null&&columnData!=null&&columnData.size()>0){
			errorCount= chechNullValue(importSetup, setup, columnData, result);
			if(result.isSuccess()){
				errorCount+= checkLookupValue(importSetup, setup,columnData,context,result);
			}
		}
		return errorCount;
	}
//******************************************************************************************************
	private int  chechNullValue(ImportSetup importSetup ,FieldSetup setup,Vector<Object> columnData,ProcessExecutionResult result){
		int errorCount=0;
		for(int i=0;i<columnData.size();i++){
			Object value=columnData.get(i);
			if((value==null)){
				if(setup.isMandatory()){
					String message= MessageHandler.getMessage("notNullColumn", 
							"ir.utopia.core.struts.ImportAction", getUserLocale().getLanguage());
						message=message.replaceAll("@column@",importSetup.getHeader(setup, getUserLocale().getLanguage()));
						message=message.replace("@row@", String.valueOf(importSetup.getUserFrom()+i));
						result.addErrorMessage(message);
						result.setSuccess(false);
						errorCount++;
				}
			}
			if(errorCount>MAXIMUM_ERROR_COUNT)break;
		}
		return errorCount;
	}
////******************************************************************************************************
	private int  checkLookupValue(ImportSetup importSetup, FieldSetup setup,Vector<Object> columnData,Map<String,Object> context,ProcessExecutionResult result){
		int errorCount=0;
		if(setup.getDisplayType().isLookup()){
			String language=getUserLocale().getLanguage();
			Object []distinctValues=distinct(columnData);
			if(distinctValues!=null){
				for(Object curValue:distinctValues){
					if(curValue==null)continue;
					Object value= getLookupValue(importSetup, setup, curValue, context,result,language);
					if(value==null||
							((Long.class.isInstance(value)||(long.class.isInstance(value)))&&((Long)value)<0) ){
						StringBuffer rowNums=new StringBuffer();
						int count=0;
						for(int i=0;i<columnData.size();i++){
							Object val=columnData.get(i);
							if(curValue.equals(val)){
								if(count>0){
									rowNums.append(",");
								}
								rowNums.append(String.valueOf(importSetup.getUserFrom()+i+1));
								count++;
							}
							if(count>=MAXIMUM_ERROR_COUNT)break;
						}
						
						String header=importSetup.getHeader(setup, language);
						String message= MessageHandler.getMessage("inValidLookupData", "ir.utopia.core.struts.ImportAction", language);
//		TODO DELETE				message= message.replaceAll("@j@", rowNums.toString()).replaceAll("@col@", header).
//						replaceAll("@value@",WebUtil.write(curValue)).replaceAll("@fetcgexp@",WebUtil.write(setup.getImportFormula()) );
						result.addErrorMessage(message);
						result.setSuccess(false);
						errorCount++;
					}
					if(errorCount>MAXIMUM_ERROR_COUNT)break;
				}
			}
		}
		return errorCount;
	}
//******************************************************************************************************
	@Override
	protected String getProcessName() {
		return MessageHandler.getMessage("importProcessName",this.getClass().getName()
				, getUserLocale().getLanguage());
		
	}
//******************************************************************************************************
	@Override
	protected BeanProcessParameter[] getProcessParameters() {
		
		try {
			BeanProcessParameter[] params=super.getProcessParameters();
			BeanProcessParameter[] result=new BeanProcessParameter[params.length+3];
			System.arraycopy(params, 0, result, 0, params.length);
			Map<String,Object>session= null;//ActionContext.getContext().getSession();
			ImportDataProvider dataProvider=(ImportDataProvider)session.get(SESSION_IMPORT_DATA_PROVIDER);
			result[params.length]=new BeanProcessParameter(ImportTransactionalRemote.PERSITENTLIST_DATA_PROVIDER,
					dataProvider);
			ImportSetup importSetup = (ImportSetup) session.get(SESSION_IMPORT_SETUP);
			int userFrom=importSetup.getUserFrom()-1;
			userFrom=userFrom<0?0:userFrom;
			int userTo=importSetup.getUserTo();
			Map<String,Object>context=createContext();
			int recordCount=dataProvider.getSize(context);
			userTo=userTo==0||userTo>recordCount?recordCount:userTo;
			result[params.length+1]=new BeanProcessParameter(ImportTransactionalRemote.RECORD_START_INDEX,
					userFrom);
			result[params.length+2]=new BeanProcessParameter(ImportTransactionalRemote.RECORD_END_INDEX,
					userTo);
			return result;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}

//******************************************************************************************************
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends UtopiaProcessBean> getFacadeClass() {
		
		if(facadeClass==null){
			ImportSetup importSetup = null;//(ImportSetup) ActionContext.getContext().getSession().get(SESSION_IMPORT_SETUP);
			this.facadeClass= (Class<UtopiaProcessBean>) BeanUtil.findRemoteClassFromPersistent(importSetup.getPersistentClass());
			}
		
		return this.facadeClass;
		
	}
//******************************************************************************************************
	public String execute() throws Exception {
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_DATA_HANDLER);
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_DATA_PROVIDER);
		ContextUtil.registerSessionContextExclusionParameter(SESSION_IMPORT_SETUP);
		Map<String,Object> session=null;//ActionContext.getContext().getSession();
		
		UtopiaPageForm pageForm=(UtopiaPageForm)session.get(Constants.PAGE_CONFIG_FORM_NAME);
		try {
			
			List<UsecaseAction >actions= pageForm.getUsecase().getUseCaseActions();
			
			for(UsecaseAction action:actions){
				if(predefindedActions.importData.ordinal()==(new Long(action.getActionId())).intValue()){
					request.setAttribute(USECASE_ACTION_PARAMAETER_NAME,action.getUsecaseActionId());
					request.setAttribute("settingName",request.getParameter("settingName"));
				}	
			}
			
			ImportSetup importSetup=getImportSetup(); 
			ImportDataHandler importHandler=getDataHandler(importSetup);
			Constants.ImportFormat type=importableForm.getFileType();
			String file=null; 
			if(importableForm.getUpload()!=null){
				File temp = File.createTempFile("import_",null);
				temp.deleteOnExit();
				FileUtils.copyFile(importableForm.getUpload()	, temp);
				type=type==null? (temp.getName().endsWith("xls")? Constants.ImportFormat.excel:Constants.ImportFormat.excel):type;
				file=temp.getPath();
				
			}
			importHandler.setFile(file, type);
			Properties props=new Properties();
			props.put(ImportDataHandler.IMPORT_PROPERTIES_EXCEL_SHEET_INDEX, importSetup.getSheetIndex());
			props.put(ImportDataHandler.EXCEL_FIRST_ROW_IS_HEADER, importSetup.isFirstLineTitle());
			String regexp=importSetup.getRegExp();
			regexp=(regexp!=null&&regexp.length()>0)?regexp.trim():null;
			
			if(regexp!=null)
				props.put(ImportDataHandler.TEXT_FETCH_EXPRESSION, regexp);
			String sql=importSetup.getSql();
			sql=(sql!=null&&sql.trim().length()>0)?sql.trim():null;
			if(sql!=null)
				props.put(ImportDataHandler.SQL, sql);	
			String resourceName=importSetup.getResourceName();
			resourceName=(resourceName!=null&&resourceName.trim().length()>0)?resourceName.trim():null;
			if(resourceName!=null)
				props.put(ImportDataHandler.RESOURCE_NAME, resourceName);
			
			importHandler.setProperties(props);
			Map<String,Object>context=createContext();
			boolean success=true;
			int []fromTo=getUserFromAndTo(importSetup, importHandler,context);
			if(fromTo[0]<0){
				success=false;
//				addActionError(MessageHandler.getMessage("invalidFromToIndex","ir.utopia.core.struts.ImportAction",getUserLocale().getLanguage()));
			}
			if(fromTo[1]<0){
				success=false;
//				addActionError(MessageHandler.getMessage("emptyFileError", 
//						"ir.utopia.core.struts.ImportAction", getUserLocale().getLanguage()));
			}
			session.put(SESSION_IMPORT_SETUP, importSetup);
			session.put(SESSION_IMPORT_DATA_HANDLER, importHandler);
			
			return success?"SUCCESS":"ERROR";
		} catch (Exception e) {
			ExceptionResult exRes= getExceptionHandler().handel(e, createContext());
			logger.log(Level.WARNING,"", e);
			if(pageForm!=null){
				ArrayList<MessageNamePair>pairs=new ArrayList<MessageNamePair>();
				pairs.addAll(exRes.getMessages());
				session.put(ACTION_MESSAGES_SESSION_KEY, pairs);
			}
			return "ERROR";
		}
	}
//********************************************************************************************************
	private int[] getUserFromAndTo(ImportSetup importSetup,ImportDataHandler importHandler,Map<String,Object>context)throws Exception{
		int userFrom=importSetup.getUserFrom();
		int userTo=importSetup.getUserTo();
		
		if(userTo<0||(userTo>0&&userTo<=userFrom)||userFrom<0){
			return new int[]{-1,-1};
			
		}
		int recordCount=importHandler.getRecordCount(context);
		if(recordCount<=0||(recordCount<userFrom)){
			return new int []{0,-1};
		}
		userTo=userTo==0||userTo>recordCount?recordCount:userTo;
		return new int[]{userFrom,userTo};
	}
//********************************************************************************************************
	private void appendMessages(ExceptionResult exRes,ProcessExecutionResult result){
		List<MessageNamePair>messages= exRes.getMessages();
		if(messages!=null&&messages.size()>0){
			for(MessageNamePair pair:messages){
				if(MessageType.error.equals(pair.getType())){
					result.addErrorMessage(pair.getMessage());
				}else if(MessageType.warning.equals(pair.getType())){
					result.addWarningMessage(pair.getMessage());
				}else if(MessageType.info.equals(pair.getType())){
					result.addInfoMessage(pair.getMessage());
				}
			}
		}
	}
//********************************************************************************************************
	private ImportDataHandler getDataHandler(ImportSetup importSetup)throws Exception{
		Object []o=new Object[0];
		Class<?>clazz= importSetup.getDataHandlerClass();
		return (ImportDataHandler)ConstructorUtils.invokeConstructor(clazz, o);
	}

//********************************************************************************************************
	public ArrayList<MessageNamePair> getMessages(ExecutionResult result){
		ArrayList<MessageNamePair>pairs=new ArrayList<MessageNamePair>();
		if(result.getErrorMessages()!=null){
			for(String error:result.getErrorMessages()){
				pairs.add(new MessageNamePair(MessageType.error,error));
			}
		}
		if(result.getWarningMessages()!=null){
			for(String warn:result.getWarningMessages()){
				pairs.add(new MessageNamePair(MessageType.warning,warn));
			}
		}
		if(result.getInfoMessages()!=null){
			for(String info:result.getInfoMessages()){
				pairs.add(new MessageNamePair(MessageType.info,info));
			}
		}
		return pairs;
	}
//********************************************************************************************************
	public T getModel() {
		if (importableForm == null)
			importableForm = createModelInstance();
		return importableForm;
	}
//********************************************************************************************************
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}
//********************************************************************************************************
	public ImportSetup getImportSetup(){
		AbstractUtopiaForm<?> f = importableForm.getForm();
		UtopiaFormMetaData formMeta=f.getMetaData();
		ImportSetup importSetup = new ImportSetup(formMeta,getUserLocale().getLanguage());
		UtopiaFormMethodMetaData[] methodsMD = f.getMetaData().getMethodMetaData();
		List<FieldSetup> fieldsSetup=new ArrayList<FieldSetup>(); 
		for(UtopiaFormMethodMetaData methodMD:methodsMD){
			InputItem inputItem = (InputItem)methodMD.getAnnotation(InputItem.class);
			if(inputItem != null){
				FieldSetup fieldSetup = new FieldSetup(methodMD);
//				fieldSetup.setImportFormula(request.getParameter(methodMD.getFieldName()+ "Excel")); TODO delete
				fieldSetup.setType(request.getParameter(methodMD.getFieldName()+ "Type"));
				String paramValue=request.getParameter("form."+methodMD.getFieldName());
				paramValue=paramValue==null?null:paramValue.trim();
				if(methodMD.getDisplayType().isLookup()){
					
					try {
						Long l=Long.parseLong(paramValue);
						if(l.intValue()>0)
							fieldSetup.setDefaultValue(l);
					} catch (Exception e) {
						logger.log(Level.WARNING,"", e);	
					}
					
				}else{//TODO currently only string type default value is supported
					fieldSetup.setDefaultValue(paramValue==null||paramValue.trim().length()==0?null:paramValue.trim());
				}
				fieldsSetup.add(fieldSetup);
			}
		}
		importSetup.setMethodMetaData(fieldsSetup.toArray(new FieldSetup[fieldsSetup.size()]));
		importSetup.setFirstLineTitle(importableForm.isFirstLineTitle());
		int userFrom,userTo;
		userFrom=importableForm.getFrom();
		userFrom=userFrom<0?0:userFrom;
		userTo=importableForm.getTo();
		importSetup.setUserFrom(userFrom);
		importSetup.setUserTo(userTo);
		importSetup.setSheetIndex(importableForm.getSheetIndex()<=1?0:importableForm.getSheetIndex()-1);
		importSetup.setRegExp(importableForm.getRegexp());
		importSetup.setSql(importableForm.getSqlText());
		importSetup.setResourceName(importableForm.getResourceName());
		return importSetup;
	}

//********************************************************************************************************
	protected void logExecutionResult( Long processUID,ProcessPersistentInfo result) {
		if(result!=null)
		{
			 List<MessageNamePair>pairs= result.getMessages();
			if(pairs!=null){
				try {
					BeanExecInfo info=importBeanCache.get(processUID)==null?
							new BeanExecInfo(System.currentTimeMillis()):importBeanCache.get(processUID);
					info.entTime=System.currentTimeMillis();
					UtopiaBasicPersistent []invalids= result.getInvlidPersistents();
					UtopiaBasicPersistent []valids= result.getValidPersistents();
					info.invalidCount+= invalids!=null?invalids.length:0;
					info.validCount+= valids!=null?valids.length:0;
					serializeToFile(logMap,pairs.toArray(new MessageNamePair[pairs.size()]),processUID);
					info.LogMap=logMap;
				}
				 catch (Exception e) {
					logger.log(Level.WARNING,"", e);
				}
				}
		}	
	}
//********************************************************************************************************
	private void serializeToFile(Map<int[],String>map,MessageNamePair []p,Long pid){
		try {
			 writeToFile(map,p, MESSAGE_LOG_FILE_SIZE, pid);
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		} 
//********************************************************************************************************
	public static int getLogPageCount(Long UID,HttpSession session){
		if(importBeanCache.containsKey(UID)){
			BeanExecInfo info=importBeanCache.get(UID);
			return info.LogMap==null?-1:info.LogMap.size();
		}
		return -1;
	}
//********************************************************************************************************
	public static int getLogCount(Long UID,HttpSession session){
		if(importBeanCache.containsKey(UID)){
			BeanExecInfo info=importBeanCache.get(UID);
			int size=0;
			if(info.LogMap!=null){
				for(int[] key:info.LogMap.keySet()){
					size+=Math.abs(key[1]-key[0])+1;
				}
			}
			return size;
		}
		return -1;
	}
//********************************************************************************************************
	public static Vector<Map<String,Object>> getImportLog(Long UID,int from,int to,String settingName,HttpSession session){
		Vector<Map<String,Object>>dataVector =new Vector<Map<String,Object>>();
		if(importBeanCache.containsKey(UID)){
			
			BeanExecInfo info=importBeanCache.get(UID);
			if(info.LogMap!=null){
					String language=WebUtil.getLanguage(session);
					int validRecordCount=info.validCount,
					invalidRecordCount=info.invalidCount;
					
					
					String currentTime="fa".equals(language)?DateUtil.convertGregorianToSolarString(new Date(System.currentTimeMillis())):
								new Date(System.currentTimeMillis()).toString();
					Date startDate=new Date(info.startTime);
					Calendar c=Calendar.getInstance();
					c.setTime(startDate);
					String startTime="fa".equals(language)?(DateUtil.convertGregorianToSolarString(startDate)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND))
															:startDate.toString();
					Date endDate=new Date(info.entTime);
					c.setTime(endDate);
					String endtime="fa".equals(language)?DateUtil.convertGregorianToSolarString(endDate)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)
														:endDate.toString();
					int pageCount=getLogPageCount(UID, session);
					List<MessageNamePair>messages=new ArrayList<MessageNamePair>();
					List<int[]>messageKeykeys=new ArrayList<int[]>();
					String status;
					if(validRecordCount==0){
						if(invalidRecordCount==0){
							status=MessageHandler.getMessage("ImportLogStatusNoRecordImported", "ir.utopia.core.struts.ImportAction", language);
						}else{
							status=MessageHandler.getMessage("ImportLogStatusImportedByWarning", "ir.utopia.core.struts.ImportAction", language);
						}
					}else{
						if(invalidRecordCount==0){
							status=MessageHandler.getMessage("ImportLogStatusImportSucessFully", "ir.utopia.core.struts.ImportAction", language);
						}else{
							status=MessageHandler.getMessage("ImportLogStatusImportedByWarning", "ir.utopia.core.struts.ImportAction", language);
						}
					}
					if(pageCount==1){
						messageKeykeys.addAll(info.LogMap.keySet());
					}else{
						int size=0;
						for(int[]key: info.LogMap.keySet()){
							if(key[0]==from){
								messageKeykeys.add(key);
								to=key[1];
								size+=key[0]-key[1]+1;
								if(size>=LOAD_PAGE_SIZE)break;
							}
						}
					}
					try {
						if(messageKeykeys.size()==0){
						logger.log(Level.WARNING,"message key for "+from+".."+to+" not found");	
						}else{
							
						L1:	for(int key[]:messageKeykeys){
							 MessageNamePair []temp= readFromFile(info.LogMap.get(key));
							 if(temp!=null){
								 for(MessageNamePair pair:temp){
									 messages.add(pair);
									 if(messages.size()==LOAD_PAGE_SIZE)break L1; 
								 }
							 }
							}
							
						}
					} catch (Exception e) {
						logger.log(Level.WARNING,"", e);
					}
					if(messages.size()==0){
						messages.add(new MessageNamePair(MessageType.info,""));
					}
					for(MessageNamePair pair:messages ){
						Map<String,Object>t=new HashMap<String, Object>();
						t.put("CO_IMPORT_LOG_STATUS",status);
						t.put("CO_IMPORT_LOG_END_TIME", endtime);
						t.put("CO_IMPORT_LOG_START_TIME",startTime);
						t.put("CUR_DATE",currentTime);
						t.put("MSG",pair.getMessage());
						t.put("LAST_IMPORTED_PK",  new BigDecimal(0));
						
						t.put("IMPORT_RECORD_COUNT", new BigDecimal(validRecordCount));
						
						t.put("TOTAL_RECORD_COUNT", new BigDecimal(validRecordCount+invalidRecordCount));
						t.put("SETTINGNAME", WebUtil.write(settingName));
						t.put("CO_IMPORT_LOG_ID", new BigDecimal(1));
						dataVector.add(t);
					
					}
			}
		}
		return dataVector;
	}
//********************************************************************************************************
	private  void writeToFile(Map<int[],String>map,MessageNamePair []messages,int fileSize,Long identifier) throws IOException { 
		if(messages==null||messages.length==0)return;
		int from=1;
		int offset=0;
		if(map!=null&&map.size()>0){
			for(int[] key:map.keySet()){
				offset+=Math.abs(key[1]-key[0])+1;
			}
		}
		int to;
		List<Object[]>arrays=null;
		arrays= ArrayUtils.breakArray(messages, fileSize);
		if(arrays!=null)
		for(Object []o:arrays){
			if(o.length==0)continue;
			to=o.length+from-1;
			String fileName=getFileName(from+offset, to+offset, identifier);
			File temp=File.createTempFile(fileName, null);
	        FileOutputStream f = new FileOutputStream( temp);
	        ObjectOutputStream s = new ObjectOutputStream(f);
	        MessageNamePair []pair=new MessageNamePair[o.length]; 
	        System.arraycopy(o, 0, pair, 0, o.length);
	        s.writeObject(pair); 
	        s.flush(); 
	        s.close();
	        map.put(new int[]{from+offset,to+offset}, temp.getAbsolutePath());
	        from=to+1;
        }
		
    } 
//********************************************************************************************************
	public static List<Object[]>  append(MessageNamePair []p1,MessageNamePair []p2){
		int size=p1.length+p2.length;
		MessageNamePair []temp=new MessageNamePair[size];
		System.arraycopy(p1, 0, temp, 0, p1.length); 
		System.arraycopy(p2, 0, temp, p1.length, p2.length);
		List<Object[]>tempRes;
		if(size<=MESSAGE_LOG_FILE_SIZE){
			tempRes=new ArrayList<Object[]>();
			tempRes.add(temp);
		}else{
			tempRes= ArrayUtils.breakArray(temp, MESSAGE_LOG_FILE_SIZE);	
		}
		return  tempRes;
	}
//********************************************************************************************************
private static String getFileName(int from,int to,Long identifier){
	return File.separator+
	"from_"+from+"_to_"+to+"_"+identifier+".fo";
}
//********************************************************************************************************
private static MessageNamePair[] readFromFile(String fileName)  throws IOException, ClassNotFoundException { 
        FileInputStream in = new FileInputStream(fileName); 
        ObjectInputStream s = new ObjectInputStream(in); 
        MessageNamePair []pair = (MessageNamePair []) s.readObject(); 
        return pair;
    } 
//********************************************************************************************************
	private static class BeanExecInfo{
		Long startTime,entTime;
		int validCount;
		int invalidCount;
		Map<int[],String>LogMap;
		
		
		public BeanExecInfo(Long startTime) {
			super();
			this.startTime = startTime;
		}
		
		
	}
}
