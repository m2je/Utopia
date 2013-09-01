package ir.utopia.core.util.tags.treeview.client;

import ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer;
import ir.utopia.core.util.tags.datainputform.client.DataInputFormContainer;
import ir.utopia.core.util.tags.datainputform.client.DataInputFormSubmitCallBack;
import ir.utopia.core.util.tags.datainputform.client.DataInputFormWidget;
import ir.utopia.core.util.tags.datainputform.client.DataInputServerService;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.tree.model.TreeNode;
import ir.utopia.core.util.tags.treeview.client.model.DnDTreeController;
import ir.utopia.core.util.tags.treeview.client.model.DnDTreeDataHandler;
import ir.utopia.core.util.tags.treeview.client.model.DnDTreeItem;
import ir.utopia.core.util.tags.treeview.client.model.NodePropertyDialog;
import ir.utopia.core.util.tags.treeview.client.model.TreeLoadCompleteCallBack;
import ir.utopia.core.util.tags.treeview.client.model.TreeNodeCallBackListener;
import ir.utopia.core.util.tags.treeview.client.model.TreeViewServerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;

public class TreeView extends AbstractDataInputFormContainer implements EntryPoint,DataInputFormContainer,DnDTreeDataHandler,DataInputFormSubmitCallBack{
	TreeViewConstants constants=(TreeViewConstants)GWT.create(TreeViewConstants.class);
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	Logger logger=Logger.getLogger(TreeView.class.getName());
	private static final String NODE_ID_PROPERTY_NAME="__nodeId";
	public static final String FORM_CLASS_PARAMETER="formClass";
	public static final String USECASE_PARAMETER="useCaseName";
	public static final String ACTION_PARAMETER="actionName";
	public static final String SAVE_ACTION_PARAMETER="saveActionName";
	public static final String ROOT_NODE_TEXT="rootNodeText";
	public static final String FORM_TITLE="title";
	TreeItem root=null;
	TreeItem previousSelectedNode;
	Tree menuTree;
	List<TreeItem >childrenLoadedNodes=new ArrayList<TreeItem>();
	ScrollPanel centerScrollable;
	String windowNumber, title;
	DataInputFormModel dataInputModel;
	String saveActionName;
	String rootNodeText;
	AbsolutePanel absolutePanel ;
	private Widget clone;
	private Widget movingNode;
	boolean isDragging;
	int []draggingStatPos;
	boolean isDragSupport=false;
	private InputItem parentLinkItem;
	boolean isSubmitingByDnD=false;
	TreeItem movedNode;
	TreeItem movedNodeParent;
	SplitLayoutPanel splitPanel;
	VerticalPanel dataRootPanel;
	boolean isNewRecord=false;
	private final DnDTreeController treeController = new DnDTreeController();
	private NodePropertyDialog dialog;
	private List<TreeItem>previousLyFoundTreeItems=new ArrayList<TreeItem>(); 
	@Override
	public void onModuleLoad() {
	    splitPanel = new SplitLayoutPanel(2);
	    splitPanel.ensureDebugId("cwSplitLayoutPanel");
	    createTree();

		absolutePanel = new AbsolutePanel();
		absolutePanel.add(getSearchTreePanel());
	    absolutePanel.add(menuTree);
	    menuTree.getElement().setDraggable("true");
	    if(LocaleInfo.getCurrentLocale().isRTL()){
	    	splitPanel.addEast(absolutePanel, 300);	
	    }else{
	    	splitPanel.addWest(absolutePanel, 300);
	    }
	    
	    centerScrollable = new ScrollPanel();
	    
	    centerScrollable.setHeight("100%");
	    splitPanel.add(centerScrollable);
		RootLayoutPanel.get().add(splitPanel);
		initDataInputForm();
		treeController.setBoundaryPanel(absolutePanel);
		treeController.setTree(menuTree);
		treeController.setDataHandler(this);
		absolutePanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		dialog=new NodePropertyDialog(this);
		menuTree.addDomHandler(new ContextMenuHandler() {
			
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
			    event.stopPropagation();
			    dialog.setCurrentNode(treeController.getCurrentSelectedItem());
			    dialog.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
			    dialog.show();
			}
		}, ContextMenuEvent.getType());
		
		}
