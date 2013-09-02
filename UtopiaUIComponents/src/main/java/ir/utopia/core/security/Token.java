package ir.utopia.core.security;

import java.io.Serializable;

public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7081955527354754999L;
	
	String token;
	boolean consumed;
	
	
	public Token(String token) {
		super();
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isConsumed() {
		return consumed;
	}
	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}
	
}
