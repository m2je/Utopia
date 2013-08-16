package ir.utopia.common.basicinformation.notificationroles.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "CM_NOTIFICATION_ROLES", uniqueConstraints = {})
@TableGenerator(name = "NotificationRolesSequenceGenerator", 
table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_NOTIFICATION_ROLES")
public class CmNotificationRoles extends AbstractCmNotificationRoles implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3611345972595984731L;

	public CmNotificationRoles() {
	}

}
