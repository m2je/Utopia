package ir.utopia.common.locality.country.bean;

import ir.utopia.common.locality.country.persistent.CmCountry;
import ir.utopia.common.locality.country.persistent.CmVCountry;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmCountry.
 * 
 * @see country.CmCountry
 * @author Jahani
 */
@Stateless
public class CmCountryFacade extends AbstractBasicUsecaseBean<CmCountry,CmVCountry> implements CmCountryFacadeRemote  {
	// property constants
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CODE = "code";


	public List<CmCountry> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmCountry> findByDescription(Object description,
			int... rowStartIdxAndCount) {
		return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
	}

	public List<CmCountry> findByCode(Object code, int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}


}