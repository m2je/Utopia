package ir.utopia.core.security.userpreferences.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.userpreferences.persistent.CoUserPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoUserPreferences.
 * 
 * @see pref.CoUserPreferences
 * @author
 */
@Stateless
public class CoUserPreferencesFacade extends AbstractBasicUsecaseBean<CoUserPreferences, CoUserPreferences> implements CoUserPreferencesFacadeRemote {

	@Override
	public void savePreferences(Long userId,List<CoUserPreferences> preferences) {
		if(preferences!=null){
			@SuppressWarnings("unchecked")
			List<CoUserPreferences>existingPreferences= (List<CoUserPreferences>)entityManager.createQuery("select model from "+CoUserPreferences.class.getSimpleName()+" model where model.coUser.coUserId=:coUserId").
					setParameter("coUserId", userId).getResultList();
			CoUser user=entityManager.find(CoUser.class, userId);
			L1: for(CoUserPreferences pref:preferences){
				if(pref.getValue()!=null&&pref.getValue().trim().length()>0 &&pref.getKey()!=null&&pref.getKey().trim().length()>0){
						for(CoUserPreferences existsingPref:existingPreferences){
							if(pref.getKey().equalsIgnoreCase(existsingPref.getKey())){
								 existsingPref.setValue(pref.getValue());
								 entityManager.merge(existsingPref);
								 continue L1;
							}
						}
						pref.setCoUser(user);
						entityManager.persist(pref);
					
				}
				
			}
		}
		
	}

	@Override
	public void savePreference(Long userId, CoUserPreferences preference) {
		ArrayList<CoUserPreferences>preferences=new ArrayList<CoUserPreferences>();
		preferences.add(preference);
		savePreferences(userId, preferences);
		
	}

	
}