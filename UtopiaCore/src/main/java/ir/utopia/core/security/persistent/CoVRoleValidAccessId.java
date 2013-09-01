package ir.utopia.core.security.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CoVRoleValidAccessId entity provides the base persistence definition of the
 * alidAccessId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class CoVRoleValidAccessId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2002729354262097288L;
	private Long coRoleId;
	private Long coUsecaseId;
	private Long coUsecaseActionId;
	private Long coActionId;
	private Long cmSubsystemId;
	private Long cmSystemId;

	// Constructors

	/** default constructor */
	public CoVRoleValidAccessId() {
	}

	/** full constructor */
	public CoVRoleValidAccessId(Long coRoleId, Long coUsecaseId,
			Long coUsecaseActionId, Long coActionId, Long cmSubsystemId,
			Long cmSystemId) {
		this.coRoleId = coRoleId;
		this.coUsecaseId = coUsecaseId;
		this.coUsecaseActionId = coUsecaseActionId;
		this.coActionId = coActionId;
		this.cmSubsystemId = cmSubsystemId;
		this.cmSystemId = cmSystemId;
	}

	// Property accessors

	@Column(name = "CO_ROLE_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getCoRoleId() {
		return this.coRoleId;
	}

	public void setCoRoleId(Long coRoleId) {
		this.coRoleId = coRoleId;
	}

	@Column(name = "CO_USECASE_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getCoUsecaseId() {
		return this.coUsecaseId;
	}

	public void setCoUsecaseId(Long coUsecaseId) {
		this.coUsecaseId = coUsecaseId;
	}

	@Column(name = "CO_USECASE_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getCoUsecaseActionId() {
		return this.coUsecaseActionId;
	}

	public void setCoUsecaseActionId(Long coUsecaseActionId) {
		this.coUsecaseActionId = coUsecaseActionId;
	}

	@Column(name = "CO_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getCoActionId() {
		return this.coActionId;
	}

	public void setCoActionId(Long coActionId) {
		this.coActionId = coActionId;
	}

	@Column(name = "CM_SUBSYSTEM_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getCmSubsystemId() {
		return this.cmSubsystemId;
	}

	public void setCmSubsystemId(Long cmSubsystemId) {
		this.cmSubsystemId = cmSubsystemId;
	}

	@Column(name = "CM_SYSTEM_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getCmSystemId() {
		return this.cmSystemId;
	}

	public void setCmSystemId(Long cmSystemId) {
		this.cmSystemId = cmSystemId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CoVRoleValidAccessId))
			return false;
		CoVRoleValidAccessId castOther = (CoVRoleValidAccessId) other;

		return ((this.getCoRoleId() == castOther.getCoRoleId()) || (this
				.getCoRoleId() != null
				&& castOther.getCoRoleId() != null && this.getCoRoleId()
				.equals(castOther.getCoRoleId())))
				&& ((this.getCoUsecaseId() == castOther.getCoUsecaseId()) || (this
						.getCoUsecaseId() != null
						&& castOther.getCoUsecaseId() != null && this
						.getCoUsecaseId().equals(castOther.getCoUsecaseId())))
				&& ((this.getCoUsecaseActionId() == castOther
						.getCoUsecaseActionId()) || (this
						.getCoUsecaseActionId() != null
						&& castOther.getCoUsecaseActionId() != null && this
						.getCoUsecaseActionId().equals(
								castOther.getCoUsecaseActionId())))
				&& ((this.getCoActionId() == castOther.getCoActionId()) || (this
						.getCoActionId() != null
						&& castOther.getCoActionId() != null && this
						.getCoActionId().equals(castOther.getCoActionId())))
				&& ((this.getCmSubsystemId() == castOther.getCmSubsystemId()) || (this
						.getCmSubsystemId() != null
						&& castOther.getCmSubsystemId() != null && this
						.getCmSubsystemId()
						.equals(castOther.getCmSubsystemId())))
				&& ((this.getCmSystemId() == castOther.getCmSystemId()) || (this
						.getCmSystemId() != null
						&& castOther.getCmSystemId() != null && this
						.getCmSystemId().equals(castOther.getCmSystemId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCoRoleId() == null ? 0 : this.getCoRoleId().hashCode());
		result = 37
				* result
				+ (getCoUsecaseId() == null ? 0 : this.getCoUsecaseId()
						.hashCode());
		result = 37
				* result
				+ (getCoUsecaseActionId() == null ? 0 : this
						.getCoUsecaseActionId().hashCode());
		result = 37
				* result
				+ (getCoActionId() == null ? 0 : this.getCoActionId()
						.hashCode());
		result = 37
				* result
				+ (getCmSubsystemId() == null ? 0 : this.getCmSubsystemId()
						.hashCode());
		result = 37
				* result
				+ (getCmSystemId() == null ? 0 : this.getCmSystemId()
						.hashCode());
		return result;
	}

}