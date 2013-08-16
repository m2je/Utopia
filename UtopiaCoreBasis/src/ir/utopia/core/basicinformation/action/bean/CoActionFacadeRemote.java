package ir.utopia.core.basicinformation.action.bean;

import ir.utopia.core.basicinformation.action.persistent.CoAction;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoActionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoActionFacadeRemote extends UtopiaBasicUsecaseBean<CoAction,CoAction> {
	

	public List<CoAction> findByName(Object name, int... rowStartIdxAndCount);

	public List<CoAction> findByMethodname(Object methodname,
			int... rowStartIdxAndCount);

	
}