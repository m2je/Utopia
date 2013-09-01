package ir.utopia.core.util.tags.datainputform.client;

import ir.utopia.core.util.tags.datainputform.client.grid.IncludedGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridMetaData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridValueModel;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWidget;
import ir.utopia.core.util.tags.datainputform.client.model.AttachmentInfo;
import ir.utopia.core.util.tags.datainputform.client.model.CustomButton;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.password.PasswordEdior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.form.ComboBox;

public class DataInputFormWidget extends VerticalPanel {
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	public static final int FORM_SUBMITER_SAVE_BUTTON=1;
	public static final int FORM_SUBMITER_SAVE_NEW_BUTTON=2;
	public static final int FORM_SUBMIT_CLASSIC=1;
	public static final int FORM_SUBMIT_AJAX=2;
	private static final transient int LABEL_WEIGHT=3;
	private static final transient int EDITOR_WEIGHT=6;
	private static final transient int ICON_WEIGHT=1;
	private static final String ATTACHMENT_EDITOR_NAME="attachments";
	private static final transient String TABLE_TD_CSS_NAME="clsLabel";
	private static final transient String TABLE_CSS_NAME="clsTableBody";
	DataInputFormConstants constants;
	FormPanel formPane;
	private String action;
	String pkName;
	private String []validationFuctions;
	private FlexTable attachmentPanel;
	private FlexTable currentAttachments;
	private FlexTable attachmentRootPanel;
	private HashMap<String, Widget>createdWidgets=new HashMap<String, Widget>();
	private HashMap<String, Label>createdLabels=new HashMap<String, Label>();
	private List<String>removedAttachments=new ArrayList<String>();
	private HashMap<Integer, Integer>attachFileRealMapping=new HashMap<Integer, Integer>();
	InputItem []items;
	VerticalPanel rootPanel;
	int submitMode=1;
	private DataInputFormContainer container;
	Hidden submitModeHidden=new Hidden("submitMode__",String.valueOf(FORM_SUBMIT_CLASSIC)); 
	Hidden pkHidden;
	Hidden localHidden=new Hidden("locale");
	Hidden actionNameHidden=new Hidden("actionName");
	Hidden saveTokenHidden=new Hidden("saveToken");
	Hidden revisionDescription=new Hidden("revisionDescription");
	ScrollPanel messagesScroller;
	VerticalPanel messagesPanel;
	DataInputFormSubmitCallBack callback;
	DataInputFormModel model;
	DataInputFormWidgetSubmitListener submitListener;
	Button submit,reset,cancel,saveNew,newRecord;
	public DataInputFormWidget(String rootFrame,DataInputFormModel model,DataInputFormConstants constants,DataInputFormContainer container){
		this.constants=constants;
		this.container=container;
		this.model=model;
		inititate(rootFrame,model,container);
	}
	private void inititate(String rootFrame,DataInputFormModel model,DataInputFormContainer container){
		if(rootFrame==null){
			formPane=new FormPanel();
		}else{
			formPane=new FormPanel(rootFrame);
		}
		formPane.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPane.setStyleName(TABLE_CSS_NAME);
		if(model==null){
			MessageUtility.error(constants.error(),constants.failToAccessServer());
			return;
		}
		GlobalEditorDataChangeListener listener=new GlobalEditorDataChangeListener(container,model.getItems());
		
		action=model.getActionUrl();
		pkName=model.getPrimaryKeyName();
		validationFuctions=model.getValidationFunctions();
		ArrayList<ArrayList<InputItem>>allRowsItems=new ArrayList<ArrayList<InputItem>>();
		ArrayList<InputItem>largeItems=new ArrayList<InputItem>();
		ArrayList<InputItem> visibleItems=new ArrayList<InputItem>();
		items= model.getItems();
		rootPanel=new VerticalPanel();
		initMessagesListBox();
		String direction=model.getDirection();
		
		for(int i=0;i<items.length;i++){
			if(items[i].getDisplayType()==InputItem.DISPLAY_TYPE_HIDDEN){
				Widget w=getEditor(items[i], direction, listener);
				createdWidgets.put(items[i].getColumnName(), w);
				rootPanel.add(w);
				continue;
			}else{
				visibleItems.add(items[i]);
			}
		}
		l1 :for(int i=0;i<visibleItems.size();i++){
			
			ArrayList<InputItem>rowItems=new ArrayList<InputItem>();
			int createdObjects=0;
			for(int j=0;j<model.getColumnCount()||createdObjects<model.getColumnCount();j++){
				int index=i+j;
				if(index>=visibleItems.size()){
					if(rowItems.size()>0)
						allRowsItems.add(rowItems);
					break l1;
				}else{
					
					if(isLargeEditor(visibleItems.get(index))){
						largeItems.add(visibleItems.get(index));
						continue;
					}
				
					createdObjects++;
					rowItems.add(visibleItems.get(index));
					if(visibleItems.get(index).isBreakLineAfer()){
						break;
					}
				}
			}
			if(createdObjects>0)
				i+=createdObjects-1;
			if(rowItems.size()>0)
				allRowsItems.add(rowItems);
		}
		int columnCount=model.getColumnCount()*3;
		
		 
		rootPanel.setStyleName(TABLE_CSS_NAME);
		if(model.getLayOut()==DataInputFormModel.LAY_OUT_DETAIL_MASTER){
			if(largeItems.size()>0){
				ArrayList<InputItem>removings=new ArrayList<InputItem>();
				for(InputItem item:largeItems){
					if(item.getDisplayType()==InputItem.DISPLAY_TYPE_SEARCH_GRID){
						Widget w=EditorFactory.createEditor(item, direction);
						createdWidgets.put(item.getColumnName(), w);
						rootPanel.add(w);
						removings.add(item);
					}
						
				}
				largeItems.removeAll(removings);
			}
		}
		rootPanel.add(getSimpleItems(allRowsItems, columnCount, direction,listener));
		if(largeItems.size()>0){
			rootPanel.add(createLargeItems(largeItems,model.getDirection(),listener));	
		}
		rootPanel.getElement().setDir(direction);
		if(model.isSupportAttachment()){
			initAttachment();
		}
		pkHidden=new Hidden(pkName);
		rootPanel.add(pkHidden);
		rootPanel.add(localHidden);
		rootPanel.add(actionNameHidden);
		rootPanel.add(getActionButtons(model));
		rootPanel.add(submitModeHidden);
		rootPanel.add(saveTokenHidden);
		rootPanel.add(revisionDescription);
		formPane.add(rootPanel);
		add(formPane);
		setStyleName(TABLE_CSS_NAME);
		setWidth("100%");
		setHeight("100%");
		submitListener=new DataInputFormWidgetSubmitListener(this);
		formPane.addSubmitCompleteHandler(submitListener);
	}
//***********************************************************************************
	private void initMessagesListBox(){
		 messagesPanel=new VerticalPanel();
		messagesScroller = new ScrollPanel(messagesPanel);
		messagesScroller.setHeight("50px");
		messagesScroller.setWidth("100%");
		rootPanel.add(messagesScroller);
		messagesScroller.setVisible(false);
		if(LocaleInfo.getCurrentLocale().isRTL()){
			messagesScroller.getElement().setDir("RTL");
		}
	}
//***********************************************************************************
	private void fireSubmitCompletion(boolean success,String recordId,int formSubmitter){
		if(callback!=null)
			callback.submitted(success,recordId,formSubmitter);
	}
//***********************************************************************************
	public void showMessages(JSONArray messages){
		if(messages==null){
			messagesScroller.setVisible(true);
			return;
		}
		ArrayList<String>errors=new ArrayList<String>();
		ArrayList<String>warnings=new ArrayList<String>();
		ArrayList<String>infos=new ArrayList<String>();
		if(messages.size()>0){
			messagesScroller.setVisible(true);
		}
		for(int i=0;i<messages.size();i++){
			JSONObject value=(JSONObject) messages.get(i);
			JSONString type= value.get("type").isString();
			if("error".equals(type.stringValue())){
				errors.add(value.get("message").toString());
			}else if("warning".equals(type.stringValue())){
				warnings.add(value.get("message").toString());
			}else{
				infos.add(value.get("message").toString());
			}
		}
		
		for(String error:errors){
			Widget message=createMessage(error, images.error().createImage());
			messagesPanel.add(message);
		}
		for(String warning:warnings){
			Widget message=createMessage(warning, images.warning().createImage());
			messagesPanel.add(message);		
			}
		for(String info:infos){
			Widget message=createMessage(info, images.info().createImage());
			messagesPanel.add(message);
		}
	}
//***********************************************************************************
	protected Widget createMessage(String message,Widget icon){
		HorizontalPanel h=new HorizontalPanel();
		h.setWidth("100%");
		h.add(icon);
		Label l=new Label(message);
		l.setStylePrimaryName("clsLabel");
		h.add(l);
		h.setCellWidth(icon, "20px");
		if(LocaleInfo.getCurrentLocale().isRTL()){
			h.getElement().setDir("RTL");
		}
		return h;
	}
//***********************************************************************************
		private boolean isLargeEditor(InputItem item){
			return item.getDisplayType()==InputItem.DISPLAY_TYPE_GRID||
			item.getDisplayType()==InputItem.DISPLAY_TYPE_LARGE_STRING||item.getDisplayType()==InputItem.DISPLAY_TYPE_SEARCH_GRID;
		}
//***********************************************************************************
		private Widget getSimpleItems(ArrayList<ArrayList<InputItem>>allRowsItems,int columnCount,String direction,GlobalEditorDataChangeListener listener){
			Grid smalElementsGridPanel=new Grid(allRowsItems.size(),columnCount);
			if(columnCount<=0){
				return smalElementsGridPanel;
			}
			smalElementsGridPanel.setStyleName(TABLE_CSS_NAME);
			int columnWidth=100/columnCount;
			int labelRatio=columnWidth*LABEL_WEIGHT/10;
			int editorRatio=columnWidth*EDITOR_WEIGHT/10;
			int iconRatio=columnWidth*ICON_WEIGHT/10;
			int row=-1;
			CellFormatter formater= smalElementsGridPanel.getCellFormatter();
			for(ArrayList<InputItem> rowItems:allRowsItems ){
				row++;
				int edCounter=0;
				for(int j=0;j<columnCount;j+=3){
					formater.setStylePrimaryName(row,j, TABLE_TD_CSS_NAME);
					formater.setWidth(row,j, labelRatio+"%");
					formater.setWidth(row,j+1, editorRatio+"%");
					formater.setWidth(row,j+2,iconRatio+"%");
					if(edCounter<rowItems.size()){
						InputItem item=rowItems.get(edCounter++);
						Widget label=createLable(item, direction);
						if(label!=null)
							smalElementsGridPanel.setWidget(row, j,label )  ;
						
						smalElementsGridPanel.setWidget(row, j+1,getEditor(item, direction,listener))  ;
						smalElementsGridPanel.setWidget(row, j+2, createIcon(item,direction))  ;
						
					}
				}
			}
			smalElementsGridPanel.getElement().setDir(direction);
			return smalElementsGridPanel;
		}
	//***********************************************************************************
		private Widget createLargeItems(ArrayList<InputItem>largeItems,String direction,GlobalEditorDataChangeListener listerner){
			Grid largePanel=new Grid(largeItems.size(),3);
			largePanel.setStyleName(TABLE_CSS_NAME);
			int rowCounter=0;
			for(InputItem item: largeItems){
				Widget label=createLable(item, direction);
				if(label!=null){
				largePanel.setWidget(rowCounter,0, label);
				}
				largePanel.setWidget(rowCounter,1,getEditor(item, direction,listerner));
				largePanel.setWidget(rowCounter,2, createIcon(item, direction));
				largePanel.getCellFormatter().setWidth(rowCounter, 1, "80%");
				largePanel.getCellFormatter().setWidth(rowCounter, 0, "10%");
				largePanel.getCellFormatter().setWidth(rowCounter++, 2, "10%");
			}
			return largePanel;
		}
//***********************************************************************************
		private Widget getEditor(InputItem item,String direction,GlobalEditorDataChangeListener listener){
			 Widget widget=EditorFactory.createEditor(item, direction);
			 if(widget instanceof IncludedGrid){
				 ((IncludedGrid)widget).setDataListener(container);
				 ((IncludedGrid)widget).setWidgetContainer(container);
			 }else if(widget instanceof LOVWidget){
				 ((LOVWidget)widget).setContainer(container);
			 }
			 EditorFactory.addListener(widget, listener);
			 String key=item.getColumnName();
			 createdWidgets.put(key, widget);
			 return widget; 
		}	
//***********************************************************************************
		protected void initAttachment(){
			attachmentPanel=getAttachmentPanel();
			attachmentRootPanel=new FlexTable();
			currentAttachments=new FlexTable();
			currentAttachments.setStylePrimaryName("clsTableBody");
			currentAttachments.setWidth("100%");
			addAttachmentRow();
			VerticalPanel p=new VerticalPanel();
			p.setWidth("100%");
			CellFormatter cellFormatter= attachmentRootPanel.getCellFormatter();
			cellFormatter.setWidth(0, 0, "25%");
			cellFormatter.setWidth(0, 1, "50%");
			cellFormatter.setWidth(0, 2, "25%");
			attachmentRootPanel.setStylePrimaryName("clsBorderedBody");
			Label attachMentListLabel=new Label(constants.attachmentFilesList());
			attachMentListLabel.setStylePrimaryName("clsLabel");
			attachmentRootPanel.setWidget(0, 0, attachMentListLabel);
			attachmentRootPanel.setWidget(0, 1, currentAttachments);
			p.add(attachmentRootPanel);
			p.add(attachmentPanel);
			rootPanel.add(p);
		}
//***********************************************************************************
	public void setData(DataInputDataModel result){
		resetForm();
		if(result!=null){
			HashMap<String, GridData>gridData= result.getIncludedGridsData();
			HashMap<String, UILookupInfo> dynamicLookupInfos=result.getDynamicLookupsInfo();
			if(gridData!=null){
				for(String key:gridData.keySet() ){
					Widget widget= createdWidgets.get(key);
					if(widget!=null){
						Object value= result.getIncludedGridsData().get(key);
						EditorFactory.setWidgetValue(widget, value);
					}
				}
			}
			HashMap<String, String>valueMode= result.getValueModel();
			for(String item:valueMode.keySet()){
				Widget widget= createdWidgets.get(item);
				if(widget!=null){
					if(dynamicLookupInfos!=null){
						if(dynamicLookupInfos.get(item)!=null){
							EditorFactory.setLookupValue(widget, dynamicLookupInfos.get(item));
						}
					}
					EditorFactory.setWidgetValue(widget, valueMode.get(item));
				}
			}
			initCurrentAttachmentPanel(result.getAttachmentInfo());
			setButtonsEnabled(result);
		}
		
		fireDataChangeCallbackFunction();
	}
//***********************************************************************************
	public void setButtonsEnabled(DataInputDataModel model){
		boolean save=container.getRecordId()==null||"0".equals(container.getRecordId());
		if(!model.isEditEnable()&&!save){
			submit.setEnabled(false);
			submit.setStylePrimaryName("clsDisabledSaveButton");
			if(saveNew!=null){
				saveNew.setEnabled(false);
				saveNew.setStyleName("clsDisabledSaveNewRecordButton");
			}
		}else{
			submit.setEnabled(true);
			submit.setStylePrimaryName("clsSaveButton");
			if(saveNew!=null){
				saveNew.setEnabled(true);
				saveNew.setStyleName("clsSaveNewRecordButton");
			}
		}
	}
//***********************************************************************************
	protected void resetForm(){
		messagesScroller.setVisible(false);
		revisionDescription.setValue(null);
			initCurrentAttachmentPanel(null);
			for(Widget widget: createdWidgets.values()){
				EditorFactory.setWidgetValue(widget, null);
			}
			pkHidden.setValue("0");
		submit.setStylePrimaryName("clsSaveButton");
		submit.setEnabled(true);
		reset.setStyleName("clsReloadButton");
		reset.setEnabled(true);
		cancel.setStyleName("clsCancelButton");
		cancel.setEnabled(true);
		if(model.isAllowNewRecord()){
			saveNew.setStyleName("clsSaveNewRecordButton");
			saveNew.setEnabled(true);
			newRecord.setStyleName("clsNewRecordButton");
			newRecord.setEnabled(true);
		}
	}
//***********************************************************************************
	protected void fireDataChangeCallbackFunction(){
		if(model.getAfterLoadCallbacks()!=null&&model.getAfterLoadCallbacks().length>0){
			for(String callback:model.getAfterLoadCallbacks()){
				try {
					fireCallBack(callback);
				} catch (Exception e) {
					GWT.log(e.toString());
				}
			}
		}
	}
//************************************************************************************
		protected native void fireCallBack(String methodName)/*-{
			$wnd[methodName]();
		}-*/;

//***********************************************************************************
		@SuppressWarnings("deprecation")
		private void initCurrentAttachmentPanel(List<AttachmentInfo> attachments){
			if(currentAttachments!=null)
				currentAttachments.removeAllRows();
			if(attachmentRootPanel!=null)
				attachmentRootPanel.setVisible(false);
			if(attachments!=null&attachmentRootPanel!=null){
				attachmentRootPanel.setVisible(true);	
				 int index=0;
				 CellFormatter formater= currentAttachments.getCellFormatter();
				 RowFormatter rowFormatter=currentAttachments.getRowFormatter();
				for(final AttachmentInfo cur:attachments){
					 Label rowIden=new Label(String.valueOf(index+1));
					 rowIden.setStylePrimaryName("clsLabel");
					 currentAttachments.setWidget(index, 0, rowIden);
					 Anchor hlink=new Anchor();
					 hlink.setText(cur.getFileName());
					 hlink.setHref(GWT.getHostPageBaseURL()+"download_Co_Ut_Attachment."+GWTConstants.STRUTS_EXTENSION+"?recordId="+URL.encodePathSegment(cur.getAttachmentId())+"&usecase="+URL.encodePathSegment(container.getUsecaseName()));
					 hlink.setStylePrimaryName("clsLabel");
					 currentAttachments.setWidget(index, 1, hlink);
					 
					 ImageHyperlink removeAttachment=new ImageHyperlink(images.drop().createImage());
					 
					 removeAttachment.addClickHandler(new ClickHandler() {
						 String myId=cur.getAttachmentId();
						@Override
						public void onClick(ClickEvent event) {
							for(int i=0;i<currentAttachments.getRowCount();i++)
							{
								
								if(Window.confirm(constants.attachmentRemoveConfirmMessage())&&event.getSource().equals( currentAttachments.getWidget(i, 2))){
									removedAttachments.add(myId);
									currentAttachments.removeRow(i);
									break;
								}	
							}
									

						}
					});
					 currentAttachments.setWidget(index, 2, removeAttachment);
					 formater.setWidth(index, 0,"10%");
					 formater.setWidth(index, 1,"80%");
					 formater.setWidth(index, 2,"10%");
					 rowFormatter.setStylePrimaryName(index, index%2==0?"clsTableOddRow":"clsTableEvenRow");
					 index++;
				}
				
			}
			return ;
		}
	//***********************************************************************************	
		private FlexTable getAttachmentPanel(){
			 FlexTable attachmentPane=new FlexTable();
//			 attachmentPane.setStylePrimaryName("clsTableBody");
			 attachmentPane.setWidth("100%");
			return attachmentPane;
		}
	//***********************************************************************************
		@SuppressWarnings("deprecation")
		public void addAttachmentRow(){
			int newRowIndex=attachmentPanel.getRowCount();
			attachmentPanel.setWidget(newRowIndex, 0, null);
			attachmentPanel.setWidget(newRowIndex, 1, new Label(constants.attachment()));
			FileUpload fileUpload= new FileUpload();
			attachmentPanel.setWidget(newRowIndex, 2, fileUpload);
			fileUpload.setStylePrimaryName("clstext");
			fileUpload.setName(ATTACHMENT_EDITOR_NAME);
			final ImageHyperlink addRow=new ImageHyperlink(images.addAttachment().createImage());
			final ImageHyperlink removeRow=new ImageHyperlink(images.removeAttachment().createImage());
			addRow.addClickHandler(new ClickHandler() {
				ImageHyperlink me=addRow;
				@Override
				public void onClick(ClickEvent event) {
					addAttachmentRow();
					me.setVisible(false);
				}
			});
			addRow.setTitle(constants.addAttachmentRow());
			
			removeRow.setTitle(constants.removeAttachmentRow());
			
			removeRow.addClickHandler(new ClickHandler() {
				int myIndex=attachFileRealMapping.size();
				@Override
				public void onClick(ClickEvent event) {
					int myRealIndex= attachFileRealMapping.get(myIndex);
					
					removeAttachmentRow(attachFileRealMapping.get(myRealIndex));
				}
			});
			attachFileRealMapping.put(attachFileRealMapping.size(), newRowIndex);
			attachmentPanel.setWidget(newRowIndex, 3, addRow);
			attachmentPanel.setWidget(newRowIndex, 4,removeRow);
			attachmentPanel.setWidget(newRowIndex, 5, null);
			
			for(int i=0;i<=newRowIndex;i++){
				attachmentPanel.getWidget(i, 4).setVisible(true);
			}
			if(newRowIndex==0){	
				attachmentPanel.getWidget(newRowIndex, 4).setVisible(false);
			}	
			CellFormatter cf=attachmentPanel.getCellFormatter();
			cf.setStylePrimaryName(newRowIndex,1, "clsLabel");
			cf.setStylePrimaryName(newRowIndex,2,"clsLabel");
			cf.setStylePrimaryName(newRowIndex,3, "clsLabel");
			cf.setStylePrimaryName(newRowIndex,4, "clsLabel");
			
			cf.setWidth(newRowIndex,0, "30%");
			cf.setWidth(newRowIndex,1, "5%");
			cf.setWidth(newRowIndex,2, "10%");
			cf.setWidth(newRowIndex,3, "5%");
			cf.setWidth(newRowIndex,4, "5%");
			cf.setWidth(newRowIndex,5, "30%");
		}
	//***********************************************************************************
		protected void removeAttachmentRow(int rowIndex){
			attachmentPanel.removeRow(rowIndex);
			attachmentPanel.getWidget(attachmentPanel.getRowCount()-1, 3).setVisible(true);
			attachmentPanel.getWidget(attachmentPanel.getRowCount()-1, 4).setVisible(attachmentPanel.getRowCount()-1!=0);
		} 
	//***********************************************************************************
		protected void reorderAttachFileMapping(int removedIndex){
			for(int key:attachFileRealMapping.keySet()){
				if(attachFileRealMapping.get(key)>=removedIndex){
					attachFileRealMapping.put(key, attachFileRealMapping.get(key)-1);
				}
			}
		}
	//***********************************************************************************
		private Widget getActionButtons( DataInputFormModel model ){
			CustomButton []customButtons= model.getCustomButtons();
			VerticalPanel root=new VerticalPanel();
			root.setWidth("100%");
			Grid defaultButtonsPanel=new Grid(1,3);
			defaultButtonsPanel.setWidth("100%");
			defaultButtonsPanel.getColumnFormatter().setWidth(0, "30%");
			defaultButtonsPanel.getColumnFormatter().setWidth(1, "30%");
			defaultButtonsPanel.getColumnFormatter().setWidth(2, "30%");
			defaultButtonsPanel.setWidget(0, 1, getDefaultActionButtonsPanel());
			root.add(defaultButtonsPanel);
			root.setCellHorizontalAlignment(defaultButtonsPanel,  HasHorizontalAlignment.ALIGN_CENTER);
			if(customButtons!=null&&customButtons.length>0){
				Grid customButtonsPanel=new Grid(1,3);
				customButtonsPanel.setWidth("100%");
				int buttonCount=customButtons.length;
				int buttonsCellWidth=10*buttonCount;
				customButtonsPanel.getColumnFormatter().setWidth(0, (100-buttonsCellWidth)/2+"%");
				customButtonsPanel.getColumnFormatter().setWidth(1, buttonsCellWidth+"%");
				customButtonsPanel.getColumnFormatter().setWidth(2, (100-buttonsCellWidth)/2+"%");
				customButtonsPanel.setWidget(0, 1, getCustomButtonsPanel(customButtons));
				root.add(customButtonsPanel);
				root.setCellHorizontalAlignment(customButtonsPanel,  HasHorizontalAlignment.ALIGN_CENTER);
			}
			
			return root;
		}
//***********************************************************************************
		protected Widget getCustomButtonsPanel(CustomButton []customButtons){
			HorizontalPanel panel=new HorizontalPanel();
			panel.setSpacing(5);
			for(final CustomButton cb:customButtons){
				Button b=new Button(cb.getHeader());
				b.setStylePrimaryName(cb.getCssClass());
				if(cb.getDisplayLogic()!=null&&cb.getDisplayLogic().trim().length()>0)
					b.setVisible(eval(cb.getDisplayLogic()));
				if(cb.getReadOnlyLogic()!=null&&cb.getReadOnlyLogic().trim().length()>0)
					b.setEnabled(eval(cb.getReadOnlyLogic()));
				createdWidgets.put(cb.getColumnName(), b);
				b.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						callNativeFunction(cb.getOnclick());
					}
				});
				panel.add(b);
			}
			return panel;
		}
