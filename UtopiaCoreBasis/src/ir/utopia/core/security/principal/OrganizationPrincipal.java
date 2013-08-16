package ir.utopia.core.security.principal;

import java.io.Serializable;
import java.security.Principal;

public class OrganizationPrincipal implements Principal,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8750056984850416118L;
	Long organizationId;
	@Override
	public String getName() {
		return organizationId.toString();
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}
