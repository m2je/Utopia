package ir.utopia.common.locality.state.bean;

import ir.utopia.common.locality.state.persistent.CmState;
import ir.utopia.common.locality.state.persistent.CmVState;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmStateFacade.
 * 
 * @author Jahani
 */
@Remote
public interface CmStateFacadeRemote extends UtopiaBasicUsecaseBean<CmState,CmVState>{ 

	public List<CmState> findByName(Object name, int... rowStartIdxAndCount);

	public List<CmState> findByDescription(Object description,
			int... rowStartIdxAndCount);

	public List<CmState> findByCode(Object code, int... rowStartIdxAndCount);

}