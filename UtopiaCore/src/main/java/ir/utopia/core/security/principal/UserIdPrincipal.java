package ir.utopia.core.security.principal;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

public class UserIdPrincipal implements Principal,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1953767156483333870L;

	private Long userId;
	private Long userLogId;
	private Set<Long>userRoles;
	public UserIdPrincipal(Long userId){
		this.userId=userId;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String getName() {
		
		return "UserID:";
	}
	public Long getUserLogId() {
		return userLogId;
	}
	public void setUserLogId(Long userLogId) {
		this.userLogId = userLogId;
	}
	public Set<Long> getUserRoles() {
		if(this.userRoles==null){
			this.userRoles=new HashSet<Long>();
		}
		return this.userRoles;
	}
	public void setUserRoles(Set<Long> userRoles) {
		this.userRoles = userRoles;
	}

}