//*******************************************************************************************************
	private void createTree() {
	    menuTree = new Tree();
	    menuTree.setAnimationEnabled(true);
	    menuTree.ensureDebugId("cwTree-staticTree");
	    menuTree.addOpenHandler(new OpenHandler<TreeItem>() {
			
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				TreeItem item= event.getTarget();
				((DnDTreeItem)item).open();
				if(!childrenLoadedNodes.contains(item)){
					loadChildren(item,null);
					childrenLoadedNodes.add(item);
				}
				((DnDTreeItem)item).open();
			}
		});
	    menuTree.addCloseHandler(new CloseHandler<TreeItem>() {
			
			@Override
			public void onClose(CloseEvent<TreeItem> event) {
				TreeItem item= event.getTarget();
				if(!item.getState())
					((DnDTreeItem)item).close();
				
			}
		});
	    
	    menuTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			
			@Override
			public void onSelection(final SelectionEvent<TreeItem> event) {
				selectNode((DnDTreeItem)event.getSelectedItem(),null);
				
			}
		});
	   menuTree.addMouseDownHandler(new MouseDownHandler() {
		
		@Override
		public void onMouseDown(MouseDownEvent event) {
			previousLyFoundTreeItems.clear();
			
		}
	});
	    loadTree(null);
	  }
//*******************************************************************************************************
	protected Widget getSearchTreePanel(){
		 Label l=new Label(constants.nodeToFind()+":");
		l.setStylePrimaryName("clsLabel");
		HorizontalPanel searchTreePanel=new HorizontalPanel();
		searchTreePanel.setStylePrimaryName("clsBorderedBody");
		searchTreePanel.setSpacing(5);
		final TextBox box=new TextBox();
		box.setStylePrimaryName("clsText");
		searchTreePanel.add(l);
		searchTreePanel.add(box);
		box.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					TreeItem item=findNodeByName(box.getText(),previousLyFoundTreeItems);
					if(item!=null){
						previousLyFoundTreeItems.add(item);
						menuTree.setSelectedItem(item);
					}
				}
			}
		});
		
		return searchTreePanel;
	}
//*******************************************************************************************************
	public void changeSelection(TreeItem item){
		if(menuTree.getSelectedItem()!=item){
			menuTree.setSelectedItem(item, true);
		}
	}
//*******************************************************************************************************
	public void deleteItem(final DnDTreeItem item){
		if(item!=null){
				String deleteUrl=dataInputModel.getDeleteUrl();
				if(deleteUrl.indexOf("?")>0){
					deleteUrl+="&";
				}else{
					deleteUrl+="?";
				}
				deleteUrl+=dataInputModel.getPrimaryKeyName()+"="+getNodeId(item);
				deleteUrl+="&submitMode__="+DataInputFormWidget.FORM_SUBMIT_AJAX;
				RequestBuilder requestBuilder = new RequestBuilder(
					      RequestBuilder.GET, deleteUrl);
				try {
					requestBuilder.sendRequest(null, new RequestCallback() {
						
						@Override
						public void onResponseReceived(Request request, Response response) {
							 String responseText = response.getText();
						      try {
						        JSONValue jsonValue = JSONParser.parseStrict(responseText);
						        JSONObject jsonResult= jsonValue.isObject();
								JSONObject executionresult=jsonResult.get("executionResult").isObject();
								JSONBoolean success= executionresult.get("success").isBoolean();
								JSONArray message=executionresult.get("messages").isArray();
								if(!success.booleanValue()){
									MessageUtility.error(constants.error(), constants.failToDelete());
								}else{
									MessageUtility.showAutoHideInfoMessage(constants.deleteSuccessMessage());
									item.remove();
								}
								showMessages(message);
						        
						      } catch (JSONException e) {
						    	  GWT.log(e.toString());
						    	  MessageUtility.error(constants.error(), constants.failToDelete());
						      }
							
							
						}
						
						@Override
						public void onError(Request request, Throwable exception) {
							GWT.log(exception.toString());	
							MessageUtility.error(constants.error(), constants.failToDelete());
							
						}
					});
				} catch (Exception e) {
					GWT.log(e.toString());
					MessageUtility.error(constants.error(), constants.failToAccessServer());
				}
			}
		
	}
