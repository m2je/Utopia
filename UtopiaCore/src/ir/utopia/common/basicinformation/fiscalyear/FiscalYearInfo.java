package ir.utopia.common.basicinformation.fiscalyear;

import java.io.Serializable;
import java.util.Date;

public class FiscalYearInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2739552044631481371L;
	private Long id;
	private String name;
	private Date startDate,endDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
