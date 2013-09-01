package ir.utopia.core.process;

import ir.utopia.core.process.model.ProcessModel;

public interface ProcessHandler {
	/**
	 * 
	 * @param useCaseActionId
	 * @return
	 */
	public  ProcessModel getProcesses(Long useCaseActionId);
}
