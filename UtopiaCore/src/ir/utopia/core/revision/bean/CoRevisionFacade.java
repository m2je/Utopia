package ir.utopia.core.revision.bean;


import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.revision.persistent.CoRevision;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.utilities.serialization.JSONSerializationUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;

/**
 * Facade for entity CoRevision.
 * 
 * @see .CoRevision
 * @author
 */
@Stateless
public class CoRevisionFacade extends AbstractBasicUsecaseBean<CoRevision, CoRevision> implements CoRevisionFacadeRemote {
	
	private static final Logger logger=Logger.getLogger(CoRevisionFacade.class.getName());
	@Override
	public void createRevision(UtopiaBasicPersistent persistent,String revisionDescription) {
		
		try {
			if(persistent!=null){
				Long userId= ContextUtil.getUserId(ContextHolder.getContext().getContextMap());
				CoRevision revision=new CoRevision();
				Class<?>remoteClass= BeanUtil.findRemoteClassFromPersistent( persistent.getClass());
				UseCase usecase= UsecaseUtil.getUseCase(remoteClass.getName());
				revision.setCoUsecaseId(usecase.getUsecaseId());
				revision.setPersistentRecordId(persistent.getRecordId());
				revision.setCreatedby(userId);
				revision.setCreated(new Date());
				revision.setSerializedObject(serialize(persistent));
				revision.setDescription(revisionDescription);
				entityManager.persist(revision);
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"fail to save revision ",e);
		}
		
	}

	protected String serialize(UtopiaBasicPersistent persistent){
		return JSONSerializationUtil.serialize(persistent);
	} 
	
}