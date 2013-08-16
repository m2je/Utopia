package ir.utopia.common.locality.city.bean;

import ir.utopia.common.locality.city.persistent.CmCity;
import ir.utopia.common.locality.city.persistent.CmVCity;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * Remote interface for CmCityFacade.
 * 
 * @author JAHANI
 */
@Remote
public interface CmCityFacadeRemote extends UtopiaBasicUsecaseBean<CmCity,CmVCity>  {
	public List<CmCity> findByName(Object name, int... rowStartIdxAndCount);

	public List<CmCity> findByAreacode(Object areacode,
			int... rowStartIdxAndCount);

	public List<CmCity> findByCode(Object code, int... rowStartIdxAndCount);

	public List<CmCity> findByDescription(Object description,
			int... rowStartIdxAndCount);

}