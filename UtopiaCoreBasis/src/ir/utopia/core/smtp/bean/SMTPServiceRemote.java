package ir.utopia.core.smtp.bean;

import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.logic.util.LogicParser;
import ir.utopia.core.process.BeanProcessParameter;
import ir.utopia.core.process.ExecutionResult;

import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface SMTPServiceRemote extends UtopiaBean {

	public static String SMTP_SERVICE_CONTEXT_VARIABLE_RECEPIENT_EMAIL=LogicParser.PARAMETER_SIGN+"RECEPIENT_EMAIL"+LogicParser.PARAMETER_SIGN;
	public static String SMTP_SERVICE_CONTEXT_VARIABLE_RECEPIENT_NAME=LogicParser.PARAMETER_SIGN+"RECEPIENT_NAME"+LogicParser.PARAMETER_SIGN;
	public static String SMTP_SERVICE_CONTEXT_VARIABLE_RECEPIENT_ID=LogicParser.PARAMETER_SIGN+"RECEPIENT_ID"+LogicParser.PARAMETER_SIGN;
	
	/**
	 * 
	 * @param templateId
	 * @param bpartnerId
	 * @param parameters
	 * @param context
	 * @return
	 */
	public ExecutionResult sendMail(Long sender,Long templateId,Long bpartnerId,BeanProcessParameter[] parameters,String attchmentsRootPath,Map<String,Object>context);
	/**
	 * 
	 * @param templateId
	 * @param bpartnerIds
	 * @param parameters
	 * @param context
	 * @return
	 */
	public ExecutionResult sendMail(Long sender,Long templateId,Long[] bpartnerIds,BeanProcessParameter[] parameters,String attchmentsRootPath,Map<String,Object>context);
}
