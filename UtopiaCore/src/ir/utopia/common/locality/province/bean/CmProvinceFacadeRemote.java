package ir.utopia.common.locality.province.bean;

import ir.utopia.common.locality.province.persistent.CmProvince;
import ir.utopia.common.locality.province.persistent.CmVProvince;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmProvinceFacade.
 * 
 * @author Jahani
 */
@Remote
public interface CmProvinceFacadeRemote extends UtopiaBasicUsecaseBean<CmProvince,CmVProvince>{

	public List<CmProvince> findByName(Object name, int... rowStartIdxAndCount);

	public List<CmProvince> findByDescription(Object description,
			int... rowStartIdxAndCount);

	public List<CmProvince> findByCode(Object code, int... rowStartIdxAndCount);

}