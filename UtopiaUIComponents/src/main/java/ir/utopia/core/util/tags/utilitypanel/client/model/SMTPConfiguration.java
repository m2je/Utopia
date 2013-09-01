package ir.utopia.core.util.tags.utilitypanel.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SMTPConfiguration implements Serializable,IsSerializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1133461054713533507L;
	Long id;
	String serverName;
	String serverAdrress;
	Integer smtpPort;
	String[] customPropertyNames;
	String[] customPropertyValues;
	String description;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerAdrress() {
		return serverAdrress;
	}
	public void setServerAdrress(String serverAdrress) {
		this.serverAdrress = serverAdrress;
	}
	public Integer getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String[] getCustomPropertyNames() {
		return customPropertyNames;
	}
	public void setCustomPropertyNames(String[] customPropertyNames) {
		this.customPropertyNames = customPropertyNames;
	}
	public String[] getCustomPropertyValues() {
		return customPropertyValues;
	}
	public void setCustomPropertyValues(String[] customPropertyValues) {
		this.customPropertyValues = customPropertyValues;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
