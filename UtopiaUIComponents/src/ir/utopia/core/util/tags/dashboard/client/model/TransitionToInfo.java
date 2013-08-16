package ir.utopia.core.util.tags.dashboard.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TransitionToInfo implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1823782205334535838L;
	
	boolean skipable;
	boolean lockable;
	Long docType;
	String docTypeName;
	int doctypeCode;
	Long usecaseAction;
	String actionName;
	public TransitionToInfo(){
		
	}
	public TransitionToInfo(boolean skipable, boolean lockable, Long docType,
			String docTypeName, int doctypeCode,String actionName, Long usecaseAction) {
		super();
		this.skipable = skipable;
		this.lockable = lockable;
		this.docType = docType;
		this.docTypeName = docTypeName;
		this.doctypeCode = doctypeCode;
		this.usecaseAction = usecaseAction;
		this.actionName=actionName;
	}
	public boolean isSkipable() {
		return skipable;
	}
	public void setSkipable(boolean skipable) {
		this.skipable = skipable;
	}
	public boolean isLockable() {
		return lockable;
	}
	public void setLockable(boolean lockable) {
		this.lockable = lockable;
	}
	public Long getDocType() {
		return docType;
	}
	public void setDocType(Long docType) {
		this.docType = docType;
	}
	public String getDocTypeName() {
		return docTypeName;
	}
	public void setDocTypeName(String docTypeName) {
		this.docTypeName = docTypeName;
	}
	public int getDoctypeCode() {
		return doctypeCode;
	}
	public void setDoctypeCode(int doctypeCode) {
		this.doctypeCode = doctypeCode;
	}
	public Long getUsecaseAction() {
		return usecaseAction;
	}
	public void setUsecaseAction(Long usecaseAction) {
		this.usecaseAction = usecaseAction;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	
}
