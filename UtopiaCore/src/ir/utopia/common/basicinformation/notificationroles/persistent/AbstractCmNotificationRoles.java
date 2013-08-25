package ir.utopia.common.basicinformation.notificationroles.persistent;

import ir.utopia.common.basicinformation.notification.persistent.CmNotification;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.role.persistence.CoRole;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractCmNotificationRoles extends AbstractUtopiaPersistent implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4440726291295598027L;
	
	private Long cmNotificationRolesId;
	private CmNotification cmNotification;
	private CoRole coRole;

	public AbstractCmNotificationRoles() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="NotificationRolesSequenceGenerator")
	@Column(name = "CM_NOTIFICATION_ROLES_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmNotificationRolesId() {
		return this.cmNotificationRolesId;
	}

	public void setCmNotificationRolesId(Long cmNotificationRolesId) {
		this.cmNotificationRolesId = cmNotificationRolesId;
	}

	@ManyToOne
	@JoinColumn(name = "CM_NOTIFICATION_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmNotification getCmNotification() {
		return this.cmNotification;
	}

	public void setCmNotification(CmNotification cmNotification) {
		this.cmNotification = cmNotification;
	}

	@ManyToOne
	@JoinColumn(name = "CO_ROLE_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoRole getCoRole() {
		return this.coRole;
	}

	public void setCoRole(CoRole coRole) {
		this.coRole = coRole;
	}

}