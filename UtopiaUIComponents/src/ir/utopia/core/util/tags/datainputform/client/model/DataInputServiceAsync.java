package ir.utopia.core.util.tags.datainputform.client.model;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataInputServiceAsync {
	 /**
	  * 
	  * @param formClass
	  * @param usecaseName
	  * @param actionName
	  * @return
	  */
	public void getDataInputForm(String formClass,String usecaseName,String actionName, AsyncCallback<DataInputFormModel> callback);
	 /**
	  * 
	  * @param formClass
	  * @param usecaseName
	  * @param actionName
	  * @param recordId
	  * @return
	  */
	 public void getFormData(String formClass,String usecaseName,String actionName,String recordId,boolean reload,String token, AsyncCallback<DataInputDataModel> callback);
	/**
	 * 
	 * @param processUId
	 * @param lookupParmeter
	 * @param dependentFields
	 * @param dependesValue
	 * @param callback
	 */
	 public void reloadLookup(Long usecaseActionId, String lookupParmeter,
			String[] dependentFields, String[] dependesValue,
			AsyncCallback<UILookupInfo> callback);
	 /**
	  * 
	  * @param formClass
	  * @param usecaseName
	  * @param actionName
	  * @param lookupParmeter
	  * @param dependentFields
	  * @param dependesValue
	  * @param callback
	  */
	void reloadLookup(String formClass, String usecaseName, String actionName,
			String lookupParmeter, String[] dependentFields,
			String[] dependesValue, AsyncCallback<UILookupInfo> callback);
}
