package ir.utopia.common.doctype.security.bean;

import ir.utopia.common.doctype.security.persistent.CmRoleDocstatusAccess;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

/**
 * Facade for entity CmRoleDocstatusAccess.
 * 
 * @see pref.CmRoleDocstatusAccess
 * @author 
 */
@Remote
public interface CmRoleDocstatusAccessFacadeRemote extends UtopiaBasicUsecaseBean<CmRoleDocstatusAccess, CmRoleDocstatusAccess> {

}