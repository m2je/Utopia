package ir.utopia.core.revision.bean;

// default package

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.revision.persistent.CoRevision;

import javax.ejb.Remote;

/**
 * Remote interface for CoRevisionFacade.
 * 
 * @author 
 */
@Remote
public interface CoRevisionFacadeRemote extends UtopiaBasicUsecaseBean<CoRevision, CoRevision>{
	
	public void createRevision(UtopiaBasicPersistent persistent,String revisionDescription);
}