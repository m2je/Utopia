package ir.utopia.security.authentication;

public  class AuthenticationForm{
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String loginLanguage;
	
	public String getLoginLanguage() {
		return loginLanguage;
	}

	public void setLoginLanguage(String loginLanguage) {
		this.loginLanguage = loginLanguage;
	}
}