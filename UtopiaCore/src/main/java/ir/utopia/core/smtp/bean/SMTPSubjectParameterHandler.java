package ir.utopia.core.smtp.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.logic.util.ParameterHandler;
import ir.utopia.core.process.BeanProcessParameter;

import java.util.Map;

public class SMTPSubjectParameterHandler extends AbstractSMTPParatemterHandler implements ParameterHandler {

	public SMTPSubjectParameterHandler(BeanProcessParameter[] parameters,
			Map<String, Object> context,CmBpartner bpartner) {
		super(parameters, context,bpartner);
	}

	@Override
	public Object getParameterValue(String parameterName) {
		Object result= super.getParameterValue(parameterName);
		return result;
	}

}
