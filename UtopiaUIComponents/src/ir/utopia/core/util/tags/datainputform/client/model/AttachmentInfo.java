package ir.utopia.core.util.tags.datainputform.client.model;

import java.io.Serializable;

public class AttachmentInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7467758117179577782L;
	private String attachmentId;
	private String fileName;
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
