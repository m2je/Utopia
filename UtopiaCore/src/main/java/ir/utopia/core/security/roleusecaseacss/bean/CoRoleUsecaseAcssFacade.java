package ir.utopia.core.security.roleusecaseacss.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.roleusecaseacss.persistent.CoRoleUsecaseAcss;
import ir.utopia.core.security.roleusecaseacss.persistent.CoVRoleUsecaseAcss;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoRoleUsecaseAcss.
 * 
 * @see roleusecaseacss.CoRoleUsecaseAcss
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoRoleUsecaseAcssFacade extends AbstractBasicUsecaseBean<CoRoleUsecaseAcss,CoVRoleUsecaseAcss>  implements CoRoleUsecaseAcssFacadeRemote {
	// property constants
	public static final String CO_USECASE_ID = "coUsecaseId";

	public List<CoRoleUsecaseAcss> findByCoUsecaseId(Object coUsecaseId,
			int... rowStartIdxAndCount) {
		return findByProperty(CO_USECASE_ID, coUsecaseId, rowStartIdxAndCount);
	}

}