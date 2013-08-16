package ir.utopia.common.locality.country.bean;

import ir.utopia.common.locality.country.persistent.CmCountry;
import ir.utopia.common.locality.country.persistent.CmVCountry;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmCountryFacade.
 * 
 * @author Jahani
 */
@Remote
public interface CmCountryFacadeRemote extends UtopiaBasicUsecaseBean<CmCountry,CmVCountry>  {
	public List<CmCountry> findByName(Object name, int... rowStartIdxAndCount);

	public List<CmCountry> findByDescription(Object description,
			int... rowStartIdxAndCount);

	public List<CmCountry> findByCode(Object code, int... rowStartIdxAndCount);

}