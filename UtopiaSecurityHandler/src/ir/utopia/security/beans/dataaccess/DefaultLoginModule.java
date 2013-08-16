/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package ir.utopia.security.beans.dataaccess;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.exception.OrganizationAccessException;
import ir.utopia.core.security.principal.EmailPrincipal;
import ir.utopia.core.security.principal.FirstnamePrincipal;
import ir.utopia.core.security.principal.LastnamePrincipal;
import ir.utopia.core.security.principal.OrganizationPrincipal;
import ir.utopia.core.security.principal.SexPrincipal;
import ir.utopia.core.security.principal.SurrogatePrincipal;
import ir.utopia.core.security.principal.UserIdPrincipal;
import ir.utopia.core.security.user.bean.CoUserFacadeRemote;
import ir.utopia.core.security.user.persistence.CoUser;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;


public class DefaultLoginModule implements LoginModule{
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(DefaultLoginModule.class.getName());
	}
	
    // initial state
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String,?> sharedState;
    
  
    
    // username and password
    private String username;
    private String password;
    
    //authentication status
    boolean auth_success = true;
    boolean commit_success = false;
    boolean password_mismatch= false;
    boolean invalid_user= false;
    
    
    //Database Configuration fields
   
    
    //User Credentials
    private String _lastname = null;
    private String _firstname = null;
    private String _email = null;
    private Constants.Sex sex;
    private long userId;
    private Long organizationId;
    
    private EmailPrincipal emailPrincipal = null;
    private FirstnamePrincipal fnamePrincipal = null;
    private LastnamePrincipal lnamePrincipal = null;
    private SurrogatePrincipal surrogate=null; 
    private UserIdPrincipal userIdPrincipal=null;
    private OrganizationPrincipal organizationPrincipal=null;
    /**
     * Initialize this <code>LoginModule</code>.
     *
     * <p>
     *
     * @param subject the <code>Subject</code> to be authenticated. <p>
     *
     * @param callbackHandler a <code>CallbackHandler</code> for communicating
     *			with the end user (prompting for user names and
     *			passwords, for example). <p>
     *
     * @param sharedState shared <code>LoginModule</code> state. <p>
     *
     * @param options options specified in the login
     *			<code>Configuration</code> for this particular
     *			<code>LoginModule</code>.
     */
    
    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map<String,?> sharedState, Map<String,?> options) {
        
        System.out.println("DefaultLoginModule:InitMethod");
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        
        
    }
    
    /**
     * Authenticate the user by prompting for a user name and password.
     *
     * <p>
     *
     * @return true in all cases since this <code>LoginModule</code>
     *		should not be ignored.
     *
     * @exception FailedLoginException if the authentication fails. <p>
     *
     * @exception LoginException if this <code>LoginModule</code>
     *		is unable to perform the authentication.
     */
    public boolean login() throws LoginException {
        
        System.out.println("DefaultLoginModule:login()");
        // get the callback handler with the user name and password
        if (callbackHandler == null)
            throw new LoginException("DefaultLoginModule: No CallbackHandler Available");
        
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Username");
        callbacks[1] = new PasswordCallback("Password: ", false);
        
        try {
            callbackHandler.handle(callbacks);
            username = ((NameCallback)callbacks[0]).getName();
            password = new String(((PasswordCallback)callbacks[1]).getPassword());
            
            auth_success = validateUser(username, password);
            
            if(!auth_success){
                if(password_mismatch){
                    throw new LoginException("Invalid Password");
                }else if(invalid_user){
                    throw new LoginException("Invalid Username");
                }
            }
            return true;
        } catch (java.io.IOException ioe) {
            throw new LoginException(ioe.toString());
        } catch (UnsupportedCallbackException use) {
            throw new LoginException("DefaultLoginModule: Not Supported"+ use.getCallback().toString() );
        }
    }
    
    /**
     * <p> This method is called if the LoginContext's
     * overall authentication succeeded
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * succeeded).
     * <p>
     *
     * @exception LoginException if the commit fails.
     *
     * @return true if this LoginModule's own login and commit
     *		attempts succeeded, or false otherwise.
     */
    public boolean commit() throws LoginException {
        System.out.println("DefaultLoginModule:commit()");
        if (auth_success) {
            commit_success= true;
            fnamePrincipal = new FirstnamePrincipal(_firstname);
            lnamePrincipal = new LastnamePrincipal(_lastname);
            emailPrincipal = new EmailPrincipal(_email);
            surrogate=new SurrogatePrincipal(userId);
            userIdPrincipal=new UserIdPrincipal(userId);
            if(sex!=null){
            	SexPrincipal sexPrincipal=new SexPrincipal(sex);
            	subject.getPrincipals().add(sexPrincipal);
            	}
            System.out.println("Adding principals");
            subject.getPrincipals().add(fnamePrincipal);
            subject.getPrincipals().add(lnamePrincipal);
            subject.getPrincipals().add(emailPrincipal);
            subject.getPrincipals().add(surrogate);
            subject.getPrincipals().add(userIdPrincipal);
            if(ServiceFactory.isSupportOrganization()&&userId!=1l){
            	inituserOrganizations();
            }
        if(userId!=1l){
        	Set<Long> roles=ServiceFactory.getSecurityProvider().getUserRoles(userId);
        	if(roles!=null){
        		userIdPrincipal.setUserRoles(roles);
        	}
        }
       
        } else {
            commit_success = false;
        }
        return commit_success;
    }
