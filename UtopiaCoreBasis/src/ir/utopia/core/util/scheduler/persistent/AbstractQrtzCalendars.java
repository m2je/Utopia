package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractQrtzCalendars entity provides the base persistence definition of the
 * QrtzCalendars entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzCalendars implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7138885974803563067L;
	private QrtzCalendarsId id;
	private String calendar;

	// Constructors

	/** default constructor */
	public AbstractQrtzCalendars() {
	}

	/** full constructor */
	public AbstractQrtzCalendars(QrtzCalendarsId id, String calendar) {
		this.id = id;
		this.calendar = calendar;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "calendarName", column = @Column(name = "CALENDAR_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzCalendarsId getId() {
		return this.id;
	}

	public void setId(QrtzCalendarsId id) {
		this.id = id;
	}

	@Column(name = "CALENDAR", unique = false, nullable = false, insertable = true, updatable = true)
	public String getCalendar() {
		return this.calendar;
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}

}