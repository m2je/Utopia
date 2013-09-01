package ir.utopia.core.util.tags.datainputform.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LinkDataModel implements Serializable,IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6465111729911077655L;
	private String url;
	private boolean enable;
	private String icon;
	private boolean perRecord;
	private String parameters;
	private String confirmMessage;
	private String title;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getConfirmMessage() {
		return confirmMessage;
	}
	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isPerRecord() {
		return perRecord;
	}
	public void setPerRecord(boolean perRecord) {
		this.perRecord = perRecord;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
}