//*******************************************************************************************************
	public void selectNode(final DnDTreeItem item,final TreeNodeCallBackListener callBack){
		if(previousSelectedNode!=null){
			((DnDTreeItem)previousSelectedNode).unSelect(false);
		}
		item.select(false);
		
		if(item!=previousSelectedNode){
			if(isModified()){
				MessageBox.confirm(constants.revetModifications(), constants.revetModificationsConfirmMessage(),new ConfirmCallback() {
					@Override
					public void execute(String btnID) {
						if("yes".equalsIgnoreCase(btnID)){
							isNewRecord=false;
							setModified(false);
							loadNodeData(true,callBack);
							previousSelectedNode=item;
						}else{
							if(previousSelectedNode!=null)
								menuTree.setSelectedItem(previousSelectedNode,true);
							return;
						}
						
					}
				});
			}else{
				loadNodeData(true,callBack);
				previousSelectedNode=item;
			}	
		}
	}
//*******************************************************************************************************
	protected void loadTree(TreeLoadCompleteCallBack callback){
		menuTree.removeItems();
		String rootNodeText=null;
		try {
			rootNodeText = getRootNodeText();
		} catch (Exception e) {
		}
	    if(rootNodeText!=null&&rootNodeText.trim().length()>0){
	    	root = new DnDTreeItem(rootNodeText);
	    	treeController.makeDraggable((DnDTreeItem)root);
//	    	nodePropertyController.registerRichClickHandler((DnDTreeItem)root);
	    	menuTree.addItem(root);
//	    	root = menuTree.addItem(rootNodeText);
	    	childrenLoadedNodes.add(root);
	    }
	    loadChildren(root, callback);
	}
//*******************************************************************************************************
	protected void initDataInputForm(){
		final TreeView adaptee=this;
		MessageBox.progress(constants.pleaseWait(), constants.loading());
		DataInputServerService.getServer().getDataInputForm(getFormClassName(),getUsecaseName(),
				getSaveActionName(), new AsyncCallback<DataInputFormModel>(){

					@Override
					public void onFailure(Throwable caught) {
						MessageUtility.stopProgress();
						GWT.log(caught.toString());
					}

					@Override
					public void onSuccess(DataInputFormModel model) {
						adaptee.dataInputModel=model;
						setItems(model.getItems());
						setUsecaseActionId(model.getUsecaseActionId());
						setEditorActionName(getFormActionName());
						setEditorFormClass(getFormClassName());
						setEditorUsecaseName(getUsecaseName());
						model.setAllowNewRecord(true);
						datainputFormWidget=new DataInputFormWidget(null,model,constants,adaptee);
						datainputFormWidget.setSubmitCallBack(adaptee);
						datainputFormWidget.setSubmitURL(model.getSaveUrl());
						datainputFormWidget.setSubmitMode(DataInputFormWidget.FORM_SUBMIT_AJAX);
					    dataRootPanel=new VerticalPanel();
						dataRootPanel.setStylePrimaryName("clsTableBody");
						dataRootPanel.setWidth("100%");
						dataRootPanel.add(new HTML("<p align=center class=\"clsPageHeader\">"+getFormTitle()+"</p>"));
						adaptee.setWidgetsMap(datainputFormWidget.getWidgetsMap());
						adaptee.setWidgetsLabelMap(datainputFormWidget.getWidgetsLabelMap());
						((VerticalPanel)datainputFormWidget).setHeight("100%");
						dataRootPanel.add(datainputFormWidget);
						centerScrollable.setWidget(dataRootPanel);
//						dataInputFormWidget.getElement().setDir(LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR");
						MessageUtility.stopProgress();
						loadNodeData(true,null);
					}
			
		});
	}