//**********************************************************************
	protected  boolean  eval(String opration){
		try {
			return evaljs(opration);
		} catch (Exception e) {
			GWT.log("fail to evaluate logic :"+opration);
			return false;
		}
	}
//**********************************************************************
	protected native boolean  evaljs(String opration)/*-{
		return  eval(opration);
	}-*/;
//************************************************************************************
public native void callNativeFunction(String functionName)/*-{
	$wnd[functionName]();
}-*/;
//***********************************************************************************
		protected Widget getDefaultActionButtonsPanel(){
			submit=new Button(model.getBundel("save"));
			submit.setStyleName("clsSaveButton");
			submit.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					submitListener.setNewRecordAfterSuccess(FORM_SUBMITER_SAVE_BUTTON);
					submit();
					
				}});
			HorizontalPanel panel=new HorizontalPanel();
			panel.getElement().setDir(model.getDirection());
			panel.add(submit);
			
			reset=new Button(model.getBundel("reset"));
			reset.setStyleName("clsReloadButton");
			reset.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					container.reload();
				}
			});
			panel.add(reset);
			
			cancel=new Button(model.getBundel("cancel"));
			cancel.setStyleName("clsCancelButton");
			cancel.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					container.redirect(model.getSearchURL());
					
				}});
			panel.add(cancel);
			if(model.isAllowNewRecord()){
				saveNew=new Button(constants.saveNewRecord());
				saveNew.setStyleName("clsSaveNewRecordButton");
				saveNew.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						submitListener.setNewRecordAfterSuccess(FORM_SUBMITER_SAVE_NEW_BUTTON);
						submit(false);
					}});
				panel.add(saveNew);
				newRecord=new Button(constants.newRecord());
				newRecord.setStyleName("clsNewRecordButton");
				newRecord.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						container.newRecord();
					}});
				panel.add(newRecord);
				
			}
			return panel;
		}
