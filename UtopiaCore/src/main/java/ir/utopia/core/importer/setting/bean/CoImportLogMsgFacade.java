package ir.utopia.core.importer.setting.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.importer.setting.persistent.CoImportLogMsg;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;
@Stateless
public class CoImportLogMsgFacade extends AbstractBasicUsecaseBean<CoImportLogMsg,CoImportLogMsg> implements CoImportLogMsgFacadeRemote  {

	@Override
	public List<?> findInMessages(String queryStr,
			Map<String, Object> queryValues) {
		Query query=entityManager.createQuery(queryStr);
		if(queryValues!=null){
			for(String key:queryValues.keySet()){
				query.setParameter(key, queryValues.get(key));
			}
		}
		return query.getResultList();
	}

	
}
