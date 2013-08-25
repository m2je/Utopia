package ir.utopia.common.doctype.persistent;



import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CmDoctype
 */
@Entity
@Table(name = "CM_DOCTYPE",  uniqueConstraints = { @UniqueConstraint(columnNames = {
		"CO_USECASE_ID", "CO_USECASE_ACTION_ID", "CM_FISCALYEAR_ID" }) })
@TableGenerator(name = "DocTypeSequenceGenerator", 
table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_DOCTYPE")
@NamedQueries(
		{@NamedQuery(name=CmDoctype.GET_USER_ACCESSIBLE_USECASES,
		query="SELECT DISTINCT CoVUserAllValidAccess.coUsecaseId FROM CoVUserAllValidAccess CoVUserAllValidAccess ,CmDoctype CmDoctype  WHERE" +
				" CoVUserAllValidAccess.coUserId=:coUserId AND CmDoctype.coUsecase.coUsecaseId=CoVUserAllValidAccess.coUsecaseId AND CmDoctype.cmFiscalyear.cmFiscalyearId=:cmFiscalyearId")
		})

public class CmDoctype extends AbstractCmDoctype implements
		java.io.Serializable {
	public static final String GET_USER_ACCESSIBLE_USECASES="coGetUserAccessibleUsecases";
	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -9223234154460805873L;

	/** default constructor */
	public CmDoctype() {
	}

	

}
