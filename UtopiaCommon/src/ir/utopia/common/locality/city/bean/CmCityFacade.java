package ir.utopia.common.locality.city.bean;

import ir.utopia.common.locality.city.persistent.CmCity;
import ir.utopia.common.locality.city.persistent.CmVCity;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmCity.
 * 
 * @see city.CmCity
 * @author JAHANI
 */
@Stateless
public class CmCityFacade extends AbstractBasicUsecaseBean<CmCity,CmVCity> implements CmCityFacadeRemote {
	// property constants
	public static final String NAME = "name";
	public static final String AREACODE = "areacode";
	public static final String CODE = "code";
	public static final String DESCRIPTION = "description";


	public List<CmCity> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmCity> findByAreacode(Object areacode,
			int... rowStartIdxAndCount) {
		return findByProperty(AREACODE, areacode, rowStartIdxAndCount);
	}

	public List<CmCity> findByCode(Object code, int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}

	public List<CmCity> findByDescription(Object description,
			int... rowStartIdxAndCount) {
		return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
	}


}