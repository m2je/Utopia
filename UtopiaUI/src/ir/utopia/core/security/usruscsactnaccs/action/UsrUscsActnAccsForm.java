package ir.utopia.core.security.usruscsactnaccs.action;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.security.usruscsactnaccs.persistence.CoUsrUscsActnAccs;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class UsrUscsActnAccsForm  extends AbstractUtopiaForm<CoUsrUscsActnAccs>{

	private Long coUsrUscsActnAccsId;
	private Long coUserId;
	private Long coUsecaseActionId;
	private String userName;
	private String usecaseactionName;

	public UsrUscsActnAccsForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoUsrUscsActnAccsId() {
		return coUsrUscsActnAccsId;
	}

	public void setCoUsrUscsActnAccsId(Long coUsrUscsActnAccsId) {
		this.coUsrUscsActnAccsId = coUsrUscsActnAccsId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoUser",formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1)
	public Long getCoUserId() {
		return coUserId;
	}

	public void setCoUserId(Long coUserId) {
		this.coUserId = coUserId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoUsecaseAction")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=2)
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
