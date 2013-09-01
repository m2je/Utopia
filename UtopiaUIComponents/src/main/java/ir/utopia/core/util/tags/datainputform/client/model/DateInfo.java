package ir.utopia.core.util.tags.datainputform.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;


public class DateInfo implements Serializable,IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5251962804636694553L;
	public static final transient int DATE_TYPE_GREGORIAN=1;
	public static final transient int DATE_TYPE_SOLAR=2;
	public static final String GERIGORIAN_DATE_FORMAT="yy MMMM dd HH:mm:ss";
	public static final String  TIME_FORMAT="HH:mm";
	int dateType; 
	private InputItem endDateItem;
	public DateInfo() {
		super();
		
	}
	
	public int getDateType() {
		return dateType;
	}
	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public InputItem getEndDateItem() {
		return endDateItem;
	}
//**************************************************************************************
	public void setEndDateItem(InputItem endDateItem) {
		this.endDateItem = endDateItem;
	}
}
