package ir.utopia.core.usecase.usecaseaction.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;
import ir.utopia.core.usecase.usecaseaction.persistence.CoVUsecaseAction;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoUsecaseActionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoUsecaseActionFacadeRemote extends UtopiaBasicUsecaseBean<CoUsecaseAction,CoVUsecaseAction>{
	

	public List<CoUsecaseAction> findByCoUsecaseId(Object coUsecaseId,
			int... rowStartIdxAndCount);
}