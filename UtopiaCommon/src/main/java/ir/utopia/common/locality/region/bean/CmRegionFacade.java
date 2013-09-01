package ir.utopia.common.locality.region.bean;

import ir.utopia.common.locality.region.persistent.CmRegion;
import ir.utopia.common.locality.region.persistent.CmVRegion;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmRegion.
 * 
 * @see fiscalyear.CmRegion
 * @author Jahani
 */
@Stateless
public class CmRegionFacade extends AbstractBasicUsecaseBean<CmRegion,CmVRegion> implements CmRegionFacadeRemote {
	// property constants
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CODE = "code";


	public List<CmRegion> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmRegion> findByDescription(Object description,
			int... rowStartIdxAndCount) {
		return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
	}

	public List<CmRegion> findByCode(Object code,int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}


}