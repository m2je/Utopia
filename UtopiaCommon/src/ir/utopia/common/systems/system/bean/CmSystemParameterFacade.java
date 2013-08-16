package ir.utopia.common.systems.system.bean;

import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.common.systems.system.persistent.CmSystemParameter;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity CmSystemParameter.
 * 
 * @see .CmSystemParameter
 * @author 
 */
@Stateless
public class CmSystemParameterFacade extends AbstractBasicUsecaseBean<CmSystemParameter, CmSystemParameter> implements CmSystemParameterFacadeRemote {

	@SuppressWarnings("unchecked")
	@Override
	public List<CmSystemParameter> getCurrentUserSystemParamters(
			Map<String, Object> context) {
		CmSystem[] systems= ServiceFactory.getSecurityProvider().getAccessibleSystems(ContextUtil.getUser(context));
		if(systems!=null&&systems.length>0){
			StringBuffer subSystemParameterQuery=new StringBuffer("Select model from "+CmSystemParameter.class.getSimpleName()+" model inner join fetch model.cmSystem where model.cmSystem.cmSystemId in(");
			Map<Integer,Long>subsystemsMap=new HashMap<Integer, Long>();
			for(int i=0;i<systems.length;i++){
				if(i>0){
					subSystemParameterQuery.append(",");
				}
				subSystemParameterQuery.append(":item"+i);
				subsystemsMap.put(i, systems[i].getCmSystemId());
			}
			subSystemParameterQuery.append(")");
			Query query= entityManager.createQuery(subSystemParameterQuery.toString());
			for(int i=0;i<systems.length;i++){
				query.setParameter("item"+i,subsystemsMap.get(i) );
			}
			return query.getResultList();
		}
		return null;
	}

	
}