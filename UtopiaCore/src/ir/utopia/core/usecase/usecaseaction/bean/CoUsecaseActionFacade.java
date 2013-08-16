package ir.utopia.core.usecase.usecaseaction.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;
import ir.utopia.core.usecase.usecaseaction.persistence.CoVUsecaseAction;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoUsecaseAction.
 * 
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoUsecaseActionFacade extends AbstractBasicUsecaseBean<CoUsecaseAction,CoVUsecaseAction> implements CoUsecaseActionFacadeRemote {
	// property constants
	public static final String CO_USECASE_ID = "coUsecaseId";
	

	public List<CoUsecaseAction> findByCoUsecaseId(Object coUsecaseId,
			int... rowStartIdxAndCount) {
		return findByProperty(CO_USECASE_ID, coUsecaseId, rowStartIdxAndCount);
	}

	
	

}