//*******************************************************************************************************
	protected void loadNodeData(final boolean reload,TreeNodeCallBackListener callBack){
		TreeItem selectedItem= menuTree.getSelectedItem();
		loadNodeData(selectedItem, reload,callBack);
	}
//*******************************************************************************************************
	protected void loadNodeData(TreeItem selectedItem,final boolean reload,final TreeNodeCallBackListener callBack){
				if(selectedItem!=null){
					datainputFormWidget.setSubmitURL(dataInputModel.getUpdateUrl());
					MessageBox.progress(constants.pleaseWait(), constants.loadingData());
					String recordId=getNodeId(selectedItem);
					DataInputServerService.getServer().
					getDataInputFormData(getFormClassName(), getUsecaseName(), getFormActionName(), recordId,reload,"", new AsyncCallback<DataInputDataModel>(){

						public void onFailure(Throwable caught) {
							MessageUtility.stopProgress();
							if(callBack!=null){
								callBack.loadCompleted(false);
							}
							MessageUtility.error(constants.error(),constants.failToAccessServer());
						}

						public void onSuccess(DataInputDataModel result) {
							MessageUtility.stopProgress();
							if(result!=null){
								setData(result);
								setContext(result.getContext());
								if(callBack!=null)
									callBack.loadCompleted(true);
							}else{
								MessageUtility.error(constants.error(),constants.failToAccessServer());
							}
						}
					});

				}
							
		
	}
//*******************************************************************************************************
	public void setItems(InputItem [] items){
		super.setItems(items);
		for(InputItem item:items){
			if(item.isParentLinkItem()){
				isDragSupport=true;
				parentLinkItem=item;
			}
		}
	}
//*******************************************************************************************************
	private void setData(DataInputDataModel values) {
		datainputFormWidget.setData(values);
		super.setContext(values.getContext());
		super.initData(values.getValueModel());
		setModified(false);
	}
//*******************************************************************************************************
	private void loadChildren( TreeItem parent,TreeLoadCompleteCallBack callback){
		Long parentId;
		if(parent==null){
			parentId=null;//root is being loaded
		}else{
			parentId=Long.parseLong(getNodeId(parent));
		}
		TreeViewServerService.getServer().loadChildren(getUsecaseName(), getFormClassName(), parentId,new childNodeLoader(parent,callback));
	}
//************************************************************************************************************************
	  public String getFormTitle(){
		  if(this.title==null){
			  this.title=getFormTitleNative();
		  }
		  return this.title;
	  }
//************************************************************************************************************************
	  public native String getFormTitleNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.treeview.client.TreeView::FORM_TITLE).value;
		}-*/;

//*******************************************************************************************************
	public String getFormClassName(){
		if(this.formClass==null){
			this.formClass=getFormClassNameNative();
		}
		return this.formClass;
	}
//*******************************************************************************************************
	  public native String getFormClassNameNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.treeview.client.TreeView::FORM_CLASS_PARAMETER).value;
		}-*/;
//************************************************************************************************************************
	  public  String getUsecaseName(){
		  if(this.usecaseName==null){
			  this.usecaseName=getUsecaseNameNative();
		  }
		  return this.usecaseName;
	  }
//************************************************************************************************************************
	  public native String getUsecaseNameNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.treeview.client.TreeView::USECASE_PARAMETER).value;
		}-*/;
//************************************************************************************************************************
	  private  String getRootNodeText(){
		if(rootNodeText==null){
			rootNodeText=getRootNodeTextNative();
			rootNodeText=rootNodeText==null?rootNodeText:"";
		}  
		return rootNodeText;
	  } 
//************************************************************************************************************************
	  private native String getRootNodeTextNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.treeview.client.TreeView::ROOT_NODE_TEXT).value;
		}-*/;
//************************************************************************************************************************
	  @Override
	public void reload() {
		loadNodeData(true,null);
	}
