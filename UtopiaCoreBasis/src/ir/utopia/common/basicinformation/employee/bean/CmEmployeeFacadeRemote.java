package ir.utopia.common.basicinformation.employee.bean;

import java.util.List;
import java.util.Map;

import ir.utopia.common.basicinformation.employee.persistent.CmEmployee;
import ir.utopia.common.basicinformation.employee.persistent.CmVEmployee;
import ir.utopia.core.bean.OrganizationSupportFacade;
import javax.ejb.Remote;


/**
 * Remote interface for CmEmployee.
 * 
 * @author Arsalani
 */
@Remote
public interface CmEmployeeFacadeRemote extends OrganizationSupportFacade<CmEmployee,CmVEmployee> {
	
	public Long findEmployeeId(Long userId,Map<String, Object> context);
	public List<CmEmployee> findByPersonBpartnerForDuplicate(Long personId,Map<String, Object> context);
}
