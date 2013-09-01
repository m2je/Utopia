package ir.utopia.security.beans;


import ir.utopia.core.bean.UtopiaBean;

import javax.ejb.Remote;
import javax.security.auth.Subject;

@Remote
public interface LoginBeanRemote extends UtopiaBean {

/**
 * 
 * @param username
 * @param password
 * @return
 */	
 public boolean login(String username, String password);

 /**
  * 
  * @return
  */
 public Subject getUserProfile();
}
