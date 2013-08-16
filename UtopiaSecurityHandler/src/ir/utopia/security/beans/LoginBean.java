package ir.utopia.security.beans;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.AbstractUtopiaBean;
import ir.utopia.security.JAASSecurityProvider;
import ir.utopia.security.handlers.DefaultCalbackHandler;

import javax.ejb.Stateless;
import javax.security.auth.Subject;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


@Stateless
public class LoginBean extends AbstractUtopiaBean implements  LoginBeanRemote {
	private static String login_config = ServiceFactory.getSecurityProvider().getAttributeValue(JAASSecurityProvider.LOGIN_CONFIG_NAME);
	String username;
	private Subject subject;
	 /**
     * Holds value of property username.
     */

    
        
    private LoginContext context =null;
    
	protected boolean initLoginContext(String username, String password) {
	        try{
	        	this.username=username;
	            //Create a new instace of the callback handler to be passed to the context
	            DefaultCalbackHandler cbh = new DefaultCalbackHandler(username, password);
	            context = new LoginContext(login_config, cbh);
	            
	            return true; 
	            
	        }catch(LoginException le){
	        	le.printStackTrace();
	            return false;
	                
	        }catch(Exception e){
	        	e.printStackTrace();
	            return false;

	        }
	}
	
public boolean login(String username, String password){
	boolean success=initLoginContext(username, password);
	if(success){
		 try{
	            context.login();
	             //load the user profile and save it in a User object and keep this object in the session
	            subject=context.getSubject();
	            
	            return true;
	         }catch(FailedLoginException fle){
	            
	            return false;
	         }catch (LoginException le){
	            le.printStackTrace();
	            return false;
	         }catch(Exception e){
	            
	            return false;
	        }
	}
	return false;
}

public Subject getUserProfile(){
    return subject;
}


}
