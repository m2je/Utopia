package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.grid.IncludedGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridMetaData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridValueModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.utilitypanel.client.model.RecepientInfo;
import ir.utopia.core.util.tags.utilitypanel.client.model.SchedulerDefinitionService;
import ir.utopia.core.util.tags.utilitypanel.client.model.SchedulerDefinitionServiceAsync;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MailRecipientPanel extends AbstractUtilityPanel implements KeyUpHandler,ClickHandler,InnerPanel{
	UtilityConstants constatnts=GWT.create(UtilityConstants.class);
	TextBox recipientText=new TextBox(),recipientCode=new TextBox(),recipientLastname=new TextBox(),recipientEMailAddress=new TextBox(),recipientName=new TextBox();
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	ListBox senderUsername=new ListBox(),mailTemplate=new ListBox();
	IncludedGrid searchGrid; 
	ListBox selectedRecepients ;
	boolean dirty=false;
	SchedulerDefinitionServiceAsync service=(SchedulerDefinitionServiceAsync)GWT.create(SchedulerDefinitionService.class);
	@SuppressWarnings("deprecation")
	public MailRecipientPanel(){
		setSpacing(50);
		VerticalPanel root=new  VerticalPanel();
		VerticalPanel sendConfigPanel=new VerticalPanel();
		Label senderMail=new Label();
		senderMail.setStylePrimaryName("clsLabel");
		senderMail.setText(constatnts.username()+":");
		sendConfigPanel.add(senderMail);
		sendConfigPanel.setSpacing(10);
		senderUsername.setWidth("250px");
		senderUsername.setStylePrimaryName("clsSelect");
		sendConfigPanel.add(senderUsername);
		Label mailTemplateL=new Label();
		mailTemplateL.setStylePrimaryName("clsLabel");
		mailTemplateL.setText(constatnts.mailTemplate()+":");
		sendConfigPanel.add(mailTemplateL);
		mailTemplate.setStylePrimaryName("clsSelect");
		mailTemplate.setWidth("250px");
		sendConfigPanel.add(mailTemplate);
		root.add(sendConfigPanel);
		HorizontalPanel h1=new HorizontalPanel();
		Label l1=new Label();
		l1.setWidth("120px");
		l1.setText(constatnts.recipientText()+":");
		l1.setStylePrimaryName("clsLabel");
		h1.setSpacing(5);
		h1.add(l1);
		recipientText.setStylePrimaryName("clsText");
		h1.add(recipientText);
		recipientText.addKeyUpHandler(this);
		root.add(h1);
		
		HorizontalPanel h2=new HorizontalPanel();
		h2.setSpacing(5);
		Label l2=new Label();
		l2.setWidth("120px");
		l2.setStylePrimaryName("clsLabel");
		l2.setText(constatnts.recipientName()+":");
		h2.add(l2);
		recipientName.setStylePrimaryName("clsText");
		recipientName.addKeyUpHandler(this);
		h2.add(recipientName);
		Label l3=new Label();
		l3.setWidth("120px");
		l3.setStylePrimaryName("clsLabel");
		l3.setText(constatnts.recipientLastname()+":");
		h2.add(l3);
		recipientLastname.setStylePrimaryName("clsText");
		recipientLastname.addKeyUpHandler(this);
		h2.add(recipientLastname);
		root.add(h2);
		
		HorizontalPanel h3=new HorizontalPanel();
		h3.setSpacing(5);
		Label l4=new Label();
		l4.setWidth("120px");
		l4.setStylePrimaryName("clsLabel");
		l4.setText(constatnts.recipientCode()+":");
		h3.add(l4);
		recipientCode.setStylePrimaryName("clsText");
		recipientCode.addKeyUpHandler(this);
		h3.add(recipientCode);
		Label l5=new Label();
		l5.setWidth("120px");
		l5.setStylePrimaryName("clsLabel");
		l5.setText(constatnts.recipientEMailAddress()+":");
		h3.add(l5);
		recipientEMailAddress.setStylePrimaryName("clsText");
		recipientEMailAddress.addKeyUpHandler(this);
		h3.add(recipientEMailAddress);
		root.add(h3);
		Button search=new Button();
		search.setStylePrimaryName("clsSearchButton");
		search.addClickHandler(this);
		search.setText(constatnts.search());
		root.add(search);
		searchGrid= getSearchGrid();
		HorizontalPanel h4=new HorizontalPanel(); 
		h4.add(searchGrid);
		VerticalPanel p1=new VerticalPanel();
		ImageHyperlink select=new ImageHyperlink(LocaleInfo.getCurrentLocale().isRTL()?images.btn_Prev().createImage():images.btn_Next().createImage());
		select.setTitle(constatnts.selectAll());
		select.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
					GridValueModel values= searchGrid.getValueModel();
					List<GridRowData>rows= values.getModifiedRows();
					if(rows!=null&&rows.size()>0){
					L1:	for(GridRowData row:rows){
							if(searchGrid.isSelectedRow(row.getRowIndex())){
								String recordKey=row.getRowId()+"";
								for(int i=0;i<selectedRecepients.getItemCount();i++){
									if(selectedRecepients.getValue(i).equals(recordKey)){
										continue L1;
									}
								}
								String []rowData=row.getData();	
								selectedRecepients.addItem(rowData[0]+"-"+rowData[1]+"-"+rowData[2],recordKey);
								dirty=true;
							}
						}
					}
			}
		});
		p1.setSpacing(10);
		p1.add(select);
		p1.setCellHorizontalAlignment(select,HorizontalPanel.ALIGN_CENTER);
		p1.setCellVerticalAlignment(select,HorizontalPanel.ALIGN_MIDDLE);
		
		ImageHyperlink delete=new ImageHyperlink(images.removeAttachment().createImage());
		delete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for(int i=selectedRecepients.getItemCount()-1;i>=0;i--){
					if(selectedRecepients.isItemSelected(i)){
						selectedRecepients.removeItem(i);
						dirty=true;
					}
					
				}
				
				
			}
		});
		delete.setTitle(constatnts.delete());
		p1.add(delete);
		p1.setCellHorizontalAlignment(delete,HorizontalPanel.ALIGN_CENTER);
		p1.setCellVerticalAlignment(delete,HorizontalPanel.ALIGN_MIDDLE);
		h4.add(p1);
		
		h4.setCellHorizontalAlignment(p1,HorizontalPanel.ALIGN_CENTER);
		h4.setCellVerticalAlignment(p1,HorizontalPanel.ALIGN_MIDDLE);
		
		Widget w=getSelectedRecepientList();
		h4.add(w);
		h4.setCellVerticalAlignment(w, HorizontalPanel.ALIGN_MIDDLE);
		h4.setCellHorizontalAlignment(w, HorizontalPanel.ALIGN_CENTER);
		root.add(h4);
		add(root);
		initMailTemplateBox();
		initSenderBox();
	}
