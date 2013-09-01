package ir.utopia.common.locality.currency.bean;

import ir.utopia.common.locality.currency.persistent.CmCurrency;
import ir.utopia.common.locality.currency.persistent.CmVCurrency;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmCurrency.
 * 
 * @see currency.CmCurrency
 * @author Jahani
 */
@Stateless
public class CmCurrencyFacade extends AbstractBasicUsecaseBean<CmCurrency,CmVCurrency> implements CmCurrencyFacadeRemote  {
	// property constants
	public static final String DESCRIPTION = "description";
	public static final String ISO_CODE = "isoCode";
	public static final String SYMBOL = "symbol";


	public List<CmCurrency> findByDescription(Object description,
			int... rowStartIdxAndCount) {
		return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
	}

	public List<CmCurrency> findByIsoCode(Object isoCode,
			int... rowStartIdxAndCount) {
		return findByProperty(ISO_CODE, isoCode, rowStartIdxAndCount);
	}

	public List<CmCurrency> findBySymbol(Object symbol,
			int... rowStartIdxAndCount) {
		return findByProperty(SYMBOL, symbol, rowStartIdxAndCount);
	}


}