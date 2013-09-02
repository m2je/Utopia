package ir.utopia.core.security.userroles.action;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.InputItem.InputItemLogicOnAction;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.security.userrole.persistence.CoUserRoles;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class UserRolesForm  extends AbstractUtopiaForm<CoUserRoles>{

	private Long coUserRolesId;
	private Long coUserId;
	private Long coRoleId;
	private String username;
	private String roleName;
	
	public UserRolesForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoUserRolesId() {
		return coUserRolesId;
	}

	public void setCoUserRolesId(Long coUserRolesId) {
		this.coUserRolesId = coUserRolesId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoUser")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=3)
	public Long getCoUserId() {
		return coUserId;
	}

	public void setCoUserId(Long coUserId) {
		this.coUserId = coUserId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,displayOnAction=InputItemLogicOnAction.OnSearch)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

}
