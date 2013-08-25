package ir.utopia.core.security.userorganizationaccess.bean;

import ir.utopia.core.bean.UtopiaBean;

import javax.ejb.Remote;

@Remote
public interface CoUserOrganizationAccessFacadeRemote extends UtopiaBean {

	public Long[] getUserAccessibleOrganizations(Long userId);
}
