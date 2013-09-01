package ir.utopia.core.util.tags.treeview.client.model;

import ir.utopia.core.util.tags.treeview.client.TreeView;
import ir.utopia.core.util.tags.treeview.client.TreeViewConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.gwtext.client.widgets.MessageBox;

public class NodePropertyDialog extends PopupPanel  {
	TreeViewConstants constants=(TreeViewConstants)GWT.create(TreeViewConstants.class);
	DnDTreeItem item;
	private TreeView treeView;
//**********************************************************************************
	public NodePropertyDialog(TreeView treeView){
		super(true);
		this.treeView=treeView;
		init();
	}
//**********************************************************************************
	protected void init(){
		MenuBar popupMenuBar = new MenuBar(true);
		MenuItem deleteItem = new MenuItem("<div class=\"deleteMenuItem\">"+constants.delete()+"</div>" , true, deleteCommand);
		MenuItem editItem = new MenuItem("<div class=\"editMenuItem\">"+constants.edit()+"</div>", true, editCommand);
		setStyleName("popup");
		deleteItem.addStyleName("popup-item");
		editItem.addStyleName("popup-item");
		popupMenuBar.addItem(deleteItem);
		popupMenuBar.addItem(editItem);

    	popupMenuBar.setVisible(true);
    	add(popupMenuBar);
	}
//**********************************************************************************
	protected Command deleteCommand = new Command() {
		public void execute() {
			int childCount=item.getChildCount();
			String confirmMessage;
			if(childCount>0){
				confirmMessage=constants.deleteTreeNodeAndChildrenConfirmMessage();
			}else{
				confirmMessage=constants.deleteTreeNodeConfirmMessage();
			}
			MessageBox.confirm(constants.warning(), confirmMessage, new MessageBox.ConfirmCallback() {
				
				@Override
				public void execute(String btnID) {
					if("yes".equals( btnID)){
						
						treeView.deleteItem(item);
					}
					
				}
				}
			);
			hide();
		}
	};
//**********************************************************************************
	protected Command editCommand = new Command() {
		public void execute() {
			hide();
			treeView.changeSelection(item);
		}
	};
//**********************************************************************************	
	public void setCurrentNode(DnDTreeItem item){
		this.item=item;
	}
//**********************************************************************************
	
}
