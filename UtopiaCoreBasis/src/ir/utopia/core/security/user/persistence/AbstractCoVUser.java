package ir.utopia.core.security.user.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoUser entity provides the base persistence definition of the CoUser
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVUser extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1569770128193025474L;
	private Long coUserId;
	private String username;
	private String password;
//	private String userImage;
	private String bpartnerName;

	// Constructors

	/** default constructor */
	public AbstractCoVUser() {
	}

	// Property accessors
	@Id
	@Column(name = "CO_USER_ID")
	public Long getCoUserId() {
		return this.coUserId;
	}

	public void setCoUserId(Long coUserId) {
		this.coUserId = coUserId;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

/*	
	@Column(name = "USER_IMAGE")
	public String getUserImage() {
		return this.userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
*/
	@Column(name = "BPARTNER_NAME")
	public String getBpartnerName() {
		return bpartnerName;
	}
	
	public void setBpartnerName(String bpartnerName) {
		this.bpartnerName = bpartnerName;
	}

}