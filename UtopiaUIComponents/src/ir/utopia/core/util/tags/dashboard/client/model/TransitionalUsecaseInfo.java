package ir.utopia.core.util.tags.dashboard.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TransitionalUsecaseInfo implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2486184426294940052L;
	Long usecaseId;
	String useCaseName;
	String formClass;
	String saveActionName;
	String fullUsecaseName;
	private List<DocStatusInfo>docStatusInfo;
	String useCaseHeader;
	String statusFieldName;
	boolean revisionSupport;
	String usecaseViewAddress;
	public Long getUsecaseId() {
		return usecaseId;
	}
	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}
	public String getUseCaseName() {
		return useCaseName;
	}
	public void setUseCaseName(String useCaseName) {
		this.useCaseName = useCaseName;
	}
	public List<DocStatusInfo> getDocStatusInfo() {
		return docStatusInfo;
	}
	public void setDocStatusInfo(List<DocStatusInfo> docStatusInfo) {
		this.docStatusInfo = docStatusInfo;
	}
	
	public void addDocStatusInfo(DocStatusInfo info){
		if(docStatusInfo==null){
			docStatusInfo=new ArrayList<DocStatusInfo>();
		}
		docStatusInfo.add(info);
	}
	public String getFormClass() {
		return formClass;
	}
	public void setFormClass(String formClass) {
		this.formClass = formClass;
	}
	public String getSaveActionName() {
		return saveActionName;
	}
	public void setSaveActionName(String saveActionName) {
		this.saveActionName = saveActionName;
	}
	public String getFullUsecaseName() {
		return fullUsecaseName;
	}
	public void setFullUsecaseName(String fullUsecaseName) {
		this.fullUsecaseName = fullUsecaseName;
	}
	public String getUseCaseHeader() {
		return useCaseHeader;
	}
	public void setUseCaseHeader(String useCaseHeader) {
		this.useCaseHeader = useCaseHeader;
	}
	public String getStatusFieldName() {
		return statusFieldName;
	}
	public void setStatusFieldName(String statusFieldName) {
		this.statusFieldName = statusFieldName;
	}
	public boolean isRevisionSupport() {
		return revisionSupport;
	}
	public void setRevisionSupport(boolean isRevisionSupport) {
		this.revisionSupport = isRevisionSupport;
	}
	public String getUsecaseViewAddress() {
		return usecaseViewAddress;
	}
	public void setUsecaseViewAddress(String usecaseViewAddress) {
		this.usecaseViewAddress = usecaseViewAddress;
	}
	
}
