package ir.utopia.core.util.tags.datainputform.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AbstractPageDataModel implements IsSerializable,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2306770034464007088L;

	protected List<LinkDataModel>links;
	protected List<AttachmentInfo>attachmentInfo;
	protected HashMap<String, String>valueModel;
	protected HashMap<String, UILookupInfo>dynamicLookupsInfo;
	protected HashMap<String, String>context;

	boolean deleteEnable = false;

	boolean reportEnable = false;

	boolean editEnable = false;

	boolean saveEnable = false;

	boolean importEnable = false;

	boolean exportEnable = false;
	public List<LinkDataModel> getLinks() {
		return links;
	}

	public void setLinks(List<LinkDataModel> links) {
		this.links = links;
	}
	
	public HashMap<String, String> getValueModel() {
		return valueModel;
	}
	public void setValueModel(HashMap<String, String> valueModel) {
		this.valueModel = valueModel;
	}

	public List<AttachmentInfo> getAttachmentInfo() {
		return attachmentInfo;
	}

	public void setAttachmentInfo(List<AttachmentInfo> attachmentInfo) {
		this.attachmentInfo = attachmentInfo;
	}

	public HashMap<String, UILookupInfo> getDynamicLookupsInfo() {
		return dynamicLookupsInfo;
	}

	public void setDynamicLookupsInfo(
			HashMap<String, UILookupInfo> dynamicLookupsInfo) {
		this.dynamicLookupsInfo = dynamicLookupsInfo;
	}

	public HashMap<String, String> getContext() {
		return context;
	}

	public void setContext(HashMap<String, String> context) {
		this.context = context;
	}

	public boolean isSaveEnable() {
		return saveEnable;
	}

	public void setSaveEnable(boolean saveEnable) {
		this.saveEnable = saveEnable;
	}

	public boolean isDeleteEnable() {
		return deleteEnable;
	}

	public void setDeleteEnable(boolean deleteEnable) {
		this.deleteEnable = deleteEnable;
	}

	public boolean isReportEnable() {
		return reportEnable;
	}

	public void setReportEnable(boolean reportEnable) {
		this.reportEnable = reportEnable;
	}

	public boolean isEditEnable() {
		return editEnable;
	}

	public void setEditEnable(boolean editEnable) {
		this.editEnable = editEnable;
	}

	public boolean isImportEnable() {
		return importEnable;
	}

	public void setImportEnable(boolean importEnable) {
		this.importEnable = importEnable;
	}

	public boolean isExportEnable() {
		return exportEnable;
	}

	public void setExportEnable(boolean exportEnable) {
		this.exportEnable = exportEnable;
	}
	
	
}
