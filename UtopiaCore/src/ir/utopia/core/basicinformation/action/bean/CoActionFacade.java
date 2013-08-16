package ir.utopia.core.basicinformation.action.bean;

import ir.utopia.core.basicinformation.action.persistent.CoAction;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoAction.
 * 
 * 
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoActionFacade extends AbstractBasicUsecaseBean<CoAction,CoAction> implements CoActionFacadeRemote {
	// property constants
	public static final String NAME = "name";
	public static final String METHODNAME = "methodname";


	public List<CoAction> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CoAction> findByMethodname(Object methodname,
			int... rowStartIdxAndCount) {
		return findByProperty(METHODNAME, methodname, rowStartIdxAndCount);
	}
}