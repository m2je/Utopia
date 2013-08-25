package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzCalendars entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_CALENDARS", uniqueConstraints = {})
public class QrtzCalendars extends AbstractQrtzCalendars implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 8773893128525393031L;

	/** default constructor */
	public QrtzCalendars() {
	}

	/** full constructor */
	public QrtzCalendars(QrtzCalendarsId id, String calendar) {
		super(id, calendar);
	}

}
