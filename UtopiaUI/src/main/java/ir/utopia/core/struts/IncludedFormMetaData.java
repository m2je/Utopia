package ir.utopia.core.struts;

import ir.utopia.core.constants.Constants.IncludedFormDisplayType;
import ir.utopia.core.form.annotation.IncludedForm.IncludedFormType;

import java.io.Serializable;
import java.util.List;

public class IncludedFormMetaData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -430565184195693653L;
	private boolean insertRowAllowed;
	private boolean updateable;
	private Class<? extends UtopiaBasicForm<?>> formClass;
	private String name;
	private UtopiaFormMethodMetaData parentLoadMethod; 
	private String includedFormColumnName;
	private String myLinkMethod;
	private IncludedFormDisplayType[] displayTypes;
	private IncludedFormType type=IncludedFormType.Interactive;
	private int preferedWith;
	private int preferedHeigth;
	boolean autoFitColumns=true;
	List<FormColorLogic> colorLogic;
	public IncludedFormType getType() {
		return type;
	}
	public void setType(IncludedFormType type) {
		this.type = type;
	}
	public String getIncludedFormColumnName() {
		return includedFormColumnName;
	}
	public void setIncludedFormColumnName(String includedFormKey) {
		this.includedFormColumnName = includedFormKey;
	}
	public String getMyLinkMethod() {
		return myLinkMethod;
	}
	public void setMyLinkMethod(String myLinkMethod) {
		this.myLinkMethod = myLinkMethod;
	}
	public String getName() {
		return (name==null||name.trim().length()==0)?formClass.getName():name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<? extends UtopiaBasicForm<?>> getFormClass() {
		return formClass;
	}
	public void setFormClass(Class<? extends UtopiaBasicForm<?>> formClass) {
		this.formClass = formClass;
	}
	public boolean isInsertRowAllowed() {
		return insertRowAllowed;
	}
	public void setInsertRowAllowed(boolean insertRowAllowed) {
		this.insertRowAllowed = insertRowAllowed;
	}
	public boolean isUpdateable() {
		return updateable;
	}
	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}
	public IncludedFormMetaData(String name, Class<? extends UtopiaBasicForm<?>> formClass,UtopiaFormMethodMetaData parentLoadMethod,IncludedFormDisplayType[] displayType) {
		super();
		this.name=name;
		this.formClass = formClass;
		this.parentLoadMethod=parentLoadMethod;
		this.displayTypes=displayType;
		this.includedFormColumnName=formClass.getName();
	}
	public UtopiaFormMethodMetaData getParentLoadMethod() {
		return parentLoadMethod;
	}
	public void setParentLoadMethod(UtopiaFormMethodMetaData methodMetaData) {
		this.parentLoadMethod = methodMetaData;
	}
	
	public String getIdMethodName(){
		return FormUtil.getMetaData(getFormClass()).getIdMethodName();
	} 
	public String getIdFieldName(){
		return FormUtil.getMetaData(getFormClass()).getIdFieldName();
	}
	public IncludedFormDisplayType[] getDisplayTypes() {
		return displayTypes;
	}
	public void setDisplayType(IncludedFormDisplayType []displayTypes) {
		this.displayTypes= displayTypes;
	}
	public boolean isShowInReport(){
		for(IncludedFormDisplayType type:displayTypes)
			if(IncludedFormDisplayType.report.equals(type)||IncludedFormDisplayType.all.equals(type)){
				return true;
			}
		return false; 
	}
	public int getPreferedWith() {
			return preferedWith;
		}
	public void setPreferedWith(int preferedWith) {
			this.preferedWith = preferedWith;
		}
	public int getPreferedHeigth() {
			return preferedHeigth;
		}
	public void setPreferedHeigth(int preferedHeigth) {
			this.preferedHeigth = preferedHeigth;
		}
	public boolean isAutoFitColumns() {
		return autoFitColumns;
	}
	public void setAutoFitColumns(boolean autoFitColumns) {
		this.autoFitColumns = autoFitColumns;
	}
	public List<FormColorLogic> getColorLogic() {
		return colorLogic;
	}
	public void setColorLogic(List<FormColorLogic> colorLogic) {
		this.colorLogic = colorLogic;
	}
	 
	 
}
