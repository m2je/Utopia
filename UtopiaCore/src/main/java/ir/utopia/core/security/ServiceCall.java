package ir.utopia.core.security;

import java.util.List;

public class ServiceCall {

	private String URI;
	private String token;
	private List<ServiceCallParameter> parameters;
	
	public String getURI() {
		return URI;
	}
	
	public void setURI(String URI) {
		this.URI = URI;
	}
	
	public List<ServiceCallParameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<ServiceCallParameter> parameters) {
		this.parameters = parameters;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
