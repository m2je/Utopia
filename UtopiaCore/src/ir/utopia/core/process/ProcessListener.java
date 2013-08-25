package ir.utopia.core.process;


import java.io.Serializable;

public interface ProcessListener extends Serializable{

	public void notifyStatusChanged(ProcessStatusChangeEvent e);
}
