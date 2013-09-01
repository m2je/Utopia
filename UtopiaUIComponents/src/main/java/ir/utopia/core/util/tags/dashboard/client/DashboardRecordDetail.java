
package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.DashboardRecord;
import ir.utopia.core.util.tags.dashboard.client.model.DocStatusInfo;
import ir.utopia.core.util.tags.dashboard.client.model.TransitionToInfo;
import ir.utopia.core.util.tags.dashboard.client.model.TransitionalUsecaseInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A composite for displaying the details of an email message.
 */
public class DashboardRecordDetail extends ResizeComposite {
	final DashBoardConstants constants=(DashBoardConstants)GWT.create(DashBoardConstants.class);
	interface Binder extends UiBinder<Widget, DashboardRecordDetail> { }
	private static final Binder binder = GWT.create(Binder.class);
	@UiField HorizontalPanel body;
	DashboardModel model;
	DashboardRecord currentRecord;
	Map<String,DatainputWidgetContainer>widgetMap=new HashMap<String, DatainputWidgetContainer>();
	@UiField HorizontalPanel recordActionPanel;
	DatainputWidgetContainer currentWidget;
  public DashboardRecordDetail() {
	  initWidget(binder.createAndBindUi(this));
	  recordActionPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER);
	  recordActionPanel.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE);
  }
  
  public void setItem(DashboardRecord item) {
	  this.currentRecord=item;
	  String usecaseName=model.getCurrent().getUseCaseName();
	   if(widgetMap.containsKey(usecaseName)){
		   currentWidget=widgetMap.get(usecaseName);
	   }else{
		   currentWidget=new DatainputWidgetContainer(this,constants,model);
		   widgetMap.put(usecaseName, currentWidget);
	   }
	   currentWidget.setCurrentRecord(item);
	   body.clear();
	   if(currentWidget.getWidget()!=null){
		   body.add(currentWidget.getWidget());
		   initActionPanel();
	   }

	  }
//****************************************************************************************************************************************
  public void clear(){
	  currentWidget=null;
	  body.clear();
	  recordActionPanel.clear();
  }
//****************************************************************************************************************************************  
  public void setModel(DashboardModel model){
	  this.model=model;
  }
//****************************************************************************************************************************************
  public void initActionPanel(){
	  recordActionPanel.clear();
	  recordActionPanel.setSpacing(5);
	  final TransitionalUsecaseInfo usInfo= model.getCurrent();
	  List<DocStatusInfo>transitions=usInfo.getDocStatusInfo();
	  if(transitions!=null){
		  for(DocStatusInfo transition:transitions){
			  if(transition.getCurrentDocStatus().equals(currentRecord.getDocStatusId())){
				  List<TransitionToInfo>tos= transition.getTranstionToList();
				  if(tos!=null){
					  for(final TransitionToInfo to:tos){
						  Button b=new Button();
						  b.setStylePrimaryName("clsConfirmButton");
						  b.setText(to.getDocTypeName());
						  recordActionPanel.add(b);
						  recordActionPanel.setCellVerticalAlignment(b, HasVerticalAlignment.ALIGN_MIDDLE);
						  recordActionPanel.setCellHorizontalAlignment(b, HasHorizontalAlignment.ALIGN_CENTER);
						  
						  b.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								currentWidget.changeStatus(to);
								
							}
						}) ;
					  }
				  }
				  
				  
			  }
		  }
	  }
	}
//****************************************************************************************************************************************
  public void updateWidget(DatainputWidgetContainer container){
	    currentWidget=container;
		body.add(container.getWidget());
		initActionPanel();
  }
}
