package ir.utopia.core.form;

import ir.utopia.core.struts.UtopiaBasicForm;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;

import java.util.ArrayList;
import java.util.List;

public class UtopiaPageForm  {
	
	private String actionName;
	private List<UtopiaBasicForm<?>> forms;
	private Long[] availableActions;
	private UseCase usecase;
	private long systemId;
	private long subSystemId;
	private String currentUri;
	private boolean success=true;
	private UtopiaFormMetaData metaData;
	public UtopiaPageForm(UseCase usecase,String actionName) {
		this.usecase=usecase;
		this.actionName=actionName;
	}
//****************************************************************	
	/**
	 * 
	 * @return
	 */
	public List<UtopiaBasicForm<?>> getForms(){
		return forms;
	}
//****************************************************************
	/**
	 * 
	 * @param forms
	 */
	public void setForms(List<UtopiaBasicForm<?>> forms){
		this.forms=forms;
	}
//****************************************************************	
	/**
	 * 
	 * @param form
	 */
public void setForm(UtopiaBasicForm<?> form){
		this.forms=new ArrayList<UtopiaBasicForm<?>>();
		forms.add(form);
	}
//****************************************************************
	/**
	 * 
	 */
	public UtopiaBasicForm<?> getForm(){
		return forms==null || forms.size() == 0 ?null:forms.get(0);
	}
//****************************************************************
	public UtopiaFormMetaData getFormMetaData(){
		return metaData==null?( getForm()!=null?getForm().getMetaData():null):metaData;
	}
//****************************************************************
	public String getUscaseName() {
		return usecase.getFullName();
	}
//****************************************************************
	public String getActionName() {
		return actionName;
	}
//****************************************************************
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
//****************************************************************	
	public Long[] getAvailableActions() {
		return availableActions;
	}
//****************************************************************
	public void setAvailableActions(Long[] availableActions) {
		this.availableActions = availableActions;
	}
//****************************************************************	
	public UseCase getUsecase() {
		return usecase;
	}
//****************************************************************	
	public void setUsecase(UseCase usecase) {
		this.usecase = usecase;
	}
//****************************************************************	
	public long getSystemId() {
		return systemId;
	}
//****************************************************************
	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}
//****************************************************************
	public long getSubSystemId() {
		return subSystemId;
	}
//****************************************************************
	public void setSubSystemId(long subSystemId) {
		this.subSystemId = subSystemId;
	}
//****************************************************************
	public int getItemCount(){
		return forms==null?0:forms.size();
	}
//****************************************************************	
	public String getCurrentUri() {
		return currentUri;
	}
//****************************************************************
	public void setCurrentUri(String currentUri) {
		this.currentUri = currentUri;
	}
//****************************************************************
	public int getWindowNumber() {
		UtopiaBasicForm<?> form=getForm();
		return form==null?0:form.getWindowNo();
	}
//****************************************************************
	public boolean isSuccess() {
		return success;
	}
//****************************************************************
	public void setSuccess(boolean success) {
		this.success = success;
	}
//****************************************************************
	public UtopiaFormMetaData getMetaData() {
		return metaData;
	}
//****************************************************************
	public void setMetaData(UtopiaFormMetaData metaData) {
		this.metaData = metaData;
	}
//****************************************************************	
	public Long getUsecaseActionId(){
		if(usecase!=null&&usecase.getUsecaseAction(actionName)!=null&&actionName!=null){
			UsecaseAction usecaseAction= usecase.getUsecaseAction(actionName);
			return usecaseAction==null?-1l:usecaseAction.getActionId();
		}
		return -1l;
	}
}
