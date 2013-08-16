package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.DashboardRecord;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class Dashboard implements EntryPoint,DashboardModelListener {

	interface GlobalResources extends ClientBundle {
	    @NotStrict
	    @Source("global.css")
	    CssResource css();
	  }
	interface Binder extends UiBinder<DockLayoutPanel, Dashboard> { }
	private static final Binder binder = GWT.create(Binder.class);
	DashboardModel model=new DashboardModel();
	@UiField TopPanel topPanel;
    @UiField DashboardRecordList recordList;
    @UiField DashboardRecordDetail recordDetail;
    @UiField Shortcuts shortcuts;
    @UiField SplitLayoutPanel splitPanel;
    final DashBoardConstants constants=(DashBoardConstants)GWT.create(DashBoardConstants.class);
	@Override
	public void onModuleLoad() {
	    GWT.<GlobalResources>create(GlobalResources.class).css().ensureInjected();
	   
	    // Create the UI defined in Mail.ui.xml.
	    DockLayoutPanel outer = binder.createAndBindUi(this);

	    // Get rid of scrollbars, and clear out the window's built-in margin,
	    // because we want to take advantage of the entire client area.
	    Window.enableScrolling(false);
	    Window.setMargin("0px");

	    // Special-case stuff to make topPanel overhang a bit.
	    Element topElem = outer.getWidgetContainerElement(topPanel);
	    topElem.getStyle().setZIndex(2);
	    topElem.getStyle().setOverflow(Overflow.VISIBLE);

	    // Listen for item selection, displaying the currently-selected item in
	    // the detail area.
	    recordList.setListener(new DashboardRecordList.Listener() {
	      public void onItemSelected(DashboardRecord item) {
	    	  recordDetail.setItem(item);
	      }
	     public void onDataSetChanged(){
	    	 recordDetail.clear();
	     }
	    });
	    recordDetail.setModel(model);
	    shortcuts.setModel(model);
	    recordList.setModel(model);
	    // Add the outer panel to the RootLayoutPanel, so that it will be
	    // displayed.
	    RootLayoutPanel root = RootLayoutPanel.get();
	    root.add(outer);
	    MessageUtility.progress(constants.pleaseWait(), constants.loadingData());
	    model.addListener(this);
	    model.loadAccessibleTranistions();

	}
	@Override
	public void modelLoaded(boolean success) {
		MessageUtility.stopProgress();
		if(success){
			if(model.getUsecaseTransitionInfos()==null||model.getUsecaseTransitionInfos().size()==0){
				MessageUtility.warn(constants.warning(), constants.youDontHaveAccessToAnyTransitions());
			}
		}else{
			MessageUtility.error(constants.error(),constants.failToAccessServer());
		}
		
	}
	@Override
	public void dashSearchCriterialChanged(UsecaseSearchCriteria searchCriteria) {
		
	}
	@Override
	public void recordUpdated(DashboardRecord record) {
		// TODO Auto-generated method stub
		
	}
	

}
