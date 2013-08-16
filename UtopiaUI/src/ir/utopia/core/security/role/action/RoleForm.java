package ir.utopia.core.security.role.action;

import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.security.role.persistence.CoRole;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class RoleForm  extends AbstractUtopiaForm<CoRole>{

	private Long coRoleId;
	private String name;



	public RoleForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoRoleId() {
		return coRoleId;
	}

	public void setCoRoleId(Long coRoleId) {
		this.coRoleId = coRoleId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
