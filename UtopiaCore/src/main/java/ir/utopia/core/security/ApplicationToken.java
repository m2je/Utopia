package ir.utopia.core.security;

import java.util.Date;

public class ApplicationToken {

	String token;
	long tokenId;
	Date validTo;
	
	public ApplicationToken(){
		
	}
	
	public ApplicationToken(String token, long tokenId, Date validTo) {
		super();
		this.token = token;
		this.tokenId = tokenId;
		this.validTo = validTo;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getTokenId() {
		return tokenId;
	}
	public void setTokenId(long tokenId) {
		this.tokenId = tokenId;
	}
	public Date getValidTo() {
		return validTo;
	}
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
}
