package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.DashboardRecord;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;


public interface DashboardModelListener {

	public void modelLoaded(boolean success);
	
	public void dashSearchCriterialChanged(UsecaseSearchCriteria searchCriteria);
	
	public void recordUpdated(DashboardRecord record);
	
	
}
