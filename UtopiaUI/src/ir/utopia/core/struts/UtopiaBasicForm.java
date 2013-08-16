package ir.utopia.core.struts;

import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.process.ExecutionResult;

import java.io.File;

public interface UtopiaBasicForm <P extends UtopiaBasicPersistent>{
	public Long getRecordId();
	
	public void setRecordId(long recordId);
	
	public void importPersistent(UtopiaBasicPersistent persistent);
	
	public UtopiaBasicPersistent convertToPersistent();
	
	public void setLocale(String locale); 
	public String getLocale();
	public boolean isDeleteable();
	public void setUpdateable(boolean updateable);
	public void setDeleteable(boolean deleteable);
	public boolean isUpdateable();
	public void setWindowNo(int windowNo);
	public int getWindowNo();
	public File [] getAttachments();
	public void setAttachments(File [] attachments);
	public void setAttachmentNames(String []filenames);
	public String[] getAttachmentNames();
	public String[] getRemovedAttachmentIds();
	public void setRemovedAttachmentIds(String[] attachmentIds);
	public UtopiaFormMetaData getMetaData();
	public ExecutionResult getExecutionResult() ;
	public void setExecutionResult(ExecutionResult executionResult) ;
	public void setSaveToken(String saveToken);
	public String getSaveToken();
	
	public void setRevisionDescription(String revisionDescription);
	
	public String getRevisionDescription();
	
	public boolean isDeleted();
	
	public void setDeleted(boolean deleted);
	
	}
