package ir.utopia.core.controlpanel.log.action;

import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.struts.AbstractUtopiaForm;

import java.util.Collection;


public class LogConfigurationForm extends  AbstractUtopiaForm<UtopiaPersistent>{
	int logBasicItem;
	int logType;
	private Collection<LogConfigurationDetail>  configRow;
	public int getLogBasicItem() {
		return logBasicItem;
	}
	public void setLogBasicItem(int logBasicItem) {
		this.logBasicItem = logBasicItem;
	}
	public int getLogType() {
		return logType;
	}
	public void setLogType(int logType) {
		this.logType = logType;
	}
	public Collection<LogConfigurationDetail> getConfigRow() {
		return configRow;
	}
	public void setConfigRow(Collection<LogConfigurationDetail> configRow) {
		this.configRow = configRow;
	}
	
	
}
