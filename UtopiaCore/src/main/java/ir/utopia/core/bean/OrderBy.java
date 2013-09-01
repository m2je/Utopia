package ir.utopia.core.bean;

import java.io.Serializable;

public class OrderBy extends EntityPair implements Serializable {
	public enum OrderbyDirection{
		ASC,DESC
	}
	OrderbyDirection direction;
	public OrderBy(Class<?> entityClass, String propertyName) {
		this(entityClass,propertyName,OrderBy.OrderbyDirection.DESC);
	}
	public OrderBy(Class<?> entityClass, String propertyName,OrderbyDirection direction) {
		super(entityClass, propertyName);
		setDirection(direction);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -56826583607085459L;
	public OrderbyDirection getDirection() {
		return direction;
	}
	public void setDirection(OrderbyDirection direction) {
		this.direction = direction;
	}

	
	
}
