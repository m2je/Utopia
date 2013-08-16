package ir.utopia.common.locality.state.bean;

import ir.utopia.common.locality.state.persistent.CmState;
import ir.utopia.common.locality.state.persistent.CmVState;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmState.
 * 
 * @see fiscalyear.CmState
 * @author Jahani
 */
@Stateless
public class CmStateFacade extends AbstractBasicUsecaseBean<CmState,CmVState> implements CmStateFacadeRemote {
	// property constants
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CODE = "code";


	public List<CmState> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmState> findByDescription(Object description,
			int... rowStartIdxAndCount) {
		return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
	}

	public List<CmState> findByCode(Object code, int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}

}