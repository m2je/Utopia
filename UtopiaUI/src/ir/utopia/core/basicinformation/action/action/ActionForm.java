package ir.utopia.core.basicinformation.action.action;

import ir.utopia.core.basicinformation.action.persistent.CoAction;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class ActionForm  extends AbstractUtopiaForm<CoAction>{

	private Long coActionId;
	private String name;
	private String methodName;

	public ActionForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoActionId() {
		return coActionId;
	}

	public void setCoActionId(Long coActionId) {
		this.coActionId = coActionId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=1,breakLineAfter=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=2,breakLineAfter=true)
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



}
