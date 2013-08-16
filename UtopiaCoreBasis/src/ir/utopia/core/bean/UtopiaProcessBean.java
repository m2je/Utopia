package ir.utopia.core.bean;

import ir.utopia.core.process.BeanProcess;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.ProcessListener;

import java.util.Map;

public interface UtopiaProcessBean extends UtopiaBean {
	
	public int getTotalStepCount(BeanProcess processBean,Map<String,Object>context);
	
	public BeanProcessExcutionResult<?> startProcess(BeanProcess processBean, Map<String,Object>context, ProcessListener listener)throws Exception;
	
}
