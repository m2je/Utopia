package ir.utopia.core.bean;

import ir.utopia.core.constants.QueryComparsionType;

public class SearchConditionItem extends EntityPair {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5611070660852890417L;
	Object value;
	QueryComparsionType equalityType;
	private boolean andWithPreviousCondition=true;
	public SearchConditionItem(Class<?> entityClass,String propertyName , Object value,
			QueryComparsionType equalityType) {
		super(entityClass,propertyName);
		this.value = value;
		this.equalityType = equalityType;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public QueryComparsionType getEqualityType() {
		return equalityType;
	}
	public void setEqualityType(QueryComparsionType equalityType) {
		this.equalityType = equalityType;
	}

	public boolean isAndWithPreviousCondition() {
		return andWithPreviousCondition;
	}

	public void setAndWithPreviousCondition(boolean andWithPreviousCondition) {
		this.andWithPreviousCondition = andWithPreviousCondition;
	}

	
	
	
}
