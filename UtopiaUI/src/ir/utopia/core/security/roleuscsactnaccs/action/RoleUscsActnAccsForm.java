package ir.utopia.core.security.roleuscsactnaccs.action;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.security.roleuscsactnaccs.persistence.CoRoleUscsActnAccs;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class RoleUscsActnAccsForm  extends AbstractUtopiaForm<CoRoleUscsActnAccs>{

	private Long coRoleUscsActnAccsId;
	private Long coRoleId;
	private Long coUsecaseActionId;
	private String usecaseactionName;
	private String roleName;
	private String actionName;
	@SearchItem
	@FormPersistentAttribute
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public RoleUscsActnAccsForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoRoleUscsActnAccsId() {
		return coRoleUscsActnAccsId;
	}

	public void setCoRoleUscsActnAccsId(Long coRoleUscsActnAccsId) {
		this.coRoleUscsActnAccsId = coRoleUscsActnAccsId;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoRole")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=3)
	public Long getCoRoleId() {
		return coRoleId;
	}

	public void setCoRoleId(Long coRoleId) {
		this.coRoleId = coRoleId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoUsecaseAction")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=4)
	public Long getCoUsecaseActionId() {
		return coUsecaseActionId;
	}

	public void setCoUsecaseActionId(Long coUsecaseActionId) {
		this.coUsecaseActionId = coUsecaseActionId;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUsecaseactionName() {
		return usecaseactionName;
	}

	public void setUsecaseactionName(String usecaseactionName) {
		this.usecaseactionName = usecaseactionName;
	}

}
