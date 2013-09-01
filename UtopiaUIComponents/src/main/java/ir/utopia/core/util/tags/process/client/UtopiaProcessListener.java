package ir.utopia.core.util.tags.process.client;

import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

public interface UtopiaProcessListener {
	
	public void notifyProcessStatusChanged(ProcessExecutionResult e);

}
