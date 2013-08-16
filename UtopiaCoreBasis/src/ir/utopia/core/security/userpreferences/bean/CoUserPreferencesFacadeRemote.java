package ir.utopia.core.security.userpreferences.bean;

import java.util.List;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.userpreferences.persistent.CoUserPreferences;

import javax.ejb.Remote;

/**
 * Remote interface for CoUserFacade.
 * 
 * @author
 */
@Remote
public interface CoUserPreferencesFacadeRemote extends UtopiaBasicUsecaseBean<CoUserPreferences, CoUserPreferences>{
	/**
	 * 
	 * @param userId
	 * @param preferences
	 */
	public void savePreferences(Long userId,List<CoUserPreferences>preferences);
	/**
	 * 
	 * @param userId
	 * @param preference
	 */
	public void savePreference(Long userId,CoUserPreferences preference);
}