package ir.utopia.core.util.tags.datainputform.client;


public interface DataInputFormContainer extends EditorDataChangeListener,IncludedGridDataHandler,WidgetContainer{

	public  void reload();


	public void redirect(String page);

	public String getWindowNo();

	public String getRecordId();

	public String getFormActionName();

	public void newRecord();
	
	public String getSaveToken();
	
	public String getUsecaseName();
}
