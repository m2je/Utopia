/**
 * 
 */
package ir.utopia.security.authorization;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.basicinformation.menu.bean.CoVValidMenusFacadeRemote;
import ir.utopia.core.basicinformation.menu.persistent.CoVSystemMenu;
import ir.utopia.core.util.Cache;
import ir.utopia.security.authentication.bean.JAASSecurityFacadeRemore;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Salarkia
 *
 */
public class UserAccessHandler {
	
	private static Cache<Long,Boolean>CACHE_RESET_MAP=new Cache<Long, Boolean>();
	private static final Logger logger=Logger.getLogger(UserAccessHandler.class.getName());
//**************************************************************************************	
	public static CmSystem[] getUserAccessibleSystems(long userId){
		try {
			JAASSecurityFacadeRemore remote=(JAASSecurityFacadeRemore)ServiceFactory.lookupFacade(JAASSecurityFacadeRemore.class.getName());
			List<CmSystem>systems= remote.loadUserAvailableSystems(userId);
			return systems!=null?systems.toArray(new CmSystem[systems.size()]):null;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//**************************************************************************************
	public static CmSubsystem[] getUserAccessibleSubSystems(long userId){
		try {
			JAASSecurityFacadeRemore remote=(JAASSecurityFacadeRemore)ServiceFactory.lookupFacade(JAASSecurityFacadeRemore.class.getName());
			List<CmSubsystem>subSystems= remote.loadUserAvailableSubsystems(userId);
			return subSystems!=null?subSystems.toArray(new CmSubsystem[subSystems.size()]):null;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//**************************************************************************************	
	public static Collection<? extends CoVSystemMenu> getUserAccessibleMenus(long userId){
			return loadUserAccessibleMenus(userId);
	}
//**************************************************************************************
	private static List<CoVSystemMenu> loadUserAccessibleMenus(long userId){
		try {
			CoVValidMenusFacadeRemote remote=(CoVValidMenusFacadeRemote)ServiceFactory.lookupFacade(CoVValidMenusFacadeRemote.class.getName());
			boolean resetCache=false;
			if(CACHE_RESET_MAP.containsKey(userId)){
				resetCache=CACHE_RESET_MAP.get(userId);
			}
			List<CoVSystemMenu>  result=remote.findByUserId(userId,resetCache, null);
			CACHE_RESET_MAP.put(userId, false);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//**************************************************************************************	
	public static  void clearMenuCache(){
		for(Long key: CACHE_RESET_MAP.keySet()){
			CACHE_RESET_MAP.put(key, true);
		}
	}
//**************************************************************************************	
}
