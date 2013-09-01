/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ir.utopia.core.util.tags.controlpanel.client;

import ir.utopia.core.util.tags.controlpanel.client.widgets.RoleOrganizationAccessPanel;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.process.client.ProcessHandler;
import ir.utopia.core.util.tags.utilitypanel.client.SMTPConfiguarytionPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.MessageBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ControlPanel implements EntryPoint {
	private static DialogBox dialogBox =new DialogBox(false,true);
  /**
   * The type passed into the
   * {@link com.google.gwt.ControlPanelGenerator.showcase.generator.ShowcaseGenerator}.
   */
  private static final class GeneratorInfo {
  }

 

  /**
   * The static images used throughout the Showcase.
   */
  public static final ControlPanelImages images = (ControlPanelImages) GWT.create(ControlPanelImages.class);



  

  
  private int windowNumber;


  /**
   * This is the entry point method.
   */
  ControlPanelConstants constants = (ControlPanelConstants) GWT.create(ControlPanelConstants.class);
  int currentPanelIndex=0;
  final ControlPanelTab []tabs=new ControlPanelTab[2];
  final RoleOrganizationAccessPanel roleOrgPanel=new RoleOrganizationAccessPanel(constants);
  public void onModuleLoad() {
	    RootPanel rootPane= RootPanel.get();
	    String[] tabTitles = new String[]{constants.roleOrganizationAccess(),constants.userOrganizationAccess()+"2"};
	    tabs[0]=roleOrgPanel;
	    roleOrgPanel.setWidth("100%");
	    DecoratedTabPanel tabPanel = new DecoratedTabPanel();
	    tabPanel.setWidth("100%");
	    tabPanel.add(roleOrgPanel, tabTitles[0]);
	    
	    VerticalPanel v=new VerticalPanel();
	    v.add(tabPanel);
	    v.setWidth("70%");
	    v.setHeight("100%");
	    rootPane.setWidth("100%");
	    rootPane.add(v);
        initWindowNumber();
  }
//****************************************************************************
  private void initWindowNumber(){
//	  ProcessHandler.progress(constants.pleaseWait(), constants.loadingData());
	  ControlPanelServerService.getServer().requestWindowNumber(new AsyncCallback<Integer>(){

		public void onFailure(Throwable e) {
			MessageUtility.stopProgress();
			GWT.log("", e);
			MessageUtility.error(constants.error(), constants.failToAccessServer());
		}

		public void onSuccess(Integer result) {
			MessageUtility.stopProgress();
			if(result==null){
				MessageUtility.error(constants.error(), constants.failToAccessServer());
			}else{
				windowNumber=result.intValue();
				for(ControlPanelTab widget: tabs){
//					widget.setWindowNumber(windowNumber);
				}
			}
		}
		  
	  });
  }

  
  
  public static void loadingProgress(ControlPanelConstants constants){
	  progress(constants, true);
  }
//****************************************************************************************  
  public static void submitingProgress(ControlPanelConstants constants){
	  progress(constants, false);
  }
//****************************************************************************************
  private static void progress(ControlPanelConstants constants,boolean loading){
	  dialogBox.center();
	  
      dialogBox.setText(constants.pleaseWait());
      VerticalPanel panel=new VerticalPanel();
      panel.add(new Label(loading? constants.loading():constants.submitingData()));
      panel.add(images.loading().createImage());
      dialogBox.setWidget(panel);
      dialogBox.show();
  }
//****************************************************************************************
  public static  void hideLoading(){
	  dialogBox.hide();
  }
//****************************************************************************************
  public static void showMessages(ControlPanelConstants constants,ExecutionResult result){
	  if(result.isSuccess()){
			DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
		    simplePopup.ensureDebugId("cwBasicPopup-simplePopup");
		    simplePopup.setWidth("150px");
		    simplePopup.setWidget(new HTML(constants.saveSuccessMessage()
		        ));
		    simplePopup.center();
		    simplePopup.show();
		}else{
			message(constants.failToSave());
		}
	  message(result.getErrorMessages());
	  message(result.getWarningMessages());
	  message(result.getInfoMessages());
  }
//****************************************************************************************
  private static void message(String ... messages){
	  if(messages!=null){
		  for(String message:messages){
			  Window.alert(message);
		  }
	  }
  }
//****************************************************************************************  
}
