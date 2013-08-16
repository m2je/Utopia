package ir.utopia.core.importer.setting.bean.model;

import ir.utopia.core.bean.ImportDataProvider;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class ImportScheduleModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3211948723474549450L;
	private Long importSettingId;
	private Date endDate;
	private Map<String,Object>context;
	ImportDataProvider dataProvider;
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public ImportScheduleModel(Date endDate,Long importSettingId,ImportDataProvider provider,Map<String,Object>context){
		this.endDate=endDate;
		this.importSettingId=importSettingId;
		this.context=context;
		this.dataProvider=provider;
	}

	public Long getImportSettingId() {
		return importSettingId;
	}

	public void setImportSettingId(Long importSettingId) {
		this.importSettingId = importSettingId;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public ImportDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(ImportDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
	
	
}