//***********************************************************************************
		private void simpleSubmit(HashMap<String, GridValueModel>gridValuMap){
			 ArrayList<Widget>hiddenParameters=new ArrayList<Widget>();
			 for(String key:gridValuMap.keySet()){
				 GridValueModel valueModel= gridValuMap.get(key);
				 GridMetaData gridMetadata=model.getInputItem(key).getGridMetaData();
				 if(valueModel!=null&&valueModel.isModified()){
					 
					 String []columns=valueModel.getColumnNames();
					 int index=0;
					 for(GridRowData row: valueModel.getModifiedRows()){
						 String []rowData=row.getData();
						 Hidden rowId=new Hidden(key+"["+index+"]."+valueModel.getPrimaryKey(),String.valueOf(row.getRowId()));
						 hiddenParameters.add(rowId);
						 
						 for(int column=0;column<columns.length;column++){
							 InputItem currentItem= gridMetadata.getColumn(columns[column]);
							 if(currentItem!=null&&currentItem.getDisplayType()==InputItem.DISPLAY_TYPE_LOV&&
									 currentItem.getLOVConfiguration().isMultiSelect()&&rowData[column]!=null){
								 String []ids= rowData[column].split(",");
								 for(int i=0;i<ids.length;i++){
									 Hidden modified=new Hidden(key+"["+index+"]."+columns[column]+"["+i+"]",ids[i]);	
									 hiddenParameters.add(modified);
								 }
							 }else{
								 Hidden modified=new Hidden(key+"["+index+"]."+columns[column],rowData[column]);	
								 hiddenParameters.add(modified);
							 }
						 }
						 index++;
					 }
					 int rowCounter=valueModel.getModifiedRows().size();
					 for(long row: valueModel.getRemovedIds()){
						 Hidden removed=new Hidden(key+"["+rowCounter+"]."+
								 valueModel.getPrimaryKey(),String.valueOf(row));
						 Hidden deletedHiden=new Hidden(key+"["+rowCounter+++"].deleted"
								 ,"true");
						 hiddenParameters.add(removed);
						 hiddenParameters.add(deletedHiden);
					 }
					 	 
				 }
				 
			 }
			 
			 for(String key:createdWidgets.keySet()){
				 Widget w=createdWidgets.get(key);
				 if(!EditorFactory.isVisible(w)){
					 EditorFactory.setWidgetValue(w, null);
				 }
				 if(w instanceof ComboBox){
					 Object value=((ComboBox)w).getValue();
					 value=value==null?"-1":value.toString();
					 hiddenParameters.add(new Hidden(key,value.toString()));
				 }
			 }
			 if(attachmentPanel!=null){
				 for(int i=0;i< attachmentPanel.getRowCount();i++){
						FileUpload fileupload=(FileUpload) attachmentPanel.getWidget(i, 2);
						if(fileupload!=null){
							 hiddenParameters.add(new Hidden("attachmentNames",fileupload.getFilename()));
						}
					 }
			 }
			 
			 for(String removedAttachment:removedAttachments){
				 hiddenParameters.add(new Hidden("removedAttachmentIds",removedAttachment));
			 }
			 hiddenParameters.add(new Hidden(ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants.WINDOW_NO_PARAMETER_NAME,container.getWindowNo()));
			 for(Widget hidden:hiddenParameters){
				 rootPanel.add(hidden);
			 }
			 pkHidden.setValue(container.getRecordId());
			 localHidden.setValue(LocaleInfo.getCurrentLocale().getLocaleName());
			 actionNameHidden.setValue(container.getFormActionName());
			 saveTokenHidden.setValue(container.getSaveToken());
			 formPane.setAction(action);
			 formPane.setMethod(FormPanel.METHOD_POST);
			 formPane.submit();
			 for(Widget hidden:hiddenParameters){
				 rootPanel.remove(hidden);
			 }

		} 
		//***********************************************************************************
		private boolean validateUserDefinedValidations(){
			if(validationFuctions!=null&&validationFuctions.length>0){
				for(String validationFunction:validationFuctions){
					if(validationFunction!=null&&validationFunction.trim().length()>0){
						String actionType;
						try {
							actionType = container.getRecordId();
							if(actionType!=null&&actionType.trim().length()>0&&Integer.parseInt(actionType)>0){
								actionType="edit";
							}else{
								actionType="save";
							}
						} catch (Exception e) {
							GWT.log(e.getMessage());
							actionType="save";
						}
						try {
							if(!callValidationFunction(actionType, validationFunction)){
								return false;
							}
						} catch (Exception e) {
							GWT.log(e.getMessage());
							return false;
						}
					}
				}
			}
			return true;
		}
