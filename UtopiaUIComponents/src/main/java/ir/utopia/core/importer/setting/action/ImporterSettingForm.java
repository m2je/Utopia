package ir.utopia.core.importer.setting.action;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.UsecaseForm;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityMapType;
import ir.utopia.core.importer.setting.persistent.CoImporterSetting;
import ir.utopia.core.struts.AbstractUtopiaForm;
import ir.utopia.core.util.DateUtil;

import java.util.Calendar;
import java.util.Date;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
@UsecaseForm
@PersistedMapForm
public class ImporterSettingForm extends AbstractUtopiaForm<CoImporterSetting>{//TODO refactor
	
	private String actionName;
	private String setting;
	private String comment;
	private String usecaseId ;
	private String settingName ;
	private Long importSetting;
	private String regexp;
	private Constants.ImportFormat  fileType;
	private String sql;
	private String resourceName;
	private Date scheduleStartDate;
	private Date scheduleEndDate;
	private String startTime;
	private String fileName;
	private String formClass;
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	@FormPersistentAttribute(method="getSetting")
	public String getSetting() {
		return setting;
	}
	public void setSetting(String setting) {
		this.setting = setting;
	}
	@FormPersistentAttribute
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getUsecaseId() {
		return usecaseId;
	}
	public void setUsecaseId(String usecaseId) {
		this.usecaseId = usecaseId;
	}
	@FormPersistentAttribute(method="getName")
	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	@FormId
	@FormPersistentAttribute(method="getCoImporterSettingId")
	public Long getImportSetting() {
		return importSetting;
	}
	public void setImportSetting(Long importSetting) {
		this.importSetting = importSetting;
	}
	@FormPersistentAttribute(method="getRegexp")
	public String getRegexp() {
		return regexp;
	}
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	@FormPersistentAttribute(method="getCoUsecase",formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP,formToEntityMapType=FormToEntityMapType.real)
	public Long getRealUsecaseId(){
		return usecaseId!=null?new Long(ServiceFactory.getSecurityProvider().decrypt(usecaseId)):-1l;
	} 
	public void setRealUsecaseId(Long usecaseId){
		this.usecaseId=String.valueOf(usecaseId);
	}
	
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	@FormPersistentAttribute(method="getFormat")
	public Constants.ImportFormat getFileType() {
		return fileType;
	}
	public void setFileType(Constants.ImportFormat fileType) {
		this.fileType = fileType;
	}
	@FormPersistentAttribute(method="getSql")
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	@FormPersistentAttribute(method="getResourceName")
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	@FormPersistentAttribute(method="getScdStartDate")
	public Date getScheduleStartDate() {
		return scheduleStartDate;
	}
	public void setScheduleStartDate(Date scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	@FormPersistentAttribute(method="getScdEndDate")
	public Date getScheduleEndDate() {
		return scheduleEndDate;
	}
	public void setScheduleEndDate(Date scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}
	
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	@FormPersistentAttribute(method="getFileName")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFormClass() {
		return formClass;
	}
	public void setFormClass(String formClass) {
		this.formClass = formClass;
	}
	@Override
	public CoImporterSetting convertToPersistent() {
		CoImporterSetting res= (CoImporterSetting)super.convertToPersistent();
//		if(res.getScdStartDate()!=null){
			Calendar c1=Calendar.getInstance();
//			c1.setTime(res.getScdStartDate());
			Calendar c2=Calendar.getInstance();
			c2.setTime(DateUtil.getTime(getStartTime()));
			c1.set(Calendar.HOUR_OF_DAY,c2.get(Calendar.HOUR_OF_DAY) );
			c1.set(Calendar.MINUTE,c2.get(Calendar.MINUTE) );
			c1.set(Calendar.SECOND,c2.get(Calendar.SECOND) );
//			res.setScdStartDate(c1.getTime());
//			res.setScdInterval(new Long((24*3600)+(0*60)+0));//TODO add next time interval every 24 hours
//		}
		
		return res;
	}
	
}
