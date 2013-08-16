package ir.utopia.core.security.usruscsactnaccs.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.usruscsactnaccs.persistence.CoUsrUscsActnAccs;
import ir.utopia.core.security.usruscsactnaccs.persistence.CoVUsrUscsActnAccs;

import javax.ejb.Remote;

/**
 * Remote interface for CoUsrUscsActnAccsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoUsrUscsActnAccsFacadeRemote extends UtopiaBasicUsecaseBean<CoUsrUscsActnAccs,CoVUsrUscsActnAccs> {
	
}