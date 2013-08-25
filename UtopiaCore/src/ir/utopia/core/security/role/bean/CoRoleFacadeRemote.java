package ir.utopia.core.security.role.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.role.persistence.CoRole;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoRoleFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoRoleFacadeRemote extends UtopiaBasicUsecaseBean<CoRole,CoRole> {
	

	public List<CoRole> findByName(Object name, int... rowStartIdxAndCount);

}