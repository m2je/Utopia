package ir.utopia.core.customproperty.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoCustomProperty entity provides the base persistence definition of
 * the CoCustomProperty entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoCustomProperty extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3718902273789787963L;

	private Long coCustomPropertyId;
	
	private String propertyName;
	private String propertyValue;
	private CoUsecase coUsecase;
	private Long recordId;
	private String extendedProps1;
	private String extendedProps2;
	private String extendedProps3;
	// Constructors

	/** default constructor */
	public AbstractCoCustomProperty() {
	}

	

	// Property accessors
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="CustomPropertyGenerator")
	@Column(name = "CO_CUSTOM_PROPERTY_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoCustomPropertyId() {
		return this.coCustomPropertyId;
	}

	public void setCoCustomPropertyId(Long coCustomPropertyId) {
		this.coCustomPropertyId = coCustomPropertyId;
	}

	

	@Column(name = "PROPERTY_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 3000)
	public String getPropertyName() {
		return this.propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Column(name = "PROPERTY_VALUE", unique = false, nullable = false, insertable = true, updatable = true, length = 3000)
	public String getPropertyValue() {
		return this.propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	

	@Column(name = "RECORD_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getRecordId() {
		return this.recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	@JoinColumn(name="CO_USECASE_ID")
	@ManyToOne
	public CoUsecase getCoUsecase() {
		return coUsecase;
	}



	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}


	@Column(name="EX_PROPS1")
	public String getExtendedProps1() {
		return extendedProps1;
	}



	public void setExtendedProps1(String extendedProps1) {
		this.extendedProps1 = extendedProps1;
	}


	@Column(name="EX_PROPS2")
	public String getExtendedProps2() {
		return extendedProps2;
	}



	public void setExtendedProps2(String extendedProps2) {
		this.extendedProps2 = extendedProps2;
	}


	@Column(name="EX_PROPS3")
	public String getExtendedProps3() {
		return extendedProps3;
	}



	public void setExtendedProps3(String extendedProps3) {
		this.extendedProps3 = extendedProps3;
	}

}