
package ir.utopia.core.util.tags.controlpanel.client.widgets;

import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.LIST_TYPE_ACTION;
import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.LIST_TYPE_ROLE;
import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.LIST_TYPE_SUB_SYSTEM;
import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.LIST_TYPE_SYSTEM;
import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.LIST_TYPE_USECASE;
import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.LIST_TYPE_USER;
import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.LOG_BY_SYSTEM_SUBSYSTEM_USECASE_ACTION;
import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.LOG_BY_USER_ROLE;
import static ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService.MAX_LIST_TYPE;
import ir.utopia.core.util.tags.controlpanel.client.ContentWidget;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanel;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanelConstants;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanelServerService;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanelAnnotations.ShowcaseData;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanelAnnotations.ShowcaseSource;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanelAnnotations.ShowcaseStyle;
import ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle(".gwt-CheckBox")
public class LogConfiguration extends ContentWidget {
	
	
	int previousType;
	ListPanel []listPanels;
	ListBox baseBox;
	RadioButton logBySys,logByUserRole;
	FormPanel formPanel=new FormPanel();
	Set<String>dataModel=new HashSet<String>();
	 VerticalPanel rootPanel;
	 Integer windowNumber;
  /**
   * The constants used in this Content Widget.
   */
  @ShowcaseSource
  public static interface LogConfigurationConstants extends Constants,
      ContentWidget.CwConstants,DataInputFormConstants {
   

   String logBySystem();
   
   String logByUserRole();

   String logDescription();

   String logName();
   
   String system();
   
   String subSystem();
   
   String usecase();
   
   String action();
   
   String user();
   
   String role();
   
   String configBasis();
  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private LogConfigurationConstants constants;
//***********************************************************************************
  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public LogConfiguration(LogConfigurationConstants constants) {
    super(constants);
    this.constants = constants;
  }
//***********************************************************************************
  @Override
  public String getDescription() {
    return constants.logDescription();
  }
//***********************************************************************************
  @Override
  public String getName() {
    return constants.logName();
  }
//***********************************************************************************
  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  public Widget onInitialize() {
	  rootPanel=new VerticalPanel();
	  logBySys=new RadioButton("accessType",constants.logBySystem());
	  logByUserRole=new RadioButton("accessType",constants.logByUserRole());
	  logBySys.setChecked(true);
	  rootPanel.add(logBySys);
	  rootPanel.add(logByUserRole);
	  logBySys.addClickListener(new ClickListener(){

		public void onClick(Widget sender) {
			initBaseBox();
			listPanels[LIST_TYPE_SYSTEM].rootPane.setVisible(true);
			listPanels[LIST_TYPE_SUB_SYSTEM].rootPane.setVisible(true);
			listPanels[LIST_TYPE_USECASE].rootPane.setVisible(true);
			listPanels[LIST_TYPE_ACTION].rootPane.setVisible(true);
			listPanels[LIST_TYPE_USER].rootPane.setVisible(true);
			listPanels[LIST_TYPE_ROLE].rootPane.setVisible(true);
		}});
	  logByUserRole.addClickListener(new ClickListener(){

		public void onClick(Widget sender) {
			initBaseBox();
			listPanels[LIST_TYPE_SYSTEM].rootPane.setVisible(false);
			listPanels[LIST_TYPE_SUB_SYSTEM].rootPane.setVisible(false);
			listPanels[LIST_TYPE_USECASE].rootPane.setVisible(false);
			listPanels[LIST_TYPE_ACTION].rootPane.setVisible(false);
		}
	  });
	  UILookupInfo i=new UILookupInfo(new String[]{"key","value"},new String[][]{{"1","salam"},{"2","salam2"}});
	  HorizontalPanel syspanel=new HorizontalPanel();
	  HorizontalPanel userRole=new HorizontalPanel();
	  syspanel.setSpacing(5);
	  userRole.setSpacing(5);
	  listPanels=new ListPanel[MAX_LIST_TYPE];
	  listPanels[LIST_TYPE_SYSTEM]=getList(i,constants.system(),LIST_TYPE_SYSTEM);
	  listPanels[LIST_TYPE_SUB_SYSTEM] = getList(i,constants.subSystem(),LIST_TYPE_SUB_SYSTEM);
	  listPanels[LIST_TYPE_USECASE]=getList(i,constants.usecase(),LIST_TYPE_USECASE);
	  listPanels[LIST_TYPE_ACTION]= getList(i,constants.action(),LIST_TYPE_ACTION);
	  syspanel.add(listPanels[LIST_TYPE_SYSTEM].rootPane);
	  syspanel.add(listPanels[LIST_TYPE_SUB_SYSTEM].rootPane);
	  syspanel.add(listPanels[LIST_TYPE_USECASE].rootPane);
	  syspanel.add(listPanels[LIST_TYPE_ACTION].rootPane);
	  listPanels[LIST_TYPE_ROLE]=getList(i,constants.role(),LIST_TYPE_ROLE);
	  listPanels[LIST_TYPE_USER]=getList(i,constants.user(),LIST_TYPE_USER);
	  userRole.add(listPanels[LIST_TYPE_ROLE].rootPane);
	  userRole.add(listPanels[LIST_TYPE_USER].rootPane);
	  initBaseBox();
	  HorizontalPanel p=new HorizontalPanel();
	  p.setSpacing(5);
	  p.add(new Label(constants.configBasis()+":"));
	  p.add(baseBox);
	  rootPanel.add(p);
	  rootPanel.add(syspanel);
	  rootPanel.add(userRole);
	  HorizontalPanel submitPanel=new HorizontalPanel();
	  rootPanel.add(submitPanel);
	  Button submit=new Button();
	  submit.addClickListener(new ClickListener(){
		public void onClick(Widget sender) {
			submit();
		}
	  });
	  submit.setText(constants.save());
	  submitPanel.add(submit);
	  Button reload=new Button();
	  reload.addClickListener(new ClickListener(){
		public void onClick(Widget sender) {
			reload();
		}});
	  reload.setText(constants.reload());
	  submitPanel.add(reload);
	  rootPanel.add(submitPanel);	
	  formPanel.setWidget(rootPanel);
	  formPanel.setAction("save_Co_Cf_LogCofiguration.action");
	  formPanel.setMethod(FormPanel.METHOD_POST);
	  formPanel.addFormHandler(new FormHandler() {
	      public void onSubmit(FormSubmitEvent event) {
	    	  ControlPanel.submitingProgress((ControlPanelConstants)constants);
	      }
	      public void onSubmitComplete(FormSubmitCompleteEvent event) {
	    	  	ControlPanel.hideLoading();
	    	  	ControlPanelServerService.getServer().getSaveResult(new AsyncCallback<ExecutionResult>(){
	    	  		private void errorAccessServer(){
	    	  			Window.alert(constants.failToAccessServer());
	    	  		}
					public void onFailure(Throwable caught) {
						ControlPanel.hideLoading();
						errorAccessServer();
					}

					public void onSuccess(ExecutionResult result) {
						ControlPanel.hideLoading();
						if(result==null){
							errorAccessServer();
						}else {
							ControlPanel.showMessages((ControlPanelConstants)constants,result);
						}
						
					}}, windowNumber);
	    	  	ControlPanel.loadingProgress((ControlPanelConstants)constants);
	      }
	    });
	  ControlPanelServerService.getServer().requestWindowNumber(new AsyncCallback<Integer>(){

		public void onFailure(Throwable caught) {
			MessageUtility.error(constants.error(),constants.failToAccessServer());
			
		}

		public void onSuccess(Integer result) {
			if(result==null){
				MessageUtility.error(constants.error(),constants.failToAccessServer());
			}else{
				windowNumber=result;
			}
			
			
		}});
	  return formPanel;
  }
//***********************************************************************************

  private void initBaseBox(){
	  if(baseBox==null){
		  baseBox=  new ListBox();
		  baseBox.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				if(getBaseType()!=previousType){
					reload();
					previousType=getBaseType();
					}
	  }});
		  }
	  for(int i=baseBox.getItemCount()-1;i>=0;i--){
		  baseBox.removeItem(0);  
	  }
	  
	  if(logBySys.isChecked()){
		 baseBox.addItem( constants.system(),String.valueOf(LIST_TYPE_SYSTEM));
		 baseBox.addItem( constants.subSystem(),String.valueOf(LIST_TYPE_SUB_SYSTEM));
		 baseBox.addItem(constants.usecase(),String.valueOf(LIST_TYPE_USECASE));
		 baseBox.addItem(constants.action(),String.valueOf(LIST_TYPE_ACTION));
	  }else{
		  baseBox.addItem(constants.role(),String.valueOf(LIST_TYPE_ROLE) );
		  baseBox.addItem(constants.user(),String.valueOf(LIST_TYPE_USER)); 
	  }
	  int type=Integer.parseInt( baseBox.getValue(baseBox.getSelectedIndex()));
	  
	  initData(type);
  }
