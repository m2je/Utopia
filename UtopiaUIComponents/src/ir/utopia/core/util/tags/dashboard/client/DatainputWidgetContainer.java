package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.DashboardRecord;
import ir.utopia.core.util.tags.dashboard.client.model.TransitionToInfo;
import ir.utopia.core.util.tags.dashboard.client.model.TransitionalUsecaseInfo;
import ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer;
import ir.utopia.core.util.tags.datainputform.client.DataInputFormContainer;
import ir.utopia.core.util.tags.datainputform.client.DataInputFormSubmitCallBack;
import ir.utopia.core.util.tags.datainputform.client.DataInputFormWidget;
import ir.utopia.core.util.tags.datainputform.client.DataInputServerService;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GWTConstants;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;

public class DatainputWidgetContainer extends AbstractDataInputFormContainer implements DataInputFormContainer,DataInputFormSubmitCallBack{
	 private DataInputFormWidget widget;
	 private DatainputWidgetContainer adaptee;		  
	 private DataInputFormModel dataInputFormModel;
	 private DashboardModel model;
	 private DashboardRecord currentRecord;
	 private Frame frame;
	  final DashBoardConstants constants;
			  public  DatainputWidgetContainer(final DashboardRecordDetail container,final DashBoardConstants constants,DashboardModel model){
				  this.model=model;
				  final TransitionalUsecaseInfo info=model.getCurrent();
				   this.constants=constants;
				  adaptee=this;
				  if(info.getUsecaseViewAddress()!=null&&info.getUsecaseViewAddress().trim().length()>0){
					  frame = new Frame(info.getUsecaseViewAddress());
					  frame.ensureDebugId("__DashboardWidgetFrame");
				  }else{
				  MessageUtility.progress(constants.pleaseWait(), constants.loading());
					DataInputServerService.getServer().getDataInputForm(info.getFormClass(),info.getFullUsecaseName(),
							"updateFromDashboard", new AsyncCallback<DataInputFormModel>() {
								
								@Override
								public void onSuccess(DataInputFormModel model) {
									MessageUtility.stopProgress();
									dataInputFormModel=model;
									if(model!=null){
										setItems(model.getItems());
										setUsecaseActionId(model.getUsecaseActionId());
										setEditorActionName(getFormActionName());
										setEditorFormClass(info.getFormClass());
										setEditorUsecaseName(getUsecaseName());
										InputItem item= model.getInputItem(info.getStatusFieldName());
										if(item==null){//if form does not have an item for status field we will add it
											 item=new InputItem(info.getStatusFieldName(),InputItem.DISPLAY_TYPE_HIDDEN, 100);
											 InputItem []items= model.getItems();
											 InputItem []temp=new InputItem[items.length+1];
											 System.arraycopy(items, 0, temp, 0, items.length);
											 temp[items.length]=item;
											 model.setItems(temp);
										}
										widget=new DataInputFormWidget(null,model,constants,adaptee);
										widget.setSubmitMode(DataInputFormWidget.FORM_SUBMIT_AJAX);
										widget.setSubmitCallBack(adaptee);
										container.updateWidget(adaptee);
										setWidgetsMap(widget.getWidgetsMap());
										setWidgetsLabelMap(widget.getWidgetsLabelMap());
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
										reload();
									}else{
										MessageUtility.error(constants.error(),constants.failToAccessServer());
									}
									
								}
								
								@Override
								public void onFailure(Throwable caught) {
									MessageUtility.stopProgress();
									MessageUtility.error(constants.error(),constants.failToAccessServer());
									GWT.log(caught.toString());
								}
							});
					}
			  }
//****************************************************************************************************************************************
	public native void fireOnLoadMethod(String methodName)/*-{
		$wnd[methodName]();
	}-*/;
//****************************************************************************************************************************************
		public void reload() {
			if(frame!=null){
				frameLoadRecord(String.valueOf(currentRecord.getRowId()));
			}else{
			super.setModified(false);
			MessageBox.progress(constants.pleaseWait(), constants.loadingData());
			final TransitionalUsecaseInfo current= model.getCurrent();
			DataInputServerService.getServer().
			getDataInputFormData(current.getFormClass(), getUsecaseName(), "update", String.valueOf(currentRecord.getRowId()),true,getSaveToken(), 
					new AsyncCallback<DataInputDataModel>(){

						@Override
						public void onFailure(Throwable caught) {
							MessageUtility.stopProgress();
							MessageUtility.error(constants.error(),constants.failToAccessServer());
							GWT.log(caught.toString());
						}

						@Override
						public void onSuccess(DataInputDataModel result) {
							MessageUtility.stopProgress();
							if(result!=null){
								initData(result);
								if(!currentRecord.isRead()){
									markAsReadUread(current,currentRecord,true);
								}
							}else{
								MessageUtility.error(constants.error(),constants.failToAccessServer());
							}
							
						}
				
			});
			}
		}
//***********************************************************************************
		public void initData(DataInputDataModel values) {
			super.setContext(values.getContext());
			widget.setData(values);
			super.initData(values.getValueModel());
		}
//****************************************************************************************************************************************
		@Override
		public void redirect(String page) {
			
		}
//****************************************************************************************************************************************
		@Override
		public String getWindowNo() {
			return null;
		}
//****************************************************************************************************************************************
		@Override
		public String getRecordId() {
			return String.valueOf(currentRecord.getRowId());
		}
//****************************************************************************************************************************************
		@Override
		public String getFormActionName() {
			return model.getCurrent().getSaveActionName();
		}
//****************************************************************************************************************************************
		@Override
		public void newRecord() {
			
		}
//****************************************************************************************************************************************
		@Override
		public String getSaveToken() {
			return null;
		}
//****************************************************************************************************************************************
		@Override
		public String getUsecaseName() {
			return model.getCurrent().getFullUsecaseName();
		}
//****************************************************************************************************************************************
		@Override
		public void submitted(boolean success, String id, int formSubmitter) {
			if(widget!=null){
				widget.setSubmitURL(dataInputFormModel.getActionUrl());
			}
			MessageUtility.stopProgress();
			if(success){
				model.updateRecord(currentRecord);
			}else{	
				MessageUtility.error(constants.error(), constants.failToSave());
			}
			
		}
//****************************************************************************************************************************************
		public void setCurrentRecord(DashboardRecord currentRecord){
			this.currentRecord=currentRecord;
			if(widget!=null)
				reload();
		}
//****************************************************************************************************************************************
		public Widget getWidget(){
			return frame==null?widget: frame;
		}
//****************************************************************************************************************************************
		public void changeStatus(TransitionToInfo to){
			if(frame!=null){
				frameChangeStatus(String.valueOf(to.getDoctypeCode()));
			}else{
			MessageUtility.progress(constants.pleaseWait(),constants.submitingData());
			TransitionalUsecaseInfo usInfo=model.getCurrent();
			String fieldName= usInfo.getStatusFieldName();
			if(fieldName!=null&&fieldName.trim().length()>0){
				Widget w= widget.getWidgetsMap().get(fieldName);
				if(w!=null){
					EditorFactory.setWidgetValue(w, to.getDoctypeCode());
				}
			}
			widget.setSubmitURL(to.getActionName()+"_"+usInfo.getFullUsecaseName()+GWTConstants.STRUTS_EXTENSION);
			widget.submit();
			}
		}
//****************************************************************************************************************************************
	public void markAsReadUread(TransitionalUsecaseInfo current,final DashboardRecord record,final boolean read){
		DashBoardServerService.getServer().markAs(current.getUsecaseId(), record.getRowId(), read, new AsyncCallback<ExecutionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(ExecutionResult result) {
				if(result!=null){
					if(result.isSuccess()){
						model.markAsRead(record, read);
					}
				}
				
			}
		});
	}	
//****************************************************************************************************************************************
public static native void frameChangeStatus(String status)/*-{
	$doc.getElementById(__DashboardWidgetFrame).contentWindow.changeStatus(status);
}-*/;
//****************************************************************************************************************************************
public static native boolean frameLoadRecord(String recordId)/*-{
	return $doc.getElementById(__DashboardWidgetFrame).contentWindow.loadRecord(recordId);
}-*/;
//****************************************************************************************************************************************

//****************************************************************************************************************************************
}