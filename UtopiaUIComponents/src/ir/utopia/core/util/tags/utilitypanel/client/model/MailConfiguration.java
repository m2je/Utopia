package ir.utopia.core.util.tags.utilitypanel.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MailConfiguration implements Serializable,IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9062922148475772478L;

	String user,passsword,description;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPasssword() {
		return passsword;
	}

	public void setPasssword(String passsword) {
		this.passsword = passsword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
