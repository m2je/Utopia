package ir.utopia.common.locality.currency.bean;

import ir.utopia.common.locality.currency.persistent.CmCurrency;
import ir.utopia.common.locality.currency.persistent.CmVCurrency;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmCurrencyFacade.
 * 
 * @author Jahani
 */
@Remote
public interface CmCurrencyFacadeRemote extends UtopiaBasicUsecaseBean<CmCurrency,CmVCurrency>   {

	public List<CmCurrency> findByDescription(Object description,
			int... rowStartIdxAndCount);

	public List<CmCurrency> findByIsoCode(Object isoCode,
			int... rowStartIdxAndCount);

	public List<CmCurrency> findBySymbol(Object symbol,
			int... rowStartIdxAndCount);

}