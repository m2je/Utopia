package ir.utopia.core.security.userlog.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.principal.UserIdPrincipal;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.userlog.persistent.CoUserLog;
import ir.utopia.core.security.userlog.persistent.CoVUserLog;

import java.util.Date;

import javax.ejb.Stateless;
import javax.security.auth.Subject;



@Stateless
public class CoUserLogFacade extends AbstractBasicUsecaseBean<CoUserLog,CoVUserLog> implements
		CoUserLogFacadeRemote {

	@Override
	public CoUserLog logUserLogin(String clientAddress, Subject user) {
		if(user!=null){
			CoUserLog log=new CoUserLog();
			Long userId= ((java.util.Set<UserIdPrincipal>)user.getPrincipals(UserIdPrincipal.class)).iterator().next().getUserId();
			log.setCoUser(entityManager.find(CoUser.class, userId));
			log.setClientAddress(clientAddress);
			log.setLoginDate(new Date());
			entityManager.persist(log);
			return log;
		}
		return null;
	}

	public void logUserLogout(Subject user) {
		if(user!=null){
			Long logId= ((java.util.Set<UserIdPrincipal>)user.getPrincipals(UserIdPrincipal.class)).iterator().next().getUserLogId();
			if(logId!=null){
				CoUserLog log=entityManager.find(CoUserLog.class, logId);
				if(log!=null){
					log.setLogOutDate(new Date());
					entityManager.merge(log);
				}
			}
		}
	}

}