//************************************************************************************************************************
	  @Override
	public void newRecord() {
		  final TreeView adaptee=this;
		  if(isModified()){
				MessageBox.confirm(constants.revetModifications(), constants.revetModificationsConfirmMessage(),new ConfirmCallback() {
					@Override
					public void execute(String btnID) {
						if("yes".equalsIgnoreCase(btnID)){
							setModified(false);
							resetDataInputForm();
							adaptee.initData(null);
						}else{
							return;
						}
						
					}});
		  }else{
			  resetDataInputForm();
	  }
	}
//***********************************************************************************
	  protected void resetDataInputForm(){
		  isNewRecord=true;
		  datainputFormWidget.setSubmitURL(dataInputModel.getSaveUrl());
		  
		  DataInputServerService.getServer().getDataInputFormData(getFormClassName(), getUsecaseName(), saveActionName, "0", false,"",new AsyncCallback<DataInputDataModel>(){

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(DataInputDataModel result) {
				datainputFormWidget.setData(result);
				setNewRecordParentId();
			}} );
	  }
//***********************************************************************************
	  protected void setNewRecordParentId(){
		  Widget parentLinkItemWidget =widgetsMap.get(parentLinkItem.getColumnName());
			if(parentLinkItemWidget!=null){
				Long currentNodeId;
				TreeItem selected=menuTree.getSelectedItem();
				if(selected==null){
					currentNodeId=null;
				}else{
					currentNodeId=Long.parseLong(getNodeId(selected));
				}
				EditorFactory.setWidgetValue(parentLinkItemWidget,currentNodeId);
				}
	  }
//***********************************************************************************
	  public void submitted(boolean result,String id,int formSubmitter){
		  try {
			if(isModified()&&result)
				  setModified(false);
			  if(result){
				  Long nodeId=Long.parseLong(id);
				  TreeViewTreeLoadCallBack callBack=new TreeViewTreeLoadCallBack(nodeId, formSubmitter==DataInputFormWidget.FORM_SUBMITER_SAVE_NEW_BUTTON);
				  if(!isSubmitingByDnD){
					  TreeItem parent;
					  if(isNewRecord){
						 parent= menuTree.getSelectedItem();
					  }else{
						 parent= menuTree.getSelectedItem().getParentItem();
					  }
					  updateNodeOnTree(parent,callBack);
				  }
				  else {
					 childrenLoadedNodes.add(movedNodeParent);
					 movedNodeParent.addItem(movedNode); 
				  }
				 
				  isNewRecord=false;
			  }
		} catch (Exception e) {
			GWT.log(e.toString());
		}
		  MessageUtility.stopProgress();
	  }
//************************************************************************************************************************
	  protected String getNodeId(TreeItem currentNode){
		  String currentNodeId="-1";
		  try {
			Element el=currentNode.getElement();
			currentNodeId=el.getPropertyString(NODE_ID_PROPERTY_NAME);
		} catch (Throwable e) {
		} 
		  currentNodeId=currentNodeId==null?"-1":currentNodeId;
		  return currentNodeId;
	  }
//************************************************************************************************************************
	  protected void updateNodeOnTree(TreeItem parent,TreeLoadCompleteCallBack callback){
		  if(parent!=null){
				 reloadNode(parent,callback);
				 parent.setState(true, true);
			 }else{
				 loadTree(callback);
			 }
	  }
//************************************************************************************************************************
	  public TreeItem findNodeById(Long id){
		  return findNodeById(null, id);
	  }
//************************************************************************************************************************
	  public TreeItem findNodeById(TreeItem currentNode, Long id){
		  if(id==null)return null;
		  return findNodeByProperty(currentNode, id, "id",null);
	  }
//************************************************************************************************************************
	  public TreeItem findNodeByName(String name){
		  return findNodeByName(name, null);
	  }
//************************************************************************************************************************
	  public TreeItem findNodeByName(String name,List<TreeItem> previouslyFound){
		  if(name==null)return null;
		  return findNodeByProperty(null, name, "name",previouslyFound);
	  } 
