package ir.utopia.core.bean;

import java.io.Serializable;

public interface BeanReportModel extends Serializable {
	
	public String getQueryString();
	
	public String[] getQueryParameters();
	
	public Object getParameterValue(String parameter);

	public String getCountQuery();
	
	
}
