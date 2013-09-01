package ir.utopia.core.security.userusecaseacss.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.userusecaseacss.persistent.CoUserUsecaseAcss;
import ir.utopia.core.security.userusecaseacss.persistent.CoVUserUsecaseAcss;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoUserUsecaseAcss.
 * 
 * @see userusecaseacss.CoUserUsecaseAcss
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoUserUsecaseAcssFacade extends AbstractBasicUsecaseBean<CoUserUsecaseAcss,CoVUserUsecaseAcss> implements CoUserUsecaseAcssFacadeRemote {
	// property constants
	public static final String CO_USECASE_ID = "coUsecaseId";

	public List<CoUserUsecaseAcss> findByCoUsecaseId(Object coUsecaseId,
			int... rowStartIdxAndCount) {
		return findByProperty(CO_USECASE_ID, coUsecaseId, rowStartIdxAndCount);
	}


}