//************************************************************************************************************************
	  public TreeItem findNodeByProperty(TreeItem currentNode, Object value,String propertyName,List<TreeItem> previouslyFound){
		  TreeItem result=null;
		  if(currentNode==null){
			  if(menuTree.getItemCount()>0){
				  for (int i=0;i<menuTree.getItemCount();i++){
					   result= findNodeByProperty(menuTree.getItem(i), value,propertyName,previouslyFound);
					  if(result !=null)return result;
				  }
			  }
				  
			  return null;
		  }
		 if(hasProperty(currentNode,value,propertyName)&&
				 (previouslyFound==null||!previouslyFound.contains(currentNode))){
			 return currentNode;
		 }
		  for(int i=0;i<currentNode.getChildCount();i++){
			  result=findNodeByProperty(currentNode.getChild(i), value,propertyName,previouslyFound);
			  if(result!=null){
				  return result;
			  }
		  }
		  
		  return null;
	  }
//************************************************************************************************************************
	  protected int getItemIndexFromParent(TreeItem item){
		  if(item.getParentItem()==null){
			  for(int i=0;i<menuTree.getItemCount();i++){
				  if(menuTree.getItem(i).equals(item)){
					  return i;
				  }
			  }
		  }else{
			  return item.getParentItem().getChildIndex(item);
		  }
		  return 0;
	  }
//************************************************************************************************************************
	  protected Stack<TreeItem> getPathToRoot(TreeItem item){
		  Stack<TreeItem>path=new Stack<TreeItem>();
		  path.push(item);
		  TreeItem currentItem=item.getParentItem();
		  while(currentItem!=null){
			  path.push(currentItem);
			  currentItem=currentItem.getParentItem();
		  }
		  return path;
	  }
//************************************************************************************************************************
	  protected boolean hasProperty(TreeItem item,Object property,String propertyName){
		  if("id".equals(propertyName)){
			  Long currentNodeId=Long.parseLong(getNodeId(item));
			  if(currentNodeId!=null){
				  if(property.equals(currentNodeId)){
					  return true;
				  }
			  }
		  }else if("name".equals(propertyName)){
			  if(item.getText().indexOf((String)property)>=0){
				  return true;
			  }
		  }
		  return false;
	  }
//************************************************************************************************************************
	  protected void reloadNode(TreeItem parent,TreeLoadCompleteCallBack callback){
		  parent.removeItems();
		  loadChildren(parent,callback);
	  }

//************************************************************************************************************************
	 @Override
	public void redirect(String previousPage) {
		
	}
//************************************************************************************************************************	 
	 public String getWindowNo(){
		 if(this.windowNumber==null){
			 this.windowNumber=getWindowNoNative();
		 }
		 return this.windowNumber;
	 }
//************************************************************************************************************************
	public native String getWindowNoNative()/*-{
    return $doc.getElementById(@ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants::WINDOW_NO_PARAMETER_NAME).value;
	}-*/;
//************************************************************************************************************************	
	@Override
	public String getRecordId() {
		return (isNewRecord||menuTree.getSelectedItem()==null)?"0":getNodeId(menuTree.getSelectedItem());
	}
//************************************************************************************************************************
	@Override
	public String getFormActionName(){
		if(this.actionName==null){
			this.actionName=getFormActionNameNative();
		}
		return this.actionName;
	}
//*************************************************************************************************************************	
	public native String getFormActionNameNative()/*-{
		return $doc.getElementById(@ir.utopia.core.util.tags.treeview.client.TreeView::ACTION_PARAMETER).value;
	}-*/;
//*************************************************************************************************************************
	public String getSaveActionName(){
		if(this.saveActionName==null){
			this.saveActionName=getFormSaveActionNameNative();
		}
		return this.saveActionName;
	}
//*************************************************************************************************************************
	public native String getFormSaveActionNameNative()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.treeview.client.TreeView::SAVE_ACTION_PARAMETER).value;
	}-*/;
//*************************************************************************************************************************
	public DataInputFormModel getDataInputformModel(){
		return dataInputModel;
	}
