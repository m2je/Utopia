package ir.utopia.core.util.tags.searchpage.client;

import ir.utopia.core.util.tags.datainputform.client.AbstractUtopiaGWTEditor;
import ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.SearchWidget;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.SearchWidgetListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.form.Hidden;

public class SearchPage extends AbstractUtopiaGWTEditor implements EntryPoint,SearchWidgetListener {
	
	public static final transient String FORM_CLASS_PARAMETER=UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME;
	public static final transient String USECASE_PARAMETER=UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME;
	public static final transient String SEARCH_DEFAULT_CONDITIONS="SearchDefaultConditions";
	public static final transient String IS_SEARCH_ENABLED="searchEnabled";
	public static final transient String IS_SELECTABLE="isSelectable";
	public static final transient String IS_ACTION_ENABLED="isActionEnabled";
	
	SearchPageConstants constants =(SearchPageConstants)GWT.create(SearchPageConstants.class);
	private SearchWidget searchWiget;
	ImExChooseDialog importDialog;
	FormPanel fPanel=new FormPanel(new NamedFrame("menuFrame"));
	SearchPageModel pageModel;
	  /**
	   * Initialize this example.
	   */
	  public void onModuleLoad() {
		  RootPanel.get().add(fPanel);
		  searchWiget=new SearchWidget(getUsecaseName(), getFormClassName(), isSearchEnabled(), isActionEnable(), false,getDefaultConditions(),this);
		  setShowTrigger(this);
		 
		  RootPanel.getBodyElement().setDir(LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR");
	  }

//************************************************************************************************************************
	  public native String getFormClassName()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.searchpage.client.SearchPage::FORM_CLASS_PARAMETER).value;
		}-*/;
//************************************************************************************************************************
	  public native String getUsecaseName()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.searchpage.client.SearchPage::USECASE_PARAMETER).value;
		}-*/;
//************************************************************************************************************************
	  private native String isSearchEnabledNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.searchpage.client.SearchPage::IS_SEARCH_ENABLED).value;
		}-*/;
//************************************************************************************************************************
	  private native String getDefaultConditionsNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.searchpage.client.SearchPage::SEARCH_DEFAULT_CONDITIONS).value;
		}-*/;
//************************************************************************************************************************
	  private native String isSelectableNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.searchpage.client.SearchPage::IS_SELECTABLE).value;
		}-*/;
//************************************************************************************************************************
	  private native String isActionEnabledNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.searchpage.client.SearchPage::IS_ACTION_ENABLED).value;
		}-*/;

//***********************************************************************************************************************
		public native void setShowTrigger(SearchPage x)/*-{

		$wnd.showReportDialog = function (jrxml ) {

		x.@ir.utopia.core.util.tags.searchpage.client.SearchPage::showReportDialog(Ljava/lang/String;)(jrxml);

		};

		}-*/;
//************************************************************************************************************************
		  private  boolean isActionEnable(){
			  boolean res;
			  try {
				res=Boolean.parseBoolean(isActionEnabledNative());
			} catch (Exception e) {
				res=false;
			}
			  return res; 
		  }
//************************************************************************************************************************
		  public boolean isSelectable(){
			  boolean res;
			  try {
				res=Boolean.parseBoolean(isSelectableNative());
			} catch (Exception e) {
				res=false;
			}
			  return res;
		  }
//************************************************************************************************************************
		  public boolean isSearchEnabled(){
			  boolean res;
			  try {
				
				res=Boolean.parseBoolean(isSearchEnabledNative());
			} catch (Exception e) {
				res=false;
			}
			  return res;
		  }
//************************************************************************************************************************
		  private  String getDefaultConditions(){
			  try {
				return getDefaultConditionsNative();
			  } catch (Throwable e) {
//				GWT.log("",e);
//				e.printStackTrace();
			}
			  return null;
		  }
//************************************************************************************************************************		  
		public void showReportDialog(String jrxml) {
			ReportParameterDialog dialog=new ReportParameterDialog(constants,getUsecaseName(),getFormClassName(),searchWiget.getSelectedIds(),
					searchWiget.getOrderby(),
							(jrxml==null||jrxml.trim().length()==0)?null:jrxml);
			dialog.center();
			dialog.setAnimationEnabled(true);
			dialog.setModal(true);
			dialog.show();
		}
//************************************************************************************************************************
		@Override
		public void configurationLoaded(SearchPageModel model,Widget searchWidgeting) {
			 this.pageModel=model;
			 RootPanel.get("searchDiv").add(searchWidgeting); 
		}
//************************************************************************************************************************		
	public void buttonClicked(int buttonType){
		if(buttonType==import_button_clicked){
			if(importDialog==null){
				importDialog=new ImExChooseDialog(false, getUsecaseName(), this);
			}
			importDialog.center();
			importDialog.setAnimationEnabled(true);
			importDialog.show();
		}
		
	}
//************************************************************************************************************************		
public void redirectToExImConfigurationPage(Long settingId,boolean export){
	fPanel.clear();
	VerticalPanel vpanel=new VerticalPanel();
	fPanel.setWidget(vpanel);
	vpanel.add(new Hidden(UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME, getFormClassName()));
	vpanel.add(new Hidden(UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME, getUsecaseName()));
	vpanel.add(new Hidden("__settingId",settingId==null?"": settingId.toString()));
	vpanel.add(new Hidden(UIComponentsConstants.FORWARD_URL, pageModel.getImportPage()));
	vpanel.add(new Hidden(UIComponentsConstants.USE_ACTION_NAME_PARAMETER_NAME, export?"export":"import"));
	fPanel.setMethod(FormPanel.METHOD_POST);
	fPanel.setAction(pageModel.getImportExportSetupPage());
	fPanel.submit();
}
//************************************************************************************************************************
public void redirectToImportPage(Long settingId){
	fPanel.clear();
	VerticalPanel vpanel=new VerticalPanel();
	fPanel.setWidget(vpanel);
	vpanel.add(new Hidden(UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME, getFormClassName()));
	vpanel.add(new Hidden(UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME, getUsecaseName()));
	vpanel.add(new Hidden("__settingId",settingId==null?"": settingId.toString()));
	vpanel.add(new Hidden(UIComponentsConstants.FORWARD_URL, pageModel.getImportPage()));
	vpanel.add(new Hidden(UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME, "import"));
	fPanel.setMethod(FormPanel.METHOD_POST);
	fPanel.setAction(pageModel.getImportPage());
	fPanel.submit();
}
		@Override
		public void rowSelected(int rowIndex, Long rowId,String []columnNames, String[] rowData,boolean selected) {
			
		}
}
