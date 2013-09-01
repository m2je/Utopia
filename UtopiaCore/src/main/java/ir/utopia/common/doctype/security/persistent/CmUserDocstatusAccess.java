package ir.utopia.common.doctype.security.persistent;



import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * CmUserDocstatusAccess 
 */
@Entity
@Table(name = "CM_USER_DOCSTATUS_ACCESS",  uniqueConstraints = {})
@NamedQueries({@NamedQuery(name=CmUserDocstatusAccess.USER_VALID_DOCSTATUS_ACCESS,query="SELECT CmDoctype.coUsecase.coUsecaseId FROM CmUserDocstatusAccess CmUserDocstatusAccess , CmDoctype CmDoctype "+(char)13+
			"WHERE CmDoctype.cmDoctypeId=CmUserDocstatusAccess.cmDocStatus.cmDoctype.cmDoctypeId AND CmUserDocstatusAccess.isactive=:isactive AND CmDoctype.isactive=:isactive "+(char)13+
			"AND CmDoctype.cmFiscalyear.cmFiscalyearId=:cmFiscalyearId AND CmUserDocstatusAccess.coUser.coUserId=:coUserId")})
public class CmUserDocstatusAccess extends AbstractCmUserDocstatusAccess
		implements java.io.Serializable {
	public static final String USER_VALID_DOCSTATUS_ACCESS="CmUserDocstatusAccess_USER_VALID_DOCSTATUS_ACCESS";

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 1124060193274351869L;

	/** default constructor */
	public CmUserDocstatusAccess() {
	}
	
	
	

}
