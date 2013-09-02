package ir.utopia.core.security.token;

import ir.utopia.core.security.exception.NotAuthenticatedException;
import ir.utopia.core.security.persistent.CoUserAppToken;

import javax.ejb.Stateless;

@Stateless
public class UserTokenManagementFacade implements UserTokenManagementFacadeRemote{
	
	public CoUserAppToken createOrFindLoginToken(String username,String password,String applicationSecret)throws NotAuthenticatedException{
		
		return null;
	}
}