//***********************************************************************************
  private void reload(){
	  int key=Integer.parseInt( baseBox.getValue(baseBox.getSelectedIndex()));
		for(int i=0;i<key;i++){
			listPanels[i].rootPane.setVisible(false);
			cleanListPanel(listPanels[i]);
		}
		initData(key);
		for(int i=key;i<listPanels.length;i++){
			if(i!=key){
					cleanListPanel(listPanels[i]);
			}
			listPanels[i].rootPane.setVisible(true);
		}
		dataModel=new HashSet<String>();
  }

//***********************************************************************************
  private void initData(final int type){
	  ControlPanel.loadingProgress((ControlPanelConstants)constants);
	  ControlPanelServerService.getServer().loadData(type,new AsyncCallback<UILookupInfo>(){

		public void onFailure(Throwable caught) {
			ControlPanel.hideLoading();
			Window.alert(constants.failToAccessServer());
		}

		public void onSuccess(UILookupInfo result) {
			ControlPanel.hideLoading();
			if(result==null){
				Window.alert(constants.failToAccessServer());
			}else{
				setData(listPanels[type], result,type,true);
			}
			
		}
		  
	  }); 
  }
//***********************************************************************************
  protected void submit(){
	int index=0  ;
	ArrayList<Hidden>hiddens=new ArrayList<Hidden>();
	hiddens.add(new Hidden("logBasicItem",String.valueOf(getBaseType())));
	hiddens.add(new Hidden("logType",logBySys.isChecked()?String.valueOf(LOG_BY_SYSTEM_SUBSYSTEM_USECASE_ACTION):String.valueOf(LOG_BY_USER_ROLE)));
	for(String data: dataModel){
		initFormPanelParams(0,data,hiddens,index++);
	}
	
	for(Hidden hidden:hiddens){
		rootPanel.add(hidden);
	}
	formPanel.submit();
	
	for(Hidden hidden:hiddens){
		rootPanel.remove(hidden);
	}
  }
