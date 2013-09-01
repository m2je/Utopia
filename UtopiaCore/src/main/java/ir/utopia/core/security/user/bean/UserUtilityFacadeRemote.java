package ir.utopia.core.security.user.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.bean.UtopiaProcessBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.security.user.persistence.CoUser;

import javax.ejb.Remote;

@Remote
public interface UserUtilityFacadeRemote extends UtopiaBean,UtopiaProcessBean{
	
	public static final String UTILITY_PROCESS_CHANGE_PASSWORD="changePassword_Co_Sc_User";
	public static final String UTILITY_PROCESS_CHANGE_PASSWORD_NEW_PASSWORD_PARAMETER="newPassword";
	public static final String USERID_PARAMETER_NAME="userId";
	/**
	 * 
	 * @param partner
	 * @param user
	 */
	public void creatUser(CoUser user,String name,String family,String code,Constants.Sex sex,String address,String email)throws Exception;
	/**
	 * 
	 * @param partner
	 * @param user
	 */
	public void updateUser(CoUser user,String name,String family,String code,Constants.Sex sex,String address,String email)throws Exception;
	/**
	 * 
	 * @param adminusername
	 * @param adminPassword
	 */
	public void encyptNonEncryptedUsers();
	/**
	 * 
	 * @param user
	 * @param bpartner
	 * @param context
	 */
	public void createUserForBpartner(CoUser user,CmBpartner bpartner)throws Exception;
	/**
	 * 
	 * @param user
	 * @param bpartner
	 * @param context
	 */
	public void updateUserForBpartner(CoUser user,CmBpartner bpartner)throws Exception;
}
