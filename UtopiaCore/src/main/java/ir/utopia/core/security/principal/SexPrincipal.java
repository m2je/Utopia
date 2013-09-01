package ir.utopia.core.security.principal;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.Sex;

import java.io.Serializable;
import java.security.Principal;

public class SexPrincipal implements Serializable, Principal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8210816732260782165L;
	Constants.Sex sex;
	public SexPrincipal(Constants.Sex sex){
		this.sex=sex;
	} 
	@Override
	public String getName() {
		return getSex().toString();
	}

	public Constants.Sex getSex(){
		return sex==null?Sex.male:sex;
	}
}