//*************************************************************************************	
	private IncludedGrid getSearchGrid(){
		String dir=LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR";
		InputItem []colMeta=new InputItem[4];
		colMeta[0]=new InputItem("recipientCode", InputItem.DISPLAY_TYPE_STRING, 1);
		colMeta[0].setHeader(constatnts.recipientCode());
		colMeta[0].setReadOnly(true);
		colMeta[1]=new InputItem("recipientName", InputItem.DISPLAY_TYPE_STRING, 2);
		colMeta[1].setHeader(constatnts.recipientName());
		colMeta[1].setReadOnly(true);
		colMeta[2]=new InputItem("recipientLastname", InputItem.DISPLAY_TYPE_STRING, 3);
		colMeta[2].setHeader(constatnts.recipientLastname());
		colMeta[2].setReadOnly(true);
		colMeta[3]=new InputItem("recipientEMailAddress", InputItem.DISPLAY_TYPE_STRING, 4);
		colMeta[3].setHeader(constatnts.recipientEMailAddress());
		colMeta[3].setReadOnly(true);
		GridMetaData meta1=new GridMetaData(colMeta, new String[]{"","","",""});
		meta1.setAllowRemoveRecord(true);
		meta1.setRemoveRecordString(constatnts.delete());
		meta1.setSelectAllStr(constatnts.selectAll());
		meta1.setSelectColumnName("");
		meta1.setUpdateable(false);
		searchGrid=new IncludedGrid("searchGrid", dir, meta1);
		
		return searchGrid; 
	}
//*************************************************************************************	
	private VerticalPanel  getSelectedRecepientList(){
		VerticalPanel root= new VerticalPanel();
		selectedRecepients = new ListBox(true);
		Label l1=new Label();
		l1.setText(constatnts.recipients()+":");
		l1.setStylePrimaryName("clsLabel");
		l1.setWidth("80px");
		HorizontalPanel h1=new HorizontalPanel();
		h1.add(l1);
		root.setCellHorizontalAlignment(l1, VerticalPanel.ALIGN_CENTER);
		root.setCellVerticalAlignment(l1, VerticalPanel.ALIGN_MIDDLE);
		selectedRecepients.setWidth("250px");
		selectedRecepients.setHeight("350px");
		selectedRecepients.setStylePrimaryName("clsSelect");
		h1.add(selectedRecepients);
		root.add(h1);
		return root;
	}
