package ir.utopia.core.basicinformation.menu.persistent;

import ir.utopia.core.ServiceFactory;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CoMenuListener {

	@PostUpdate
	@PostRemove
	@PostPersist
	public void cleanMenuCache(CoMenu menu){
		ServiceFactory.getSecurityProvider().notifyMenuUpdated();
	}
}
