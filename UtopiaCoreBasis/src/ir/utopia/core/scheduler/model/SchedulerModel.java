package ir.utopia.core.scheduler.model;

import ir.utopia.core.process.BeanProcess;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

public class SchedulerModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2983602862147601177L;

	private Properties props;
	private Map<String, Object> context;
	private BeanProcess processBean; 
	public SchedulerModel(BeanProcess processBean,Properties props,Map<String, Object> context){
		this.props=props;
		this.context=context;
		this.processBean=processBean;
	}
	
	public void setProperty(String propertyName,Object value){
		if(props==null){
			props=new Properties();
		}
	}
	
	public Object getProperty(String propertyName){
		if(props==null){
			return null;
		}
		return props.get(propertyName);
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public BeanProcess getProcessBean() {
		return processBean;
	}

	public void setProcessBean(BeanProcess processBean) {
		this.processBean = processBean;
	} 
	
	
}
