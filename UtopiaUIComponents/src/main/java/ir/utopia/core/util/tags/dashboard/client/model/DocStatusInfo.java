package ir.utopia.core.util.tags.dashboard.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DocStatusInfo implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5076418610511343355L;
	Long currentDocStatus;
	String currentDocStatusName;
	int docStatusCode;
	String name;
	String desciption;
	
	List<TransitionToInfo>transtionToList;
	
	
	public Long getCurrentDocStatus() {
		return currentDocStatus;
	}
	public void setCurrentDocStatus(Long currentDocStatus) {
		this.currentDocStatus = currentDocStatus;
	}
	public String getCurrentDocStatusName() {
		return currentDocStatusName;
	}
	public void setCurrentDocStatusName(String currentDocStatusName) {
		this.currentDocStatusName = currentDocStatusName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesciption() {
		return desciption;
	}
	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public int getDocStatusCode() {
		return docStatusCode;
	}
	
	public void setDocStatusCode(int docStatusCode) {
		this.docStatusCode = docStatusCode;
	}

	public void addToDoctype(Long docType,Long usecaseAction,String actionName,String docTypeName,int doctypeCode,boolean skipable,boolean lockable){
		if(transtionToList==null){
			transtionToList=new ArrayList<TransitionToInfo>();
		}
		for(TransitionToInfo toInfo: transtionToList){
			if(toInfo.getDocType().equals(docType))
				return;
		}
		transtionToList.add(new TransitionToInfo(skipable, lockable, docType, docTypeName, doctypeCode,actionName, usecaseAction));
	}
	public List<TransitionToInfo> getTranstionToList() {
		return transtionToList;
	}
	
}
