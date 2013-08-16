package ir.utopia.core.util.tags.utilitypanel.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UtilityPanel  implements EntryPoint {

	UtilityConstants constants=(UtilityConstants)GWT.create(UtilityConstants.class);
	int currentPanelIndex=0;
	final UtilityPanelTab []tabs=new UtilityPanelTab[4]; 
	public void onModuleLoad() {
		 RootPanel rootPane= RootPanel.get();
		 DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		    tabPanel.setWidth("100%");
		    tabPanel.setAnimationEnabled(true);

		    String[] tabTitles = new String[]{constants.SMTPConfiguration(),constants.mailAccountConfiguration(),constants.mailTemplate(),constants.schedulerConfig()};
		    final SMTPConfiguarytionPanel smtpPanel=new SMTPConfiguarytionPanel(constants);
		    tabs[0]=smtpPanel;
		    
		    tabPanel.add(smtpPanel, tabTitles[0]);
		    
		    final MailConfigurationPanel mailPanel=new MailConfigurationPanel(constants);
		    tabs[1]=mailPanel;
		    mailPanel.setWidth("100%");
		    tabPanel.add(mailPanel, tabTitles[1]);

		    // Add a tab
			final MailTemplatePanel mailTemplate=new MailTemplatePanel();
			tabs[2]=mailTemplate;
			mailTemplate.setWidth("100%");
			tabPanel.add(mailTemplate, tabTitles[2]);
		    
		    final ScheduleDefinitionPanel schedulePanel=new ScheduleDefinitionPanel();
		    tabs[3]=schedulePanel;
		    schedulePanel.setWidth("100%");
		    tabPanel.add(schedulePanel, tabTitles[3]);
		    
		    // Return the content
		    tabPanel.selectTab(0);
		    tabPanel.ensureDebugId("cwTabPanel");
		    VerticalPanel v=new VerticalPanel();
		    v.add(tabPanel);
		    v.setWidth("70%");
		    v.setHeight("100%");
		    rootPane.setWidth("100%");
		    
		    rootPane.add(v);
		    if(LocaleInfo.getCurrentLocale().isRTL()){
		    	RootPanel.getBodyElement().setDir("rtl");
		    	rootPane.getElement().setDir("rtl");
		    	tabPanel.getElement().setDir("rtl");
		    }
		    
		    tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
				
				@Override
				public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
					if(!tabs[currentPanelIndex].discardPanel()){
						event.cancel();
					}
				}
			});
		    tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
				
				@Override
				public void onSelection(SelectionEvent<Integer> event) {
					int previousTab=currentPanelIndex;
					currentPanelIndex=event.getSelectedItem();
					
					if(previousTab==0&&currentPanelIndex==1){
						if(smtpPanel.isPanelDataModified()){
							smtpPanel.cleaDataModification();
							mailPanel.reload(true);
						}
					}
				}
			});
		    rootPane.setWidth("100%");
	}

	

}
