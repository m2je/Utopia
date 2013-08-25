/**
 * 
 */
package ir.utopia.core.security.principal;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;

/**
 * @author Salarkia
 *
 */
public class LocalePricipal implements Principal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7140429326422421111L;

	private Locale locale; 
	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	public LocalePricipal(Locale locale){
		this.locale=locale;
	}
	public String getName() {
		return locale.toString();
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	 public boolean equals(Object o) {
			if (o == null)
			    return false;

		        if (this == o)
		            return true;
		 
		        if (!(o instanceof LocalePricipal))
		            return false;
		        LocalePricipal that = (LocalePricipal)o;

			if (this.getName().equals(that.getName()))
			    return true;
			return false;
		    }

}
