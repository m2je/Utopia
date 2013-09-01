package ir.utopia.core.controlpanel.log.action;

import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.UsecaseForm;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityMapType;
import ir.utopia.core.log.persistent.CoCpLogConfig;
import ir.utopia.core.struts.AbstractUtopiaForm;
@PersistedMapForm
@UsecaseForm
public class LogConfigurationDetail extends AbstractUtopiaForm<CoCpLogConfig>{
 private boolean removed;	
 long systemId;
 long subSystemId;
 long usecaseId;
 long actionId;
 long userIds;
 long roleId;
 long logConfigId;
 @FormId
 @FormPersistentAttribute(method="getCoCpLogConfigId")
public long getLogConfigId() {
	return logConfigId;
}
public void setLogConfigId(long logConfigId) {
	this.logConfigId = logConfigId;
}
@FormPersistentAttribute(method="getCmSystem",
		formToEntityMapType=FormToEntityMapType.real,formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
public long getSystemId() {
	return systemId;
}
public void setSystemId(long systemId) {
	this.systemId = systemId;
}
@FormPersistentAttribute(method="getCmSubsystem",
		formToEntityMapType=FormToEntityMapType.real,formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
public long getSubSystemId() {
	return subSystemId;
}
public void setSubSystemId(long subSystemId) {
	this.subSystemId = subSystemId;
}
@FormPersistentAttribute(method="getCoUsecase",
		formToEntityMapType=FormToEntityMapType.real,formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
public long getUsecaseId() {
	return usecaseId;
}
public void setUsecaseId(long usecaseId) {
	this.usecaseId = usecaseId;
}
@FormPersistentAttribute(method="getCoAction",
		formToEntityMapType=FormToEntityMapType.real,formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
public long getActionId() {
	return actionId;
}
public void setActionId(long actionId) {
	this.actionId = actionId;
}
@FormPersistentAttribute(method="getCoUser",
		formToEntityMapType=FormToEntityMapType.real,formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
public long getUserIds() {
	return userIds;
}
public void setUserIds(long userIds) {
	this.userIds = userIds;
}
@FormPersistentAttribute(method="getCoRole",
		formToEntityMapType=FormToEntityMapType.real,formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
public long getRoleId() {
	return roleId;
}
public void setRoleId(long roleId) {
	this.roleId = roleId;
}
public boolean isRemoved() {
	return removed;
}
public void setRemoved(boolean removed) {
	this.removed = removed;
}
 
}