//***********************************************************************************		
		private void markNonvalidated(Label label,Widget widget){
			if(label!=null){
				label.setStyleName("clsErrorLabel");
			}
			if(widget instanceof PasswordEdior){
				((PasswordEdior)widget).markasError();
			}
		}
//***********************************************************************************
		private boolean validateMandatoryField(InputItem item,Object value){
			if(item.isMandatory()&&!item.isHidden()&&valueIsNull(item, value)){
					MessageUtility.error(constants.error(),constants.fieldIsMandatory().replace("@p1@", item.getHeader()));
					return false;
			}
			return true;
		}

	//***********************************************************************************
		private boolean valueIsNull(InputItem item,Object value){
			return EditorFactory.valueIsNull(item.getDisplayType(), value);
		}
	//***********************************************************************************
		private boolean validateInputData(InputItem item,Object value,Widget widget){
			if(value!=null&&value.toString().trim().length()>0){
				if(item.getDisplayType()==InputItem.DISPLAY_TYPE_NUMERIC){
					 return validateNumeric(item, value);
				}else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_DATE){
					return validateDate(item, value, EditorFactory.getDateFormat(widget));
				}
			}
			return true;
		}
	//***********************************************************************************
		private boolean validateNumeric(InputItem item,Object value){
			return EditorFactory.validateNumeric(constants, item, value);
		}
