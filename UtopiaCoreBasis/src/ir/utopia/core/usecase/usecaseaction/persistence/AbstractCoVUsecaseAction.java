package ir.utopia.core.usecase.usecaseaction.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoUsecaseAction entity provides the base persistence definition of
 * the CoUsecaseAction entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVUsecaseAction extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 9126315075084908537L;

	private Long coUsecaseActionId;
	
	private String usecaseName;
	private String actionName;
	
	
	// Constructors

	/** default constructor */
	public AbstractCoVUsecaseAction() {
	}


	// Property accessors
	@Id
	@Column(name = "CO_USECASE_ACTION_ID")
	public Long getCoUsecaseActionId() {
		return this.coUsecaseActionId;
	}

	public void setCoUsecaseActionId(Long coUsecaseActionId) {
		this.coUsecaseActionId = coUsecaseActionId;
	}


	@Column(name = "USECASE_NAME")
	public String getUsecaseName() {
		return usecaseName;
	}


	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}


	@Column(name = "ACTION_NAME")
	public String getActionName() {
		return actionName;
	}


	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
}