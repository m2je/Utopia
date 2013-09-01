package ir.utopia.core.util.tags.treeview.client.model;

import java.util.HashMap;

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;



public class DnDTreeController implements MouseDownHandler,MouseUpHandler{

	private HashMap<Integer,DnDTreeItem> treeItems;
	private Widget movingWidget;
	private int itemCount;
	private Tree tree;
	private boolean dragging = false;
	private Widget clone;
	private AbsolutePanel boundaryPanel;
	private DnDTreeDataHandler dataHandler;
	Duration duration;
	boolean isDragAllowed=false;
	DnDTreeItem currentSelectedItem;
	private MouseListenerAdapter mouseListenerAdapter = new MouseListenerAdapter() {
		public void onMouseUp(Widget sender, int x, int y) {
			currentSelectedItem=(DnDTreeItem)treeItems.get(sender.getElement().getPropertyInt("index__"));
			if(isDragAllowed){
				Duration now =new Duration();
				DnDTreeItem dndMovingItem = (DnDTreeItem) treeItems.get(movingWidget.getElement().getPropertyInt("index__"));
				TreeItem newParent = hitTestRightTree(tree);
				if(duration!=null&&Math.abs(now.getStartMillis()-duration.getStartMillis())>1000){
					if (newParent != null && newParent != dndMovingItem) {
						if(dataHandler!=null)
							dataHandler.moveNode(dndMovingItem, newParent);
						else
							newParent.addItem(dndMovingItem);
					}
				}
					
			}
			dragging = false;
			if(movingWidget!=null)
				DOM.releaseCapture(movingWidget.getElement());
			if(clone!=null)
				clone.removeFromParent();
		}

		public void onMouseDown(Widget sender, int x, int y) {
			if(isDragAllowed){
				movingWidget = sender;
				DnDTreeItem dndMovingItem = (DnDTreeItem) treeItems.get(movingWidget.getElement().getPropertyInt("index__"));
				dragging = true;
				clone = new Label(dndMovingItem.getText());
				clone.setStylePrimaryName("clsLabel");
				boundaryPanel.add(clone,x+movingWidget.getAbsoluteLeft()-boundaryPanel.getAbsoluteLeft(), y+movingWidget.getAbsoluteTop()-boundaryPanel.getAbsoluteTop());
				DOM.setCapture(movingWidget.getElement());
				duration=new Duration();
			}
		}

		public void onMouseMove(Widget sender, int x, int y) {
			if (!dragging) return;
			boundaryPanel.setWidgetPosition(clone, x+movingWidget.getAbsoluteLeft()-boundaryPanel.getAbsoluteLeft(), y+movingWidget.getAbsoluteTop()-boundaryPanel.getAbsoluteTop());
		}
	};

	public DnDTreeController(){
		treeItems = new HashMap<Integer,DnDTreeItem>();
		itemCount = 0;
	}

	public void makeDraggable(DnDTreeItem item){
		treeItems.put(itemCount, item);
		item.setIndex(itemCount);
		itemCount++;
		item.addMouseDownHandler(this);
		item.addMouseListener(mouseListenerAdapter);
		item.addMouseUpHandler(this);
		
	}
	public void onMouseDown(MouseDownEvent event) {
		if(NativeEvent.BUTTON_LEFT== event.getNativeButton()){
			isDragAllowed=true;
		}else{
			isDragAllowed=false;
		}
		
	}
	@Override
	public void onMouseUp(MouseUpEvent event) {
		isDragAllowed=false;
		
	}
	public void setTree(Tree tree) {
		this.tree = tree;
	}


	public void setBoundaryPanel(AbsolutePanel boundaryPanel) {
		this.boundaryPanel = boundaryPanel;
	}

	public AbsolutePanel getBoundaryPanel() {
		return boundaryPanel;
	}

	private TreeItem hitTestRightTree(Tree tree) {
		TreeItem result =null;
		for(int i=0;i<tree.getItemCount();i++){
			DnDTreeItem root = (DnDTreeItem) tree.getItem(i);
			result = findDropTarget(root);
			if(result!=null)break;
		}

		return result;
	}

	private TreeItem findDropTarget(TreeItem root) {
		if (root.getChildCount() == 0) {
			if (clone.getAbsoluteLeft() > root.getAbsoluteLeft()
					&& clone.getAbsoluteTop() > root.getAbsoluteTop()
					&& clone.getAbsoluteLeft()+clone.getOffsetWidth() < root.getAbsoluteLeft()+root.getAbsoluteLeft()+100
					&& clone.getAbsoluteTop()+clone.getOffsetHeight() < root.getAbsoluteTop()+root.getOffsetHeight()+30){
				return root;
			}
			else return null;
		}
		for (int i = root.getChildCount()-1; i >= 0; i--) {
			TreeItem item = findDropTarget((TreeItem)root.getChild(i));
			if (item != null) return item;
		}
		if (clone.getAbsoluteLeft() > root.getAbsoluteLeft()
				&& clone.getAbsoluteTop() > root.getAbsoluteTop()
				&& clone.getAbsoluteLeft()+clone.getOffsetWidth() < root.getAbsoluteLeft()+root.getAbsoluteLeft()+100
				&& clone.getAbsoluteTop()+clone.getOffsetHeight() < root.getAbsoluteTop()+root.getOffsetHeight()+30){
			return root;
		}
		return null;
	}

	public DnDTreeDataHandler getDataHandler() {
		return dataHandler;
	}

	public void setDataHandler(DnDTreeDataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}

	
//*******************************************************************************************************
	public DnDTreeItem getCurrentSelectedItem() {
			return currentSelectedItem;
		}
//*******************************************************************************************************
}