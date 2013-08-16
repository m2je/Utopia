package ir.utopia.core.security.roleusecaseacss.action;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.security.roleusecaseacss.persistent.CoRoleUsecaseAcss;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class RoleUsecaseAcssForm  extends AbstractUtopiaForm<CoRoleUsecaseAcss>{

	private Long coRoleUsecaseAcssId;
	private Long coRoleId;
	private Long coUsecaseId;
	private String roleName;
	private String usecaseName;

	public RoleUsecaseAcssForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoRoleUsecaseAcssId() {
		return coRoleUsecaseAcssId;
	}

	public void setCoRoleUsecaseAcssId(Long coRoleUsecaseAcssId) {
		this.coRoleUsecaseAcssId = coRoleUsecaseAcssId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoRole")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1)
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
	@FormPersistentAttribute(method="getCoUsecase")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=2)
	public Long getCoUsecaseId() {
		return coUsecaseId;
	}

	public void setCoUsecaseId(Long coUsecaseId) {
		this.coUsecaseId = coUsecaseId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUsecaseName() {
		return usecaseName;
	}

	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}

}
