package ir.utopia.core.process;

import java.io.Serializable;

public class BeanProcessParameter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4943069612259684281L;
	private String name;
	private Object value;
	
	public BeanProcessParameter(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
