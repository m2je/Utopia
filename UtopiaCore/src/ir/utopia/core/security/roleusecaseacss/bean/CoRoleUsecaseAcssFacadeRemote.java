package ir.utopia.core.security.roleusecaseacss.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.roleusecaseacss.persistent.CoRoleUsecaseAcss;
import ir.utopia.core.security.roleusecaseacss.persistent.CoVRoleUsecaseAcss;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoRoleUsecaseAcssFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoRoleUsecaseAcssFacadeRemote  extends UtopiaBasicUsecaseBean<CoRoleUsecaseAcss,CoVRoleUsecaseAcss>{

	public List<CoRoleUsecaseAcss> findByCoUsecaseId(Object coUsecaseId,
			int... rowStartIdxAndCount);

}