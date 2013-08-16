package ir.utopia.core.util.tags.process.client;

public interface UtopiaProcessHandlerInterface {
	
	public static final int SERVICE_ACTION_TYPE_EXCUTE_WITH_CONFIRM=0;
	public static final int SERVICE_ACTION_TYPE_UPDATE_STATUS=1;
	public static final int SERVICE_ACTION_TYPE_EXCUTE_WITHOUT_CONFIRM=2;
	
	public  void callAction(Long processIdentifier,String []params,String []values,String processTitle, String submitpath,ProcessConstants constants,boolean alertForsuccess,int executionType,boolean refreshAfterExecute) ;
	
	public void addProcessListener(UtopiaProcessListener listener);
	
	public boolean removeProcessListener(UtopiaProcessListener listener);
}