//*************************************************************************************	
	private void search(){
		((ServiceDefTarget)service).setServiceEntryPoint("SearchBpartner_Co_Ut_Scheduler.htm");
		service.searchBPartner(recipientText.getText(), recipientName.getText(), recipientLastname.getText(), recipientCode.getText(), recipientEMailAddress.getText(),new AsyncCallback<GridValueModel>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(GridValueModel result) {
					if(result==null){
						Window.alert(constants.failToAccessServer());
					}else{
						EditorFactory.setWidgetValue(searchGrid, result);
					}
			}
		});
	}
//*************************************************************************************	
	@Override
	public void onClick(ClickEvent event) {
		search();
		
	}
//*************************************************************************************	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		if(13==event.getNativeKeyCode())
			search();
	}
//*************************************************************************************	
	private void initMailTemplateBox(){
		AsyncCallback<UILookupInfo> callback= super.initBox(senderUsername, "loadUsernamesLookup_Co_Ut_Scheduler.htm",service);
		service.loadUsernamesLookup(callback);
	}
//*************************************************************************************
	private void initSenderBox(){
		AsyncCallback<UILookupInfo> callback=super.initBox(mailTemplate, "loadMailTemplateLookup_Co_Ut_Scheduler.htm",service);
		service.loadMailTemplateLookup(callback);
	}
//*************************************************************************************	
	@Override
	public boolean validate() {
		if(mailTemplate.getSelectedIndex()>0){
			if(senderUsername.getSelectedIndex()>0){
				if(selectedRecepients.getItemCount()>0){
					return true;
				}else{
					Window.alert(constants.selectOneRecepitentAtList());
				}
			}else{
				Window.alert(constants.selectMailSender());
			}
		}else{
			Window.alert(constants.selectMailTemplateMessage());
		}
		return false;
	}
//*************************************************************************************
	@Override
	public void reset() {
		selectedRecepients.clear();
		this.mailTemplate.setSelectedIndex(0);
		this.senderUsername.setSelectedIndex(0);
		recipientText.setText("");
		recipientCode.setText("");
		recipientEMailAddress.setText("");
		recipientName.setText("");
		recipientLastname.setText("");
		searchGrid.setGridData(null);
		dirty=false;
	}
//*************************************************************************************	
	@Override
	public boolean isModified() {
		return dirty;
	}
//*************************************************************************************
	public void setValue(UILookupInfo data,Long mailTemplate,Long mailuserId){
		reset();
		EditorFactory.setWidgetValue(this.mailTemplate, mailTemplate==null?null:mailTemplate.toString());
		EditorFactory.setWidgetValue(senderUsername, mailuserId==null?null:mailuserId.toString());
		if(data.getData()!=null){
			for(String []item:data.getData()){
				selectedRecepients.addItem(item[1],item[0]);
			}
		}
		dirty=false;
	}
//*************************************************************************************
	@Override
	public void reload() {
		reset();
		initMailTemplateBox();
		initSenderBox();
		dirty=false;
	}
//*************************************************************************************	
	@Override
	public void setValue(Object value) {
		reset();
		if(value instanceof RecepientInfo){
			RecepientInfo info= (RecepientInfo)value;
			EditorFactory.setWidgetValue(senderUsername, info.getSelectedUsername()) ;
			EditorFactory.setWidgetValue(mailTemplate, info.getSelectedTemplate()) ;
			EditorFactory.setListBoxValue(selectedRecepients, info.getRecepients());
		}
		dirty=false;
	} 
//*************************************************************************************
	public RecepientInfo getValue(){
		RecepientInfo result=new RecepientInfo();
		ArrayList<Long>selectedIds=new ArrayList<Long>();
		for(int i=0;i<selectedRecepients.getItemCount();i++){
			selectedIds.add(Long.parseLong( selectedRecepients.getValue(i)));
		}
		result.setSelectedRecepients(selectedIds.toArray(new Long[selectedIds.size()]));
		result.setSelectedUsername(Long.parseLong(senderUsername.getValue(senderUsername.getSelectedIndex())));
		result.setSelectedTemplate(Long.parseLong(mailTemplate.getValue(mailTemplate.getSelectedIndex())));
		return result;
	}
}
