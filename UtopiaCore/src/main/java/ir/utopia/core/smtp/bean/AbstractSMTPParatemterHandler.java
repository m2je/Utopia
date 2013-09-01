package ir.utopia.core.smtp.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.logic.util.ParameterHandler;
import ir.utopia.core.process.BeanProcessParameter;

import java.util.Map;

public class AbstractSMTPParatemterHandler implements ParameterHandler {
	protected BeanProcessParameter[] parameters;
	protected Map<String, Object> context;
	protected CmBpartner bpartner;
	public AbstractSMTPParatemterHandler(BeanProcessParameter[] parameters, Map<String, Object> context,CmBpartner bpartner){
		this.parameters=parameters;
		this.context=context;
		this.bpartner=bpartner;
	}
	@Override
	public Object getParameterValue(String parameterName) {
		if(parameterName==null||parameterName.trim().length()==0)return "";
		if(parameters!=null){
			for(BeanProcessParameter param:parameters){
				if(param.getName().equals(parameterName)){
					return param.getValue();
				}
			}
		}
		return context.get(parameterName);
	}

}
