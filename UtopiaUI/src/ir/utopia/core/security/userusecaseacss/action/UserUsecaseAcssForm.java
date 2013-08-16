package ir.utopia.core.security.userusecaseacss.action;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.security.userusecaseacss.persistent.CoUserUsecaseAcss;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class UserUsecaseAcssForm  extends AbstractUtopiaForm<CoUserUsecaseAcss>{

	private Long coUserUsecaseAcssId;
	private Long coUserId;
	private Long coUsecaseId;
	private String username;
	private String usecaseName;

	public UserUsecaseAcssForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoUserUsecaseAcssId() {
		return coUserUsecaseAcssId;
	}

	public void setCoUserUsecaseAcssId(Long coUserUsecaseAcssId) {
		this.coUserUsecaseAcssId = coUserUsecaseAcssId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoUser")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1)
	public Long getCoUserId() {
		return coUserId;
	}

	public void setCoUserId(Long coUserId) {
		this.coUserId = coUserId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
