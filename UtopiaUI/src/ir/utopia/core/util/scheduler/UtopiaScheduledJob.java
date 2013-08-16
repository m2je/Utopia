package ir.utopia.core.util.scheduler;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.log.bean.CoActionLogFacadeRemote;
import ir.utopia.core.log.persistent.ActionLogMessageType;
import ir.utopia.core.log.persistent.ActionLogStatus;
import ir.utopia.core.log.persistent.ActionLogger;
import ir.utopia.core.log.persistent.CoActionLog;
import ir.utopia.core.log.persistent.CoActionLogDtl;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.process.ProcessEngine;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.smtp.bean.SMTPServiceRemote;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.scheduler.bean.QrtzMailRecepientsFacadeRemote;
import ir.utopia.core.util.scheduler.persistent.JobGroup;
import ir.utopia.core.util.scheduler.persistent.QrtzMailRecepients;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
public class UtopiaScheduledJob implements Job {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(SchedulerConfigAction.class.getName());
	}
	private static UseCase us;
	private static UsecaseAction ucAction;
	private static ArrayList<LookupInfo>infos;
	static{
		
		try {
			us= UsecaseUtil.getUseCase("Co", "Ut", "Scheduler");
			ucAction= us.getUsecaseAction(predefindedActions.process.name());
			LookupInfo lookupInfo=new LookupInfo(CoUsecaseAction.class,ucAction.getUsecaseActionId() );
			infos=new ArrayList<LookupInfo>();
			infos.add(lookupInfo);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.log(Level.INFO,"UtopiaScheduledJob==>Job is"+context.getJobDetail().getKey().getName());
			CoActionLog log=new CoActionLog();
			log.setLookupInfos(infos);
			log.setStatus(ActionLogStatus.successful);
			log.setLogger(ActionLogger.scheduler);
			log.setStartTime(new Date());
			JobDetail jobDetail= context.getJobDetail();
			JobDataMap dataMap = jobDetail.getJobDataMap();
			Map<String,Object>applicationContext=new HashMap<String, Object>();
			Subject subject= ServiceFactory.getSecurityProvider().loginUser(dataMap.getLong(SchedulerConfigAction.JOB_DETAIL_OWNER_ID));
			applicationContext.put(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME, subject);
			applicationContext.put(SecurityProvider.LOGIN_LANGUAGE_PARAMETER_NAME, dataMap.getString(SchedulerConfigAction.JOB_DETAIL_LANGUAGE));
			doExecute(context, log, jobDetail, dataMap, applicationContext);
			
			log.setEndTime(new Date());
			CoActionLogFacadeRemote remote=(CoActionLogFacadeRemote)ServiceFactory.lookupFacade(CoActionLogFacadeRemote.class);
			remote.save(log);
		} catch (Throwable e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
		
		}
	}
	private void doExecute(JobExecutionContext context, CoActionLog log,
			JobDetail jobDetail, JobDataMap dataMap,
			Map<String, Object> applicationContext) {
		try {
			
			HashSet<CoActionLogDtl>dtls=new HashSet<CoActionLogDtl>();
			
			Object dayOfMonth=dataMap.get(SchedulerConfigAction.JOB_DETAIL_SOLAR_REAL_DAY_OF_MONTH);
			if(dayOfMonth!=null){
				int []d=DateUtil.gerToSolar(new Timestamp(System.currentTimeMillis()));
				if(d[3]==Integer.parseInt(String.valueOf(dayOfMonth))){
					logger.log(Level.INFO,"Skipping job :"+context.getJobDetail().getKey().getName()+" because it was schedule with solar day of month :"+dayOfMonth+" but today is :"+d[3]);
					return;
				}
			}
			
			
			String group=jobDetail.getKey().getGroup();
			Object usecaseactionId= dataMap.get(SchedulerConfigAction.JOB_DETAIL_USCASE_ACTION_KEY);
			boolean processExectionWasSuccessful=true;
			HashMap<String, Object>parameterValues=new HashMap<String, Object>();
			for(int i=0;i<100;i++){
				String customKey=dataMap.getString(SchedulerConfigAction.JOB_DETAIL_CUSTOM_PROPS_KEY+i);
				if(customKey==null){
					break;
				}
				String customValue=dataMap.getString(SchedulerConfigAction.JOB_DETAIL_CUSTOM_PROPS_VALUE+i);
				parameterValues.put(customKey, customValue);	
			}
			if(usecaseactionId!=null){
				long l= Long.parseLong((String)usecaseactionId);
				ExecutionResult result= ProcessEngine.executeProcess(l, parameterValues, applicationContext);
				processExectionWasSuccessful=result.isSuccess();
			}
			if(JobGroup.Email.toString().equalsIgnoreCase(group)){
				if(processExectionWasSuccessful){
					ExecutionResult result=sendEmails(context, parameterValues, applicationContext);
					log.setStatus(ActionLogStatus.successfulWithWarninig);
					for(MessageNamePair pair:result.getMessages()){
						CoActionLogDtl detail=new CoActionLogDtl();
						detail.setType(ActionLogMessageType.convert(pair.getType()));
						detail.setMessage(pair.getMessage());
						detail.setReferenceKey1(pair.getReferenceKey1());
						detail.setReferenceKey2(pair.getReferenceKey2());
						detail.setReferenceKey3(pair.getReferenceKey3());
						detail.setCoActionLog(log);
					}
				}else{
					CoActionLogDtl detail=new CoActionLogDtl();
					detail.setMessage(MessageHandler.getMessage("sendEmailFailureBecaseOfProcessFailure", 
							"ir.utopia.core.util.scheduler.SchedulerConfigAction",
							dataMap.getString(SchedulerConfigAction.JOB_DETAIL_LANGUAGE) ));
					detail.setType( ActionLogMessageType.Warninig);
					detail.setCoActionLog(log);
					dtls.add(detail);
					log.setStatus(ActionLogStatus.failed);
				}
			}
			log.setCoActionLogDtls(dtls);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
			log.setStatus(ActionLogStatus.failed);
		}
	}