//***********************************************************************************
  private  void initFormPanelParams(int start,String data,ArrayList<Hidden>hiddens,int index){
	  if(data==null||data.trim().length()<start+3)return ;
	  
	  int endIndex=data.indexOf("*",start);
	  String keyValue=data.substring(0,endIndex);
	  int delIndex=keyValue.indexOf("|");
	  String key=keyValue.substring(0,delIndex);
	  String value=keyValue.substring(delIndex+1);
	  int type=Integer.parseInt(key);
	  
	  switch (type) {
		case LIST_TYPE_SYSTEM:
			key="systemId";
			break;
		case LIST_TYPE_SUB_SYSTEM:
			key="subSystemId";
			break;

		case LIST_TYPE_USECASE:
			key="usecaseId";
			break;

		case LIST_TYPE_ACTION:
			key="actionId";
			break;
		case LIST_TYPE_ROLE:
			key="roleId";
			break;

		default:
			key="userIds";
			break;
	}
	  hiddens.add(new Hidden("configRow["+index+"]."+key,value));
	  if(data.length()>keyValue.length()+1)
		  initFormPanelParams(keyValue.length(), data.substring(keyValue.length()+1), hiddens, index);
  }
 
//***********************************************************************************
  private ListPanel getList(UILookupInfo infos,String title,int type){
	  
	  ListPanel result=new ListPanel(title);
	if(infos!=null){
		String [][]data=infos.getData();
		if(data==null)return result;
		
	  }
	
	  return result;
  } 
  
