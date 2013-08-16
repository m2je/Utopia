package ir.utopia.common.doctype.security.bean;

import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.common.doctype.security.persistent.CmUserDocstatusAccess;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * Facade for entity CmUserDocstatusAccess.
 * 
 * @see pref.CmUserDocstatusAccess
 * @author 
 */
@Remote
public interface CmUserDocstatusAccessFacadeRemote extends UtopiaBasicUsecaseBean<CmUserDocstatusAccess, CmUserDocstatusAccess> {

	public List<CmDocStatus> getUserAccessibleDocStatuses(Long userId,Map<String,Object>context);
}