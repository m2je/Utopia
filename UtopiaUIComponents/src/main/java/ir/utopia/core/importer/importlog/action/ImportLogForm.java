package ir.utopia.core.importer.importlog.action;

import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.IncludedForm;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityMapType;
import ir.utopia.core.form.annotation.InputItem.InputItemLogicOnAction;
import ir.utopia.core.importer.setting.persistent.CoImportLog;
import ir.utopia.core.importer.setting.persistent.ImportStatus;
import ir.utopia.core.struts.AbstractUtopiaForm;
import ir.utopia.core.util.DateUtil;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;

@PersistedMapForm
@DataInputForm(includedForms=@IncludedForm(formClass=ImportLogMessagesForm.class,name="logMessages"))
public class ImportLogForm extends AbstractUtopiaForm<CoImportLog>{
	private Collection<ImportLogMessagesForm> logMessages;
	private Long importLogId;
	private String startTime;
	private String endTime;
	private ImportStatus status;
	private Long recordCount;
	private Long lastImportedPk;
	private Long importSettingId;
	private String importSettingName;
	private Long totalRecordCount;
	private String settingName;
	
	
	@SearchItem(index=1)
	@FormPersistentAttribute(method="getName")
	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	@FormPersistentAttribute(method="getCoImportLogMsg")
	public Collection<ImportLogMessagesForm> getLogMessages() {
		return logMessages;
	}

	public void setLogMessages(Collection<ImportLogMessagesForm> logMessages) {
		this.logMessages = logMessages;
	}
	@FormId
	@FormPersistentAttribute(method="getCoImportLogId")
	public Long getImportLogId() {
		return importLogId;
	}

	public void setImportLogId(Long importLogId) {
		this.importLogId = importLogId;
	}
	@InputItem(index=2)
	@SearchItem(index=2)
	@FormPersistentAttribute(method="getStartTime",formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE_TIME)
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	@InputItem(index=3)
	@SearchItem(index=3)
	@FormPersistentAttribute(method="getEndTime",formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE_TIME)
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@InputItem(index=4)
	@SearchItem(index=4)
	@FormPersistentAttribute(method="getStatus")
	public ImportStatus isSuccess() {
		return status;
	}

	public void setSuccess(ImportStatus status) {
		this.status = status;
	}
	@InputItem(index=5)
	@SearchItem(index=5)
	@FormPersistentAttribute(method="getRecordCount")
	public Long getRecordCount() {
		return recordCount;
	}
	
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	@InputItem(index=6)
	@SearchItem(index=6)
	@FormPersistentAttribute(method="getLastImportedPk")
	public Long getLastImportedPk() {
		return lastImportedPk;
	}

	public void setLastImportedPk(Long lastImportedPk) {
		this.lastImportedPk = lastImportedPk;
	}
	@InputItem(index=7)
	@FormPersistentAttribute(method="getCoImporterSetting",formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
	public Long getImportSettingId() {
		return importSettingId;
	}
	
	
	public void setImportSettingId(Long importSettingId) {
		this.importSettingId = importSettingId;
	}
	
	@FormPersistentAttribute(method="getCoImportSettingId",formToEntityMapType=FormToEntityMapType.virtual)
	public String getImportSettingName() {
		return importSettingName;
	}

	public void setImportSettingName(String importSettingName) {
		this.importSettingName = importSettingName;
	}
	
	
	@SearchItem(index=8)
	@InputItem(index=102,displayOnAction=InputItemLogicOnAction.OnReport)
	@FormPersistentAttribute(method="getTotalRecordCount")
	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	@InputItem(index=100,displayOnAction=InputItemLogicOnAction.OnReport)
	public String getCurrentDate() {
		String locale=getLocale();
		locale=locale==null?"en":locale;
		return	DateUtil.getDateAndTimeString(new Date(System.currentTimeMillis()), new Locale(locale));
	}

	public void setCurrentDate(String currentDate) {
	}
	
}
