package ir.utopia.common.basicinformation.notification.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "CM_NOTIFICATION", uniqueConstraints = {})
@TableGenerator(name = "NotificationSequenceGenerator", 
table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_NOTIFICATION")
public class CmNotification extends AbstractCmNotification implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9158359219602749345L;

	public CmNotification() {
	}

}
