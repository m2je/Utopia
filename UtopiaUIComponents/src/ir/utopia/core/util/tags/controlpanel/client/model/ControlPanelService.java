package ir.utopia.core.util.tags.controlpanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.InteractiveRemoteServiceCallSupport;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ControlPanelService extends RemoteService,InteractiveRemoteServiceCallSupport{
	public static final int LOG_BY_SYSTEM_SUBSYSTEM_USECASE_ACTION=0;
	public static final int LOG_BY_USER_ROLE=1;
	
	public static final int LIST_TYPE_SYSTEM=0;
	public static final int LIST_TYPE_SUB_SYSTEM=LIST_TYPE_SYSTEM+1;
	public static final int LIST_TYPE_USECASE=LIST_TYPE_SUB_SYSTEM+1;
	public static final int LIST_TYPE_ACTION=LIST_TYPE_USECASE+1;
	public static final int LIST_TYPE_ROLE=LIST_TYPE_ACTION+1;
	public static final int LIST_TYPE_USER=LIST_TYPE_ROLE+1;
	
	public static final int MAX_LIST_TYPE=LIST_TYPE_USER+1;
	public UILookupInfo loadData(int type);
	
	public UILookupInfo[] loadDetailData(Long[] parentIds,int parentType,int rootType);
	
	
	/**
	 * 
	 * @return
	 */
	public int requestWindowNumber();
	/**
	 * 
	 * @param actionName
	 * @param windoNumber
	 * @return
	 */
	public ExecutionResult getExecutionResult(String actionName,int windowNumber);
	
	
}
