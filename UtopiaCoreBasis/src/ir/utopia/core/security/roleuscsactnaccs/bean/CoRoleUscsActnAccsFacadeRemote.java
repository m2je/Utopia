package ir.utopia.core.security.roleuscsactnaccs.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.roleuscsactnaccs.persistence.CoRoleUscsActnAccs;
import ir.utopia.core.security.roleuscsactnaccs.persistence.CoVRoleUscsActnAccs;

import javax.ejb.Remote;

/**
 * Remote interface for CoRoleUscsActnAccsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoRoleUscsActnAccsFacadeRemote extends UtopiaBasicUsecaseBean<CoRoleUscsActnAccs,CoVRoleUscsActnAccs> {
	
}