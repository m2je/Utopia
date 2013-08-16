package ir.utopia.core.security.roleorganizationaccess.bean;

// default package

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.roleorganizationaccess.persistent.CoRoleOrganizationAcss;

import javax.ejb.Remote;

/**
 * Remote interface for CoRoleOrganizationAcssFacade.
 * 
 * @author Salarkia
 */
@Remote
public interface CoRoleOrganizationAcssFacadeRemote extends UtopiaBasicUsecaseBean<CoRoleOrganizationAcss, CoRoleOrganizationAcss> {
	
}