/**
 * 
 */
package ir.utopia.core.util.tags.datainputform.client.model;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Salarkia
 *
 */
public class DataInputFormModel implements IsSerializable {
	public static final transient int LAY_OUT_SIMPLE=1;
	public static final transient int LAY_OUT_DETAIL_MASTER=2;
	InputItem [] items;
	int columnCount;
	Long usecaseActionId;
	String direction;
	String serverMessage;
	String actionUrl;
	String currencySign;
	String primaryKeyName;
	public int layOut=LAY_OUT_SIMPLE;
	private String []validationFunctions;
	String saveUrl,updateUrl,deleteUrl,searchURL;
	boolean allowNewRecord;
	boolean confirmRevisionDecription=false;
	boolean revisionTextManadary=false;
	private String[] onLoadMethods; 
	private String[] afterLoadCallbacks;
	private CustomButton[] customButtons;
	public String[] getValidationFunctions() {
		return validationFunctions;
	}
	public void setValidationFunctions(String[] validationFunctions) {
		this.validationFunctions = validationFunctions;
	}
	boolean supportAttachment;
	
	public boolean isSupportAttachment() {
		return supportAttachment;
	}
	public void setSupportAttachment(boolean supportAttachment) {
		this.supportAttachment = supportAttachment;
	}
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}
	private HashMap<String, String>bundels=new HashMap<String, String>();
	public String getCurrencySign() {
		return currencySign;
	}
	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getServerMessage() {
		return serverMessage;
	}
	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public int getColumnCount() {
		return columnCount;
	}
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	public InputItem[] getItems() {
		return items;
	}
	public void setItems(InputItem[] items) {
		this.items = items;
	}
	/**
	 * 
	 */
	public DataInputFormModel() {
		
	}
	public void setBundel(String key,String value){
		bundels.put(key, value);
	}
	public String getBundel(String key){
		return bundels.get(key);
	}
	public Long getUsecaseActionId() {
		return usecaseActionId;
	}
	public void setUsecaseActionId(Long usecaseActionId) {
		this.usecaseActionId = usecaseActionId;
	}
	public int getLayOut() {
		return layOut;
	}
	public void setLayOut(int layOut) {
		this.layOut = layOut;
	}
	public String getSaveUrl() {
		return saveUrl;
	}
	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}
	public String getUpdateUrl() {
		return updateUrl;
	}
	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
	public boolean isAllowNewRecord() {
		return allowNewRecord;
	}
	public void setAllowNewRecord(boolean allowNewRecord) {
		this.allowNewRecord = allowNewRecord;
	}
	public String getDeleteUrl() {
		return deleteUrl;
	}
	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}
	
	public InputItem getInputItem(String columnName){
		if(items!=null){
			for(InputItem item:items){
				if(columnName.equals( item.getColumnName())){
					return item;
				}
			}
		}
		return null;
	}
	public boolean isConfirmRevisionDecription() {
		return confirmRevisionDecription;
	}
	public void setConfirmRevisionDecription(boolean confirmRevisionDecription) {
		this.confirmRevisionDecription = confirmRevisionDecription;
	}
	public boolean isRevisionTextManadary() {
		return revisionTextManadary;
	}
	public void setRevisionTextManadary(boolean revisionTextManadary) {
		this.revisionTextManadary = revisionTextManadary;
	}
	public String[] getOnLoadMethods() {
		return onLoadMethods;
	}
	public void setOnLoadMethods(String[] onLoadMethods) {
		this.onLoadMethods = onLoadMethods;
	}
	public CustomButton[] getCustomButtons() {
		return customButtons;
	}
	public void setCustomButtons(CustomButton[] customButtons) {
		this.customButtons = customButtons;
	}
	public void setSearchURL(String searchURL) {
		this.searchURL=searchURL;
	}
	public String getSearchURL() {
		return searchURL;
	}
	public String[] getAfterLoadCallbacks() {
		return afterLoadCallbacks;
	}
	public void setAfterLoadCallbacks(String[] afterLoadCallbacks) {
		this.afterLoadCallbacks = afterLoadCallbacks;
	}
	
	
}
