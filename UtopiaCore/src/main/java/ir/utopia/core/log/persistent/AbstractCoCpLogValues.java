package ir.utopia.core.log.persistent;

// default package

import ir.utopia.core.constants.Constants.LogStatusType;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoCpLogValues entity provides the base persistence definition of the
 * CoCpLogValues entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoCpLogValues extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 18269923775833069L;
	private Long coCpLogValuesId;
	private CoCpLogConfig coCpLogConfig;
	private LogStatusType status;
	private String description;
	// Constructors

	



	/** default constructor */
	public AbstractCoCpLogValues() {
	}

	

	// Property accessors
	@Id
	@Column(name = "CO_CP_LOG_VALUES_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoCpLogValuesId() {
		return this.coCpLogValuesId;
	}

	public void setCoCpLogValuesId(Long coCpLogValuesId) {
		this.coCpLogValuesId = coCpLogValuesId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_CP_LOG_CONFIG_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoCpLogConfig getCoCpLogConfig() {
		return this.coCpLogConfig;
	}

	public void setCoCpLogConfig(CoCpLogConfig coCpLogConfig) {
		this.coCpLogConfig = coCpLogConfig;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name="STATUS")
	public LogStatusType getStatus() {
		return status;
	}

	public void setStatus(LogStatusType status) {
		this.status = status;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}