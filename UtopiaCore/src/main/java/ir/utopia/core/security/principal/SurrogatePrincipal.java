/**
 * 
 */
package ir.utopia.core.security.principal;

import java.io.Serializable;
import java.security.Principal;

/**
 * @author salarkia
 * if user is SurrogatePrincipal of any other 
 * user then can use this principal as an alternate principal
 * to access the other user roles and grants 
 */
public class SurrogatePrincipal implements Principal,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4823621246216798663L;
	private long userId; 
	/**
	 * 
	 */
	public SurrogatePrincipal(long userId) {
		this.userId=userId;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return Long.toString(userId);
	}
	/**
	 * return userId
	 * @return
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * 
	 */
	public String toString() {
	  return("SurrogatePrincipal:  " + userId);
	}

	/**
	 * 
	 */
    public boolean equals(Object o) {
	if (o == null)
	    return false;

    if (this == o)
        return true;
 
    if (!(o instanceof SurrogatePrincipal))
        return false;
        SurrogatePrincipal that = (SurrogatePrincipal)o;

	if (this.getName().equals(that.getName()))
	    return true;
	return false;
    }
		 
	/**
	 * 
	 */	 
    public int hashCode() {
    	return (int)userId;
    }
}