//*************************************************************************************************************************
	public void showMessages(JSONArray messages){
		if(messages==null){
			
			return;
		}
		ArrayList<String>errors=new ArrayList<String>();
		ArrayList<String>warnings=new ArrayList<String>();
		ArrayList<String>infos=new ArrayList<String>();
		
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
			MessageUtility.error(constants.error(), error);
		}
		for(String warning:warnings){
			MessageUtility.warn(constants.warning(), warning);	
			}
		for(String info:infos){
			MessageUtility.info(constants.info(), info);
		}
		}
//*************************************************************************************************************************
	class childNodeLoader implements AsyncCallback<TreeNode[]>{
		TreeItem parent;
		TreeLoadCompleteCallBack callback;
		public childNodeLoader (TreeItem parent,TreeLoadCompleteCallBack callback){
			this.parent=parent;
			this.callback=callback;
		}
		@Override
		public void onSuccess(TreeNode[] result) {
			if(parent!=null){
				parent.removeItems();
			}
			if(result!=null){
				for(TreeNode cNode:result){
					TreeItem newNode=new DnDTreeItem(cNode.getName());
					newNode.setTitle(cNode.getDescription()==null?"":cNode.getDescription());
					treeController.makeDraggable((DnDTreeItem)newNode);
					if(parent==null){
						menuTree.addItem(newNode);
					}else{
						parent.addItem(newNode);
						}
					
					if(cNode.getChildrenCount()>0){
						TreeItem loading=new TreeItem(constants.pleaseWait());
						newNode.addItem(loading);
					}
					newNode.getElement().setPropertyString(NODE_ID_PROPERTY_NAME, String.valueOf(cNode.getNodeId()));
				}
				
			}
			if(parent!=null){
				parent.setState(true,false);
			}
			else if(menuTree.getItemCount()>0){
//				menuTree.getItem(0).setState(true,false);
			}
			if(callback!=null){
				callback.loadCompleted(true);
			}
		}

		@Override
		public void onFailure(Throwable caught) {
			GWT.log(caught.getMessage());
			if(callback!=null){
				callback.loadCompleted(false);
			}
			MessageUtility.error(constants.error(),constants.failToAccessServer());
		}
	}
//***********************************************************************************************************************************************
	@Override
	public void moveNode(final TreeItem node,final TreeItem parent) {
		final Widget parentLinkItemWidget =widgetsMap.get(parentLinkItem.getColumnName());
		if(parentLinkItemWidget!=null){
			String confirmMessage=constants.nodeParentChangeConfirm().replaceAll("@A@", node.getText()).replaceAll("@B@", parent.getText());
			MessageBox.confirm(constants.warning(), confirmMessage,new ConfirmCallback() {
				
				@Override
				public void execute(String btnID) {
					if("yes".equalsIgnoreCase(btnID)){
						MessageUtility.progress(constants.pleaseWait(), constants.submitingData());
						EditorFactory.setWidgetValue(parentLinkItemWidget,Long.parseLong(getNodeId(parent)));
						isSubmitingByDnD=true;
						movedNode=node;
						movedNodeParent=parent;
						datainputFormWidget.submit();	
						}
					}
			} );
	}
	}
//***********************************************************************************************************************************************
	@Override
	public String getSaveToken() {
		return null;
	}
//***********************************************************************************************************************************************
	public class TreeViewTreeLoadCallBack implements TreeLoadCompleteCallBack {

		Long selectedNodeId;
		boolean newRecordAfterLoad;
		Tree tree;
		public TreeViewTreeLoadCallBack(Long selectedNodeId,
				boolean newRecordAfterLoad) {
			
			this.selectedNodeId = selectedNodeId;
			this.newRecordAfterLoad = newRecordAfterLoad;
		}

		@Override
		public void loadCompleted(boolean success) {
			if(selectedNodeId!=null){
				final TreeItem item= findNodeById(selectedNodeId);
				if(item!=null){
					if(newRecordAfterLoad){
						menuTree.setSelectedItem(item);
						resetDataInputForm();

					}else{
						menuTree.setSelectedItem(item);
					}
				}
			}
		}
	}
//***********************************************************************************************************************************************
	public static class TreeNodeLoader{
		
	}
}
