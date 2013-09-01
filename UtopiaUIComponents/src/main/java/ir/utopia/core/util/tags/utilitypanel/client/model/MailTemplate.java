package ir.utopia.core.util.tags.utilitypanel.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MailTemplate implements Serializable,IsSerializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6159057820100547714L;
	
	Long templateId;
	String name,description,template;
	String []attachmentNames;
	String []attachmetPaths;
	String []resourceNames;
	String subject;
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String[] getAttachmentNames() {
		return attachmentNames;
	}
	public void setAttachmentNames(String[] attachmentNames) {
		this.attachmentNames = attachmentNames;
	}
	public String[] getAttachmetPaths() {
		return attachmetPaths;
	}
	public void setAttachmetPaths(String[] attachmetPaths) {
		this.attachmetPaths = attachmetPaths;
	}
	public String[] getResourceNames() {
		return resourceNames;
	}
	public void setResourceNames(String[] resourceNames) {
		this.resourceNames = resourceNames;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
}
