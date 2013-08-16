package ir.utopia.core.security.usruscsactnaccs.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoUsrUscsActnAccs entity provides the base persistence definition of
 * the CoUsrUscsActnAccs entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVUsrUscsActnAccs extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4248519249006307515L;
	private Long coUsrUscsActnAccsId;
	private String userName;
	private String usecaseactionName;
	
	

	// Constructors
	/** default constructor */
	public AbstractCoVUsrUscsActnAccs() {
	}

	

	// Property accessors
	@Id
	@Column(name = "CO_USR_USCS_ACTN_ACCS_ID")
	public Long getCoUsrUscsActnAccsId() {
		return this.coUsrUscsActnAccsId;
	}

	public void setCoUsrUscsActnAccsId(Long coUsrUscsActnAccsId) {
		this.coUsrUscsActnAccsId = coUsrUscsActnAccsId;
	}

	@Column(name = "USECASEACTION_NAME")
	public String getUsecaseactionName() {
		return usecaseactionName;
	}



	public void setUsecaseactionName(String usecaseactionName) {
		this.usecaseactionName = usecaseactionName;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}


}