//***********************************************************************************
		private boolean validateDate(InputItem item,Object value,int dateType){
			Object enddate=null;
			if(item.getDateInfo()!=null&&item.getDateInfo().getEndDateItem()!=null){
				enddate=EditorFactory.getWidgetValue(createdWidgets.get(item.getDateInfo().getEndDateItem().getColumnName()));
			}
			return EditorFactory.validateDate(item, enddate, constants, value, dateType);
		}
//***********************************************************************************
		private void clearPreviousErrors(){
			for(Label label: createdLabels.values()){
				label.setStyleName("clsLabel");
			}
			for(Widget w:createdWidgets.values()){
				if(w instanceof PasswordEdior){
					((PasswordEdior)w).clearError();
				}
			}
		}
//***********************************************************************************
		public boolean validate(){
			clearPreviousErrors();
			if(validateUserDefinedValidations()){
				for(InputItem item:items){
					Widget widget= createdWidgets.get(item.getColumnName());
					Object value= EditorFactory.getWidgetValue(widget);
					Label label= createdLabels.get(item.getColumnName());
					if(widget!=null&&EditorFactory.isVisible(widget)){
						if(!validateMandatoryField(item, value)||!validateInputData(item, value,widget)){
							markNonvalidated(label, widget);
							return false;
							}
						if(widget instanceof InputVerifier){
							if(!((InputVerifier)widget).verify()){
								return false;
							}
						}
						if(widget instanceof PasswordEdior){
							String password1=((PasswordEdior)widget).getPassword();
							String password2=((PasswordEdior)widget).getConfirmValue();
							if(!password1.equals(password2)){
								MessageUtility.error(constants.error(), constants.passwordMissMathError()  );
								markNonvalidated(label, widget);
								return false;
							}
						}
					}
				}
				return true;
			}else{
				return false;
			}
			
			
		}

