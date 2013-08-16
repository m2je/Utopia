package ir.utopia.common.locality.province.bean;

import ir.utopia.common.locality.province.persistent.CmProvince;
import ir.utopia.common.locality.province.persistent.CmVProvince;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmProvince.
 * 
 * @see fiscalyear.CmProvince
 * @author Jahani
 */
@Stateless
public class CmProvinceFacade extends AbstractBasicUsecaseBean<CmProvince,CmVProvince> implements CmProvinceFacadeRemote {
	// property constants
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CM_STATE_ID = "cmStateId";
	public static final String CODE = "code";


	public List<CmProvince> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmProvince> findByDescription(Object description,
			int... rowStartIdxAndCount) {
		return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
	}

	public List<CmProvince> findByCmStateId(Object cmStateId,
			int... rowStartIdxAndCount) {
		return findByProperty(CM_STATE_ID, cmStateId, rowStartIdxAndCount);
	}

	public List<CmProvince> findByCode(Object code,int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}


}