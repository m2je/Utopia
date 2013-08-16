package ir.utopia.core.util.tags.searchpage.client;

import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.SearchPageServerService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReportParameterDialog extends DialogBox {

	SearchPageConstants constants;
	Long[] selectedRecords;
	String orderbyCol;
	String usecaseName;
	String formClass;
	ListBox reportType;
	String jrxml;
	public ReportParameterDialog(SearchPageConstants constants,String usecaseName,String formClass,Long []selectedRecords,String orderbyCol,String jrxml){
		super(false,true);
		this.constants=constants;
		this.selectedRecords=selectedRecords;
		this.orderbyCol=orderbyCol;
		this.formClass=formClass;
		this.usecaseName=usecaseName;
		this.jrxml=jrxml;
		initDialog();
	}
//*******************************************************************************	
	private void initDialog(){
		setAnimationEnabled(true);
		VerticalPanel  dialogContents=new VerticalPanel();
		reportType=new ListBox();
		reportType.setStylePrimaryName("clsSelect");
		reportType.addItem(constants.reportTypePDF(), "0");
		reportType.addItem(constants.reportTypeExcel(), "1");
		reportType.addItem(constants.reportTypeHtml(), "2");
		reportType.addItem(constants.reportTypeText(), "3");
		Grid widgetGrid=new Grid(2,4);
		Label l1=new Label(constants.reportType());
		l1.setStylePrimaryName("clsLabel");
		widgetGrid.setWidget(0, 0, l1);
		widgetGrid.setWidget(0, 1, reportType);
		Label l2=new Label(constants.records());
		l2.setStylePrimaryName("clsLabel");
		widgetGrid.setWidget(1,0,l2);
		ListBox records=new ListBox();
		records.addItem(constants.allRecords(),"0");
		if(selectedRecords!=null&&selectedRecords.length>0){
			records.addItem(constants.selectedRecords(),"1");
			records.setSelectedIndex(1);
		}
			
		widgetGrid.setWidget(1, 1, records);
		dialogContents.add(widgetGrid);
		Grid g=new Grid(1,2);
		Button runReport=new Button();
		runReport.setText(constants.report());
		
		runReport.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				runReport();
				hide();
			}});
		Button cancel=new Button();
		cancel.setText(constants.cancelProcess());
		cancel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				hide();
				
			}});
		g.setWidget(0, 0, runReport);
		g.setWidget(0, 1, cancel);
		cancel.setStylePrimaryName("clsCancelButton");
		runReport.setStylePrimaryName("clsReportButton");
		dialogContents.add(g);
		dialogContents.getElement().setDir(LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR");
		setWidget(dialogContents);
	}
//***************************************************************************
	private void runReport(){
		SearchPageServerService.getServer().prepareReport(usecaseName, formClass, selectedRecords, orderbyCol,new AsyncCallback<Long>(){

			@Override
			public void onFailure(Throwable e) {
				MessageUtility.error(constants.error(), constants.failToAccessServer());
				GWT.log("", e);
			}

			@Override
			public void onSuccess(Long result) {
				if(result!=null){
					String url= GWT.getHostPageBaseURL()+"SearchReport.htm?reportUID="+
							result+"&reportType="+(reportType.getValue(reportType.getSelectedIndex()));
					url=jrxml!=null?url+"&jrxml="+jrxml:url;
					openReportWindow(url);
				}else{
					MessageUtility.error(constants.error(), constants.failToAccessServer());
				}
				
			}});
	}
//***************************************************************************
	public native void openReportWindow(String url)/*-{
		 $wnd.open(url);
	}-*/;
//***************************************************************************
}
