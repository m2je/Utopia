package ir.utopia.core.util.tags.datainputform.client.model;


import com.google.gwt.user.client.rpc.RemoteService;

public interface DataInputService extends RemoteService {
	 /**
	  * 
	  * @param formClass
	  * @param usecaseName
	  * @param actionName
	  * @return
	  */
	public DataInputFormModel getDataInputForm(String formClass,String usecaseName,String actionName);
	 /**
	  * 
	  * @param formClass
	  * @param usecaseName
	  * @param actionName
	  * @param recordId
	  * @return
	  */
	 public DataInputDataModel getFormData(String formClass,String usecaseName,String actionName,String recordId,boolean reload,String token);
	 /**
	  * 
	  * @param usecaseActionId
	  * @param lookupParmeter
	  * @param dependentFields
	  * @param dependesValue
	  * @return
	  */
	 public UILookupInfo reloadLookup(Long  usecaseActionId, String lookupParmeter,
			String[] dependentFields, String[] dependesValue);
	 /**
	  * 
	  * @param formClass
	  * @param usecaseName
	  * @param actionName
	  * @param lookupParmeter
	  * @param dependentFields
	  * @param dependesValue
	  * @return
	  */
	 public UILookupInfo reloadLookup(String formClass,String usecaseName,String actionName,String lookupParmeter,
				String[] dependentFields, String[] dependesValue);
	
}
