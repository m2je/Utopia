package ir.utopia.common.basicinformation.employee.persistent;

import java.util.Date;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.common.basicinformation.jobtitle.persistent.CmJobTitle;
import ir.utopia.common.locality.province.persistent.CmProvince;
import ir.utopia.common.locality.state.persistent.CmState;
import ir.utopia.core.persistent.AbstractOrganizationData;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * AbstractCmEmployee entity provides the base persistence definition of the 
 * CmEmployee entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public abstract class AbstractCmEmployee  extends AbstractOrganizationData implements java.io.Serializable {

	//Fields
	
	private static final long serialVersionUID = 2421999172938379436L;
	private Long cmEmployeeId;
	private CmPersonBpartner cmPersonBpartner;
	private CmJobTitle cmJobTitle;
	private CmEmployee cmEmployee;
	private String code;
	private Date hireDate;
	private Long salary;
	private Long bonus;
	private CmProvince cmProvince;
	private CmState cmState;

	
	
	
	// Constructors

	/** default constructor */
	public AbstractCmEmployee() {
	}



	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
				generator="EmployeeSequenceGenerator")
	@Column(name = "CM_EMPLOYEE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmEmployeeId() {
		return cmEmployeeId;
	}
	public void setCmEmployeeId(Long cmEmployeeId) {
		this.cmEmployeeId = cmEmployeeId;
	}



	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE})	
	@JoinColumn(name = "CM_PERSON_BPARTNER_ID", referencedColumnName = "CM_PERSON_BPARTNER_ID")
	public CmPersonBpartner getCmPersonBpartner() {
		return cmPersonBpartner;
	}
	public void setCmPersonBpartner(CmPersonBpartner cmPersonBpartner) {
		this.cmPersonBpartner = cmPersonBpartner;
	}



	@ManyToOne
	@JoinColumn(name = "CM_JOBTITLE_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmJobTitle getCmJobTitle() {
		return cmJobTitle;
	}
	public void setCmJobTitle(CmJobTitle cmJobTitle) {
		this.cmJobTitle = cmJobTitle;
	}



	@ManyToOne
	@JoinColumn(name = "MANAGER_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmEmployee getCmEmployee() {
		return cmEmployee;
	}
	public void setCmEmployee(CmEmployee cmEmployee) {
		this.cmEmployee = cmEmployee;
	}
	
	
	
	@Column(name = "CODE", unique = false, nullable = false, insertable = true, updatable = true)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}



	@Temporal(TemporalType.DATE)
	@Column(name = "HIREDATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getHireDate() {
		return hireDate;
	}
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}



	@Column(name = "SALARY", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getSalary() {
		return salary;
	}
	public void setSalary(Long salary) {
		this.salary = salary;
	}



	@Column(name = "BONUS", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getBonus() {
		return bonus;
	}
	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}


    @Transient
	public CmProvince getCmProvince() {
    	if(getCmPersonBpartner()!=null){
    		cmProvince = getCmPersonBpartner().getCmProvince();
    	}
		return cmProvince;
	}

	public void setCmProvince(CmProvince cmProvince) {
		this.cmProvince = cmProvince;
	}


	@Transient
	public CmState getCmState() {
		if(getCmPersonBpartner()!=null){
			cmState = getCmPersonBpartner().getCmState();
		}
		return cmState;
	}

	public void setCmState(CmState cmState) {
		this.cmState = cmState;
	}

}
