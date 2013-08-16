package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.TransitionalUsecaseInfo;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DashboardClientService extends RemoteService {

	public List<TransitionalUsecaseInfo> getTransitionalUsecases(); 
	
	public SearchPageData getDashBoardRecords(UsecaseSearchCriteria criteria); 
	
	public ExecutionResult markAs(Long usecaseId, Long recordId,
			 boolean read); 
}