//***********************************************************************************
	private void trimInputsData(){
		for(InputItem item:items){
			Widget widget= createdWidgets.get(item.getColumnName());
			if(!(widget instanceof IncludedGrid)){
				Object value= EditorFactory.getWidgetValue(widget);
				if(item.getDisplayType()==InputItem.DISPLAY_TYPE_NUMERIC||
						item.getDisplayType()==InputItem.DISPLAY_TYPE_LARGE_STRING||
						item.getDisplayType()==InputItem.DISPLAY_TYPE_PASSWORD||
						item.getDisplayType()==InputItem.DISPLAY_TYPE_STRING){
					EditorFactory.setWidgetValue(widget, ((String)value).trim());
				}
			}
			
			}
	}		
//***********************************************************************************
	public void setSubmitCallBack(DataInputFormSubmitCallBack callback){
		this.callback=callback;
	}
//***********************************************************************************
	public void submit(){
		submit(false);
	}
//***********************************************************************************
	public void submit(boolean showRevision){
		trimInputsData();
		if(validate()){
			try {
				if(showRevision||model.isConfirmRevisionDecription()){
					submitWithRevision(model.isRevisionTextManadary());
				}else{
					doSubmit();
				}
				
			} catch (Exception e) {
				GWT.log("", e);
				MessageUtility.error(constants.error(),constants.failToSubmit());
				fireSubmitCompletion(false,null,FORM_SUBMITER_SAVE_BUTTON);
			}
			}
		}

