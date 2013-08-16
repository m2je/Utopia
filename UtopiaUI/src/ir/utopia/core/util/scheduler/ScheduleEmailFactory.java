package ir.utopia.core.util.scheduler;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.mailtemplate.persistent.CoMailTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.ConstructorUtils;

public abstract class ScheduleEmailFactory {

	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ScheduleEmailFactory.class.getName());
	}
	public ScheduledEmailHandler  getMailHandler(String handlerClass,String sender,CmBpartner recepient, CoMailTemplate template){
		if(handlerClass==null||handlerClass.trim().length()==0){
			handlerClass=DefaultScheduledEmailHandler.class.getName();
		}
		try {
			Class<?> clazz= Class.forName(handlerClass);
			ScheduledEmailHandler handler=(ScheduledEmailHandler)ConstructorUtils.invokeConstructor(clazz, new Object[]{});
			handler.setMailTemplate(template);
			handler.setRecepient(recepient);
			handler.setSender(sender);
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}

}