//******************************************************************************************************************************
	private void inituserOrganizations() throws LoginException {
		if(organizationId==null){
			logger.log(Level.INFO,"No default organization id was found for user,loading avaiable organizations to iniate the default organization");
			try {
				Long []availableOrgs=ServiceFactory.getSecurityProvider().getAvialableOrganizations(subject);
				if(availableOrgs!=null&availableOrgs.length>0){
					organizationId=availableOrgs[0];
					CoUserFacadeRemote userBean=(CoUserFacadeRemote)ServiceFactory.lookupFacade(CoUserFacadeRemote.class);
					userBean.setDefaultOrganization(userId, organizationId);
				}else{
					throw new OrganizationAccessException("fail to load default organizationId for user:["+userId+"] , you have to define organization access level for this user or the default organization");
				}
				
			}catch (OrganizationAccessException e) {
				throw new LoginException(e.getMessage());
			} 
			catch (Exception e) {
				logger.log(Level.WARNING,"fail to locate organization access for user:["+userId+"]",e);
			}
		}
		organizationPrincipal=new OrganizationPrincipal();
		organizationPrincipal.setOrganizationId(organizationId);
		subject.getPrincipals().add(organizationPrincipal);
	}
    
    /**
     * <p> This method is called if the LoginContext's
     * overall authentication failed.
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * did not succeed).
     *
     * <p> If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> and <code>commit</code> methods),
     * then this method cleans up any state that was originally saved.
     *
     * <p>
     *
     * @exception LoginException if the abort fails.
     *
     * @return false if this LoginModule's own login and/or commit attempts
     *		failed, and true otherwise.
     */
    public boolean abort() throws LoginException {
        
        System.out.println("DefaultLoginModule:abort()");
        if (!auth_success) {
            // authentication failure
            username = null;
            password = null;
            this.subject.getPrincipals().clear();
            return true;
        }
        return false;
    }
    
    /**
     * Logout the user.
     *
     * <p> This method removes the <code>SamplePrincipal</code>
     * that was added by the <code>commit</code> method.
     *
     * <p>
     *
     * @exception LoginException if the logout fails.
     *
     * @return true in all cases since this <code>LoginModule</code>
     *          should not be ignored.
     */
    public boolean logout() throws LoginException {
        System.out.println("DefaultLoginModule:logout()");
        this.username = null;
        this.password = null;
        this.subject.getPrincipals().clear();
        return true;
    }
    
    
    /**
     *  This method does the actual authentication by validating in the database
     *  if the user exists and if the password matches or not.
     *
     */
    
    public boolean validateUser(String username, String password){
    	try {
			CoUserFacadeRemote bean=(CoUserFacadeRemote)ServiceFactory.lookupFacade(CoUserFacadeRemote.class.getName());
			CoUser user= bean.findByUsername(username);
			invalid_user=user==null;
			if(user!=null)
				password_mismatch=! ServiceFactory.getSecurityProvider().decrypt(user.getPassword()).equals(password);
			auth_success=!invalid_user&&!password_mismatch;
			
			if(auth_success){
            	CmBpartner partner=user.getCmBpartner();
            	_firstname=partner.getName();
            	_lastname=partner.getSecoundName();
            	_email=partner.getEmailaddress();
            	userId=user.getCoUserId();
            	Set<CmPersonBpartner>person=partner.getCmPersonBpartner();
            	sex=person==null||person.size()==0?null:partner.getCmPersonBpartner().iterator().next().getSex();
            	organizationId=user.getCmOrganization()!=null?user.getCmOrganization().getCmOrganizationId():null;
			}
    	}
        catch(Exception e){
        	e.printStackTrace();
        	auth_success=false;
        }
        return auth_success;
    }
}
