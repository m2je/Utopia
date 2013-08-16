package ir.utopia.common.doctype.bean;

import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

/**
 * Remote interface for CmDocStatusFacade.
 * 
 * @author 
 */
@Remote
public interface CmDocStatusFacadeRemote extends UtopiaBasicUsecaseBean<CmDocStatus, CmDocStatus> {
}