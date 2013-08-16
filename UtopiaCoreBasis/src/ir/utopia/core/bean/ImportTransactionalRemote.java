package ir.utopia.core.bean;

public interface ImportTransactionalRemote extends UtopiaProcessBean{

	public static final int SAVE_BATCH_SIZE=UtopiaBasicUsecaseBean.SAVE_BATCH_SIZE;
	
	public static final String PERSITENTLIST_DATA_PROVIDER="dataProvider";
	
	public static final String RECORD_START_INDEX="startIndex";
	
	public static final String RECORD_END_INDEX="endIndex";

}
