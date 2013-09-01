package ir.utopia.core.struts;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.MappedSuperForm;
import ir.utopia.core.persistent.SoftDeletePersistent;
@MappedSuperForm
public abstract class AbstractUtopiaSoftDeleteForm<P extends SoftDeletePersistent> extends  AbstractUtopiaBasicForm<P> implements UtopiaSoftDeleteForm<P> {

	private boolean isactive;
	
	//*******************************************************************************************
	@FormPersistentAttribute(formToEntityMap=FormPersistentAttribute.FormToEntityDataTypeMap.ISACTIVE,method="getIsactive")
	@InputItem(index=0,displayType=Constants.DisplayTypes.checkBox,breakLineAfter=true,defaultValue="true")
	public boolean isActive() {
		return isactive;
	}
//*******************************************************************************************
	public void setActive(boolean isactive) {
		this.isactive = isactive;
	}
//*******************************************************************************************	
	public void setActive(String isactive) {
		this.isactive = isactive!=null&&
		("on".equalsIgnoreCase(isactive)||"true".equalsIgnoreCase(isactive));
	}

}
