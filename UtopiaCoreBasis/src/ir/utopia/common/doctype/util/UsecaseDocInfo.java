package ir.utopia.common.doctype.util;

import java.util.List;

public class UsecaseDocInfo {

	String columnName;
	String methodName;
	Class<?> enumClass;
	List<UsecaseDocStatusInfo>statuses;
	public UsecaseDocInfo(String columnName, String methodName, Class<? extends Enum<?>> enumClass) {
		super();
		this.columnName = columnName;
		this.methodName=methodName;
		this.enumClass = enumClass;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Class<? > getEnumClass() {
		return enumClass;
	}
	public void setEnumClass(Class<?> enumClass) {
		this.enumClass = enumClass;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public List<UsecaseDocStatusInfo> getStatuses() {
		return statuses;
	}
	public void setStatuses(List<UsecaseDocStatusInfo> statuses) {
		this.statuses = statuses;
	}
	
}
