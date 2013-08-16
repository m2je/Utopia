package ir.utopia.core.persistent;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.customproperty.bean.CoCustomPropertyFacadeRemote;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PostRemove;

public class CustomPropertyListener {

private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CustomPropertyListener.class.getName());
	}
	@PostRemove
	public void cascadeCustomPropertyDelete(UtopiaBasicPersistent p) {
		if(p!=null){
		try {
			Class<?> remoteClass =BeanUtil.findRemoteClassFromPersistent(p.getClass());
			UseCase usecase= UsecaseUtil.getUseCase(remoteClass.getName());
			CoCustomPropertyFacadeRemote attachmentFacase=(CoCustomPropertyFacadeRemote)ServiceFactory.lookupFacade(CoCustomPropertyFacadeRemote.class);
			attachmentFacase.deleteCustomProperties(usecase.getUsecaseId(), p.getRecordId());
		} catch (Throwable e) {
			logger.log(Level.WARNING,"fail to find and delete custom properties of class:"+p.getClass()+" and recordId:"+p.getRecordId(),e);
		}
		}
	}
}
