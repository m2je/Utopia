package ir.utopia.core.util.tags.importpage.client;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ImExPageDataModel implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3639841692195533652L;

	String description,name;
	long settingId;
	String setting;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getSettingId() {
		return settingId;
	}
	public void setSettingId(long settingId) {
		this.settingId = settingId;
	}
	public String getSetting() {
		return setting;
	}
	public void setSetting(String setting) {
		this.setting = setting;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
