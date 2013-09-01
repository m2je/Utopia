package ir.utopia.core.util.tags.dashboard.client.model;

import ir.utopia.core.util.tags.datainputform.client.grid.model.OrderBy;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchCriteria;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UsecaseSearchCriteria implements Serializable,IsSerializable,SearchCriteria{
	public static int SEARCH_PAGE_SIZE=25;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2869182109393388977L;
	Long usecaseId;
	Long fromDocTypeId;
	OrderBy orderBy;
	int dir=0;
	int from=0;
	int to=from+SEARCH_PAGE_SIZE;
	String [] searchConditions;
	String [] conditionValues;
	public UsecaseSearchCriteria(){
		
	}
	public UsecaseSearchCriteria(Long usecaseId, Long fromDocTypeId) {
		super();
		this.usecaseId = usecaseId;
		this.fromDocTypeId = fromDocTypeId;
	}
	public Long getUsecaseId() {
		return usecaseId;
	}
	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}
	public Long getFromDocTypeId() {
		return fromDocTypeId;
	}
	public void setFromDocTypeId(Long fromDocTypeId) {
		this.fromDocTypeId = fromDocTypeId;
	}
	public OrderBy getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}
	public int getDir() {
		return dir;
	}
	public void setDir(int dir) {
		this.dir = dir;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	@Override
	public String[] getSearchConditions() {
		return searchConditions;
	}
	@Override
	public void setConditionValues(String[] values) {
		this.conditionValues=values;
	}
	@Override
	public void setSearchConditions(String[] conditions) {
		this.searchConditions=conditions;
	}
	@Override
	public String[] getConditionValues() {
		return conditionValues;
	}
	
	
}
