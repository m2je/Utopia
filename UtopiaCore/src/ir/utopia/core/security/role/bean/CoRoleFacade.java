package ir.utopia.core.security.role.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.role.persistence.CoRole;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoRole.
 * 
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoRoleFacade extends AbstractBasicUsecaseBean<CoRole,CoRole> implements CoRoleFacadeRemote {
	// property constants
	public static final String NAME = "name";
	
	
	

	public List<CoRole> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}


	
}