//***********************************************************************************
		private void doSubmit(){
			HashMap<String,GridValueModel>grids=new HashMap<String,GridValueModel>();
			ArrayList<Widget>temporaryEnabledWidget=new ArrayList<Widget>();
			for(String key:createdWidgets.keySet()){
				Widget widget=createdWidgets.get(key);
				if(widget instanceof IncludedGrid){
					grids.put(key,((IncludedGrid) widget).getValueModel());
				}else if(widget!=null&&!EditorFactory.isWidgetEnabled(widget)){
					temporaryEnabledWidget.add(widget);
					EditorFactory.setWidgetEnabled(widget, true);
				} 
			}
						try {
							simpleSubmit(grids);
						} catch (Exception e) {
							GWT.log(e.toString());
						}
			if(FORM_SUBMIT_AJAX==submitMode ){		
				for(Widget w:temporaryEnabledWidget){
					EditorFactory.setWidgetEnabled(w, false);
				}		
					}
		}




//***********************************************************************************
private Widget createIcon(InputItem item,String direction){
	return new Label();
}

//***********************************************************************************
private Widget createLable(InputItem item,String direction){
	if(item.getDisplayType()!=InputItem.DISPLAY_TYPE_PASSWORD){
		Label label=EditorFactory.createLable(item, direction,true);
		if(label!=null){
			if(item.isHidden())
				label.setVisible(false);
			createdLabels.put(item.getColumnName(), label);
			}
		return label;
	}
	return null;
}
//***********************************************************************************
	public HashMap<String, Widget>getWidgetsMap(){
		return createdWidgets;
	}	
