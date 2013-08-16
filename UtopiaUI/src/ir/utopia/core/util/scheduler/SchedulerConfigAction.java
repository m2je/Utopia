package ir.utopia.core.util.scheduler;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import ir.utopia.common.basicinformation.businesspartner.bean.CmBpartnerFacadeRemote;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.systems.subsystem.bean.CmSubsystemFacadeRemote;
import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.bean.CmSystemFacadeRemote;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.constants.QueryComparsionType;
import ir.utopia.core.mailtemplate.bean.CoMailTemplateFacadeRemote;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.smtp.bean.CoSmtpAddressesFacadeRemote;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.usecase.bean.CoUsecaseFacadeRemote;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.bean.CoUsecaseActionFacadeRemote;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.exceptionhandler.ExceptionHandler;
import ir.utopia.core.util.exceptionhandler.ExceptionResult;
import ir.utopia.core.util.scheduler.bean.QrtzCronTriggersFacadeRemote;
import ir.utopia.core.util.scheduler.bean.QrtzMailRecepientsFacadeRemote;
import ir.utopia.core.util.scheduler.persistent.JobGroup;
import ir.utopia.core.util.scheduler.persistent.QrtzCronTriggers;
import ir.utopia.core.util.scheduler.persistent.QrtzMailRecepients;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridValueModel;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.converter.AbstractUtopiaUIHandler;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.utilitypanel.client.model.RecepientInfo;
import ir.utopia.core.util.tags.utilitypanel.client.model.ScheduleInfo;
import ir.utopia.core.util.tags.utilitypanel.client.model.SchedulerConfigurationModel;
import ir.utopia.core.util.tags.utilitypanel.client.model.SchedulerDefinitionService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
public class SchedulerConfigAction  extends UtopiaBasicAction implements SchedulerDefinitionService,ServletContextAware {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(SchedulerConfigAction.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8936001964737594863L;
	private ServletContext servletContext;
	public static final String JOB_DETAIL_DESCRIPTION_KEY="__DESCRIPTION";
	public static final String JOB_DETAIL_USCASE_ACTION_KEY="__USECASE_ACTION";
	public static final String JOB_DETAIL_CUSTOM_PROPS_KEY="__CUSTOM_PROPS_KEY_";
	public static final String JOB_DETAIL_CUSTOM_PROPS_VALUE="__CUSTOM_PROPS_VALUE_";
	
	public static final String JOB_DETAIL_MAIL_TEMPLATE="MAIL_TEMPLATE_";
	public static final String JOB_DETAIL_MAIL_SENDER="MAIL_SENDER_";
	
	public static final String JOB_DETAIL_OWNER_ID="TASK_OWNER_ID";
	public static final String JOB_DETAIL_LANGUAGE="TASK_LANGUAGE";
	
	public static final String JOB_DETAIL_SOLAR_REAL_DAY_OF_MONTH="SolarRealDayOfMonth_";
	
	public static final String SCHEDULER_DEFAULT_NAME="QuartzScheduler";
	
	public static final String SCHEDULER_WEB_APPLICATION_ROOT_DIR="ROOT_PATH";
//******************************************************************************************************
	public String getServletRootPath(){
		String webXML=servletContext.getRealPath("web.xml");
		webXML=webXML.substring(0,webXML.lastIndexOf(File.separatorChar));
		return webXML;
	} 
	
//******************************************************************************************************
	@Override
	public UILookupInfo loadConfigLookup(int type) {
		Scheduler scheduler= lookupScheduler();
		String [] columns=new String []{"key","value","description","usecaseaction"};
		String [][]data=new String [][]{};
		try {
			JobGroup group=type==SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL?JobGroup.Email:JobGroup.Process;
			Set<JobKey>jobKeys= scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group.toString()));
			if(jobKeys!=null&&jobKeys.size()>0){
				data=new String[jobKeys.size()][2];
				int i=0;
				for(JobKey key:jobKeys){
					JobDetail jobDetail= scheduler.getJobDetail(key);
					
					jobDetail.getJobDataMap().getString(JOB_DETAIL_USCASE_ACTION_KEY);
					jobDetail.getJobDataMap().getString(JOB_DETAIL_DESCRIPTION_KEY);
					data[i][0]= key.getName();
					data[i++][1]= key.getName();
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		UILookupInfo res=new UILookupInfo(columns,data);
		return res;
	}
//******************************************************************************************************
	protected Scheduler  lookupScheduler(){
		try {
			StdSchedulerFactory sf = (StdSchedulerFactory) servletContext
			.getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);
			Scheduler scheduler ;
			if(sf==null){
				logger.log(Level.WARNING,"fail to lookup schedule from servlet context,using default scheduler");
				scheduler=StdSchedulerFactory.getDefaultScheduler();
			}else{
				scheduler = sf.getScheduler();	
			}
			return scheduler;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//******************************************************************************************************
	@Override
	public ProcessExecutionResult saveConfiguration(SchedulerConfigurationModel configuration) {
		 return createOrUpdateSchedule(configuration, predefindedActions.save);
	}
//******************************************************************************************************
	private ProcessExecutionResult createOrUpdateSchedule(SchedulerConfigurationModel configuration,predefindedActions action){
		ProcessExecutionResult result=new ProcessExecutionResult();
		Map<String,Object>context=super.createContext();
		try {
			JobGroup group=configuration.getType()==SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL?JobGroup.Email:JobGroup.Process;
			if(!action.equals(predefindedActions.save)&&!configuration.getConfigurationName().equals(configuration.getConfigurationNewName())){
				logger.log(Level.INFO,"Scheduler name has changed ,recreating scheduler");
				deleteConfiguration(configuration.getType(), configuration.getConfigurationName());
				action=predefindedActions.save;
			}
			 JobDetail job =action.equals(predefindedActions.save)? newJob(UtopiaScheduledJob.class)
	            .withIdentity(configuration.getConfigurationNewName(), group.name())
	            .build():findJobDetail(configuration);
			 JobDataMap dataMap= job.getJobDataMap();
//			 dataMap.clear();
			 if(configuration.getDescription()!=null)
				 dataMap.put(JOB_DETAIL_DESCRIPTION_KEY, configuration.getDescription());
			 if(configuration.getUsecaseActionId()!=null)
				 dataMap.put(JOB_DETAIL_USCASE_ACTION_KEY, String.valueOf(configuration.getUsecaseActionId()));
			 initCustomProps(configuration, dataMap);
			 
			 ScheduleInfo scheduleInfo=configuration.getScheduleInfo();
			 Date startDate,endDate;
			 Date[] d=getStartEndDate(configuration);
			 startDate=d[0];
			 endDate=d[1];
			 String cronStr = getScheduleCronStr(context, startDate,
					scheduleInfo);
			 dataMap.put(JOB_DETAIL_OWNER_ID, ContextUtil.getUserId(context));
			 dataMap.put(JOB_DETAIL_LANGUAGE,ContextUtil.getLoginLanguage(context));
			 dataMap.put(SCHEDULER_WEB_APPLICATION_ROOT_DIR, getServletRootPath());
			 if(ScheduleInfo.SCHEDULE_TYPE_MONTHLY== configuration.getScheduleInfo().getScheduleType()&&WebUtil.isUsingSolarDate(context)){
				 dataMap.put(JOB_DETAIL_SOLAR_REAL_DAY_OF_MONTH, configuration.getScheduleInfo().getDaysOfMounth()[0]) ;//TO handle unknown days firing
			 }
			 if(group==JobGroup.Email){
				  RecepientInfo info= configuration.getRecepients();
				  dataMap.put(JOB_DETAIL_MAIL_TEMPLATE, info.getSelectedTemplate());
				  dataMap.put(JOB_DETAIL_MAIL_SENDER, info.getSelectedUsername());
			  }
			 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			logger.log(Level.INFO,"Scheduling job ,with cron trigger with value:"+cronStr+" which starts at :"+df.format(startDate)+(endDate!=null ?" and finishes at:"+df.format(endDate):""));
			 TriggerBuilder<?>tbuilder= newTrigger()
			    .withIdentity("trigger_"+group.name()+"_"+configuration.getConfigurationNewName(),  group.name())
			    .withSchedule(cronSchedule(cronStr))
			    .forJob(configuration.getConfigurationNewName(), group.name());
			 if(startDate!=null){
				 tbuilder=tbuilder.startAt(startDate);
			 }
			 if(endDate!=null){
				 tbuilder=tbuilder.endAt(endDate);
			 }
			 
			  Trigger trigger = tbuilder.build();
			  
			  Scheduler scheduler=lookupScheduler();
			  if(predefindedActions.save.equals(action)){
				  scheduler.scheduleJob(job, trigger);
			  	}
			  else{
				  Trigger t=findJobTrigger(group, job.getKey());
				  scheduler.rescheduleJob(t.getKey(), trigger);
			  }
			  
			  
			  if(!scheduler.isStarted())
				  scheduler.start();
			  
			  if(group==JobGroup.Email){
				  saveMailRecepients(configuration,group);
			  }
				logger.log(Level.WARNING,"Job instance created with name :"+configuration.getConfigurationNewName()+
					  " for group :"+ group.name());
			  result.setSuccess(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
			ExceptionResult exresult= getExceptionHandler().handel(e, context);
			
			initProcessResultMessges(result, exresult.getMessages());
		}
		return result;
	} 
//******************************************************************************************************
	private JobDetail findJobDetail(SchedulerConfigurationModel configuration){
		try {
			Scheduler scheduler= lookupScheduler();
			JobGroup group=configuration.getType()==SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL?JobGroup.Email:JobGroup.Process;
			Set<JobKey>jobKeys= scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group.toString()));
			if(jobKeys!=null&&jobKeys.size()>0){
				for(JobKey key:jobKeys){
					JobDetail jobDetail= scheduler.getJobDetail(key);
					if(group.name().equals(key.getGroup())&& key.getName().equals(configuration.getConfigurationName())){
						return jobDetail;
						}
					}
				}
		} catch (SchedulerException e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//******************************************************************************************************
	private Trigger findJobTrigger(JobGroup group,JobKey key){
		try {
			Scheduler scheduler= lookupScheduler();
			String triggerKey="trigger_"+group.name()+"_"+key.getName();
			Trigger trigger=scheduler.getTrigger(new TriggerKey(triggerKey,  group.name()));
			return trigger;
		} catch (SchedulerException e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}

//******************************************************************************************************
	private void saveMailRecepients( SchedulerConfigurationModel configuration,JobGroup jobGroup){
		 RecepientInfo recepientsInfo= configuration.getRecepients();
		Long[] infos= recepientsInfo.getSelectedRecepients();
		try {
			QrtzMailRecepientsFacadeRemote bean=(QrtzMailRecepientsFacadeRemote)ServiceFactory.lookupFacade(QrtzMailRecepientsFacadeRemote.class);
			ArrayList<Long> ids=  new ArrayList<Long>();
			if(infos!=null){
				for(Long l:infos){
					ids.add(l);
				}
			}
			bean.saveMailRecepients(ids,SCHEDULER_DEFAULT_NAME , configuration.getConfigurationNewName(), jobGroup, super.createContext());
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//******************************************************************************************************
	private void initCustomProps(SchedulerConfigurationModel configuration,
			JobDataMap dataMap) {
		String []customPropsKeys=configuration.getCustomPropertyNames();
		 if(customPropsKeys!=null){
			 String []customPropsValues=configuration.getCustomPropertyValues();
			 int index=0;
			 for(String customPropKey:customPropsKeys){
				 dataMap.put(JOB_DETAIL_CUSTOM_PROPS_KEY+""+index, customPropKey);
				 dataMap.put(JOB_DETAIL_CUSTOM_PROPS_VALUE+""+index, customPropsValues[index++]);
			 }
		 }
	}
//******************************************************************************************************	
	private Date[] getStartEndDate(SchedulerConfigurationModel configuration){
		Date startDate=new Date(),endDate=null; 
		try {
			
			 if(configuration.getStartDate()!=null&&configuration.getStartDate().trim().length()>0){
				 
				 if(DateUtil.useSolarDate(getUserLocale()) ){
					 startDate=DateUtil.solarToDate(configuration.getStartDate());
					}else{
					 startDate=DateUtil.gerToDate(configuration.getStartDate());
					}
				 
			 }
			 if(configuration.getEnddate()!=null&&configuration.getEnddate().trim().length()>0){
				 if(DateUtil.useSolarDate(getUserLocale()) ){
					 endDate=DateUtil.solarToDate(configuration.getEnddate());
					}else{
						endDate=DateUtil.gerToDate(configuration.getEnddate());
					}
				
			 }
			 ScheduleInfo scheduleInfo=configuration.getScheduleInfo();
			 SimpleDateFormat df=new SimpleDateFormat("hh:mm");
			 if(scheduleInfo.getStartHour()!=null&&scheduleInfo.getStartHour().trim().length()>0){
				 Date d=df.parse(scheduleInfo.getStartHour().trim());
				 Calendar startDateC=Calendar.getInstance();
				 Calendar temp=Calendar.getInstance();
				 temp.setTime(d);
				 startDateC.setTime(startDate);
				 startDateC.set(Calendar.HOUR_OF_DAY,temp.get(Calendar.HOUR_OF_DAY));
				 startDateC.set(Calendar.MINUTE,temp.get(Calendar.MINUTE));
				 startDate=startDateC.getTime();
			 }
			 if(endDate!=null&&scheduleInfo.getFinishHour()!=null&&scheduleInfo.getFinishHour().trim().length()>0){
				 Date d=df.parse(scheduleInfo.getFinishHour().trim());
				 Calendar endDateC=Calendar.getInstance();
				 Calendar temp=Calendar.getInstance();
				 temp.setTime(d);
				 endDateC.setTime(endDate);
				 endDateC.set(Calendar.HOUR_OF_DAY,temp.get(Calendar.HOUR_OF_DAY));
				 endDateC.set(Calendar.MINUTE,temp.get(Calendar.MINUTE));
				 endDate=endDateC.getTime();
			 }
		} catch (Exception e) {
			logger.log(Level.WARNING,"fail to parse start and end date");
			logger.log(Level.WARNING,"",e);
		}
		return new Date[]{startDate,endDate};
	}
//******************************************************************************************************
	private String getScheduleCronStr(Map<String, Object> context,
			Date startDate, ScheduleInfo scheduleInfo) {
		String daysOfWeekSTR="*";
		 String second="0";
		 String min="*";
		 String hour="*";
		 String daysOfMonth="*";
		 String month="*";
		 String year="*";
		 String cronStr;
		 switch(scheduleInfo.getScheduleType()){
		 	case ScheduleInfo.SCHEDULE_TYPE_HOURLY:{
		 		daysOfWeekSTR=getDaysOfWeekStr(scheduleInfo.getDaysOfWeek());
		 		min="*/"+String.valueOf(scheduleInfo.getInterval());
		 		daysOfMonth="?";
		 	}break;
		 	case ScheduleInfo.SCHEDULE_TYPE_DAILY:{
		 		daysOfWeekSTR=getDaysOfWeekStr(scheduleInfo.getDaysOfWeek());
		 		Calendar c=Calendar.getInstance();
		 		c.setTime(startDate);
		 		hour=String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		 		min=String.valueOf(c.get(Calendar.MINUTE));
		 		daysOfMonth="?";
		 	}break;
		 	case ScheduleInfo.SCHEDULE_TYPE_MONTHLY:{
		 		Calendar c=Calendar.getInstance();
		 		c.setTime(startDate);
		 		hour=String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		 		min=String.valueOf(c.get(Calendar.MINUTE));
		 		daysOfWeekSTR="?";
		 		if(WebUtil.isUsingSolarDate(context)){
		 			daysOfMonth="";
		 			logger.log(Level.INFO,"schedule as monthly with solar date");
		 			int dayOfMonth= scheduleInfo.getDaysOfMounth()[0];
		 			Set<Integer>daysSet=new HashSet<Integer>();
		 			for(int i=1;i<=12;i++){
		 				int []date= DateUtil.convertGregorianToSolar(startDate);
		 				int[] d= DateUtil.convertSolarToGregorian(date[0], i, i<=6?(dayOfMonth>=31?31:dayOfMonth):(i<12?(dayOfMonth>=30?30:dayOfMonth):dayOfMonth>=29?29:dayOfMonth)) ;
		 				daysSet.add(d[2]);
		 			}
		 			int index=0;
		 			for(int d:daysSet){
		 				daysOfMonth+=(index++>0?",":"")+d;
		 			}
		 			
		 			month="1-12";
		 		}else{
			 		daysOfMonth=String.valueOf(scheduleInfo.getDaysOfMounth()[0]);
		 		}
		 	}break;
		 	
		 	case ScheduleInfo.SCHEDULE_TYPE_YEARLY:{
		 		Date d;
		 		if(WebUtil.isUsingSolarDate(context)){
		 			d=DateUtil.solarToDate(scheduleInfo.getDayOfYear());
		 		}else{
		 			d=DateUtil.gerToDate(scheduleInfo.getDayOfYear());
		 		}
		 		Calendar c=Calendar.getInstance();
		 		c.setTime(d);
		 		hour=String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		 		min=String.valueOf(c.get(Calendar.MINUTE));
		 		daysOfMonth=String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		 		month=String.valueOf(c.get(Calendar.MONTH));
		 	}break;
		 }
		  cronStr=second+" "+min+" "+hour+" "+daysOfMonth+" "+month+" "+daysOfWeekSTR+" "+year+"";
		return cronStr;
	}
//******************************************************************************************************
	private String getDaysOfWeekStr(Integer []days){
 		Arrays.sort(days);
 		String daysOfWeekSTR="";
 		for(int day: days){
 			if(daysOfWeekSTR.length()>0){
 				daysOfWeekSTR+=",";
 			}
 			daysOfWeekSTR+=day;
 		}
 		return daysOfWeekSTR;
	}
//******************************************************************************************************
	@Override
	public ProcessExecutionResult deleteConfiguration(int type,String configurationName) {
		ProcessExecutionResult result=new ProcessExecutionResult(true, ProcessExecutionResult. PROCESS_STATUS_FINISHED);
		logger.log(Level.INFO,"about to delete job detail type:"+type+" and configurationName:"+configurationName);
		Scheduler scheduler= lookupScheduler();
		try {
			JobGroup group=type==SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL?JobGroup.Email:JobGroup.Process;
			Set<JobKey>jobKeys= scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group.toString()));
			if(jobKeys!=null&&jobKeys.size()>0){
				for(JobKey key:jobKeys){
					if(key.getName().equals(configurationName)){
						if(scheduler.deleteJob(key)){
							logger.log(Level.INFO,"job detail for type:"+type+" and configurationName:"+configurationName+" was successully removed");
						}
						logger.log(Level.INFO,"job detail for type:"+type+" and configurationName:"+configurationName+" was not removed");
					}
				}
			}
		}catch(Exception e){
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
			ExceptionResult exresult= getExceptionHandler().handel(e, super.createContext());
			initProcessResultMessges(result, exresult.getMessages());
		}
		return result;
	}
//******************************************************************************************************
	@Override
	public SchedulerConfigurationModel loadConfiguration(int configType,String configName) {
		Map<String,Object>context=super.createContext();
		try {
			Scheduler scheduler= lookupScheduler();
			JobGroup group=configType==SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL?JobGroup.Email:JobGroup.Process;
			Set<JobKey>jobKeys= scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group.toString()));
			if(jobKeys!=null&&jobKeys.size()>0){
				for(JobKey key:jobKeys){
					JobDetail jobDetail= scheduler.getJobDetail(key);
					if(group.name().equals(key.getGroup())&& key.getName().equals(configName)){
						SchedulerConfigurationModel result=new SchedulerConfigurationModel();
						result.setConfigurationName(key.getName());
						result.setDescription(jobDetail.getJobDataMap().getString(JOB_DETAIL_DESCRIPTION_KEY));
						JobDataMap dataMap= jobDetail.getJobDataMap();
						initUsecaseActions(key, result, dataMap);
						initCustomProperties(result, dataMap);
						String triggerKey="trigger_"+group.name()+"_"+key.getName();
						Trigger trigger=scheduler.getTrigger(new TriggerKey(triggerKey,  group.name()));
						Locale l=new Locale(ContextUtil.getLoginLanguage(context));
						if(trigger.getStartTime()!=null)
							result.setStartDate(DateUtil.getDateString(trigger.getStartTime(), l));
						if(trigger.getEndTime()!=null)
							result.setEnddate(DateUtil.getDateString(trigger.getEndTime(), l));
						String cronStr= getCronSchedule(group, triggerKey);
//						if(cronStr==null)
//							cronStr="*/12 * * * 3,6 ?";
						ScheduleInfo info = getScheduleInfo(trigger, cronStr,jobDetail.getJobDataMap().get(JOB_DETAIL_SOLAR_REAL_DAY_OF_MONTH));
						result.setScheduleInfo(info);
						result.setRecepients(getMailRecepients(jobDetail,group, key.getName()));
						return result;
						}
					}
					
				}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//******************************************************************************************************
	private RecepientInfo getMailRecepients(JobDetail jobDetail,JobGroup group,String jobName){
		RecepientInfo result=new RecepientInfo();
		try {
			QrtzMailRecepientsFacadeRemote facade=(QrtzMailRecepientsFacadeRemote)ServiceFactory.lookupFacade(QrtzMailRecepientsFacadeRemote.class.getName());
			List<QrtzMailRecepients> mailRecepients= facade.findScheduledTaskRecepients(SCHEDULER_DEFAULT_NAME, jobName, group, super.createContext());
			if(mailRecepients!=null){
				String [][]data= new String[mailRecepients.size()][2];
				int index=0;
				for(QrtzMailRecepients mailRecepient:mailRecepients){
					data[index][0]=String.valueOf(mailRecepient.getId().getCmBpartnerId());
					CmBpartner bpartner=mailRecepient.getCmBpartner();
					data[index++][1]=String.valueOf(bpartner.getCode()+"-"+bpartner.getName()+"-"+bpartner.getSecoundName());
				}
				UILookupInfo recepients=new UILookupInfo(new String[]{"key","value"}, data);
				result.setRecepients(recepients);
				try {
					result.setSelectedUsername(Long.parseLong(String.valueOf(jobDetail.getJobDataMap().get(JOB_DETAIL_MAIL_SENDER))));
				} catch (Exception e) {
				}
				try {
					result.setSelectedTemplate(Long.parseLong(String.valueOf(jobDetail.getJobDataMap().get(JOB_DETAIL_MAIL_TEMPLATE))));
				} catch (Exception e) {
					
				}
				
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return result;
	}
//******************************************************************************************************
	private ScheduleInfo getScheduleInfo(Trigger trigger, String cronStr,Object solarDayOfMonth) {
		int scheduleType= CronHelper.getScheduleType(cronStr);
		ScheduleInfo info=new ScheduleInfo();
		info.setScheduleType(scheduleType);
		info.setDaysOfWeek(CronHelper.getDayofWeek(cronStr));
		info.setStartHour(CronHelper.getHourAndMinute(trigger.getStartTime()));
		info.setFinishHour(CronHelper.getHourAndMinute(trigger.getEndTime()));
		info.setInterval(CronHelper.getInterval(cronStr));
		if(solarDayOfMonth!=null){
			info.setDaysOfMounth(Integer.parseInt(String.valueOf(solarDayOfMonth)));
		}else{
			info.setDaysOfMounth(CronHelper.getDaysOfMonth(cronStr));
		}
		
		return info;
	}
//******************************************************************************************************
	private String getCronSchedule(JobGroup group,String jobName ){
		try {
			Map<String,Object>context=super.createContext();
			QrtzCronTriggersFacadeRemote bean=(QrtzCronTriggersFacadeRemote)ServiceFactory.lookupFacade(QrtzCronTriggersFacadeRemote.class);
			List<QrtzCronTriggers>results= bean.findByProperties(new String[]{"id.triggerGroup","id.triggerName"}, new String []{group.toString(),jobName},  0,1);
			if(results!=null&&results.size()>0){
				return results.get(0).getCronExpression();
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//******************************************************************************************************	
	private void initCustomProperties(SchedulerConfigurationModel result,
			JobDataMap dataMap) {
		ArrayList<String>customKeys=new ArrayList<String>();
		ArrayList<String>customValues=new ArrayList<String>();
		for(int i=0;i<100;i++){
			String customKey=dataMap.getString(JOB_DETAIL_CUSTOM_PROPS_KEY+i);
			if(customKey==null){
				break;
			}
			customKeys.add(customKey);
			String customValue=dataMap.getString(JOB_DETAIL_CUSTOM_PROPS_VALUE+i);
			customValues.add(customValue);
		}
		result.setCustomPropertyNames(customKeys.toArray(new String[customKeys.size()]));
		result.setCustomPropertyValues(customValues.toArray(new String[customValues.size()]));
	}
//******************************************************************************************************	
	private void initUsecaseActions(JobKey key,
			SchedulerConfigurationModel result, JobDataMap dataMap) {
		try {
			Long usActionId=Long.parseLong( dataMap.getString(JOB_DETAIL_USCASE_ACTION_KEY));
			UseCase us=UsecaseUtil.getUsecaseFromUsecaseAtion(usActionId);
			
			UILookupInfo usActions= loadUsecaseActionLookup(us.getUsecaseId());
			usActions.setSelectedId(String.valueOf(usActionId));
			result.setUsecaseActions(usActions);
			
			UILookupInfo usCases= loadUsecaseLookup(us.getSubSystemId());
			usCases.setSelectedId(String.valueOf(us.getUsecaseId()));
			result.setUsecases(usCases);
			
			UILookupInfo subsystems=loadSubSystemLookup(us.getSystemId());
			subsystems.setSelectedId(String.valueOf(us.getSubSystemId()));
			result.setSubsystems(subsystems);
			
			UILookupInfo systems=loadSystemLookup();
			systems.setSelectedId(String.valueOf(us.getSystemId()));
			result.setSystems(systems);
			
		} catch (Exception e) {
			logger.log(Level.WARNING,"","fail to load usecase action for schedule task :"+key.getName());
		}
	}
//******************************************************************************************************
	@Override
	public ProcessExecutionResult updateConfiguration(
			SchedulerConfigurationModel configuration) {
		
		return createOrUpdateSchedule(configuration, predefindedActions.update);
	}
//******************************************************************************************************
	@Override
	public UILookupInfo loadMailTemplateLookup() {
		try {
			Map<String,Object>context= createContext();
			CoMailTemplateFacadeRemote bean=(CoMailTemplateFacadeRemote)ServiceFactory.lookupFacade(CoMailTemplateFacadeRemote.class);
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(bean.loadLookup( null), ContextUtil.getLoginLanguage(context));
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		}
		return null;
	}
//******************************************************************************************************
	@Override
	public UILookupInfo loadSubSystemLookup(Long cmSystemId) {
		try {
			Map<String,Object>context= createContext();
			CmSubsystemFacadeRemote bean=(CmSubsystemFacadeRemote)ServiceFactory.lookupFacade(CmSubsystemFacadeRemote.class);
			LookupConfigurationModel lookupConfigModel=BeanUtil.getLookupConfigurationModel(context,CmSubsystem.class);
			lookupConfigModel.addCondition("CmSubsystem.cmSystem.cmSystemId=:cmSystemId", context);
			lookupConfigModel.setParameterValue("cmSystemId", cmSystemId);
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(bean.loadLookup(lookupConfigModel, null), ContextUtil.getLoginLanguage(context));
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		}
		return null;
	}
//******************************************************************************************************
	@Override
	public UILookupInfo loadSystemLookup() {
		try {
			Map<String,Object>context= createContext();
			CmSystemFacadeRemote bean=(CmSystemFacadeRemote)ServiceFactory.lookupFacade(CmSystemFacadeRemote.class);
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(bean.loadLookup( null), ContextUtil.getLoginLanguage(context));
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		}
		return null;
	}
//******************************************************************************************************
	@Override
	public UILookupInfo loadUsecaseActionLookup(Long usecaseId) {
		Map<String,Object>context= createContext();
		try {
			CoUsecaseActionFacadeRemote bean=(CoUsecaseActionFacadeRemote)ServiceFactory.lookupFacade(CoUsecaseActionFacadeRemote.class);
			LookupConfigurationModel lookupModel=new LookupConfigurationModel(CoUsecaseAction.class);
			lookupModel.setOwnDisplayColumns("coAction.name");
			lookupModel.setPrimaryKeyColumn("coUsecaseActionId");
			lookupModel.addCondition("CoUsecaseAction.coUsecase.coUsecaseId=:coUsecaseId", context);
			lookupModel.setParameterValue("coUsecaseId", usecaseId);
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(bean.loadLookup( lookupModel,null), ContextUtil.getLoginLanguage(context)) ;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		} 
		return null;
	}
//******************************************************************************************************
	@Override
	public UILookupInfo loadUsecaseLookup(Long subsystemId) {
		Map<String,Object>context= createContext();
		try {
			CoUsecaseFacadeRemote bean=(CoUsecaseFacadeRemote)ServiceFactory.lookupFacade(CoUsecaseFacadeRemote.class);
			LookupConfigurationModel lookupModel=new LookupConfigurationModel(CoUsecase.class);
			lookupModel.setOwnDisplayColumns("name");
			lookupModel.setPrimaryKeyColumn("coUsecaseId");
			lookupModel.addCondition("CoUsecase.cmSubsystem.cmSubsystemId=:cmSubsystemId", context);
			lookupModel.setParameterValue("cmSubsystemId", subsystemId);
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(bean.loadLookup( lookupModel,null), ContextUtil.getLoginLanguage(context)) ;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		} 
		return null;
	}
//******************************************************************************************************
	@Override
	public UILookupInfo loadUsernamesLookup() {
		Map<String,Object>context= createContext();
		try {
			CoSmtpAddressesFacadeRemote bean=(CoSmtpAddressesFacadeRemote)ServiceFactory.lookupFacade(CoSmtpAddressesFacadeRemote.class);
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(bean.loadLookup(null), ContextUtil.getLoginLanguage(context)) ;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		} 
		return null;
	}
//******************************************************************************************************	
	@Override
	public GridValueModel searchBPartner(String contains,String name,String lastname,String code,String email) {
		
		Map<String,Object>context= createContext();
		try {
			CmBpartnerFacadeRemote bean=(CmBpartnerFacadeRemote)ServiceFactory.lookupFacade(CmBpartnerFacadeRemote.class);
			ArrayList<String>propsName=new ArrayList<String>();
			ArrayList<String>propsValues=new ArrayList<String>();
			
			if(contains!=null&&contains.trim().length()>0){
				propsName.add("code");
				propsName.add("name");
				propsName.add("secoundName");
				propsName.add("emailaddress");
				propsValues.add(contains);
				propsValues.add(contains);
				propsValues.add(contains);
				propsValues.add(contains);
			}
			if(name!=null&&name.trim().length()>0){
				propsName.add("name");
				propsValues.add(name);
			}
			if(lastname!=null&&lastname.trim().length()>0){
				propsName.add("secoundName");
				propsValues.add(lastname);
			}
			if(code!=null&&code.trim().length()>0){
				propsName.add("code");
				propsValues.add(code);
			}
			if(email!=null&&email.trim().length()>0){
				propsName.add("emailaddress");
				propsValues.add(email);
			}
			List<CmBpartner>bpartners;
			if(propsName.size()>0){
				bpartners=bean.findByProperties(propsName.toArray(new String[propsName.size()]), propsValues.toArray(new String[propsValues.size()]),false,QueryComparsionType.like, null);
			}else{
				bpartners=bean.findAll( 0,100);
			}
			return convertToGridData(bpartners);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		} 
		return null;
	}
//******************************************************************************************************
	private GridValueModel convertToGridData(List<CmBpartner>partners){
		GridValueModel result=new GridValueModel();
		if(partners!=null){
			List<GridRowData>data=new ArrayList<GridRowData>();
			int index=0;
			for(CmBpartner partner:partners){
				GridRowData row=new GridRowData(partner.getCmBpartnerId(), index++, new String[]{partner.getCode(),partner.getName(),partner.getSecoundName(),partner.getEmailaddress()});
				data.add(row);
			}
			result.setRows(data.toArray(new GridRowData[data.size()]));
		}
		
		return result;
	}
//******************************************************************************************************
@Override
public void setServletContext(ServletContext context) {
	this.servletContext=context;
	
}
//******************************************************************************************************
public ExceptionHandler getExceptionHandler(){
	return new SchedulerExceptionHandler();
}
}
