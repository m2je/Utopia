package ir.utopia.core.security.userlog.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.userlog.persistent.CoUserLog;
import ir.utopia.core.security.userlog.persistent.CoVUserLog;

import javax.ejb.Remote;
import javax.security.auth.Subject;

@Remote
public interface CoUserLogFacadeRemote extends UtopiaBasicUsecaseBean<CoUserLog,CoVUserLog >{
	
	public CoUserLog logUserLogin(String clientAddress,Subject user); 
	
	public void logUserLogout(Subject user);
}