//***********************************************************************************************************************
		private ExecutionResult sendEmails(JobExecutionContext context,HashMap<String, Object>parameterValues,Map<String,Object>applicationContext){
			ExecutionResult result=new ExecutionResult(true);
			try {
				SMTPServiceRemote service=(SMTPServiceRemote)ServiceFactory.lookupFacade(SMTPServiceRemote.class);
				QrtzMailRecepientsFacadeRemote facade=(QrtzMailRecepientsFacadeRemote)ServiceFactory.lookupFacade(QrtzMailRecepientsFacadeRemote.class.getName());
				List<QrtzMailRecepients> mailRecepients= facade.findScheduledTaskRecepients(SchedulerConfigAction.SCHEDULER_DEFAULT_NAME, context.getJobDetail().getKey().getName(), JobGroup.Email, applicationContext);
				if(mailRecepients!=null){
					for(QrtzMailRecepients recepient:mailRecepients){
						ExecutionResult cResult=service.sendMail((Long)context.get(SchedulerConfigAction.JOB_DETAIL_MAIL_SENDER),
								(Long)context.get(SchedulerConfigAction.JOB_DETAIL_MAIL_TEMPLATE), recepient.getCmBpartner().getCmBpartnerId(), ProcessEngine.extractProcessParametres(parameterValues) ,
								(String) context.get(SchedulerConfigAction.SCHEDULER_WEB_APPLICATION_ROOT_DIR), applicationContext);
						result.setSuccess(result.isSuccess()&&cResult.isSuccess());
						result.addMessages(cResult.getMessages());
					}
					
				}
				
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
				result.setSuccess(false);
			}
			return result;
			 
		}
}
