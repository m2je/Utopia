package ir.utopia.core.usecase.usecaseaction.persistence;

import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoUsecaseAction entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_USECASE_ACTION", uniqueConstraints = {})
@TableGenerator(name = "UsecaseActionSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME",
		valueColumnName = "CURRENTID", pkColumnValue = "CO_USECASE_ACTION")
// @LookupJoinColumn(joins=
//	 @LookupJoin(
//			 joinToClass=CoAction.class,
//			 lookupConfiguration=@LookupConfiguration()),primaryKeyClass=CoUsecaseAction.class,
//			 condition=" CoUsecaseAction.coUsecase.coUsecaseId in " +
//	 		"(select  CoUsecaseActionModel.coUsecase.coUsecaseId from CoUsecaseAction CoUsecaseActionModel where CoUsecaseActionModel.coUsecaseActionId=@coUsecaseAction@)")
		@LookupConfiguration(displayColumns={"coAction.name","coUsecase.name"},displayItemSeperator="-")
public class CoUsecaseAction extends AbstractCoUsecaseAction implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -5102730350354060208L;

	/** default constructor */
	public CoUsecaseAction() {
	}


}