//***********************************************************************************
	public HashMap<String, Label>getWidgetsLabelMap(){
		return createdLabels;
	}	
//***********************************************************************************
	public native boolean callValidationFunction(String actiontype,String nativeMethodName)/*-{
		return $wnd[nativeMethodName](actiontype);
	}-*/;
//***********************************************************************************
	public void setSubmitURL(String submitURL){
		this.action=submitURL;
	}
//***********************************************************************************
	public int getSubmitMode() {
		return submitMode;
	}
//***********************************************************************************
	public void setSubmitMode(int submitMode) {
		if(FORM_SUBMIT_AJAX==submitMode ){
			this.submitMode = submitMode;
			submitModeHidden.setValue(String.valueOf(FORM_SUBMIT_AJAX));
		}else{
			this.submitMode = FORM_SUBMIT_CLASSIC;
			submitModeHidden.setValue(String.valueOf(FORM_SUBMIT_CLASSIC));
		}
	}
//***********************************************************************************
	public void setRevisionDecription(String decription){
		revisionDescription.setValue(decription);
	}
//***********************************************************************************
	public void submitWithRevisionDescription(String decription){
		setRevisionDecription(decription);
		doSubmit();
	}
//***********************************************************************************
	public void submitWithRevision(boolean mandatory){
		RevisionDescriptionDialog dialog=new RevisionDescriptionDialog(mandatory, this);
		dialog.show();
	}
//***********************************************************************************
	public static class DataInputFormWidgetSubmitListener implements FormPanel.SubmitCompleteHandler{
		DataInputFormWidget dataInputForm;
		int formSubmitter=FORM_SUBMITER_SAVE_BUTTON;
		public DataInputFormWidgetSubmitListener(DataInputFormWidget dataInputForm){
			this.dataInputForm=dataInputForm;
		}
		public void setNewRecordAfterSuccess(int submitter){
			this.formSubmitter=submitter;
		}
		@Override
		public void onSubmitComplete(SubmitCompleteEvent event) {
			if(FORM_SUBMIT_AJAX==this.dataInputForm.submitMode){
				this.dataInputForm.messagesScroller.setVisible(false);
				this.dataInputForm.messagesPanel.clear();
				String result=event.getResults();
				if(result!=null&&result.trim().length()>0){
					try {
						result= result.replaceAll("<pre>","").replaceAll("</pre>", "");
						JSONValue jsonValue = JSONParser.parseStrict(result);
						JSONObject jsonResult= jsonValue.isObject();
						JSONObject executionresult=jsonResult.get("executionResult").isObject();
						JSONBoolean success= executionresult.get("success").isBoolean();
						JSONArray message=executionresult.get("messages").isArray();
						this.dataInputForm.showMessages(message);
						String recordId=null;
						if(!success.booleanValue()){
							MessageUtility.error(this.dataInputForm.constants.error(), this.dataInputForm.constants.failToSave());
						}else{
							this.dataInputForm.setSubmitURL(this.dataInputForm.model.getUpdateUrl());
							recordId=jsonResult.get("recordId").isNumber().toString();
							this.dataInputForm.pkHidden.setValue(recordId);
						}
						this.dataInputForm.fireSubmitCompletion(success.booleanValue(),recordId,formSubmitter);
						
					} catch (Exception e) {
						GWT.log("fail to parse response from server: "+result);
						this.dataInputForm.fireSubmitCompletion(false,null,formSubmitter);
					}
					
				}else{
					GWT.log("submit response from server is null");
					MessageUtility.warn(this.dataInputForm.constants.warning(), this.dataInputForm.constants.failToSave());
					this.dataInputForm.fireSubmitCompletion(false,null,formSubmitter);
				}
			}else{
				this.dataInputForm.fireSubmitCompletion(true,null,formSubmitter);
			}
		}
	}
}
