package ir.utopia.core.util.scheduler;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.util.exceptionhandler.DefaultExceptionHandler;
import ir.utopia.core.util.exceptionhandler.ExceptionResult;

import java.util.Map;

import org.quartz.SchedulerException;

public class SchedulerExceptionHandler extends DefaultExceptionHandler {

	@Override
	public ExceptionResult handel(Throwable e, Map<String, Object> context) {
		if(e instanceof SchedulerException){
			ExceptionResult result=new ExceptionResult();
			String lang=ContextUtil.getLoginLanguage(context);
			result.addMessage(MessageHandler.getMessage("invalidSchedulerConfiguration", "ir.utopia.core.util.scheduler.SchedulerConfigAction",lang ), MessageType.error);
			result.addMessage(MessageHandler.getMessage("checkForCorrectConfiguration", "ir.utopia.core.util.scheduler.SchedulerConfigAction",lang ), MessageType.info);
			return result;
		}else{
			return super.handel(e, context);	
		}
		
	}

	
}