//***********************************************************************************
  private void setData(ListPanel panel,UILookupInfo info,int type,boolean linkEnabled){
	  String [][]data=info.getData();
	  int displayCoumn=info.getDisplayColumnIndex();
	  String []ids=new String[data.length];
	  cleanListPanel(panel);
	  CheckBox []boxes=new CheckBox[ids.length];
	  for(int i=0;i<data.length;i++){
		  boxes[i]=new CheckBox();
		  Widget label;
		  ListClickListener listener=new ListClickListener(i,type);
		  if(linkEnabled){
			  label=new Hyperlink(data[i][displayCoumn],"#");
			  ((Hyperlink)label).addClickListener(listener);
			  
		  }else{
			  label=new Label(data[i][displayCoumn]);
			  boxes[i].setChecked(true);
		  }
		  boxes[i].addClickListener(listener);
		  boxes[i].setEnabled(linkEnabled);
		  ids[i]=data[i][0];
		  panel.grid.setWidget(i, 0, boxes[i]);
		  panel.grid.setWidget(i, 1, label);
	  }
	  panel.selectedIndex=-1;
	  panel.boxes=boxes;
	  panel.ids=ids;
  }
//***********************************************************************************
  private void cleanListPanel(ListPanel panel){
	  int rowCount=panel.grid.getRowCount();
	  for(int i=0;i<rowCount;i++){
		  panel.grid.removeRow(0);
	  }
	  panel.ids=new String[0];
  }
//***********************************************************************************
  private class ListPanel {
	  DecoratorPanel panel = new DecoratorPanel();
	  FlexTable grid=new FlexTable();
	  VerticalPanel rootPane=new VerticalPanel();
	  String []ids;
	  CheckBox []boxes;
	  int parentType;
	  long parentId;
	  private int selectedIndex; 
	  public ListPanel(String title){
		  rootPane.add(new Label(title));
		  ScrollPanel scroller=new ScrollPanel(grid);
		  scroller.setSize("200", "400");
		  panel.add(scroller);
		  rootPane.add(panel);
	  }
	 private Long getSelectedId(){
		 try {
			return Long.parseLong(ids[selectedIndex]);
		} catch (Exception e) {
			
		}
		return null;
	 }
	 private boolean isSelected(int index){
		 if(boxes!=null&&boxes.length>index){
			 return boxes[index].isChecked();
		 }
		 return false;
	 }
	 private boolean isEnabled(int index){
		 if(boxes!=null&&boxes.length>index&&index>=0){
			 return boxes[index].isEnabled();
		 }
		 return false;
	 }
  }
