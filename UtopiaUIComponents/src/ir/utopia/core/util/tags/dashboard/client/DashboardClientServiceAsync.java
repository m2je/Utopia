package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.TransitionalUsecaseInfo;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DashboardClientServiceAsync {


	void getDashBoardRecords(UsecaseSearchCriteria criteria,
			AsyncCallback<SearchPageData> callback);

	void getTransitionalUsecases(
			AsyncCallback<List<TransitionalUsecaseInfo>> callback);

	void markAs(Long usecaseId, Long recordId,boolean read,
			 AsyncCallback<ExecutionResult> callback);

}
