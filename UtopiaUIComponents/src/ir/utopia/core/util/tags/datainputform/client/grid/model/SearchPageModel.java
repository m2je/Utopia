package ir.utopia.core.util.tags.datainputform.client.grid.model;

import ir.utopia.core.util.tags.datainputform.client.model.AbstractPageDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchPageModel extends AbstractPageDataModel implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7385172973407406833L;
	
	
	public SearchPageModel(){
		
	} 
	Long usecaseActionId;
	InputItem [] searchItems;
	InputItem [] orderbyItems;
	InputItem []searchResultItems;
	String direction;
	String deleteUrl;
	String editUrl;
	String saveUrl;
	String reportUrl;
	String importExportSetupPage;
	String importPage;
	public String getSaveUrl() {
		return saveUrl;
	}
	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}
	String primaryKeyFieldName;
	public InputItem[] getSearchItems() {
		return searchItems;
	}
	public void setSearchItems(InputItem[] searchItems) {
		this.searchItems = searchItems;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public String getDeleteUrl() {
		return deleteUrl;
	}
	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}
	public String getEditUrl() {
		return editUrl;
	}
	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}
	public String getReportUrl() {
		return reportUrl;
	}
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	
	public String getPrimaryKeyFieldName() {
		return primaryKeyFieldName;
	}
	public void setPrimaryKeyFieldName(String primaryKeyFieldName) {
		this.primaryKeyFieldName = primaryKeyFieldName;
	}
	public InputItem[] getOrderbyItems() {
		return orderbyItems;
	}
	public void setOrderbyItems(InputItem[] orderbyItems) {
		this.orderbyItems = orderbyItems;
	}
	public Long getUsecaseActionId() {
		return usecaseActionId;
	}
	public void setUsecaseActionId(Long usecaseActionId) {
		this.usecaseActionId = usecaseActionId;
	}
	
	public String getImportExportSetupPage() {
		return importExportSetupPage;
	}
	public void setImportExportSetupPage(String importExportSetupPage) {
		this.importExportSetupPage = importExportSetupPage;
	}
	public String getImportPage() {
		return importPage;
	}
	public void setImportPage(String importPage) {
		this.importPage = importPage;
	}
	public InputItem[] getSearchResultItems() {
		return searchResultItems;
	}
	public void setSearchResultItems(InputItem[] searchResultItems) {
		this.searchResultItems = searchResultItems;
	}
	
	
	
	
}
