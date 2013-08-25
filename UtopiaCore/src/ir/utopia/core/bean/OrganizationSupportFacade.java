package ir.utopia.core.bean;

import ir.utopia.core.persistent.OrganizationData;
import ir.utopia.core.persistent.lookup.model.Condition;

/**
 * 
 * @author Salarkia
 *
 * @param <P>
 * @param <S>
 */
public interface OrganizationSupportFacade<P extends OrganizationData,S extends OrganizationData> extends UtopiaBasicUsecaseBean<P,S>{

	public Condition getOrganizationCondition(Class<?> persistenObject,Condition condition);
}
