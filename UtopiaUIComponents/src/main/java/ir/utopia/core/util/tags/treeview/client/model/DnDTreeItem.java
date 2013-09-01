package ir.utopia.core.util.tags.treeview.client.model;

import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class DnDTreeItem extends TreeItem{
	private static final UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	Image colapsedIcon=images.folder().createImage();
	Image openIcon=images.folderOpen().createImage();
	Image leaf=images.leaf().createImage();
	private FocusPanel focus;
	private String type;
	private Widget content;
	private Label text;
	private Image image;
	private HorizontalPanel widgetPanel;
	int index;
//************************************************************************************	
	public DnDTreeItem(){
		super();
	}
//************************************************************************************
	public DnDTreeItem(String html){
		this(new Label(html));
		resize(html.length());
	}
//************************************************************************************
	public DnDTreeItem(Label widget) {
		this();
		text=widget;
		text.setStylePrimaryName("gwt-TreeItem");
		widgetPanel=new HorizontalPanel();
		widgetPanel.setSpacing(2);
		image= leaf;
		widgetPanel.add(image);
		widgetPanel.add(widget);
		content = widgetPanel;
		content.setStylePrimaryName("tree-item");
		focus = new DnDPanel(content);
		focus.setPixelSize(200, 20);
		focus.setTabIndex(-1);
		setWidget(focus);
	}
//************************************************************************************
	public void addMouseUpHandler(MouseUpHandler handler){
		focus.addMouseUpHandler(handler);
	}
//************************************************************************************
	public void addMouseDownHandler(MouseDownHandler mouseDownHandler) {
		focus.addMouseDownHandler(mouseDownHandler);
		
	}
//************************************************************************************
	public void open(){
		widgetPanel.clear();
		widgetPanel.add(openIcon);
		widgetPanel.add(text);
	}
//************************************************************************************
	public void close(){
		widgetPanel.clear();
		widgetPanel.add(colapsedIcon);
		widgetPanel.add(text);
	}
//************************************************************************************
	public void unSelect(boolean colapsed){
//		Window.alert("unselected");
		text.setStylePrimaryName("gwt-TreeItem");
	}
//************************************************************************************
	public void select(boolean colapsed){
//		Window.alert("selected");
		text.setStylePrimaryName("gwt-Selected-TreeItem");
	}
//************************************************************************************
	public void setIndex(int index) {
		this.index=index;
		focus.getElement().setPropertyInt("index__", index);
	}
//************************************************************************************
	public int getIndex() {
		return index;
	}
//************************************************************************************
	public void addMouseListener(MouseListener mouseListener){
		focus.addMouseListener(mouseListener);
	}
//************************************************************************************
	public void removeMouseListener(MouseListener mouseListener){
		focus.removeMouseListener(mouseListener);
	}
//************************************************************************************
	public String toString(){
//		String text = ((Label) content).getText();
		if (text != null) return "DnDTreeItem: "+focus.getElement().toString()+" "+text.getText();
		else return "DnDTreeItem: "+focus.getElement().toString();
	}
//************************************************************************************
	public void resize(int width){
		focus.setPixelSize(width*10, 20);
	}
//************************************************************************************
	public void setType(String type) {
		this.type = type;
	}
//************************************************************************************
	public String getType() {
		return type;
	}
//************************************************************************************
	public Widget getContent() {
		return content;
	}
//************************************************************************************
	public String getText(){
		return text.getText();
	}
//************************************************************************************
	@Override
	public void addItem(TreeItem item) {
		super.addItem(item);
		updateTreeNode();
	}
//************************************************************************************
	protected void updateTreeNode(){
		if(getState()){
			open();
		}else{
			close();
		}
	}
	
//************************************************************************************
	protected class DnDPanel extends FocusPanel{
		public DnDPanel(Widget child){
			super(child);
//			sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU);
		}
//		public void onBrowserEvent(Event event) {
//			if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
//	    		event.cancelBubble(true);//This will stop the event from being propagated to parent elements.
//		        event.preventDefault();
//		        fireNodeEvent(event);
//				System.out.println();
//		        }else{
//		        	super.onBrowserEvent(event);
//		        }
			
	       
//		}
	}
}