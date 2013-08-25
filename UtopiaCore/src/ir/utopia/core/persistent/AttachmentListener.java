package ir.utopia.core.persistent;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.attachment.bean.CoAttachmentFacadeRemote;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PostRemove;


public class AttachmentListener {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AttachmentListener.class.getName());
	}
	@PostRemove
	public void cascadeAttachmentsDelete(UtopiaBasicPersistent p) {
		if(p!=null){
		try {
			Class<?> remoteClass =BeanUtil.findRemoteClassFromPersistent(p.getClass());
			UseCase usecase= UsecaseUtil.getUseCase(remoteClass.getName());
			CoAttachmentFacadeRemote attachmentFacase=(CoAttachmentFacadeRemote)ServiceFactory.lookupFacade(CoAttachmentFacadeRemote.class);
			attachmentFacase.deleteAttachments(usecase.getUsecaseId(), null, p.getRecordId());
		} catch (Throwable e) {
			logger.log(Level.WARNING,"fail to find and delete attachment of class:"+p.getClass()+" and recordId:"+p.getRecordId(),e);
		}
		}
	}
//**********************************************************************************************************	
	
}
