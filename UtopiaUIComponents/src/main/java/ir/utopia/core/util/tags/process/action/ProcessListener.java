package ir.utopia.core.util.tags.process.action;

import ir.utopia.core.process.ProcessStatusChangeEvent;

import java.io.Serializable;

public interface ProcessListener extends Serializable {

	public void notifyProcessStatusChanged(ProcessStatusChangeEvent e);
}
