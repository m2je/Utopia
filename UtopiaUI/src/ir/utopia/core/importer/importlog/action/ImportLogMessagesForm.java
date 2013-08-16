package ir.utopia.core.importer.importlog.action;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.importer.setting.persistent.CoImportLogMsg;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class ImportLogMessagesForm extends AbstractUtopiaForm<CoImportLogMsg>{

	private Long importLogMsgId;
	private Long importLogId;
	private String msg;
	@FormId
	@FormPersistentAttribute(method="getCoImportLogMsgId")
	public Long getImportLogMsgId() {
		return importLogMsgId;
	}
	public void setImportLogMsgId(Long importLogMsgId) {
		this.importLogMsgId = importLogMsgId;
	}
	@FormPersistentAttribute(method="getCoImportLog",formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1)
	public Long getImportLogId() {
		return importLogId;
	}
	public void setImportLogId(Long importLogId) {
		this.importLogId = importLogId;
	}
	@FormPersistentAttribute(method="getMsg")
	@InputItem(isManadatory=true,index=2)
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
