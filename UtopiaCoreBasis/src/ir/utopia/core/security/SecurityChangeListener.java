package ir.utopia.core.security;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.persistent.UtopiaPersistent;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class SecurityChangeListener {

	@PostPersist
	@PostUpdate
	@PostRemove
	public void securityChanged(UtopiaPersistent persistent){
		ServiceFactory.getSecurityProvider().notifyRoleUpdated(-1l);
		ServiceFactory.getSecurityProvider().notifyUserUpdated(-1l);
	}
}
