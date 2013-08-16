package ir.utopia.common.doctype.security.persistent;

// Generated by MyEclipse Persistence Tools

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * CmRoleDocstatusAccess generated by MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CM_ROLE_DOCSTATUS_ACCESS", uniqueConstraints = {})
@NamedQueries({@NamedQuery(name=CmRoleDocstatusAccess.GET_ACCESSIBLE_USECASES,
	query="SELECT CmDoctype.coUsecase.coUsecaseId FROM CmRoleDocstatusAccess CmRoleDocstatusAccess ,CmDoctype CmDoctype ,CoUserRoles CoUserRoles " +(char)13+
			" WHERE CmDoctype.cmDoctypeId=CmRoleDocstatusAccess.cmDocStatus.cmDoctype.cmDoctypeId AND CoUserRoles.coUser.coUserId=:userId AND " +(char)13+
			"CoUserRoles.coRole.coRoleId=CmRoleDocstatusAccess.coRole.coRoleId AND CoUserRoles.isactive=:isactive AND CoUserRoles.coRole.isactive=:isactive AND " +(char)13+
			"CmRoleDocstatusAccess.isactive=:isactive AND CmDoctype.isactive=:isactive AND CmDoctype.cmFiscalyear.cmFiscalyearId=:cmFiscalyearId")})
public class CmRoleDocstatusAccess extends AbstractCmRoleDocstatusAccess
		implements java.io.Serializable {
	
	public static final String GET_ACCESSIBLE_USECASES="CmRoleDocstatusAccess_GET_ACCESSIBLE_USECASES";
	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -1303810021054648969L;

	/** default constructor */
	public CmRoleDocstatusAccess() {
	}


}
