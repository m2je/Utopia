package ir.utopia.common.basicinformation.notification.persistent;

import ir.utopia.common.basicinformation.notificationroles.persistent.CmNotificationRoles;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class AbstractCmNotification extends AbstractUtopiaPersistent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6981943863186595761L;
	
	private Long cmNotificationId;
	private Date startDate;
	private Date endDate;
	private String notification;
	private Set<CmNotificationRoles> cmNotificationRoleses = new HashSet<CmNotificationRoles>(
			0);

	public AbstractCmNotification() {
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="NotificationSequenceGenerator")
	@Column(name = "CM_NOTIFICATION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmNotificationId() {
		return this.cmNotificationId;
	}

	public void setCmNotificationId(Long cmNotificationId) {
		this.cmNotificationId = cmNotificationId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "NOTIFICATION", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
	public String getNotification() {
		return this.notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "cmNotification")
	public Set<CmNotificationRoles> getCmNotificationRoleses() {
		return this.cmNotificationRoleses;
	}

	public void setCmNotificationRoleses(
			Set<CmNotificationRoles> cmNotificationRoleses) {
		this.cmNotificationRoleses = cmNotificationRoleses;
	}

}