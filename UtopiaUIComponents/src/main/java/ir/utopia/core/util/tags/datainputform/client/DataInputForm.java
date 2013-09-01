package ir.utopia.core.util.tags.datainputform.client;

import ir.utopia.core.util.tags.datainputform.client.model.DataInputDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.MessageBox;

public class DataInputForm extends AbstractDataInputFormContainer implements EntryPoint,DataInputFormContainer,DataInputFormSubmitCallBack {
	
	
	
	static boolean checked = false;
	
	DataInputFormConstants constants=(DataInputFormConstants)GWT.create(DataInputFormConstants.class);
	DataInputFormModel model;
	public void onModuleLoad() {
		refreshPanel();
		
	}
//***********************************************************************************
	private void refreshPanel(){
		MessageBox.progress(constants.pleaseWait(), constants.loading());
		DataInputServerService.getServer().getDataInputForm(getFormClassName(),getUsecaseName(),
				getFormActionName(), new DataInputServiceCallbackHandler());
	}
//***********************************************************************************
	public void reload(){
		reload(getRecordId(),true);
	}
//***********************************************************************************
	public void newRecord() {
		// TODO Auto-generated method stub
		
	}
//***********************************************************************************
	@Override
	public void submitted(boolean result,String id,int formSubmitter) {
		setModified(false);
	}
//***********************************************************************************	
	private void reload(String recordId,boolean reload){
		super.setModified(false);
		MessageBox.progress(constants.pleaseWait(), constants.loadingData());
		DataInputServerService.getServer().
		getDataInputFormData(getFormClassName(), getUsecaseName(), getFormActionName(), recordId,reload,getSaveToken(), new DataInputDataServiceCallbackHandler());
	}
//***********************************************************************************	
	public native String getFormClassName()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants::USE_CASE_FORM_CLASS_PARAMETER_NAME).value;
	}-*/;
//***********************************************************************************
	public native String getFormActionName()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants::USE_ACTION_NAME_PARAMETER_NAME).value;
	}-*/;
//***********************************************************************************
	public native String getUsecaseName()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants::USE_CASE_NAME_PARAMETER_NAME).value;
	}-*/; 
//***********************************************************************************	
	public native String getRecordId()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants::RECORDID_PARAMETER_NAME).value;
	}-*/;
//***********************************************************************************
	public native String getWindowNo()/*-{
    return $doc.getElementById(@ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants::WINDOW_NO_PARAMETER_NAME).value;
	}-*/;
//***********************************************************************************
	public native String isActionFailedForm()/*-{
    return $doc.getElementById(@ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants::SUCCESS_PARAMETER_NAME).value;
	}-*/;
//***********************************************************************************
	@Override
	public native String getSaveToken() /*-{
    return $doc.getElementById(@ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants::SAVE_TOKEN_PARAMETER_NAME).value;
	}-*/;
//***********************************************************************************
	public void redirect(String url){
		Window.Location.replace(GWT.getHostPageBaseURL()+url);
	}
//***********************************************************************************
	private void refreshComponents(DataInputFormModel model){
		if(datainputFormWidget!=null){
			RootPanel.get().remove(datainputFormWidget);
		}
		super.setItems(model.getItems());
		super.setUsecaseActionId(model.getUsecaseActionId());
		super.setEditorActionName(getFormActionName());
		super.setEditorFormClass(getFormClassName());
		super.setEditorUsecaseName(getUsecaseName());
		datainputFormWidget=new DataInputFormWidget("menuFrame",model,constants,this);
		datainputFormWidget.setSubmitCallBack(this);
		super.setWidgetsMap(datainputFormWidget.getWidgetsMap());
		super.setWidgetsLabelMap(datainputFormWidget.getWidgetsLabelMap());
		RootPanel.get().getElement().setDir(LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR");
		RootPanel.get().add(datainputFormWidget);
		String []onloadMethods=model.getOnLoadMethods();
		if(onloadMethods!=null){
			for(String method:onloadMethods){
				if(method!=null&&method.trim().length()>0){
					try {
						fireOnLoadMethod(method);
					} catch (Throwable e) {
						GWT.log(e.getMessage());	
					}
				}
			}
		}
		if(Boolean.TRUE.toString().equalsIgnoreCase(isActionFailedForm())){
			reload(getRecordId(),false);	
		}else{
			reload(getWindowNo(), false);
		}
	}




//***********************************************************************************
	class DataInputServiceCallbackHandler implements AsyncCallback<DataInputFormModel>{

		
		public void onFailure(Throwable e) {
			GWT.log("", e);
			refreshComponents(null);	
			MessageUtility.stopProgress();
		}

		
		public void onSuccess(DataInputFormModel result) {
			model=result;
			refreshComponents(result);
			MessageUtility.stopProgress();
		}
		
	}
//***********************************************************************************
	public void initData(DataInputDataModel values) {
		setContext(values.getContext());
		datainputFormWidget.setData(values);
		super.initData(values.getValueModel());
	}
//************************************************************************************
	public native void fireOnLoadMethod(String methodName)/*-{
		$wnd[methodName]();
	}-*/;
//***********************************************************************************	
	class DataInputDataServiceCallbackHandler implements AsyncCallback<DataInputDataModel>{

	public void onFailure(Throwable caught) {
		MessageUtility.stopProgress();
		MessageUtility.error(constants.error(),constants.failToAccessServer());
	}

	public void onSuccess(DataInputDataModel result) {
		MessageUtility.stopProgress();
		if(result!=null){
			initData(result);			
		}else{
			MessageUtility.error(constants.error(),constants.failToAccessServer());
		}
	}
		
	}


}