//***********************************************************************************
  private class ListClickListener implements ClickListener{
	  int index;
	  int type;
	  
	  ListClickListener(int index,int type){
		  this.index=index;
		  this.type=type;
		
	  }
	  public void onClick(Widget sender) {
		  boolean checkbox=sender instanceof CheckBox;
		  FlexTable grid=listPanels[type].grid;
		  listPanels[type].selectedIndex=index;
		  String selectedStyle="<A class=cp-SelectedLink";
		  boolean enable=true;
		  
		  if(checkbox){
			   enable=!((CheckBox)sender).isChecked();
			   long currentId=listPanels[type].getSelectedId();
			   updateDataModel(type,currentId, enable);
			   }
		  else{
			  enable=!((CheckBox)grid.getWidget(index, 0)).isChecked();
			}
		  for(int i=0;i<grid.getRowCount();i++){
				if(grid.getWidget(i, 1) instanceof Hyperlink){
					 Hyperlink link= (Hyperlink)grid.getWidget(i, 1);
					  String currentLink=DOM.getInnerHTML(link.getElement());
					  if(i==index){
						  currentLink=currentLink.replaceFirst("<A", selectedStyle);
					  }else{
						  currentLink=currentLink.replaceFirst(selectedStyle, "<A");
					  }
					  DOM.setInnerHTML(link.getElement(),currentLink);
				  }
			  }
		  
		  ControlPanel.loadingProgress((ControlPanelConstants)constants);
		  Long []ids=getListIds();
		  ControlPanelServerService.getServer().loadDetailData(ids,type,getBaseType(),new ListDataHandler(type,ids[type],enable));	
		}
	 
  }
//***********************************************************************************
  private void updateDataModel( int type,long currentId,boolean enable){
	
	StringBuffer pathKey	=new StringBuffer();  
	for(int i=getBaseType();i<MAX_LIST_TYPE;i++){
		if(i==type)break;
		if((listPanels[i].isEnabled(listPanels[i].selectedIndex)&&
				listPanels[i].isSelected(listPanels[i].selectedIndex))	){
			Long ckey=listPanels[i].getSelectedId() ; 
			if(ckey!=null){
				pathKey.append(i).append("|").append(ckey).append("*");
			}
		}
		
	}	
	String lastKey=type+"|"+currentId+"*";
	
	if(enable){
		dataModel.remove(pathKey.toString()+lastKey);
		if(pathKey.length()>0){
			dataModel.add(pathKey.toString());	
		}
	}else{
		if(dataModel.contains(pathKey.toString())){
			  dataModel.remove(pathKey.toString());
		  }	
		dataModel.add(pathKey.append(lastKey).toString());
	}
	 
  }
//***********************************************************************************
  private Long[] getListIds(){
	  Long[] ids=new Long[listPanels.length];
	  int index=0;
	  for(int i=0;i<listPanels.length;i++){
		ids[index]=listPanels[i].getSelectedId();
		index++;
	  }
	  return ids;
  }
//***********************************************************************************
  private int getBaseType(){
	  return Integer.parseInt(baseBox.getValue(baseBox.getSelectedIndex()));
  }
//***********************************************************************************
  private class ListDataHandler implements AsyncCallback<UILookupInfo[]>{
	  int type;
	  boolean linkEnabled; 
	  long id;
	  ListDataHandler (int type,long id,boolean disableDetails){
		  this.type=type;
		  this.linkEnabled=disableDetails;
		  this.id=id;
	  }
	  public void onFailure(Throwable caught) {
		  	ControlPanel.hideLoading();
			Window.alert(constants.failToAccessServer());
		}

	  private void updateListPanles(int currenType,UILookupInfo[] infos){
		  for(int i=0;i<listPanels.length;i++){
			  if(i==currenType||infos[i]==null)continue;
			  if(logBySys.isChecked()&&currenType<LIST_TYPE_ROLE&&(i==LIST_TYPE_ROLE||i==LIST_TYPE_USER)){
				  setData(listPanels[i], infos[i],i,!linkEnabled);  
			  }else{
				  setData(listPanels[i], infos[i],i,linkEnabled);
			  }
			  listPanels[i].parentType=currenType;
			  listPanels[i].parentId=id;
		  }
	  }
	  
		public void onSuccess(UILookupInfo[] result) {
			ControlPanel.hideLoading();
			if(result==null){
				Window.alert(constants.failToAccessServer());
			}else{
				updateListPanles(type, result);
			}
			
		}
  }
//***********************************************************************************
  
}
