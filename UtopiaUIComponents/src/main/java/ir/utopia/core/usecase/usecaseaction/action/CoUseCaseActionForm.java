package ir.utopia.core.usecase.usecaseaction.action;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

@PersistedMapForm
@DataInputForm
public class CoUseCaseActionForm extends AbstractUtopiaForm<CoUsecaseAction> {

	private Long coUsecaseActionId;
	
	private Long coUsecaseId;
	private Long coActionId;
	private String usecaseName;
	private String actionName;

	public CoUseCaseActionForm() {
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoUsecaseActionId() {
		return coUsecaseActionId;
	}

	public void setCoUsecaseActionId(Long coUsecaseActionId) {
		this.coUsecaseActionId = coUsecaseActionId;
	}
 
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoAction")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1)
	public Long getCoActionId() {
		return coActionId;
	}

	public void setCoActionId(Long coActionId) {
		this.coActionId = coActionId;
	}
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUsecaseName() {
		return usecaseName;
	}

	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoUsecase")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1)
	public Long getCoUsecaseId() {
		return coUsecaseId;
	}
	public void setCoUsecaseId(Long coUsecaseId) {
		this.coUsecaseId = coUsecaseId;
	}

}
