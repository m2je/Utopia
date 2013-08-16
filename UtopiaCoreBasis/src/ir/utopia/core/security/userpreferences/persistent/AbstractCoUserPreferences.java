package ir.utopia.core.security.userpreferences.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;
import ir.utopia.core.security.user.persistence.CoUser;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoUserPreferences 
 */
@MappedSuperclass
public abstract class AbstractCoUserPreferences extends AbstractBasicPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8958283360079071088L;
	private Long coUserPreferencesId;
	private CoUser coUser;
	private String key;
	private String value;

	// Constructors

	/** default constructor */
	public AbstractCoUserPreferences() {
	}

	/** full constructor */
	public AbstractCoUserPreferences(Long coUserPreferencesId, CoUser coUser,
			String key, String value) {
		this.coUserPreferencesId = coUserPreferencesId;
		this.coUser = coUser;
		this.key = key;
		this.value = value;
	}

	
	@GeneratedValue(strategy=GenerationType.TABLE,
	generator="CoUseprefernecesSequenceGenerator")
	@Id
	@Column(name = "CO_USER_PREFERENCES_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserPreferencesId() {
		return this.coUserPreferencesId;
	}

	public void setCoUserPreferencesId(Long coUserPreferencesId) {
		this.coUserPreferencesId = coUserPreferencesId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUser getCoUser() {
		return this.coUser;
	}

	public void setCoUser(CoUser coUser) {
		this.coUser = coUser;
	}

	@Column(name = "KEY", unique = false, nullable = false, insertable = true, updatable = true, length = 250)
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "VALUE", unique = false, nullable = false, insertable = true, updatable = true, length = 2000)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}