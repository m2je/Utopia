package ir.utopia.core.util.tags.datainputform.client.grid.model;

public interface SearchCriteria {

	public int getFrom();
	
	public void setFrom(int from);
	
	public int getTo();
	
	public void setTo(int to);
	
	public void setSearchConditions(String[] conditions);
	
	public String [] getSearchConditions();
	
	public void  setConditionValues(String[] values);
	
	public String [] getConditionValues();
	
	public void setOrderBy(OrderBy orderBy);
	
	public OrderBy getOrderBy();
}

