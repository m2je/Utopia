package ir.utopia.common.systems.subsystem.bean;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.subsystem.persistent.CmVSubsystem;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity CmSubsystem.
 * 
 * @see ir.utopia.core.CmSubsystem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CmSubsystemFacade extends AbstractBasicUsecaseBean<CmSubsystem,CmVSubsystem> implements CmSubsystemFacadeRemote {
private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CmSubsystemFacade.class.getName());
	}
	// property constants
	public static final String NAME = "name";
	
	
	public List<CmSubsystem> findByName(Object name,int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}



	@Override
	@SuppressWarnings("unchecked")
	public CmSubsystem getSubSystemId(String systemAbbrevation,
			String subSystemAbbrivation) {
		logger.log(Level.INFO,"finding all CmSubsystem instances");
		try {
			final String queryString = "select model from CmSubsystem model inner join cmsystem ";//TODO
			Query query = entityManager.createQuery(queryString);
			List<CmSubsystem> result=query.getResultList();
			return (result==null||result.size()==0)?null:result.get(0) ;
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE, "find all failed", re);
			throw re;
		}
	}

	

	

}